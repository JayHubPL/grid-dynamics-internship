package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.griddynamics.exceptions.StackEmptyException;

public class PrimeStackPopTest {
    
    @Test
    public void Pop_StackIsEmpty_ThrowException() {
        PrimeStack primeStack = new PrimeStack(3);

        assertThrows(StackEmptyException.class, () -> {
            primeStack.pop();
        });
    }

    @Test
    public void Pop_StackIsFull_CorrectlyRemoveElementAndDecreaseSize() {
        PrimeStack primeStack = new PrimeStack(1);

        primeStack.push(2L);

        assertEquals(2L, primeStack.pop());
        assertEquals(0, primeStack.size());
    }

    @Test
    public void Pop_RemovingPrimes_ReturnElementsInCorrectOrder() {
        PrimeStack primeStack = new PrimeStack(3);

        primeStack.push(2L);
        primeStack.push(3L);

        assertEquals(3L, primeStack.pop());

        primeStack.push(5L);
        
        assertEquals(5L, primeStack.pop());
        assertEquals(2L, primeStack.pop());
        assertEquals(0, primeStack.size());
    }

}
