package com.griddynamics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

    public void execute(String query, Object... args) throws SQLException {
        try (PreparedStatement prepStmt = applyArgsToQuery(query, args)) {
            prepStmt.executeUpdate(); // this throws if query returns ResultSet
            conn.commit();
        }
    }

    public <T> Optional<T> findOne(String query, Function<ResultSet, T> mapper, Object... args) throws SQLException {
        try (PreparedStatement prepStmt = applyArgsToQuery(query, args)) {
            ResultSet result = prepStmt.executeQuery(); // this throws if query does not return ResultSet
            return switch (getRowCount(result)) {
                case 0 -> Optional.empty();
                case 1 -> Optional.of(mapper.apply(result));
                default -> throw new SQLException("Returned more than one record matching the query: " + query);
            };
        }
    }

    public <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) throws SQLException {
        try (PreparedStatement prepStmt = applyArgsToQuery(query, args)) {
            ResultSet result = prepStmt.executeQuery(); // this throws if query does not return ResultSet
            List<T> mappedRecords = new ArrayList<>();
            while (result.next()) {
                mappedRecords.add(mapper.apply(result));
            }
            return mappedRecords;
        }
    }

    @Override
    public void close() throws SQLException {
        conn.rollback();
        stmt.close();
        conn.close();
    }

    private PreparedStatement applyArgsToQuery(String query, Object[] args) throws SQLException {
        PreparedStatement prepStmt = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            prepStmt.setObject(i + 1, args[i]);
        }
        return prepStmt;
    }

    private int getRowCount(ResultSet result) throws SQLException {
        result.last();
        int rowCount = result.getRow();
        result.beforeFirst();
        return rowCount;
    }

}
