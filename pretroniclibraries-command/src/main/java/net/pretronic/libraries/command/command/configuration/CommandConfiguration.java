/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:41
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

package net.pretronic.libraries.command.command.configuration;

public interface CommandConfiguration {

    boolean isEnabled();

    String getName();

    String getPermission();

    String getDescription();

    String[] getAliases();

    boolean hasAlias(String alias);

    static DefaultCommandConfigurationBuilder newBuilder(){
        return new DefaultCommandConfigurationBuilder();
    }

    static CommandConfiguration name(String name){
        return newBuilder().name(name).create();
    }

    static CommandConfiguration name(String name,String... aliases){
        return newBuilder().name(name).aliases(aliases).create();
    }
}
