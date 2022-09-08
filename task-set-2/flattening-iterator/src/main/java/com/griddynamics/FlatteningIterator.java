package com.griddynamics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class FlatteningIterator<T> implements Iterator<T> {

    private final Iterator<Iterator<T>> iterators;
    private Iterator<T> currentIterator;

    @SafeVarargs
    public FlatteningIterator(Iterator<T>... iterators) {
        this.iterators = Arrays.asList(iterators).iterator();
        currentIterator = Collections.emptyIterator();
        if (this.iterators.hasNext()) {
            currentIterator = this.iterators.next();
        }
    }

    public FlatteningIterator(Collection<Iterator<T>> iterators) {
        if (iterators == null) {
            throw new IllegalArgumentException("Collection of iterators cannot be null");
        }
        this.iterators = iterators.iterator();
        currentIterator = Collections.emptyIterator();
        if (this.iterators.hasNext()) {
            this.currentIterator = this.iterators.next();
        }
    }

    @Override
    public boolean hasNext() {
        return currentIterator.hasNext() || iterators.hasNext();
    }

    @Override
    public T next() {
        while (!currentIterator.hasNext() && iterators.hasNext()) {
            currentIterator = iterators.next();
        }
        return currentIterator.next();        
    }
    
}
