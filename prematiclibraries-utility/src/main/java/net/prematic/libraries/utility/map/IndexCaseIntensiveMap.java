/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Philipp Elvin Friedhoff
 * @since 08.12.19, 21:15
 *
 * The PrematicDatabaseQuery Project is under the Apache License, version 2.0 (the "License");
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

package net.prematic.libraries.utility.map;

import net.prematic.libraries.utility.map.caseintensive.CaseIntensiveMap;
import net.prematic.libraries.utility.map.index.IndexMap;

public interface IndexCaseIntensiveMap<V> extends IndexMap<String, V>, CaseIntensiveMap<V> {

}