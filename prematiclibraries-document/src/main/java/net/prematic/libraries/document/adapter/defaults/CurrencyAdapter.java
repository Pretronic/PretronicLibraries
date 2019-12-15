/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 10.06.19 16:47
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

package net.prematic.libraries.document.adapter.defaults;

import net.prematic.libraries.document.DocumentRegistry;
import net.prematic.libraries.document.adapter.DocumentAdapter;
import net.prematic.libraries.document.entry.DocumentBase;
import net.prematic.libraries.document.entry.DocumentEntry;
import net.prematic.libraries.utility.reflect.TypeReference;

import java.util.Currency;

public class CurrencyAdapter implements DocumentAdapter<Currency> {

    @Override
    public Currency read(DocumentBase entry, TypeReference<Currency> type) {
        if(entry.isPrimitive()) return Currency.getInstance(entry.toPrimitive().getAsString());
        throw new IllegalArgumentException("Can't convert a object to a currency.");
    }

    @Override
    public DocumentEntry write(String key, Currency object) {
        return DocumentRegistry.getFactory().newPrimitiveEntry(key, object.getCurrencyCode());
    }
}
