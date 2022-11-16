package com.griddynamics.jdbcutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDriver implements DatabaseConnectionProvider {

    private final ConnectionAttributes attributes;

    public JDBCDriver(ConnectionAttributes connectionAttributes) {
        this.attributes = connectionAttributes;
    }

    @Override
    public Connection getConnection() throws SQLException {
        String dbURL = String.format("jdbc:postgresql://%s:%d/%s",
            attributes.ipAddress().getHostAddress(), attributes.port(), attributes.databaseName());
        return DriverManager.getConnection(dbURL, attributes.user(), attributes.password());
    }
    
}
