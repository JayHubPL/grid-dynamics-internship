package com.griddynamics;

import java.util.function.Function;

public final class Result<T, E extends Exception> {

    private T value;
    private E err;

    private final boolean isOK;

    private Result(T value) {
        this.value = value;
        err = null;
        isOK = true;
    }

    private Result(E err) {
        this.err = err;
        value = null;
        isOK = false;
    }

    private Result() {
        value = null;
        err = null;
        isOK = true;
    }

    // ### FACTORY METHODS ###

    // is '? extends Exception' necessary here?
    public static <T> Result<T, ? extends Exception> ok(T value) {
        return new Result<>(value);
    }

    public static <E extends Exception> Result<?, E> err(E err) {
        return new Result<>(err);
    }

    // is '? extends Exception' necessary here?
    public static Result<?, ? extends Exception> of(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception exception) {
            return new Result<>(exception);
        }
        return new Result<>();
    }

    // ### MAPPERS ###

    // is '? extends Exception' necessary here? should map() it be allowed for Ok(null)?
    public <U> Result<U, ? extends Exception> map(Function<T, U> mapper) {
        if (!isOK) {
            throw new IllegalOperationException("Tried to map result value when Result was of type Err");
        }
        return new Result<>(mapper.apply(value));
    }

    public <U extends Exception> Result<?, U> mapErr(Function<E, U> mapper) {
        if (isOK) {
            throw new IllegalOperationException("Tried to map error value when Result was of type Ok");
        }
        return new Result<>(mapper.apply(err));
    }

    // did I understand correctly the behaviour of this method?
    public <R, U extends Exception> Result<R, U> flatMap(Function<T, Result<R, U>> mapper) {
        if (!isOK) {
            throw new IllegalOperationException("Tried to map result value when Result was of type Err");
        }
        return mapper.apply(value);
    }

    // ### VALUE EXTRACTION ###

    public <R> Object orElse(R value) {
        return isOK ? this.value : value;
    }

    // should it throw internal E-type exception or IllegalOperationException when not OK?
    public T unwrap() {
        if (!isOK) {
            throw new IllegalOperationException("Tried to unwrap Result of type Err");
        }
        return value;
    }

}
