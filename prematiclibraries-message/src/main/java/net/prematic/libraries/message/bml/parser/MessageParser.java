/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 22.12.19, 22:04
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package net.prematic.libraries.message.bml.parser;

import net.prematic.libraries.message.bml.Function;
import net.prematic.libraries.message.bml.Message;
import net.prematic.libraries.message.bml.functions.RandomTextFunction;
import net.prematic.libraries.message.bml.functions.SubstringFunction;
import net.prematic.libraries.message.bml.module.Module;
import net.prematic.libraries.message.bml.module.RootModule;
import net.prematic.libraries.utility.parser.StringParser;

public class MessageParser {

    private final StringParser parser;

    private ParserState state;
    private MessageSequence sequence;

    private int characterMark;
    private int lineMark;

    public MessageParser(StringParser parser) {
        this.parser = parser;
        this.state = ParserState.START;
    }

    public StringParser getParser() {
        return parser;
    }


    public ParserState getState() {
        return state;
    }

    public void setState(ParserState state) {
        this.state = state;
    }


    public MessageSequence getSequence() {
        return sequence;
    }

    public void setSequence(MessageSequence sequence) {
        this.sequence = sequence;
    }


    public int getCharacterMark() {
        return characterMark;
    }

    public void setCharacterMark(int characterMark) {
        this.characterMark = characterMark;
    }

    public int getLineMark() {
        return lineMark;
    }

    public void setLineMark(int lineMark) {
        this.lineMark = lineMark;
    }

    public void extendMarkPrevious(){
        if(characterMark > 0) characterMark--;
        else{
            lineMark--;
            characterMark = parser.getLines()[lineMark].length-1;
        }
    }

    public void mark(){
        this.characterMark = parser.charIndex();
        this.lineMark = parser.lineIndex();
    }

    public void markNext(){
        if(!parser.hasNextChar()) return;
        parser.skipChar();
        mark();
        parser.previousChar();
    }

    public String getString(){
        return parser.get(lineMark,characterMark,parser.lineIndex(),parser.charIndex());
    }


    public void nextModule(Module module){

    }

    public Function getFunction(String name){
        if(name.equalsIgnoreCase("randomText")) return new RandomTextFunction();
        else if(name.equalsIgnoreCase("substring")) return new SubstringFunction();
        parser.throwException("Function "+name+" not found");
        return null;
    }

    public Message parse(){
        parser.resetIndex();
        mark();
        RootModule root = new RootModule();
        this.sequence = new MessageSequence(root);
        while (parser.hasNextChar()) this.state.parse(this,parser.nextChar());
        return new Message(root.getNext());
    }

}