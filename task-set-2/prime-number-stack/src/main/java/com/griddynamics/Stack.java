package com.griddynamics;

public interface Stack<T> extends Iterable<T> {

    public void push(T elem);

    public T pop();

    public T peek();

    public int size();

}
