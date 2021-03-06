/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:45
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

package net.pretronic.libraries.plugin.description.mainclass;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.entry.DocumentEntry;

import java.util.Map;

public class MultipleMainClass implements MainClass{

    private final Map<String,String> mainClasses;

    public MultipleMainClass(Map<String, String> mainClasses) {
        this.mainClasses = mainClasses;
    }

    @Override
    public Map<String, String> getMainClasses() {
        return mainClasses;
    }

    @Override
    public String getMainClass(String instanceName) {
        return mainClasses.get(instanceName);
    }

    @Override
    public DocumentEntry write(String key) {
        Document document = Document.newDocument(key);
        mainClasses.forEach(document::set);
        return document;
    }
}
