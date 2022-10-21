package com.griddynamics.countries;

import java.sql.SQLException;
import java.util.List;

import org.javatuples.Pair;

import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.utils.ResultSetMappers;

public class CountryQueries {

    private final DatabaseHandler db;
    
    public CountryQueries(DatabaseHandler db) {
        this.db = db;
    }

    // 1. Biggest population for country (just a number)
    public int biggestPopulation() throws SQLException {
        return db.findOne("""
                SELECT MAX(population)
                FROM countries
                """, ResultSetMappers.INT_MAPPER).get();
    }

    // 2. Country with the biggest population (id and name of the country)
    public Pair<Integer, String> countryWithTheBiggestPopulation() throws SQLException {
        return db.findOne("""
                SELECT id, name
                FROM countries
                ORDER BY population DESC
                LIMIT 1
                """, ResultSetMappers.INT_STRING_MAPPER).get();
    }

    // 3. Country with the biggest population density  (id and name of the country)
    public Pair<Integer, String> countryWithTheBiggestDensity() throws SQLException {
        return db.findOne("""
                SELECT id, name, CAST(population AS float)/area AS density 
                FROM countries
                WHERE countries.area > 0
                ORDER BY density DESC
                LIMIT 1
                """, ResultSetMappers.INT_STRING_MAPPER).get();
    }

    // 4. Top N(10) countries with the lowest population density (names of the countries)
    public List<String> countriesWithLowestDensity(int bottomN) throws SQLException {
        return db.findMany("""
                SELECT name, CAST(population AS float)/area AS density 
                FROM countries
                WHERE countries.area > 0
                ORDER BY density
                LIMIT ?
                """, ResultSetMappers.STRING_MAPPER, bottomN);
    }

    // 5. Countries with population density higher than average across all countries (name)
    public List<String> countriesWithDensityAboveAverage() throws SQLException {
        return db.findMany("""
                SELECT c1.name
                FROM countries AS c1
                WHERE c1.area > 0 AND CAST(c1.population AS float)/c1.area > ( 
                    SELECT AVG(CAST(population AS float)/area)
                    FROM countries AS c2
                    WHERE c2.area > 0
                    LIMIT 1 
                )
                """, ResultSetMappers.STRING_MAPPER);
    }

    // 6. Country which has a population, closest to the average population of all countries (name)
    public String countryWithDensityClosestToTheAverage() throws SQLException {
        return db.findOne("""
                SELECT c1.name
                FROM countries AS c1, (
                    SELECT AVG(CAST(c2.population AS float)/c2.area) AS average_density
                    FROM countries AS c2
                    WHERE c2.area > 0
                    LIMIT 1
                ) AS subquery
                WHERE c1.area > 0
                ORDER BY ABS(CAST(c1.population AS float)/c1.area - subquery.average_density)
                LIMIT 1
                """, ResultSetMappers.STRING_MAPPER).get();
    }

    // 7. For each country, find a number of countries having greater area (name and number)
    public List<Pair<Integer, String>> countCountriesWithGreaterArea() throws SQLException {
        return db.findMany("""
                SELECT COUNT(*), c1.name
                FROM countries AS c1 CROSS JOIN countries AS c2
                WHERE c1.area < c2.area
                GROUP BY c1.name
                """, ResultSetMappers.INT_STRING_MAPPER);
    }

    // 8. Country with the longest name
    public List<String> countriesWithTheLongestName() throws SQLException {
        return db.findMany("""
                SELECT name
                FROM countries, (
                    SELECT LENGTH(name) AS len
                    FROM countries
                    ORDER BY len DESC
                    LIMIT 1
                ) AS max_len
                WHERE LENGTH(name) = max_len.len
                """, ResultSetMappers.STRING_MAPPER);
    }

    // 9. All countries with name starting with the given letter(A) (names)
    public List<String> countriesNamesStartingWithLetter(Character c) throws SQLException {
        return db.findMany(String.format("""
                SELECT name
                FROM countries
                WHERE name LIKE \'%c%%\'
                """, c), ResultSetMappers.STRING_MAPPER);
    }

    // 10. All countries with name containing given letter(F), sorted in alphabetical order (names)
    public List<String> countriesContainingLetter(Character c) throws SQLException {
        return db.findMany(String.format("""
                SELECT name
                FROM countries
                WHERE name LIKE \'%%%c%%\'
                ORDER BY name
                """, c), ResultSetMappers.STRING_MAPPER);
    }

}
