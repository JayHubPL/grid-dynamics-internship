package com.griddynamics;

import java.util.Iterator;
import java.util.stream.LongStream;

import com.griddynamics.exceptions.*;

public class PrimeStack implements Stack<Long> {

    private Long[] stack;
    private int size;
    private int capacity;

    public PrimeStack(int capacity) {
        if (capacity <= 0) {
            throw new InvalidCapacityException(capacity);
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
        if (elem == null) {
            throw new PushingNullValueException();
        }
        if (size + 1 > capacity) {
            throw new StackCapacityReachedException(capacity);
        }
        if (!isPrime(elem)) {
            throw new NumberNotPrimeException(elem);
        }
        if (size > 0 && stack[size - 1] >= elem) {
            throw new IllegalStateException(
                String.format("Cannot push primes lesser or equal to the last one; last prime: %d, tried to push: %d", stack[size - 1], elem)
            );
        }
        stack[size] = elem;
        size++;
    }

    @Override
    public Long pop() {
        if (size == 0) {
            throw new StackEmptyException();
        }
        size--;
        return stack[size];
    }

    @Override
    public Long peek() {
        if (size == 0) {
            throw new StackEmptyException();
        }
        return stack[size - 1];
    }

    @Override
    public int size() {
        return size;
    }

    private boolean isPrime(long num) {
        return num > 1 && LongStream.rangeClosed(2, (long) Math.sqrt(num)).noneMatch(n -> num % n == 0);
    }

}
