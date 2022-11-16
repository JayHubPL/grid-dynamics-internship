package com.griddynamics.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import org.javatuples.Pair;
import org.javatuples.Triplet;

public class ResultSetMappers {
    
    public static final Function<ResultSet, Integer> INT_MAPPER = rs -> {
        try {
            return rs.getInt(1);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    public static final Function<ResultSet, Pair<Integer, String>> INT_STRING_MAPPER = rs -> {
        try {
            return Pair.with(rs.getInt(1), rs.getString(2));

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    public static final Function<ResultSet, String> STRING_MAPPER = rs -> {
        try {
            return rs.getString(1);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    public static final Function<ResultSet, Triplet<String, String, Integer>> STR_STR_INT_MAPPER = rs -> {
        try {
            return Triplet.with(rs.getString(1), rs.getString(2), rs.getInt(3));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

}
