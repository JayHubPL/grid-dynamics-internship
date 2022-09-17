package com.griddynamics;

import java.util.Arrays;
import java.util.Iterator;

public class StackIterator<T> implements Iterator<T> {

    private T[] stackSnapshot;
    private int topIndex;

    public StackIterator(T[] stack, int size) {
        stackSnapshot = Arrays.copyOf(stack, size);
        topIndex = size - 1;
    }

    @Override
    public boolean hasNext() {
        return topIndex >= 0;
    }

    @Override
    public T next() {
        return stackSnapshot[topIndex--];
    }

}
