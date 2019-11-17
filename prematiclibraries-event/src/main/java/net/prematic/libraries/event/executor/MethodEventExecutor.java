/*
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 20.08.19, 19:12
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

package net.prematic.libraries.event.executor;

import net.prematic.libraries.event.EventException;
import net.prematic.libraries.utility.interfaces.ObjectOwner;

import java.lang.reflect.Method;

public class MethodEventExecutor implements EventExecutor{

    private final ObjectOwner owner;
    private final byte priority;
    private final Object listener;
    private final Class<?> allowedClass;
    private final Method method;

    public MethodEventExecutor(ObjectOwner owner, byte priority, Object listener,Class<?> allowedClass, Method method) {
        this.owner = owner;
        this.priority = priority;
        this.listener = listener;
        this.allowedClass = allowedClass;
        this.method = method;
    }

    @Override
    public byte getPriority() {
        return priority;
    }

    @Override
    public ObjectOwner getOwner() {
        return owner;
    }

    public Object getListener() {
        return listener;
    }

    @Override
    public void execute(Object... events) {
        for (Object event : events) {
            if(allowedClass.isAssignableFrom(event.getClass())){
                try{
                    this.method.invoke(this.listener,event);
                }catch (Exception exception){
                    throw new EventException("Could not execute listener "+listener,exception);
                }
            }
        }
    }
}
