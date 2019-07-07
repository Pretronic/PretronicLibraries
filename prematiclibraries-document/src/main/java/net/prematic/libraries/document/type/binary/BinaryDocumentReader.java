/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 05.07.19 13:23
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

package net.prematic.libraries.document.type.binary;

import net.prematic.libraries.document.Document;
import net.prematic.libraries.document.DocumentEntry;
import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.PrimitiveEntry;
import net.prematic.libraries.document.io.DocumentReader;
import net.prematic.libraries.utility.io.IORuntimeException;
import net.prematic.libraries.utility.parser.StringParser;

import java.io.*;
import java.nio.charset.Charset;

import static net.prematic.libraries.document.type.binary.BinaryDocumentWriter.*;


public class BinaryDocumentReader implements DocumentReader {

    @Override
    public Document read(byte[] content) {
        return read(content,Charset.defaultCharset());
    }

    @Override
    public Document read(byte[] content, Charset charset) {
        ByteArrayInputStream stream = new ByteArrayInputStream(content);
        return read(stream,charset);
    }

    @Override
    public Document read(String content) {
        return read(content.getBytes());
    }

    @Override
    public Document read(File location) {
        return read(location,Charset.defaultCharset());
    }

    @Override
    public Document read(File location, Charset charset) {
        try {
            FileInputStream stream = new FileInputStream(location);
            Document content =  read(stream,charset);
            stream.close();
            return content;
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    @Override
    public Document read(InputStream input) {
        return read(input,Charset.defaultCharset());
    }

    @Override
    public Document read(InputStream input, Charset charset) {
        try {
            DocumentEntry entry = next(input instanceof DataInputStream? (DataInputStream) input :new DataInputStream(input),charset,false);
            if(entry != null && entry.isObject()) return entry.toDocument();
            else throw new IllegalArgumentException("First entry ist not a document (object).");
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    @Override
    public Document read(StringParser parser) {
        throw new UnsupportedOperationException("It is not possible to read a binary from a string parser.");
    }


    private DocumentEntry next(DataInputStream stream, Charset charset, boolean key) throws IOException {
        switch (stream.readByte()){//Read type
            case TYPE_BYTE: return nextPrimitive(stream,charset,stream.readByte(),key);
            case TYPE_BOOLEAN: return nextPrimitive(stream,charset,stream.readBoolean(),key);
            case TYPE_INTEGER: return nextPrimitive(stream,charset,stream.readInt(),key);
            case TYPE_LONG: return nextPrimitive(stream,charset,stream.readLong(),key);
            case TYPE_DOUBLE: return nextPrimitive(stream,charset,stream.readDouble(),key);
            case TYPE_FLOAT: return nextPrimitive(stream,charset,stream.readFloat(),key);
            case TYPE_SHORT: return nextPrimitive(stream,charset,stream.readShort(),key);
            case TYPE_CHARACTER: return nextPrimitive(stream,charset,stream.readChar(),key);
            case TYPE_STRING: return nextPrimitive(stream,charset,readString(stream,charset),key);
            case TYPE_OBJECT_IN: return readObject(stream,charset,key?readString(stream,charset):null);
            case TYPE_ARRAY_IN: return readArray(stream,charset,key?readString(stream,charset):null);
            default: return null;
        }
    }

    private PrimitiveEntry nextPrimitive(DataInputStream stream, Charset charset,Object object, boolean key) throws IOException{
        return DocumentRegistry.getFactory().newPrimitiveEntry(key?readString(stream,charset):null,object);
    }

    private Document readObject(DataInputStream stream, Charset charset, String key) throws IOException{
        Document document = DocumentRegistry.getFactory().newDocument(key);
        while(stream.available() > 0){
            DocumentEntry entry = next(stream,charset,true);
            if(entry == null) break;
            else document.entries().add(entry);
        }
        return document;
    }

    private Document readArray(DataInputStream stream, Charset charset, String key) throws IOException{
        Document document = DocumentRegistry.getFactory().newArrayEntry(key);
        while(stream.available() > 0){
            DocumentEntry entry = next(stream,charset,false);
            if(entry == null) break;
            else document.entries().add(entry);
        }
        return document;
    }

    private String readString(DataInputStream stream, Charset charset) throws IOException {
        byte[] bytes = new byte[stream.readInt()];
        stream.read(bytes,0,bytes.length);
        return new String(bytes,charset);
    }
}