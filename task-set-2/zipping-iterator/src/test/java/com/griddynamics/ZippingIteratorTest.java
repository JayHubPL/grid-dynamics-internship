package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;

public class ZippingIteratorTest {
    
    @Test
    public void HasNext_DoesNotChangeInternalState() {
        Iterator<Integer> iter1 = List.of(1).iterator();
        Iterator<String> iter2 = List.of("one").iterator();
        BiFunction<Integer, String, String> zippingFunction = (i, s) -> Integer.toString(i) + " = " + s;

        ZippingIterator<Integer, String, String> zippingIterator = new ZippingIterator<>(iter1, iter2, zippingFunction);

        zippingIterator.hasNext();
        zippingIterator.hasNext();
        zippingIterator.hasNext();

        assertTrue(zippingIterator.hasNext());
        assertEquals("1 = one", zippingIterator.next());
        assertFalse(zippingIterator.hasNext());
    }

    @Test
    public void HasNext_IteratorsHaveDifferentLenghts_StopsWhenReachingTheEndOfShorterOne() {
        Iterator<Integer> iter1 = List.of(1, 2, 3).iterator();
        Iterator<Integer> iter2 = List.of(10, 20, 30, 40).iterator();
        BiFunction<Integer, Integer, Integer> zippingFunction = (a, b) -> a + b;

        ZippingIterator<Integer, Integer, Integer> zippingIterator = new ZippingIterator<>(iter1, iter2, zippingFunction);

        assertTrue(zippingIterator.hasNext());
        List<Integer> expected = List.of(11, 22, 33);
        List<Integer> actual = new ArrayList<>();
        zippingIterator.forEachRemaining(actual::add);

        assertIterableEquals(expected, actual);
    }

    @Test
    public void HasNext_GivenEmptyIterators_CreateEmptyZippingIterator() {
        Iterator<Integer> emptyIterator = Collections.emptyIterator();
        BiFunction<Integer, Integer, Integer> zippingFunction = (a, b) -> a + b;

        ZippingIterator<Integer, Integer, Integer> zippingIterator = new ZippingIterator<>(emptyIterator, emptyIterator, zippingFunction);

        assertFalse(zippingIterator.hasNext());
    }

    @Test
    public void ZippingFunction_GivenNullParameters_ThrowException() {
        Iterator<Integer> emptyIterator = Collections.emptyIterator();
        BiFunction<Integer, Integer, Integer> zippingFunction = (a, b) -> a + b;

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> {
                new ZippingIterator<>(null, emptyIterator, zippingFunction);
            }),

            () -> assertThrows(IllegalArgumentException.class, () -> {
                new ZippingIterator<>(emptyIterator, null, zippingFunction);
            }),

            () -> assertThrows(IllegalArgumentException.class, () -> {
                new ZippingIterator<>(emptyIterator, emptyIterator, null);
            })
        );
    }
}
