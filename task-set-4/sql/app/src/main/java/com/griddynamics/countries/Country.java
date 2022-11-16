package com.griddynamics.countries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import com.griddynamics.continents.Continent;
import com.griddynamics.utils.CountriesDBHandler;

public record Country(Integer id, String name, int population, int area, Continent continent) {

    public static final Function<ResultSet, Country> mapper = rs -> {
        try {
            return new Country(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("population"),
                rs.getInt("area"),
                CountriesDBHandler.continentIdMapping.get(rs.getInt("continent_id"))
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

}
