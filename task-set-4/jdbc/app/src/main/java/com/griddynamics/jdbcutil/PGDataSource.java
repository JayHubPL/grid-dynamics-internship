package com.griddynamics.jdbcutil;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;

public class PGDataSource implements DatabaseConnectionProvider {

    private final ConnectionAttributes attributes;

    public PGDataSource(ConnectionAttributes connectionAttributes) {
        this.attributes = connectionAttributes;
    }

    @Override
    public Connection getConnection() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName(attributes.databaseName());
        dataSource.setServerNames(new String[]{attributes.ipAddress().getHostAddress()});
        dataSource.setPortNumbers(new int[]{attributes.port()});
        dataSource.setUser(attributes.user());
        dataSource.setPassword(attributes.password());
        return dataSource.getConnection();
    }
    
}
