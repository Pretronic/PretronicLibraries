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

package net.pretronic.libraries.document.simple;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentAttributes;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.utility.reflect.Primitives;

import java.util.List;

public class SimpleDocumentAttribute extends AbstractDocumentNode implements DocumentAttributes {

    public SimpleDocumentAttribute() {}

    public SimpleDocumentAttribute(List<DocumentEntry> entries) {
        super(entries);
    }

    @Override
    public DocumentEntry getEntry(String key) {
        return findLocalEntry(key);
    }

    @Override
    public DocumentEntry getEntry(String[] keys, int offset) {
        return keys.length == 1 ? getEntry(keys[0]) : null;
    }

    @Override
    public DocumentAttributes set(String key, Object value) {
        if(!Primitives.isPrimitive(value)) throw new IllegalArgumentException("DocumentAttribute allows only primitive values.");
        remove(key);
        this.entries.add(Document.factory().newPrimitiveEntry(key, value));
        return this;
    }

    @Override
    public DocumentAttributes remove(String key) {
        DocumentEntry entry = findLocalEntry(key);
        if(entry != null) super.entries.remove(entry);
        return this;
    }

    @Override
    public DocumentAttributes clear() {
        super.clear();
        return this;
    }

    @Override
    public boolean isAttributes() {
        return true;
    }

    @Override
    public DocumentAttributes copy() {
        return new SimpleDocumentAttribute(super.entries);
    }
}
