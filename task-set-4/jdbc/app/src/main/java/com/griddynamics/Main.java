package com.griddynamics;

import java.net.InetAddress;
import java.sql.SQLException;

public class Main {
    
    public static final String DATABASE_NAME = "users";
    public static final String USER = "postgres";
    public static final String PASSWORD = "docker";
    public static final int PORT = 5432;
    public static final InetAddress IP = InetAddress.getLoopbackAddress();

    public static void main(String[] args) {
        ConnectionAttributes connectionAttributes = new ConnectionAttributes(IP, PORT, DATABASE_NAME, USER, PASSWORD);
        DatabaseConnectionProvider connectionProvider = new JDBCDriverConnectionProvider(connectionAttributes);
        try (DatabaseHandler db = new DatabaseHandler(connectionProvider)) {

            db.initializeUsersTable();
            db.populateUsersTable(100);
            db.removeUsersWhereId("'42'");
            db.getUsersTable().stream()
                .forEach(System.out::println);
            db.removeUsersWhereId("'1' OR 1 = 1");
            /*
             * SQL injection vulnerability example
             * 1 = 1 is always true, which means we can access/delete all of the table's content
             * no matter what was the rest of the WHERE conditions
             */
            db.getUsersTable().stream()
                .forEach(System.out::println);

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}
