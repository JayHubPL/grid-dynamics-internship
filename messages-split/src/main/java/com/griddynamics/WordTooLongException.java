package com.griddynamics;

public class WordTooLongException extends RuntimeException {

    private final String msg;

    WordTooLongException(String word, int limit) {
        this.msg = "Word \"" + word + "\" is more than " + limit + " characters long!";
    }

    @Override
    public String getMessage() {
        return msg;
    }
    
}
