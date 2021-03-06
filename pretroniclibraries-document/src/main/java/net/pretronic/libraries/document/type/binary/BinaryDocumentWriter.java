/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:44
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

package net.pretronic.libraries.document.type.binary;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.ArrayEntry;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.entry.DocumentNode;
import net.pretronic.libraries.document.entry.PrimitiveEntry;
import net.pretronic.libraries.document.io.DocumentWriter;
import net.pretronic.libraries.utility.io.IORuntimeException;

import java.io.*;
import java.nio.charset.Charset;

/**
 * The @{@link BinaryDocumentWriter} writes the document structure into the fast and compact
 * binary format which is developed by pretronic.
 */
public class BinaryDocumentWriter implements DocumentWriter {

    public static final byte TYPE_BOOLEAN = 20;
    public static final byte TYPE_BYTE = 22;
    public static final byte TYPE_INTEGER = 23;
    public static final byte TYPE_LONG = 24;
    public static final byte TYPE_DOUBLE = 25;
    public static final byte TYPE_FLOAT = 26;
    public static final byte TYPE_SHORT = 27;
    public static final byte TYPE_CHARACTER = 28;
    public static final byte TYPE_STRING = 29;

    public static final byte TYPE_OBJECT_IN = 30;
    public static final byte TYPE_OBJECT_OUT = 31;

    public static final byte TYPE_ARRAY_IN = 32;
    public static final byte TYPE_ARRAY_OUT = 33;

    public static final byte TYPE_ATTRIBUTE_IN = 34;
    public static final byte TYPE_ATTRIBUTE_OUT = 35;
    public static final byte TYPE_ATTRIBUTE_EMPTY = 36;

    @Override
    public byte[] write(Document document) {
        return write(document,Charset.defaultCharset());
    }

    @Override
    public byte[] write(Document document, Charset charset) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(output,document,charset);
        return output.toByteArray();
    }

    @Override
    public String write(Document document, boolean pretty) {
        return new String(write(document));
    }

    @Override
    public void write(File location, Document document, boolean pretty) {
        write(location,Charset.defaultCharset(), document, pretty);
    }

    @Override
    public void write(File location, Charset charset, Document document, boolean pretty) {
        try {
            if(!location.exists() && !location.createNewFile()){
                throw new IllegalArgumentException("Could not create "+location.getName());
            }
            FileOutputStream output = new FileOutputStream(location);
            write(output,charset, document, pretty);
            output.close();
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    @Override
    public void write(OutputStream output, Document document, boolean pretty) {
        write(output,Charset.defaultCharset(), document, pretty);
    }

    @Override
    public void write(OutputStream output, Charset charset, Document document, boolean pretty) {
        try {
            writeObject(output instanceof DataOutputStream? (DataOutputStream) output :new DataOutputStream(output),charset,document);
        } catch (IOException exception) {
            throw new IORuntimeException(exception);
        }
    }

    @Override
    public void write(Writer output, Document document, boolean pretty) {
        throw new UnsupportedOperationException("It is not possible to write binary content to a writer.");
    }

    private void writeObject(DataOutputStream stream, Charset charset, Document document) throws IOException{
        stream.write(TYPE_OBJECT_IN);
        if(document.getKey() != null) writeString(stream,charset,document.getKey());
        writeAttributes(stream,charset,document);
        writeObjectEntries(stream,charset, document,true);
        stream.write(TYPE_OBJECT_OUT);
    }

    private void writeArray(DataOutputStream stream, Charset charset, ArrayEntry document) throws IOException{
        stream.write(TYPE_ARRAY_IN);
        if(document.getKey() != null) writeString(stream,charset,document.getKey());
        writeAttributes(stream,charset,document);
        writeObjectEntries(stream,charset, document,false);
        stream.write(TYPE_ARRAY_OUT);
    }

    private void writeObjectEntries(DataOutputStream stream, Charset charset, DocumentNode node, boolean key) throws IOException{
        for(DocumentEntry entry : node) {
            if(entry.isPrimitive()){
                writePrimitiveValue(stream,charset,entry.toPrimitive());
                if(key) writeString(stream,charset,entry.getKey());
            }else if(entry.isArray()){
                writeArray(stream,charset,entry.toArray());
            }else if(entry.isObject()){
                writeObject(stream,charset, entry.toDocument());
            }
        }
    }

    private void writePrimitiveValue(DataOutputStream stream, Charset charset, PrimitiveEntry entry) throws IOException{
        Object object = entry.getAsObject();
        if(object instanceof Boolean){
            stream.write(TYPE_BOOLEAN);
            stream.writeBoolean((boolean) object);
        }else if(object instanceof Byte){
            stream.write(TYPE_BYTE);
            stream.write((byte) object);
        }else if(object instanceof Integer){
            stream.write(TYPE_INTEGER);
            stream.writeInt((int) object);
        }else if(object instanceof Long){
            stream.write(TYPE_LONG);
            stream.writeLong((long) object);
        }else if(object instanceof Double){
            stream.write(TYPE_DOUBLE);
            stream.writeDouble((double) object);
        }else if(object instanceof Float){
            stream.write(TYPE_FLOAT);
            stream.writeFloat((float) object);
        }else if(object instanceof Short){
            stream.write(TYPE_SHORT);
            stream.writeShort((short) object);
        }else if(object instanceof Character){
            stream.write(TYPE_CHARACTER);
            stream.writeChar((char) object);
        }else if(object instanceof String){
            stream.write(TYPE_STRING);
            writeString(stream,charset, (String) object);
        }
        writeAttributes(stream,charset,entry);
    }

    private void writeAttributes(DataOutputStream stream, Charset charset, DocumentEntry entry) throws IOException{
        if(!entry.hasAttributes() || entry.getAttributes().isEmpty()){
            stream.write(TYPE_ATTRIBUTE_EMPTY);
        }else{
            stream.write(TYPE_ATTRIBUTE_IN);
            writeObjectEntries(stream,charset,entry.getAttributes(),true);
            stream.write(TYPE_ATTRIBUTE_OUT);
        }
    }


    private void writeString(DataOutputStream stream, Charset charset,String content) throws IOException{
        byte[] bytes = content.getBytes(charset);
        stream.writeInt(bytes.length);
        stream.write(bytes);
    }
}
