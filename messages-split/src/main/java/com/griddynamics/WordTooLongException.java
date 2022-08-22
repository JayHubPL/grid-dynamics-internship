package com.griddynamics;

public class WordTooLongException extends RuntimeException {

    private final String msg;

    WordTooLongException(String word, int limit) {
        this.msg = String.format("Word \"%s\" is more than %d characters long!", word, limit);
    }

    @Override
    public String getMessage() {
        return msg;
    }
    
}
