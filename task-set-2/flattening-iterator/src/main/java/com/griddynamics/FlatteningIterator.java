package com.griddynamics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class FlatteningIterator<T> implements Iterator<T> {

    private final Iterator<Collection<T>> collectionsIterator;
    private Iterator<T> currentCollectionIterator;

    @SafeVarargs
    public FlatteningIterator(Collection<T>... collections) {
        this.collectionsIterator = Arrays.asList(collections).iterator();
        if (collectionsIterator.hasNext()) {
            this.currentCollectionIterator = collectionsIterator.next().iterator();
        } else {
            throw new IllegalArgumentException("Not enough parameters provided, must be at least one");
        }
    }

    @Override
    public boolean hasNext() {
        return currentCollectionIterator.hasNext() || collectionsIterator.hasNext();
    }

    @Override
    public T next() {
        while (!currentCollectionIterator.hasNext() && collectionsIterator.hasNext()) {
            currentCollectionIterator = collectionsIterator.next().iterator();
        }
        return currentCollectionIterator.next();        
    }
    
}
