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

package net.pretronic.libraries.document.type.yaml;

import net.pretronic.libraries.document.Document;
import net.pretronic.libraries.document.io.DocumentReader;
import net.pretronic.libraries.utility.parser.StringParser;

/**
 * The @{@link YamlDocumentReader} reads the document structure from the yaml (https://yaml.org/spec/) file format.
 */
public class YamlDocumentReader implements DocumentReader {

    @Override
    public Document read(StringParser parser) {
        return new YamlParser().parse(parser);
    }
}
