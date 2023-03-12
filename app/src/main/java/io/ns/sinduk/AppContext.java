package io.ns.sinduk;

import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

    private static final ConcurrentHashMap<Class<?>, Object> registry = new ConcurrentHashMap<>();

    public static  <T> void registerObject(Class<T> clazz, T object) {
        registry.put(clazz, object);
    }

    public static  <T> T getObject(Class<T> clazz) {
        return clazz.cast(registry.get(clazz));
    }

}
