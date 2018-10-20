package net.prematic.libraries.caching;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 20.10.18 16:21
 *
 */


import net.prematic.libraries.caching.object.CacheObjectLoader;
import net.prematic.libraries.caching.object.CacheObjectQuery;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PrematicCache<O> implements Cache<O> {

    public static ExecutorService EXECUTOR;

    private int maxSize;
    private long expireTime;
    private CacheExpireTask expireTask;
    private List<CacheEntry> objects;
    private Map<String,CacheObjectQuery<O>> queries;
    private Map<String,CacheObjectLoader<O>> loaders;

    public PrematicCache() {
        this.objects = new ArrayList<>();
        this.queries = new LinkedHashMap<>();
        this.loaders = new LinkedHashMap<>();
    }
    public List<O> getAll(){
        List<O> objects = new LinkedList<>();
        Iterator<CacheEntry> iterator = this.objects.iterator();
        CacheEntry entry = null;
        while((entry = iterator.next()) != null) objects.add(entry.getObject());
        return objects;
    }

    public List<CacheEntry> getObjects() {
        return objects;
    }
    public Map<String, CacheObjectQuery<O>> getQueries() {
        return queries;
    }
    public Map<String, CacheObjectLoader<O>> getLoaders() {
        return loaders;
    }
    public int size(){
        return this.objects.size();
    }
    public long getExpireTime() {
        return this.expireTime;
    }
    public List<CacheEntry> getAsList(){
        return this.objects;
    }
    public O get(String identifierName, Object identifier) {
        try{
            CacheObjectQuery<O> query = this.queries.get(identifierName.toLowerCase());
            if(query != null){
                Iterator<CacheEntry> iterator = this.objects.iterator();
                CacheEntry entry = null;
                while((entry = iterator.next()) != null) if(query.is(identifier,entry.getObject())) return entry.getObject();
            }
        }catch (Exception exception){}
        CacheObjectLoader<O> loader = this.loaders.get(identifierName.toLowerCase());
        return loader.load(identifier);
    }
    public PrematicCache<O> setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }
    public PrematicCache<O> setExpire(long expireTime, TimeUnit unit) {
        this.expireTime = unit.toMillis(expireTime);
        createExpireTask();
        return this;
    }
    public void insert(O object) {
        if(this.maxSize > 0 && size() >= this.maxSize) this.objects.remove(0);
        this.objects.add(new CacheEntry(System.currentTimeMillis(),object));
    }
    public O remove(String identifierName, Object identifier) {
        CacheObjectQuery<O> query = this.queries.get(identifierName.toLowerCase());
        if(query != null){
            Iterator<CacheEntry> iterator = this.objects.iterator();
            CacheEntry entry = null;
            while((entry = iterator.next()) != null){
                if(query.is(identifier,entry.getObject())){
                    iterator.remove();
                    return entry.getObject();
                }
            }
        }
        return null;
    }
    public void registerQuery(String name, CacheObjectQuery<O> query){
        this.queries.put(name.toLowerCase(),query);
    }
    public void registerLoader(String name, CacheObjectLoader<O> loader){
        this.loaders.put(name.toLowerCase(),loader);
    }
    private void createExpireTask(){
        if(EXECUTOR == null) EXECUTOR = Executors.newSingleThreadExecutor();
        shutdown();
        this.expireTask = new CacheExpireTask<>(this);
        EXECUTOR.execute(this.expireTask);
    }
    public void shutdown(){
        if(this.expireTask != null) this.expireTask.shutdown();
    }

    public class CacheEntry {

        private long entered;
        private O object;

        public CacheEntry(long entered, O object) {
            this.entered = entered;
            this.object = object;
        }
        public long getEntered() {
            return entered;
        }
        public O getObject() {
            return object;
        }
    }
}

