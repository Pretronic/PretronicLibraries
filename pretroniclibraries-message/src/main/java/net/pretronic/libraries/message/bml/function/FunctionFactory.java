/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 21.03.20, 17:04
 * @web %web%
 *
 * The PretronicLibraries Project is under the Apache License, version 2.0 (the "License");
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

package net.pretronic.libraries.message.bml.function;

import net.pretronic.libraries.message.bml.MessageProcessor;
import net.pretronic.libraries.message.bml.Module;
import net.pretronic.libraries.message.bml.builder.BuildContext;
import net.pretronic.libraries.message.bml.builder.MessageBuilder;
import net.pretronic.libraries.message.bml.builder.MessageBuilderFactory;
import net.pretronic.libraries.message.bml.builder.StaticTextMessageBuilder;

public class FunctionFactory implements MessageBuilderFactory {

    private final MessageProcessor processor;

    public FunctionFactory(MessageProcessor processor) {
        this.processor = processor;
    }

    @Override
    public MessageBuilder create(String name) {
        Function function = processor.getFunction(name);
        if(function == null) return new StaticTextMessageBuilder("{FUNCTION NOT FOUND;"+name+"}");
        return new Builder(function);
    }

    public static class Builder implements MessageBuilder {

        private final Function function;

        public Builder(Function function) {
            this.function = function;
        }

        @Override
        public Object build(BuildContext context, boolean requiresString, String name, Module leftOperator, String operation
                , Module rightOperator, Module[] parameters, Module extension, Module next0) {

            Object result = function.execute(context,leftOperator,operation,rightOperator,parameters);
            if(next0 != null){
                Object next = Module.build(next0,context,requiresString);
                if(next != null) return result+next.toString();
                else return result;
            }else return result;
        }
    }
}
