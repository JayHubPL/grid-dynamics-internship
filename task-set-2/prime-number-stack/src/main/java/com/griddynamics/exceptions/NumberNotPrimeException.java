package com.griddynamics.exceptions;

public class NumberNotPrimeException extends RuntimeException {

    private final String msg;

    public NumberNotPrimeException(long num) {
        msg = String.format("Tried to add non-prime number: %d", num);
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
