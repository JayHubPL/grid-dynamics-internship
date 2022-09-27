package com.griddynamics.http;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super(String.format("Authorization with token was not aquired"));
    }

}
