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

package net.pretronic.libraries.event;

import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.lang.reflect.Method;

/**
 * Contains information about a listener and when this method should be executed.
 */
public class MethodEntry {

    private final byte priority;
    private final ObjectOwner owner;
    private final Object listener;
    private final Method method;

    public MethodEntry(byte priority, ObjectOwner owner, Object listener, Method methode) {
        this.priority = priority;
        this.owner = owner;
        this.listener = listener;
        this.method = methode;
    }

    public ObjectOwner getOwner() {
        return this.owner;
    }

    public int getPriority() {
        return this.priority;
    }

    public Object getListener() {
        return this.listener;
    }

    public Method getMethod() {
        return this.method;
    }

    public void invoke(Object event){
        try{
            this.method.invoke(this.listener,event);
        }catch (Exception exception){
            throw new EventException("Could not execute listener "+listener,exception);
        }
    }
}
