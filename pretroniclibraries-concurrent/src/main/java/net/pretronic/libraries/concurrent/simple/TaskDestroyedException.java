/*
 * (C) Copyright 2020 The PretronicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 11.03.20, 18:42
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

package net.pretronic.libraries.concurrent.simple;

import net.pretronic.libraries.concurrent.Task;
import net.pretronic.libraries.concurrent.TaskState;

/**
 * This exception is thrown, when someone tyies to start an destroyed task (Destroyed tasks can not start anymore).
 */
public class TaskDestroyedException extends RuntimeException{

    protected TaskDestroyedException() {
    }

    public TaskDestroyedException(String message) {
        super(message);
    }

    public TaskDestroyedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskDestroyedException(Throwable cause) {
        super(cause);
    }

    public TaskDestroyedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void validate(Task task){
        if(task.getState() == TaskState.DESTROYED) throw new TaskDestroyedException("Task "+task.getName()+" is destroyed and can not be used.");
    }
}
