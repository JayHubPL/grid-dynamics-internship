package com.griddynamics.exceptions;

public class StackEmptyException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Tried to read from an empty stack";
    }

}
