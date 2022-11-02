package com.griddynamics.connectionpooling;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.griddynamics.jdbcutil.ConnectionAttributes;
import com.griddynamics.jdbcutil.JDBCDriver;

public class CachedDataSource implements DataSource, Closeable {
    
    private final Connection cachedConn;
    
    public CachedDataSource(ConnectionAttributes attributes) throws SQLException {
        cachedConn = new JDBCDriver(attributes).getConnection();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return cachedConn;
    }

    @Override
    public void close() throws IOException {
        try {
            if (cachedConn != null && !cachedConn.isClosed()) {
                cachedConn.close();
            }
        } catch (SQLException sqlException) {
            throw new IOException(sqlException);
        }
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        DriverManager.setLoginTimeout(seconds);      
    }

    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }

}
