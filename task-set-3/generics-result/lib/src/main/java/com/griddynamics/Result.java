package com.griddynamics;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract sealed class Result<T, E extends Exception> {
    
    /**
     * Creates an instanse of {@code Result} of variant {@code Ok} storing given {@code value}.
     * @param <T> type of {@code value}
     * @param <E> type of exception
     * @param value value stored in {@code Ok} variant
     * @return instance of {@code Ok} variant with {@code value} of type {@code T}
     */
    public static <T, E extends Exception> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    /**
     * Creates an instance of {@code Result} of variant {@code Err} storing given {@code exception}.
     * @param <E> type of {@code exception}, must extend {@code Exception}
     * @param exception exception stored in {@code Err} variant
     * @return instance of {@code Err} variant with {@code exception} of type {@code E}
     */
    public static <T, E extends Exception> Result<T, E> err(E exception) {
        return new Err<>(exception);
    }

    /**
     * Creates an instance of {@code Result} of variant depending on the outcome of {@code supplier.get()}.
     * While running {@code supplier.get()}, if an exception is thrown, the {@code Err} variant storing that
     * exception will be returned, otherwise the {@code Ok} variant with returned value of type {@code T}.
     * @param <T> return type of the {@code supplier}
     * @param supplier code for which the result is being determined
     * @return instance of {@code Ok} with return value given by the {@code supplier} if no exception is thrown during
     * execution of {@code supplier.get()} or instance of {@code Err} if exception is thrown, storing it inside {@code Err}
     */
    public static <T> Result<T, Exception> of(Supplier<T> supplier) {
        try {
            return new Ok<>(supplier.get());
        } catch (Exception exception) {
            return new Err<>(exception);
        }
    }

    /**
     * If {@code Result}'s variant is {@code Ok}, maps {@code value} to another value given mapping function {@code mapper}.
     * If {@code Result}'s variant is {@code Err}, does nothing.
     * @param <R> type of value after mapping
     * @param mapper function, which maps a value of type {@code T} to a new value of type {@code R}
     * @return {@code Ok} variant with new mapped value or unchanged {@code Err} variant
     */
    public abstract <R> Result<R, E> map(Function<T, R> mapper);
    
    /**
     * If {@code Result}'s variant is {@code Err}, maps {@code exception} to another exception given mapping function {@code mapper}.
     * If {@code Result}'s variant is {@code Ok}, does nothing.
     * @param <U> type of exception after mapping
     * @param mapper function, which maps an exception of type {@code E} to a new exception of type {@code U}
     * @return {@code Err} variant with new mapped exception or unchanged {@code Ok} variant
     */
    public abstract <U extends Exception> Result<T, U> mapErr(Function<E, U> mapper);

    /**
     * For {@code Result}'s variant {@code Ok}, maps {@code T value} to a new {@code Result<R, U>} and returns it.
     * New {@code Result} may have different value and exception types.
     * For {@code Err}, does nothing.
     * @param <R> type of value after mapping
     * @param <U> type of exception after mapping
     * @param mapper function, which maps a value to a new {@code Result}
     * @return new {@code Result} instance or unchanged {@code Err} variant
     */
    public abstract <R, U extends Exception> Result<R, ? extends Exception> flatMap(Function<T, Result<R, U>> mapper);

    /**
     * Returns value stored in {@code Ok} variant or if {@code Result} is of {@code Err} variant, returns {@code value}
     * given in the argument.
     * @param value value, which should be returned when {@code Result} is of {@code Err} variant
     * @return value of type {@code T} based on the variant of {@code Result}
     */
    public abstract T orElse(T value);

    /**
     * Unwraps the {@code Result} type based on its variant and returns {@code value} stored in {@code Ok} variant
     * or throws the exception stored in {@code Err} variant.
     * @return value stored in {@code Ok variant}
     * @throws E exception stored in {@code Err} variant
     */
    public abstract T unwrap() throws E;

    private static final class Ok<T, E extends Exception> extends Result<T, E> {
    
        private T value;
    
        protected Ok(T value) {
            this.value = value;
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
        public <R, U extends Exception> Result<R, ?> flatMap(Function<T, Result<R, U>> mapper) {
            return mapper.apply(value);
        }
    
        @Override
        public T orElse(T value) {
            return this.value;
        }
    
        @Override
        public T unwrap() throws E {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Ok<T, E> other = (Ok<T, E>) obj;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }
    
    }

    private static final class Err<T, E extends Exception> extends Result<T, E> {
    
        private E exception;
    
        protected Err(E exception) {
            if (exception == null) {
                throw new IllegalArgumentException("Exception cannot be null");
            }
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
        public <R, U extends Exception> Result<R, ? extends Exception> flatMap(Function<T, Result<R, U>> mapper) {
            return new Err<>(exception);
        }
    
        @Override
        public T orElse(T value) {
            return value;
        }
    
        @Override
        public T unwrap() throws E {
            throw exception;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((exception == null) ? 0 : exception.hashCode());
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Err<T, E> other = (Err<T, E>) obj;
            if (exception == null) {
                if (other.exception != null)
                    return false;
            } else if (!exception.equals(other.exception))
                return false;
            return true;
        }
    
    }

}
