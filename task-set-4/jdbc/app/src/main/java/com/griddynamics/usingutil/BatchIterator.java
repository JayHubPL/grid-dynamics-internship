package com.griddynamics.usingutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.griddynamics.jdbcutil.DatabaseHandler;

public class BatchIterator<T> implements Iterator<T> {

    private final DatabaseHandler db;
    private final int batchSize;
    private final String batchQuery;
    private final Function<ResultSet, T> mapper;
    private List<T> currentBatch;
    private int lastRowProcessed = 0;
    private int batchPos = 0;

    public BatchIterator(DatabaseHandler db, Function<ResultSet, T> mapper, int batchSize, String tableName) {
        this.db = db;
        this.mapper = mapper;
        this.batchSize = batchSize;
        batchQuery = String.format("""
                SELECT *
                FROM (SELECT ROW_NUMBER() OVER() AS row, * FROM %s) AS subquery
                WHERE row > ? AND row <= ?
                """, tableName);
    }

    @Override
    public boolean hasNext() {
        if (batchPos == batchSize || currentBatch == null) {
            currentBatch = getNextBatch();
        }
        return !currentBatch.isEmpty();
    }

    @Override
    public T next() {
        return currentBatch.get(batchPos++);
    }

    private List<T> getNextBatch() {
        try {
            List<T> batch = db.findMany(batchQuery, mapper, lastRowProcessed, lastRowProcessed + batchSize);
            lastRowProcessed += batch.size();
            batchPos = 0;
            return batch;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
    
}
