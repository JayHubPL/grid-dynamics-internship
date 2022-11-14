package com.griddynamics;

public interface EvictionStrategy {

    /**
     * This method is invoked when the cache should perform eviction.
     * Each evicion strategy has its own procedure to accomplish this task.
     * In general, minimum of one element should be removed from the cache
     * and no new elements should take its place in order to enable
     * adding/updating cache entries without going over the maximum capacity
     * of the given cache.
     */
    default void evict() {};

}
