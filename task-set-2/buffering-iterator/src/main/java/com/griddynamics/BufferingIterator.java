package com.griddynamics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BufferingIterator<T> implements Iterator<List<T>> {

    private final Iterator<T> iterator;
    private final int batchSize;

    public BufferingIterator(Iterator<T> iterator, int batchSize) {
        if (iterator == null) {
            throw new IllegalArgumentException("Iterator cannot be null");
        }
        if (batchSize <= 0) {
            throw new IllegalArgumentException(String.format("Batch size must be greater than 0, was: %d", batchSize));
        }
        this.iterator = iterator;
        this.batchSize = batchSize;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public List<T> next() {
        List<T> batch = new ArrayList<>();
        for (int i = 0; i < batchSize && iterator.hasNext(); i++) {
            batch.add(iterator.next());
        }
        return batch;
    }
    
}
