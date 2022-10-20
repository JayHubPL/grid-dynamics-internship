package com.griddynamics.people;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record Person(Integer id, String name, int age, List<Integer> citizenship) {

    public static final Function<ResultSet, Person> MAPPER = rs -> {
        try {
            return new Person(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                Arrays.asList((Integer[])rs.getArray("citizenship").getArray())
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

}
