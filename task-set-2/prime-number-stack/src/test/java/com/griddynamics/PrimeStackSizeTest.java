package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PrimeStackSizeTest {
    
    @Test
    public void Size_StackIsEmpty_ReturnsZero() {
        PrimeStack primeStack = new PrimeStack(10);

        assertEquals(0, primeStack.size());
    }

    @Test
    public void Size_AddingElemsToStack_ReturnsNumberOfElems() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);
        assertEquals(1, primeStack.size());
        primeStack.push(3L);
        assertEquals(2, primeStack.size());
        primeStack.push(5L);
        assertEquals(3, primeStack.size());
    }

    @Test
    public void Size_RemovingElemsFromStack_SizeShouldDecrease() {
        PrimeStack primeStack = new PrimeStack(10);

        primeStack.push(2L);
        primeStack.push(3L);
        primeStack.push(5L);
        
        assertEquals(3, primeStack.size());
        primeStack.pop();
        assertEquals(2, primeStack.size());
        primeStack.pop();
        assertEquals(1, primeStack.size());
        primeStack.pop();
        assertEquals(0, primeStack.size());
    }
}
