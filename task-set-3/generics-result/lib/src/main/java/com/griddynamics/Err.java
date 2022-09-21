package com.griddynamics;

import java.util.function.Function;

public final class Err<T, E extends Exception> extends Result<T, E> {
    
    private E exception;

    protected Err(E exception) {
        this.exception = exception;
    }

    @Override
    public <R> Result<R, E> map(Function<T, R> mapper) {
        return new Err<>(exception);
    }

    @Override
    public <U extends Exception> Result<T, U> mapErr(Function<E, U> mapper) {
        return new Err<>(mapper.apply(exception));
    }

    @Override
    public <R, U extends Exception> Result<?, ?> flatMap(Function<T, Result<R, U>> mapper) {
        return new Err<>(exception);
    }

    @Override
    public Object orElse(Object value) {
        return value;
    }

    @Override
    public T unwrap() throws E {
        throw exception;
    }

}
