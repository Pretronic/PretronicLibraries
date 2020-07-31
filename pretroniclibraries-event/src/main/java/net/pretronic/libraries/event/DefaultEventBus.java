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

import net.pretronic.libraries.event.executor.BiConsumerEventExecutor;
import net.pretronic.libraries.event.executor.ConsumerEventExecutor;
import net.pretronic.libraries.event.executor.EventExecutor;
import net.pretronic.libraries.event.executor.MethodEventExecutor;
import net.pretronic.libraries.event.network.DefaultNetworkEventOrigin;
import net.pretronic.libraries.event.network.EventOrigin;
import net.pretronic.libraries.event.network.NetworkEvent;
import net.pretronic.libraries.utility.GeneralUtil;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.annonations.Internal;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DefaultEventBus implements EventBus {

    private final NetworkEventHandler networkEventHandler;
    private final Executor executor;
    private final Map<Class<?>, List<EventExecutor>> executors;
    private final Map<Class<?>,Class<?>> mappedClasses;

    public DefaultEventBus() {
        this(GeneralUtil.getDefaultExecutorService());
    }

    public DefaultEventBus(Executor executor) {
        this(executor,new NetworkEventHandler());
    }

    public DefaultEventBus(NetworkEventHandler networkEventHandler) {
        this(GeneralUtil.getDefaultExecutorService(),networkEventHandler);
    }

    public DefaultEventBus(Executor executor,NetworkEventHandler networkEventHandler) {
        this.networkEventHandler = networkEventHandler;
        this.executor = executor;
        this.executors = new LinkedHashMap<>();
        this.mappedClasses = new LinkedHashMap<>();
    }

    @Override
    public void subscribe(ObjectOwner owner, Object listener) {
        Objects.requireNonNull(owner,"Owner can't be null.");
        Objects.requireNonNull(listener,"Listener can't be null.");

        for(Method method : listener.getClass().getDeclaredMethods()){
            try{
                Listener info = method.getAnnotation(Listener.class);
                if(info != null && method.getParameterTypes().length == 1){
                    Class<?> eventClass = method.getParameterTypes()[0];
                    Class<?> mappedClass = this.mappedClasses.get(eventClass);
                    if(mappedClass == null) mappedClass = eventClass;

                    addExecutor(mappedClass,new MethodEventExecutor(owner,info.priority(),listener,eventClass,method));
                }
            }catch (Exception exception){
                throw new IllegalArgumentException("Could not register listener "+listener,exception);
            }
        }
    }

    @Override
    public <T> void subscribe(ObjectOwner owner, Class<T> eventClass, Consumer<T> handler, byte priority) {
        Objects.requireNonNull(owner,"Owner can't be null.");
        Objects.requireNonNull(eventClass,"Event type can't be null.");
        Objects.requireNonNull(handler,"Handler can't be null.");

        Class<?> mappedClass = this.mappedClasses.get(eventClass);
        if(mappedClass == null) mappedClass = eventClass;

        addExecutor(mappedClass,new ConsumerEventExecutor<>(owner,priority,eventClass,handler));
    }

    @Override
    public <T> void subscribe(ObjectOwner owner, Class<T> eventClass, BiConsumer<T, EventOrigin> handler, byte priority) {
        Objects.requireNonNull(owner,"Owner can't be null.");
        Objects.requireNonNull(eventClass,"Event type can't be null.");
        Objects.requireNonNull(handler,"Handler can't be null.");

        Class<?> mappedClass = this.mappedClasses.get(eventClass);
        if(mappedClass == null) mappedClass = eventClass;

        addExecutor(mappedClass,new BiConsumerEventExecutor<>(owner,priority,eventClass,handler));
    }

    @Override
    public void unsubscribe(Object listener) {
        Objects.requireNonNull(listener,"Listener can't be null.");
        executors.forEach((event, executors) -> Iterators.removeSilent(executors,
                executor -> executor instanceof MethodEventExecutor
                        && ((MethodEventExecutor) executor).getListener().equals(listener)));
    }

    @Override
    public void unsubscribe(Consumer<?> handler) {
        Objects.requireNonNull(handler,"Handler can't be null.");
        executors.forEach((event, executors) -> Iterators.removeSilent(executors,
                executor -> executor instanceof ConsumerEventExecutor
                        && ((ConsumerEventExecutor) executor).getConsumer().equals(handler)));
    }

    @Override
    public void unsubscribe(BiConsumer<?, EventOrigin> handler) {
        Objects.requireNonNull(handler,"Handler can't be null.");
        executors.forEach((event, executors) -> Iterators.removeSilent(executors,
                executor -> executor instanceof ConsumerEventExecutor
                        && ((BiConsumerEventExecutor) executor).getConsumer().equals(handler)));
    }

    @Override
    public void unsubscribe(ObjectOwner owner) {
        Objects.requireNonNull(owner,"Owner can't be null.");
        executors.forEach((event, executors) -> Iterators.removeSilent(executors, executor -> executor.getOwner().equals(owner)));
    }

    @Override
    public void unsubscribeAll(Class<?> eventClass) {
        Objects.requireNonNull(eventClass,"Class can't be null.");
        this.executors.remove(eventClass);
    }

    @Override
    public void addExecutor(Class<?> eventClass, EventExecutor executor) {
        List<EventExecutor> executors = this.executors.computeIfAbsent(eventClass, k -> new ArrayList<>());
        executors.add(executor);
        sortByPriority(executors);
    }

    @Override
    public <T, E extends T> E callEvent(Class<T> executionClass, E event) {
        Objects.requireNonNull(executionClass,"Class can't be null.");
        Objects.requireNonNull(event,"Event can't be null.");
        callEvents(null,executionClass,new Object[]{event});
        return event;
    }

    @Override
    public <T, E extends T> void callEventAsync(Class<T> executionClass, E event, Consumer<T> callback) {
        if(callback != null) executor.execute(()-> callback.accept(callEvent(executionClass,event)));
        else executor.execute(()-> callEvent(event));
    }

    @Override
    public <T> void callEvents(EventOrigin origin,Class<T> executionClass, Object... events) {
        callEventsInternal(origin,executionClass,events);
    }

    @Override
    public <T> void callEventsAsync(EventOrigin origin,Class<T> executionClass, Runnable callback, Object... events) {
        if(callback != null){
            executor.execute(()->{
                callEventsInternal(origin,executionClass,events);
                callback.run();
            });
        }else executor.execute(()-> callEvent(events));
    }

    @Override
    public Class<?> getMappedClass(Class<?> original) {
        return this.mappedClasses.get(original);
    }

    @Override
    public void registerMappedClass(Class<?> original, Class<?> mapped) {
        this.mappedClasses.put(original,mapped);
    }

    @Internal
    private <T> void callEventsInternal(EventOrigin origin,Class<T> executionClass, Object[] events){
        List<EventExecutor> executors = this.executors.get(executionClass);
        if(networkEventHandler.isNetworkEvent(executionClass)){
            networkEventHandler.handleNetworkEvents(origin,executionClass,events);
        }
        if(executors != null) executors.forEach(executor -> executor.execute(events));
    }

    @Internal
    private void sortByPriority(List<EventExecutor> executors){
        //Sort all listeners by the priority.
        executors.sort((o1, o2) -> o1.getPriority() >= o2.getPriority()?0:-1);
    }

    public static class NetworkEventHandler {

        public EventOrigin getLocal(){
            return DefaultNetworkEventOrigin.newInstance();
        }

        public boolean isNetworkEvent(Class<?> executionClass){
            return executionClass.getAnnotation(NetworkEvent.class) != null;
        }

        public void handleNetworkEvents(EventOrigin origin,Class<?> executionClass, Object[] events){
            //Unused, when not implemented
        }

    }
}
