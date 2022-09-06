package com.griddynamics;

public class InsufficientMaxLineLengthException extends RuntimeException {

    private final String msg;

    InsufficientMaxLineLengthException(int maxLineLength) {
        this.msg = String.format(
                "Message contains words which are >= 3 characters long, so max line length must be >= 3 in order to split them, was: %d",
                maxLineLength);
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
