package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

public class PrimeStackIteratorTest {

    @Test
    public void Iterator_StackHasElements_IteratesCorrectly() {
        PrimeStack primeStack = new PrimeStack(10);
        
        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);

        Iterator<Long> primeStackIterator = primeStack.iterator();

        assertTrue(primeStackIterator.hasNext());
        assertEquals(5L, primeStackIterator.next());
        assertTrue(primeStackIterator.hasNext());
        assertEquals(3L, primeStackIterator.next());
        assertTrue(primeStackIterator.hasNext());
        assertEquals(2L, primeStackIterator.next());
        assertFalse(primeStackIterator.hasNext());
    }

    @Test
    public void Iterator_StackIsEmpty_ShouldNotHaveNext() {
        PrimeStack primeStack = new PrimeStack(10);

        assertFalse(primeStack.iterator().hasNext());
    }

    @Test
    public void Iterator_IteratesThroughElemsWhilePopingFromStack_ShouldIterateOnTheImmutableCopy() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);

        int iterations = 0;
        for (Iterator<Long> iter = primeStack.iterator(); iter.hasNext(); iterations++) {
            iter.next();
            primeStack.pop();
        }

        assertEquals(0, primeStack.size());
        assertEquals(3, iterations);
    }

    @Test
    public void Iterator_IteratesThroughElemsWhilePushingOnStack_ShouldIterateOnTheImmutableCopy() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);

        int iterations = 0;
        for (Iterator<Long> iter = primeStack.iterator(); iter.hasNext(); iterations++) {
            iter.next();
            primeStack.push(2L);
        }

        assertEquals(6, primeStack.size());
        assertEquals(3, iterations);
    }

    @Test
    public void Iterator_HasNextDoesNotChangeInternalState() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);

        Iterator<Long> primeStackIterator = primeStack.iterator();

        primeStackIterator.hasNext();
        primeStackIterator.hasNext();
        primeStackIterator.hasNext();

        assertTrue(primeStackIterator.hasNext());
        assertEquals(2L, primeStackIterator.next());
    }
}
