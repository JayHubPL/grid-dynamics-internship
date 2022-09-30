package com.griddynamics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDriverConnectionProvider implements DatabaseConnectionProvider {

    private final ConnectionAttributes attributes;

    public JDBCDriverConnectionProvider(ConnectionAttributes connectionAttributes) {
        this.attributes = connectionAttributes;
    }

    @Override
    public Connection getConnection() throws SQLException {
        String dbURL = String.format("jdbc:postgresql://%s:%d/%s",
            attributes.ipAddress().getHostAddress(), attributes.port(), attributes.databaseName());
        return DriverManager.getConnection(dbURL, attributes.user(), attributes.password());
    }
    
}
