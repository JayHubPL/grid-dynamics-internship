package com.griddynamics;

import java.util.Map;
import java.util.Optional;

public abstract class Cache<K, V> implements EvictionStrategy {

    protected final int capacity;
    protected final Map<K, CachedValue<V>> entries;

    /**
     * Cache enables storing values based on some input data (key) for future use/read
     * decreasing the amount of requests to the backend and reducing latency. The {@code EvictionStrategy}
     * interfase must be implemented to provide a mechanism for removing outdated or unwanted data.
     * Provided {@code map} object should have constant read and write operations for the best
     * performance.
     * @param capacity maximum allowed number of entries in the cache
     * @param map allows for storing cache entries using key and value pair
     */
    public Cache(int capacity, Map<K, CachedValue<V>> map) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least equal to 1");
        }
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        this.capacity = capacity;
        entries = map;
    }

    /***
     * Allows to get {@code value}s from the cache using the {@code key}.
     * @param key used to retrieve {@code value} associated with it
     * @return {@code Optional} containg possibly-{@code null} value mapped to the provied {@code key}
     * or an empty {@code Optional} instance if no such mapping exists.
     */
    public Optional<V> get(K key) {
        CachedValue<V> cachedValue = entries.get(key);
        return (cachedValue == null) ? Optional.empty() : Optional.ofNullable(cachedValue.getValue());
    }

    /**
     * Creates/updates an entry in the cache. If no mapping for the given {@code key} was present
     * the pair of that key and {@code value} is added evicting enough entries to not pass the specified
     * maximum {@code capacity}. If there already was a {@code value} associated with the {@code key},
     * the value is overriden.
     * @param key used to map {@code value}
     * @param value which must be stored
     */
    public void put(K key, V value) {
        while (entries.size() >= capacity) {
            evict();
        }
        entries.put(key, new CachedValue<>(value));
    }

}
