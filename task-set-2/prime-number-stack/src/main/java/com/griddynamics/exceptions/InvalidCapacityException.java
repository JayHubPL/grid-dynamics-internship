package com.griddynamics.exceptions;

public class InvalidCapacityException extends RuntimeException {

    private final String msg;

    public InvalidCapacityException(int capacity) {
        msg = String.format("Stack capacity must be a positive integer, was: %d", capacity);
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
