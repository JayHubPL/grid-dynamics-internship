package com.griddynamics.jdbcutil;

import java.net.InetAddress;

public record ConnectionAttributes(InetAddress ipAddress, int port, String databaseName, String user, String password) {}
