package com.griddynamics;

public class InvalidStartingDateException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Starting date cannot be null";
    }

}
