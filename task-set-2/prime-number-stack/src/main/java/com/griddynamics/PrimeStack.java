package com.griddynamics;

import java.util.Iterator;
import java.util.stream.LongStream;

public class PrimeStack implements Stack<Long> {

    private Long[] stack;
    private int size;
    private int capacity;

    public PrimeStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                String.format("Stack capacity must be a positive integer, was: %d", capacity)
            );
        }
        this.stack = new Long[capacity];
        this.capacity = capacity;
    }

    @Override
    public Iterator<Long> iterator() {
        return new StackIterator<Long>(stack, size);
    }

    @Override
    public void push(Long elem) {
        validatePushInput(elem);
        stack[size++] = elem;
    }

    @Override
    public Long pop() {
        if (size == 0) {
            throw new IllegalStateException("Tried to pop from an empty stack");
        }
        size--;
        return stack[size];
    }

    @Override
    public Long peek() {
        if (size == 0) {
            throw new IllegalStateException("Tried to peek into an empty stack");
        }
        return stack[size - 1];
    }

    @Override
    public int size() {
        return size;
    }

    private void validatePushInput(Long elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Tried to push null element");
        }
        if (size + 1 > capacity) {
            throw new IllegalStateException(
                "Maximum stack capacity reached, cannot add new elements without removing"
            );
        }
        if (!isPrime(elem)) {
            throw new IllegalArgumentException(
                String.format("Tried to add non-prime number: %d", elem)
            );
        }
        if (size > 0 && stack[size - 1] >= elem) {
            throw new IllegalStateException(
                String.format(
                    "Cannot push primes lesser or equal to the last one; last prime: %d, tried to push: %d",
                    stack[size - 1], elem
                )
            );
        }
    }

    private boolean isPrime(long num) {
        return num > 1 && LongStream.rangeClosed(2, (long) Math.sqrt(num)).noneMatch(divisor -> num % divisor == 0);
    }

}
