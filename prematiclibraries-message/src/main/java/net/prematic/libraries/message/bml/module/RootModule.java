/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 27.12.19, 14:23
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

package net.prematic.libraries.message.bml.module;

import net.prematic.libraries.message.bml.variable.VariableSet;

public class RootModule implements Module{

    private Module next;

    public Module getNext() {
        return next;
    }

    @Override
    public void setNext(Module next) {
        this.next = next;
    }

    @Override
    public void pushParameter(Module module) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object process(VariableSet variables) {
        return next.process(variables);
    }

    @Override
    public void process(StringBuilder builder, VariableSet variables) {
        next.process(builder,variables);
    }
}