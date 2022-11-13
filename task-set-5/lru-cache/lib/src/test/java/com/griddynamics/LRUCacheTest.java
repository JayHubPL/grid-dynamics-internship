package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class LRUCacheTest {
    
    @Test
    public void constructor_CapacityIsNegative_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            new LRUCache<String, String>(-5);
        });
    }

    @Test
    public void constructor_CapacityIsZero_ShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            new LRUCache<String, String>(0);

        });
    }

    @Test
    public void constructor_CapacityIsPositive_ShouldNotThrow() {
        assertDoesNotThrow(() -> {
            new LRUCache<String, String>(1);
        });
    }

    @Test
    public void put_AddingNewItemWhileCapacityIsFull_ShouldEvict() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("Key1", "Value1");
        assertEquals(1, getCacheSize(cache));
        cache.put("Key2", "Value2");
        assertEquals(2, getCacheSize(cache));
        cache.put("Key3", "Value3");
        assertEquals(2, getCacheSize(cache));
    }

    @Test
    public void put_ValueIsNull_ShouldCorrectlyAdd() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        cache.put("Key", null);
        assertEquals(1, getCacheSize(cache));
        assertNull(cache.getUnwrap("Key"));
    }

    @Test
    public void get_ValueIsNull_ShouldReturnOptionalWithNull() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        cache.put("Key", null);
        var optCachedValue = cache.get("Key");
        assertTrue(optCachedValue.isPresent());
        var cachedValue = optCachedValue.get();
        assertEquals(ValueAcquisitionStatus.OK, cachedValue.getValueStatus());
        var value = cachedValue.unwrap();
        assertNull(value);
    }

    @Test
    public void get_NoMappingForTheKey_ShouldReturnEmptyOptional() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        var optCachedValue = cache.get("Key");
        assertTrue(optCachedValue.isEmpty());
    }

    @Test
    public void get_ValueWasNotComputed_ShouldReturnCachedValueWithCorrectFailureStatus() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        addFailedCacheRecord(cache, "Key");
        var optCachedValue = cache.get("Key");
        assertTrue(optCachedValue.isPresent());
        var cachedValue = optCachedValue.get();
        assertEquals(ValueAcquisitionStatus.FAILED_TO_COMPUTE, cachedValue.getValueStatus());
        assertThrows(IllegalStateException.class, () -> {
            cachedValue.unwrap();
        });
    }

    @Test
    public void getUnwrap_ValueWasNotComputed_ShouldThrowOnUnwrap() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        addFailedCacheRecord(cache, "Key");
        assertThrows(IllegalStateException.class, () -> {
            cache.getUnwrap("Key");
        });
    }

    @Test
    public void getUnwrap_NoMappingForTheGivenKey_ShouldReturnEmptyOptional() {
        LRUCache<String, String> cache = new LRUCache<>(5);
        var optValue = cache.get("Key");
        assertTrue(optValue.isEmpty());
    }

    private <K> int getCacheSize(LRUCache<K, ?> cache) {
        return getEntriesMap(cache).size();
    }

    private <K> void addFailedCacheRecord(LRUCache<K, ?> cache, K key) {
        var entries = getEntriesMap(cache);
        entries.put(key, CachedValue.failedToCompute());
    }

    @SuppressWarnings("unchecked")
    private <K> Map<K, CachedValue<?>> getEntriesMap(LRUCache<K, ?> cache) {
        try {
            var entriesField = cache.getClass().getSuperclass().getDeclaredField("entries");
            entriesField.setAccessible(true);
            var entries = (Map<K, CachedValue<?>>) entriesField.get(cache);
            return entries;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
