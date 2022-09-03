package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import com.griddynamics.exceptions.InvalidCapacityException;

public class PrimeStackTest {
    
    @Test
    public void PrimeStack_NegativeCapacityGiven_ThrowException() {
        assertThrows(InvalidCapacityException.class, () -> {
            new PrimeStack(-1);
        });
    }

    @Test
    public void PrimeStack_ZeroCapacityGiven_ThrowException() {
        assertThrows(InvalidCapacityException.class, () -> {
            new PrimeStack(0);
        });
    }

    @Test
    public void PrimeStack_PositiveCapacityGiven_ConstructCorrectly() {
        int expectedCapacity = 10;
        PrimeStack primeStack = new PrimeStack(expectedCapacity);
        assertEquals(0, primeStack.size());
        try {
            assertTrue(checkCapacity(primeStack, expectedCapacity));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private boolean checkCapacity(PrimeStack primeStack, int expectedCapacity)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field capacityField = primeStack.getClass().getDeclaredField("capacity");
        capacityField.setAccessible(true);
        return (expectedCapacity == (int) capacityField.get(primeStack));
    }
}
