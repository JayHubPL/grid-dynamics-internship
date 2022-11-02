package com.griddynamics.jdbcutil;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;

public class PGDataSource implements DatabaseConnectionProvider {

    private final PGSimpleDataSource dataSource;

    public PGDataSource(ConnectionAttributes attributes) {
        this.dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName(attributes.databaseName());
        dataSource.setServerNames(new String[]{attributes.ipAddress().getHostAddress()});
        dataSource.setPortNumbers(new int[]{attributes.port()});
        dataSource.setUser(attributes.user());
        dataSource.setPassword(attributes.password());
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
}
