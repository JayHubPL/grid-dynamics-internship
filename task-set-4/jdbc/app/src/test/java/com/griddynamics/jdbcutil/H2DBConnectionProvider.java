package com.griddynamics.jdbcutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DBConnectionProvider implements DatabaseConnectionProvider {

    private static final String H2_URL = "jdbc:h2:mem:test_h2.db;DB_CLOSE_DELAY=-1";
    private static final String USER = "";
    private static final String PASSWORD = "";
    
    private final Connection conn;

    public H2DBConnectionProvider() throws SQLException {
        conn = DriverManager.getConnection(H2_URL, USER, PASSWORD);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return conn;
    }
    
}
