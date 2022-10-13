package com.griddynamics.connectionpooling;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class LongActionThread extends Thread {

    private final Connection conn;
    private final String query;

    public LongActionThread(Connection conn, int sleepDurationInSecs) {
        query = String.format("""
                SELECT pg_sleep(%d)
                """, sleepDurationInSecs);
        this.conn = conn;
    }

    @Override
    public void run() {
        System.out.printf("Thread #%d started%n", getId());
        try (Statement stmt = conn.createStatement()){
            long start = System.currentTimeMillis();
            stmt.execute(query);
            long end = System.currentTimeMillis();
            System.out.printf("Thread #%d finished with time: %fs%n", getId(), (end - start) / 1000.);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
    
}
