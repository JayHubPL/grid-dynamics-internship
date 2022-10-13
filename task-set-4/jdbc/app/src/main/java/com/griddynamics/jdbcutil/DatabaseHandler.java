package com.griddynamics.jdbcutil;

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

    protected final Connection conn;
    protected final Statement stmt;
    
    public DatabaseHandler(DatabaseConnectionProvider provider) throws SQLException {
        conn = provider.getConnection();
        conn.setAutoCommit(false);
        stmt = conn.createStatement();
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
            if (!result.next()) {
                return Optional.empty();
            }
            T found = mapper.apply(result);
            if (result.next()) {
                throw new SQLException("Returned more than one record matching the query: " + query);
            }
            return Optional.of(found);
            
            // return switch (getRowCount(result)) {
            //     case 0 -> Optional.empty();
            //     case 1 -> {
            //         result.next();
            //         yield Optional.of(mapper.apply(result));
            //     }
            //     default -> throw new SQLException("Returned more than one record matching the query: " + query);
            // };
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

    // public void getCursor(String tableName) throws SQLException {
    //     String query = String.format("""
    //             DECLARE curs CURSOR FOR SELECT * FROM %s
    //             """, tableName);
    //     stmt.execute(query);
    //     query = """
    //             OPEN curs
    //             """;
    //     stmt.execute(query);
    //     query = """
    //             RETURN NEXT curs
    //             """;
    //     // conn.commit();
    //     ResultSet result = stmt.executeQuery(query);
    // }

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

    // private int getRowCount(ResultSet result) throws SQLException {
    //     result.last();
    //     int rowCount = result.getRow();
    //     result.beforeFirst();
    //     return rowCount;
    // }

}
