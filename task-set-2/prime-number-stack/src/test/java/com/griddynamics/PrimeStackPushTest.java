package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

public class PrimeStackPushTest {
    
    @Test
    public void Push_StackCapacityReached_ThrowException() {
        PrimeStack primeStack = new PrimeStack(3);

        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);

        assertThrows(IllegalStateException.class, () -> {
            primeStack.push(7L);
        });
    }

    @Test
    public void Push_ZeroAndPositiveOne_ThrowException() {
        PrimeStack primeStack = new PrimeStack(3);

        assertThrows(IllegalArgumentException.class, () -> {
            primeStack.push(0L);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            primeStack.push(1L);
        });
    }

    @Test
    public void Push_NegativeNumber_ThrowException() {
        PrimeStack primeStack = new PrimeStack(3);

        assertThrows(IllegalArgumentException.class, () -> {
            primeStack.push(-1L);
        });
    }

    @Test
    public void Push_VeryLargePrimeNumber_AddSuccessfully() {
        PrimeStack primeStack = new PrimeStack(3);
        long largePrime = (long) Math.pow(2, 62) - 57L;

        primeStack.push(largePrime);

        assertEquals(1, primeStack.size());
        assertEquals(largePrime, primeStack.peek());
    }

    @Test
    public void Push_NullValue_ThrowException() {
        PrimeStack primeStack = new PrimeStack(3);

        assertThrows(IllegalArgumentException.class, () -> {
            primeStack.push(null);
        });
    }

    @Test
    public void Push_AddingPrimesWithinCapacity_ReturnElementsInCorrectOrder() {
        PrimeStack primeStack = new PrimeStack(3);

        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);
        
        assertEquals(3, primeStack.size());
        assertEquals(5L, primeStack.pop());
        assertEquals(3L, primeStack.pop());
        assertEquals(2L, primeStack.pop());
        assertEquals(0, primeStack.size());
    }

    @Test
    public void Push_AddingNewElement_CreatesImmutableCopy() {
        PrimeStack primeStack = new PrimeStack(10);
        Long num = Long.valueOf(2L);

        primeStack.push(num);
        num += 1L;

        assertNotEquals(num, primeStack.pop());
    }

    @Test
    public void Push_StackContainsPrimesBiggerThanNewOne_ThrowException() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(3L);

        assertThrows(IllegalStateException.class, () -> {
            primeStack.push(2L);
        });
    }

    @Test
    public void Push_StackContainsPrimeEqualToNewOne_ThrowException() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(3L);

        assertThrows(IllegalStateException.class, () -> {
            primeStack.push(3L);
        });
    }
    
}
