package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BufferingIteratorTest {
    
    @Test
    public void HasNext_GivenEmptyIterator_HasNextIsFalse() {
        int batchSize = 10;
        Iterator<Object> iterator = Collections.emptyList().iterator();

        BufferingIterator<Object> bufferingIterator = new BufferingIterator<>(iterator, batchSize);

        assertFalse(bufferingIterator.hasNext());
    }

    @Test
    public void HasNext_IteratorContainsOneItem_HasNextIsTrue() {
        int batchSize = 10;
        Iterator<Integer> iterator = List.of(1).iterator();

        BufferingIterator<Integer> bufferingIterator = new BufferingIterator<>(iterator, batchSize);

        assertTrue(bufferingIterator.hasNext());
        bufferingIterator.next();

        assertFalse(bufferingIterator.hasNext());
    }

    @Test
    public void Next_MultipleOfBatchSize_CorrectlySplitsIntoLists() {
        int batchSize = 3;
        Iterator<Integer> iterator = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9).iterator();

        BufferingIterator<Integer> bufferingIterator = new BufferingIterator<>(iterator, batchSize);

        assertTrue(bufferingIterator.hasNext());
        assertIterableEquals(List.of(1, 2, 3), bufferingIterator.next());

        assertTrue(bufferingIterator.hasNext());
        assertIterableEquals(List.of(4, 5, 6), bufferingIterator.next());

        assertTrue(bufferingIterator.hasNext());
        assertIterableEquals(List.of(7, 8, 9), bufferingIterator.next());

        assertFalse(bufferingIterator.hasNext());
    }

    @Test
    public void Next_NumberOfElementsIsNotDivisibleByBatchSize_CorrectlySplitsIntoLists() {
        int batchSize = 3;
        Iterator<Integer> iterator = List.of(1, 2, 3, 4).iterator();

        BufferingIterator<Integer> bufferingIterator = new BufferingIterator<>(iterator, batchSize);

        assertTrue(bufferingIterator.hasNext());
        assertIterableEquals(List.of(1, 2, 3), bufferingIterator.next());
        
        assertTrue(bufferingIterator.hasNext());
        assertIterableEquals(List.of(4), bufferingIterator.next());
        
        assertFalse(bufferingIterator.hasNext());
    }
}
