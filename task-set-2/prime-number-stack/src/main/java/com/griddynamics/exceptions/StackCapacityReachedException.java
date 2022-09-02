package com.griddynamics.exceptions;

public class StackCapacityReachedException extends RuntimeException {

    private final String msg;

    public StackCapacityReachedException(int capacity) {
        msg = String.format("Tried to push on the stack while it reached maximum capacity of %d", capacity);
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
