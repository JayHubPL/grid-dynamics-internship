package com.griddynamics;

import java.util.function.Function;

public final class Ok<T, E extends Exception> extends Result<T, E> {
    
    private T value;

    protected Ok(T value) {
        this.value = value;
    }

    protected Ok() {
        this(null);
    }

    @Override
    public <R> Result<R, E> map(Function<T, R> mapper) {
        return new Ok<>(mapper.apply(value));
    }

    @Override
    public <U extends Exception> Result<T, U> mapErr(Function<E, U> mapper) {
        return new Ok<>(value);
    }

    @Override
    public <R, U extends Exception> Result<?, ?> flatMap(Function<T, Result<R, U>> mapper) {
        return mapper.apply(value);
    }

    @Override
    public Object orElse(Object value) {
        return this.value;
    }

    @Override
    public T unwrap() throws E {
        return value;
    }

}
