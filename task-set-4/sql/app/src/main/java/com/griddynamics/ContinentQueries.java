package com.griddynamics;

import java.sql.SQLException;
import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.utils.ResultSetMappers;

public class ContinentQueries {
    
    private final DatabaseHandler db;
    
    public ContinentQueries(DatabaseHandler db) {
        this.db = db;
    }

    // 1. Count of countries for each continent (name of the continent and number)
    public List<Pair<Integer, String>> numberOfCountriesOnEachContinent() throws SQLException {
        return db.findMany("""
                SELECT COUNT(*) AS countries_count, continents.name
                FROM continents JOIN countries ON continents.id = countries.continent_id
                GROUP BY continents.name
                """, ResultSetMappers.INT_STRING_MAPPER);
    }

    // 2. Total area for each continent (print continent name and total area), sorted by area from biggest to smallest 
    public List<Pair<Integer, String>> continentsArea() throws SQLException {
        return db.findMany("""
                SELECT SUM(countries.area) AS continent_area, continents.name
                FROM continents JOIN countries ON continents.id = countries.continent_id
                GROUP BY continents.name
                ORDER BY continent_area DESC
                """, ResultSetMappers.INT_STRING_MAPPER);
    }

    // 3. Average population density per continent (name of the continent and number)
    public List<Pair<Integer, String>> continentsAveragePopulationDensity() throws SQLException {
        return db.findMany("""
                SELECT AVG(CAST(countries.population AS double)/countries.area) AS average_continent_density , continents.name
                FROM continents JOIN countries ON continents.id = countries.continent_id
                GROUP BY continents.name
                """, ResultSetMappers.INT_STRING_MAPPER);
    }

    // 4. For each continent, find a country with the smallest area (print continent name, country name and area)
    public List<Triplet<String, String, Integer>> smallestCountryOnTheContinent() throws SQLException {
        return db.findMany("""
                SELECT continent_name, countries.name, min_area 
                FROM ( SELECT continents.name AS continent_name, MIN(countries.area) AS min_area
                    FROM countries JOIN continents ON continents.id = countries.continent_id
                    GROUP BY continents.name, countries.name
                ) AS subquery
                LEFT JOIN countries ON countries.area = subquery.min_area
                """, ResultSetMappers.STR_STR_INT_MAPPER);
    }

    // 5. Find all continents, which have total area greater than 15 million km2 (names)
    public List<String> continentsWithAreaGreaterThan(int area) throws SQLException {
        return db.findMany("""
                SELECT continents.name
                FROM continents JOIN countries ON continents.id = countries.continent_id
                GROUP BY continents.name
                HAVING SUM(countries.area) > ?
                """, ResultSetMappers.STRING_MAPPER, area);
    }

    // 6. Find all continents, which have average country population less than 20 million (names)
    public List<String> continentsWithAverageCountryPopulationLessThan(int population) throws SQLException {
        return db.findMany("""
                SELECT continents.name
                FROM continents JOIN countries ON continents.id = countries.continent_id
                GROUP BY continents.name
                HAVING AVG(countries.population) < ?
                """, ResultSetMappers.STRING_MAPPER, population);
    }

}
