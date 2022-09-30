package com.griddynamics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler implements AutoCloseable {

    private final Connection conn;
    private final Statement stmt;
    
    public DatabaseHandler(DatabaseConnectionProvider provider) throws SQLException {
        conn = provider.getConnection();
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
    }

    public void initializeUsersTable() throws SQLException {
        createUsersTable(10, 30, true);
    }

    public void createUsersTable(int idMaxLen, int usernameMaxLen, boolean reset) throws SQLException {
        if (reset) {
            String dropTableSql = """
                    DROP TABLE IF EXISTS users
                    """;
            stmt.execute(dropTableSql);
        }
        String createTableSql = String.format("""
                CREATE TABLE IF NOT EXISTS users
                (id varchar(%d) UNIQUE NOT NULL, username varchar(%d) NOT NULL)
                """, idMaxLen, usernameMaxLen);
        stmt.execute(createTableSql);
        conn.commit();
    }

    public void populateUsersTable(int numberOfUsers) throws SQLException {
        String addUserSql = """
                INSERT INTO users
                VALUES (?, ?)
                """;
        try (PreparedStatement prepStmt = conn.prepareStatement(addUserSql)) {
            for (int i = 1; i <= numberOfUsers; i++) {
                prepStmt.setString(1, Integer.toString(i));
                prepStmt.setString(2, "Username" + i);
                prepStmt.addBatch();
            }
            prepStmt.executeBatch();
            conn.commit();
        }
    }

    public void removeUsersWhereId(String whereStatement) throws SQLException {
        String removeSql = String.format("""
                DELETE FROM users
                WHERE id = %s
                """, whereStatement);
        stmt.executeUpdate(removeSql);
        conn.commit();
    }

    public List<User> getUsersTable() throws SQLException {
        String selectSql = """
                SELECT * FROM users
                """;
        ResultSet result = stmt.executeQuery(selectSql);
        List<User> users = new ArrayList<>();
        while (result.next()) {
            String id = result.getString("id");
            String username = result.getString("username");
            users.add(new User(id, username));
        }
        return users;
    }

    @Override
    public void close() throws SQLException {
        conn.rollback();
        stmt.close();
        conn.close();
    }

}
