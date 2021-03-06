/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:39
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

package net.pretronic.libraries.utility.map;

import net.pretronic.libraries.utility.map.caseintensive.CaseIntensiveLinkedHashMap;

import java.util.Map;

public class IndexCaseIntensiveLinkedHashMap<V> extends CaseIntensiveLinkedHashMap<V> implements IndexCaseIntensiveMap<V> {

    @Override
    public boolean containsIndex(int index) {
        return this.size() > index;
    }

    @Override
    public V getIndex(int index) {
        int i = 0;
        for (Map.Entry<String, V> entry : this.entrySet()) {
            if(i == index) return entry.getValue();
            i++;
        }
        return null;
    }
}
