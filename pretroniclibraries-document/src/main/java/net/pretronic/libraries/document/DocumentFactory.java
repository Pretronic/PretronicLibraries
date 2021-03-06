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

package net.pretronic.libraries.document;

import net.pretronic.libraries.document.entry.ArrayEntry;
import net.pretronic.libraries.document.entry.DocumentAttributes;
import net.pretronic.libraries.document.entry.DocumentEntry;
import net.pretronic.libraries.document.entry.PrimitiveEntry;
import net.pretronic.libraries.document.simple.SimpleDocumentFactory;

import java.util.List;

/**
 * The {@link DocumentFactory} creates new instances of the different entry types.
 * A factory is registered in the {@link DocumentRegistry}, only one factory can exist per application.
 *
 * <p>Default implementation: {@link SimpleDocumentFactory}</p>
 */
public interface DocumentFactory {

    /** Create a new @{@link DocumentContext} from the default factory.
     *
     * @return The new created context
     */
    DocumentContext newContext();

    /** Create a new @{@link Document} from the default factory.
     *
     * @return The new created context
     */
    Document newDocument();

    Document newDocument(String key);

    Document newDocument(String key, List<DocumentEntry> entries);


    PrimitiveEntry newPrimitiveEntry(String key, Object object);

    ArrayEntry newArrayEntry(String key);

    ArrayEntry newArrayEntry(String key, List<DocumentEntry> entries);


    DocumentAttributes newAttributes();

    DocumentAttributes newAttributes(List<DocumentEntry> entries);
}
