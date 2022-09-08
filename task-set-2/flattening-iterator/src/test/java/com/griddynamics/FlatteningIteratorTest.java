package com.griddynamics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FlatteningIteratorTest {
    
    @Test
    public void HasNext_DoesNotChangeInternalState() {
        Iterator<Integer> iterator = List.of(1).iterator();
        FlatteningIterator<Integer> flatteningIterator = new FlatteningIterator<>(iterator);

        flatteningIterator.hasNext();
        flatteningIterator.hasNext();
        flatteningIterator.hasNext();

        assertTrue(flatteningIterator.hasNext());
        assertEquals(1, flatteningIterator.next());
    }

    @Test
    public void Next_ContainsMultipleIterators_CorrectlyIteratesThroughAllIterators() {
        Iterator<Integer> iterator1 = List.of(1).iterator();
        Iterator<Integer> iterator2 = List.of(2, 3).iterator();
        Iterator<Integer> iterator3 = List.of(4, 5, 6).iterator();
        
        FlatteningIterator<Integer> flatteningIterator = new FlatteningIterator<>(iterator1, iterator2, iterator3);

        List<Integer> expected = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> actual = new ArrayList<>();
        flatteningIterator.forEachRemaining(actual::add);

        assertIterableEquals(expected, actual);
        assertFalse(flatteningIterator.hasNext());
    }

    @Test
    public void Next_ContainsEmptyIterator_CorrectlyIteratesThroughAllIteratorsSkippingEmptyOnes() {
        Iterator<Integer> emptyIterator = Collections.emptyIterator();
        Iterator<Integer> iterator1 = List.of(1, 2).iterator();
        Iterator<Integer> iterator2 = List.of(3, 4, 5).iterator();

        List<Iterator<Integer>> iterators = List.of(
            emptyIterator, iterator1, emptyIterator, iterator2, emptyIterator
        );
        
        FlatteningIterator<Integer> flatteningIterator = new FlatteningIterator<>(iterators);

        List<Integer> expected = List.of(1, 2, 3, 4, 5);
        List<Integer> actual = new ArrayList<>();
        flatteningIterator.forEachRemaining(actual::add);
        
        assertIterableEquals(expected, actual);
        assertFalse(flatteningIterator.hasNext());
    }

    @Test
    public void FlatteningIterator_GivenNullCollection_ThrowException() {
        Collection<Iterator<Object>> nullCollection = null;

        assertThrows(IllegalArgumentException.class, () -> {
            new FlatteningIterator<Object>(nullCollection);
        });
    }

    @Test
    public void FlatteningIterator_GivenEmptyArray_ConstructEmptyFlatteningIterator() {
        FlatteningIterator<Object> flatteningIterator = new FlatteningIterator<>();

        assertFalse(flatteningIterator.hasNext());
    }

}
