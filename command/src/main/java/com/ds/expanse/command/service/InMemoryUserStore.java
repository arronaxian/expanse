package com.ds.expanse.command.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserStore {
    private final Logger log = LogManager.getLogger(InMemoryUserStore.class);

    private static ThreadLocal<InMemoryUserStore> threadLocalValue = ThreadLocal.withInitial(InMemoryUserStore::new);
    private static Map<String, Object> userStore = new ConcurrentHashMap<>();

    public static InMemoryUserStore getInMemoryUserStore() {
        return threadLocalValue.get();
    }

    public void put(String name, Object value) {
        userStore.put(name, value);
    }

    public Object get(String name) {
        return userStore.get(name);
    }

    public void clear() {
        userStore.clear();
    }
}
