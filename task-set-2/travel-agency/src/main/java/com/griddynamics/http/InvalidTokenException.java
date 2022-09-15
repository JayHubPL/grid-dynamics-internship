package com.griddynamics.http;

public class InvalidTokenException extends Exception {

    private String msg;

    public InvalidTokenException(String token) {
        msg = String.format("Authorization with token %s was not aquired", token);
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
