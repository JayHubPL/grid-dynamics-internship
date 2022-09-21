package com.griddynamics;

import java.util.function.Function;

public abstract sealed class Result<T, E extends Exception> permits Ok<T, E>, Err<T, E> {
    
    public static <T> Result<T, Exception> ok(T value) {
        return new Ok<T, Exception>(value);
    }

    public static <E extends Exception> Result<Object, E> err(E exception) {
        return new Err<Object, E>(exception);
    }

    public static Result<?, ? extends Exception> of(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception exception) {
            return new Err<Object, Exception>(exception);
        }
        return new Ok<>();
    }

    public abstract <R> Result<R, E> map(Function<T, R> mapper);
    
    public abstract <U extends Exception> Result<T, U> mapErr(Function<E, U> mapper);

    public abstract <R, U extends Exception> Result<?, ?> flatMap(Function<T, Result<R, U>> mapper);

    public abstract Object orElse(Object value);

    public abstract T unwrap() throws E;

}
