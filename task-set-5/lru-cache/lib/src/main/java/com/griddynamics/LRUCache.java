package com.griddynamics;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache<K, V> extends Cache<K, V> {

    /*
     * This value helps preventing map from rehasing which is a
     * costly and unnecesary action which must be avoided in cache
     * implementations.
     */
    private static final int MAP_CAPPACITY_MULT = 2;

    /*
     * Default load factor of the HashMap is 0.75.
     */
    private static final float MAP_LOAD_FACTOR = 0.75f;

    /**
     * Creates a {@code Cache} implementing LRU eviction strategy using {@code LinkedHashMap}
     * @param capacity defines maximum number of cached entries
     */
    public LRUCache(int capacity) {
        super(capacity, new LinkedHashMap<K, CachedValue<V>>(MAP_CAPPACITY_MULT * capacity, MAP_LOAD_FACTOR, true) {
            
            /*
            * The method:
            *     protected boolean removeEldestEntry(Entry<K, V> eldest)
            * is responisible for handling eviction in the case of Segmented LRU strategy
            * which is being used in this LRUCache class. No further implementation is
            * required.
            */
            @Override
            protected boolean removeEldestEntry(Entry<K, CachedValue<V>> eldest) {
                return size() > capacity;
            }

        });
    }

}
