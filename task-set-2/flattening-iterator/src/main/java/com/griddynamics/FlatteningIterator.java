package com.griddynamics;

import java.util.Arrays;
import java.util.Iterator;

public class FlatteningIterator<T> implements Iterator<T> {

    private final Iterator<Iterator<T>> iterators;
    private Iterator<T> currentCollectionIterator;

    @SafeVarargs
    public FlatteningIterator(Iterator<T>... iterators) {
        this.iterators = Arrays.asList(iterators).iterator();
        if (this.iterators.hasNext()) {
            this.currentCollectionIterator = this.iterators.next();
        } else {
            throw new IllegalArgumentException("Not enough parameters provided, must be at least one");
        }
    }

    @Override
    public boolean hasNext() {
        return currentCollectionIterator.hasNext() || iterators.hasNext();
    }

    @Override
    public T next() {
        while (!currentCollectionIterator.hasNext() && iterators.hasNext()) {
            currentCollectionIterator = iterators.next();
        }
        return currentCollectionIterator.next();        
    }
    
}
