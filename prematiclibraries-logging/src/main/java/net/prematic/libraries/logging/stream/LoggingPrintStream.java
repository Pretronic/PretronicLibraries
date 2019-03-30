/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 25.03.19 19:22
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

package net.prematic.libraries.logging.stream;

import net.prematic.libraries.logging.MessageInfo;
import net.prematic.libraries.logging.PrematicLogger;
import net.prematic.libraries.logging.level.LogLevel;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * The logging output stream is for hooking the default java output stream in to the logger.
 */
public class LoggingPrintStream extends PrintStream {

    private final PrematicLogger logger;
    private final LogLevel level;
    private final MessageInfo info;

    public LoggingPrintStream(PrematicLogger logger){
        this(logger,LogLevel.INFO);
    }

    public LoggingPrintStream(PrematicLogger logger,LogLevel level){
        this(logger,level,(MessageInfo) null);
    }

    public LoggingPrintStream(PrematicLogger logger,LogLevel level, MessageInfo info){
        this(logger,level,info,System.out);
    }

    public LoggingPrintStream(PrematicLogger logger,LogLevel level,OutputStream out) {
        super(out);
        this.logger = logger;
        this.level = level;
        this.info = null;
    }

    public LoggingPrintStream(PrematicLogger logger,LogLevel level, MessageInfo info,OutputStream out) {
        super(out);
        this.level = level;
        this.info = info;
        this.logger = logger;
    }

    public LoggingPrintStream(PrematicLogger logger,LogLevel level, MessageInfo info,OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
        this.level = level;
        this.info = info;
        this.logger = logger;
    }

    public LoggingPrintStream(PrematicLogger logger,LogLevel level, MessageInfo info,OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
        super(out, autoFlush, encoding);
        this.level = level;
        this.info = info;
        this.logger = logger;
    }

    public PrematicLogger getLogger(){
        return this.logger;
    }

    @Override
    public void print(boolean b) {
        this.logger.log(info,level,b);
    }

    @Override
    public void print(char c) {
        this.logger.log(info,level,c);
    }

    @Override
    public void print(int i) {
        this.logger.log(info,level,i);
    }

    @Override
    public void print(long l) {
        this.logger.log(info,level,l);
    }

    @Override
    public void print(float f) {
        this.logger.log(info,level,f);
    }

    @Override
    public void print(double d) {
        this.logger.log(info,level,d);
    }

    @Override
    public void print(char[] s) {
        this.logger.log(info,level,s);
    }

    @Override
    public void print(String s) {
        this.logger.log(info,level,s);
    }

    @Override
    public void print(Object obj) {
        this.logger.log(info,level,obj);
    }

    @Override
    public void println() {
        this.logger.log(info,level,"");
    }

    @Override
    public void println(boolean x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(char x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(int x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(long x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(float x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(double x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(char[] x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(String x) {
        this.logger.log(info,level,x);
    }

    @Override
    public void println(Object x) {
        this.logger.log(info,level,x);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof LoggingPrintStream && getLogger().equals(this.logger);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new LoggingPrintStream(this.logger);
    }

    @Override
    public String toString() {
        return "Logging stream for "+this.logger.toString();
    }

    public static void hook(PrematicLogger logger){
        System.setOut(new LoggingPrintStream(logger,LogLevel.INFO));
        System.setErr(new LoggingPrintStream(logger,LogLevel.ERROR));
    }
}
