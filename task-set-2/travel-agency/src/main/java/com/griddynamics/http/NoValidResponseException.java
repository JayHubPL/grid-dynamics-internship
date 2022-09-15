package com.griddynamics.http;

import com.griddynamics.connections.Connection;

public class NoValidResponseException extends RuntimeException {

    private final String msg;

    public NoValidResponseException(Connection connection) {
        msg = String.format("Failed to receive valid response for connection %s", connection);
    }

    @Override
    public String getMessage() {
        return msg;
    }
    
}
