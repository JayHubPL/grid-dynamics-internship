package com.griddynamics.http;

import com.griddynamics.connections.Connection;

public record RequestBody(String origin, String destination) {
    
    public RequestBody(Connection connection) {
        this(connection.origin().code(), connection.destination().code());
    }

}
