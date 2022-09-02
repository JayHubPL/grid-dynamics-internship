package com.griddynamics.exceptions;

public class PushingNullValueException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Tried to push a null value on the stack";
    }
    
}
