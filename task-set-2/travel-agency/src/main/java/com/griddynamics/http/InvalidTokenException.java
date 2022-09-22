package com.griddynamics.http;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String token) {
        super(String.format("Authorization with token %s was not aquired", token));
    }

}
