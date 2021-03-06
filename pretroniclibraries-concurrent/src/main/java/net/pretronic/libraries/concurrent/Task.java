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

package net.pretronic.libraries.concurrent;

import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A task allows you to execute one or more runnables, it is an advancement thread.
 *
 * <p>You are able to delay a task and set a period. So all runnables will be executed
 * after the delay is finished and every period again.</p>
 *
 * <p>Tasks are created over a task scheduler. This scheduler manages all tasks and their threads.
 * If you have created a task you can start and stop them.</p>
 */
public interface Task extends Runnable{

    /**
     * Get the id of the task.
     *
     * @return The task id
     */
    int getID();

    /**
     * Every task can have a name. The name is useful for identifying a task and using a task simpler.
     *
     * @return The name of the task.
     */
    String getName();

    /**
     * Get the state of the task (@See TaskState.class)
     *
     * @return The current state of the task.
     */
    TaskState getState();

    /**
     * Get the delay of the task.
     *
     * <p>The {@link #call()} will be executed after the delay.</p>
     *
     * @return The delay of the task.
     */
    long getDelay();

    /**
     * Get the interval of the task.
     *
     * <p>The call will be executed every period again.</p>
     *
     * @return The period of the task.
     */
    long getInterval();

    /**
     * Get the owner of the task.
     *
     * <p>Every task has an owner, all task from a specified process can be identified.
     * So it is possible to split your project in modules and start or stop them.</p>
     *
     * @return The owner of the task.
     */
    ObjectOwner getOwner();

    /**
     * Get the task scheduler which is managing this task.
     *
     * @return The task scheduler of the task
     */
    TaskScheduler getScheduler();

    /**
     * Get all attached runnables of the task.
     *
     * @return A collection of all attached runnables
     */
    Collection<Runnable> getRunnables();

    /**
     * Check if the task is running or not.
     *
     * @return True if the task is running
     */
    boolean isRunning();

    /**
     * Check if the task is running respectively executing async.
     *
     * @return True if the task is executed async
     */
    boolean isAsync();

    /**
     * Append a runnable to this task.
     *
     * <p>Every runnable will be executed on every call (The call is automatically executed after the delay and will be executed every period again).</p>
     *
     * @param runnable The runnable which should be attached.
     * @return The current task.
     */
    Task append(Runnable runnable);

    /**
     * Append more then one runnable to the task.
     *
     * @param runnable The runnable which should be added.
     * @return The current task.
     */
    Task append(Runnable... runnable);

    /**
     * Remove an attached runnable form the task.
     *
     * <p>The runnable will no longer be executed.</p>
     *
     * @param runnable The runnable which should be removed
     * @return The current task.
     */
    Task remove(Runnable runnable);

    /**
     * Set a start delay to this task.
     *
     * @param delay The new time.
     * @param unit In which unit your time is.
     * @return The current task.
     */
    Task setDelay(long delay, TimeUnit unit);

    /**
     * Set a new period / interval to this task.
     *
     * <p>If a period is set the task will not stop automatically, it will be run again and again.</p>
     *
     * @param period The new time.
     * @param unit In Which unit your time is.
     * @return The current task.
     */
    Task setInterval(long period, TimeUnit unit);

    /**
     * Add a task listener will be executed by every state change.
     *
     * <p>The task future contains information about the state.</p>
     *
     * @param listener The task listener
     * @return The current task.
     */
    Task addListener(Consumer<TaskFuture> listener);

    /**
     * Clear all attached runnables from this task.
     */
    void clear();

    /**
     * Execute all running runnables.
     *
     * <p>The call is automatically executed after the delay and will be executed every period again</p>
     *
     * Important: This will execute all runnables immediately.
     */
    void call();

    /**
     * Start a stopped task.
     */
    void start();

    /**
     * The task is stopped, some tasks can be restarted.
     */
    void stop();

    /**
     * Destroy this task, the task is stopped and can no longer be used.
     */
    void destroy();


}
