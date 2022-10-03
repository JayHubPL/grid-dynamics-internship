package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PrimeStackPeekTest {
    
    @Test
    public void Peek_StackIsEmpty_ThrowException() {
        PrimeStack primeStack = new PrimeStack(3);

        assertThrows(IllegalStateException.class, () -> {
            primeStack.peek();
        });
    }

    @Test
    public void Peek_StackIsFull_CorrectlyReturnLastElement() {
        PrimeStack primeStack = new PrimeStack(3);

        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);

        assertEquals(5L, primeStack.peek());
        assertEquals(3, primeStack.size());
    }

    @Test
    public void Peek_WhileAddingAndRemovingToStack_CorrectlyReturnMostRecentElems() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);
        assertEquals(2L, primeStack.peek());
        primeStack.push(3L);
        assertEquals(3L, primeStack.peek());
        primeStack.push(5L);
        assertEquals(5L, primeStack.peek());

        assertEquals(3, primeStack.size());

        primeStack.pop();
        assertEquals(3L, primeStack.peek());
        primeStack.pop();
        assertEquals(2L, primeStack.peek());

        assertEquals(1, primeStack.size());
    }

    @Test
    public void Peek_StackGetsEmptyAfterRemovingLastElem_ThrowException() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);
        assertEquals(2L, primeStack.peek());

        primeStack.pop();
        assertThrows(IllegalStateException.class, () -> {
            primeStack.peek();
        });
    }

    @Test
    public void Peek_StackContainsImmutableElement_ReturnsCopy() {
        PrimeStack primeStack = new PrimeStack(10);
        Long num = Long.valueOf(2L);

        primeStack.push(num);
        num += 1L;

        assertNotEquals(num, primeStack.peek());
    }

}
