package com.griddynamics.people;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public record Person(Integer id, String name, int age) {

    public static final Function<ResultSet, Person> MAPPER = rs -> {
        try {
            return new Person(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age")
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    @Override
    public String toString() {
        return String.format("%-5d %-10s %-3d", id, name, age);
        // str += " " + citizenship.stream()
        //     .map(i -> Integer.toString(i))
        //     .map(s -> String.format("%-3s", s))
        //     .collect(Collectors.joining(", "));
        // return str;
    }

}
