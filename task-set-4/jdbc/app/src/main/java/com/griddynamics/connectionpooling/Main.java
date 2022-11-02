package com.griddynamics.connectionpooling;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.griddynamics.jdbcutil.ConnectionAttributes;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Main {
    
    public static final String DATABASE_NAME = "users";
    public static final String USER = "postgres";
    public static final String PASSWORD = "docker";
    public static final int PORT = 5432;
    public static final InetAddress IP = InetAddress.getLoopbackAddress();

    public static void main(String[] args) throws SQLException, InterruptedException, IOException {
        ConnectionAttributes connectionAttributes = new ConnectionAttributes(IP, PORT, DATABASE_NAME, USER, PASSWORD);
        int threadNo = 10;
        int sleepDuration = 1;

        // Using single connection
        CachedDataSource cachedDataSource = new CachedDataSource(connectionAttributes);
        runThreads(cachedDataSource, threadNo, sleepDuration);
        cachedDataSource.close();
        /*
         * All 10 threads finished in 10.097000 seconds
         * 
         * We didn't achieve any profit using only one connection
         * with 10 threads. First thread blocks the access to the connection
         * for the rest of threads and releases it only when it finishes its task.
         * That's why the total time is the product of task duration and number of threads.
         * We didn't achieve parallelization.
         */

        // Using connection pooling
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("databaseName", DATABASE_NAME);
        config.addDataSourceProperty("user", USER);
        config.addDataSourceProperty("password", PASSWORD);
        config.setMaximumPoolSize(threadNo);
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        runThreads(hikariDataSource, threadNo, sleepDuration);
        hikariDataSource.close();
        /*
         * All 10 threads finished in 1.016000 seconds
         * 
         * All threads acquired their own unblocked connection. Each of them ran in parrarel
         * so the total time is equal to the duration of a singular task. We did not
         * waste time on creating/closing connections, only on accessing and returning them
         * to the pool.
         * We did achieve parallelization with the benefits of connection pooling.
         */
    }

    public static void runThreads(DataSource ds, int threadNo, int sleepDuration)
    throws IOException, SQLException, InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNo; i++) {
            threads.add(new LongActionThread(ds.getConnection(), sleepDuration));
        }
        long start = System.currentTimeMillis();
        threads.forEach(Thread::start);
        for (var thread : threads) {
            thread.join();
        }
        long stop = System.currentTimeMillis();
        System.out.printf("All %d threads finished in %f seconds%n", threadNo, (stop - start) / 1000.);
    }
}
