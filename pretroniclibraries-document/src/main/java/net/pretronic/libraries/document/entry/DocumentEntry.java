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

package net.pretronic.libraries.document.entry;

/**
 * A @{@link DocumentEntry} represents any kind of entry.
 */
public interface DocumentEntry extends DocumentBase{

    String getKey();

    void setKey(String key);

    /**
     * Get all attributes of this entry
     *
     * @return All attributes
     */
    DocumentAttributes getAttributes();

    /**
     * Set a set of attributes.
     *
     * @param attributes The attributes to set
     */
    void setAttributes(DocumentAttributes attributes);

    /**
     * Check if this entry has attributes.
     *
     * @return True if the attribute data is available
     */
    boolean hasAttributes();


    default DocumentEntry copy(){
        return copy(getKey());
    }

    /**
     * Copy this entry with the included data and define a new key.
     *
     * @param key The new key of the entry
     * @return The new entry
     */
    DocumentEntry copy(String key);

}
