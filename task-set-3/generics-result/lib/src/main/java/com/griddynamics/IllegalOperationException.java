package com.griddynamics;

public class IllegalOperationException extends RuntimeException {
    
    private final String msg;

    public IllegalOperationException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
