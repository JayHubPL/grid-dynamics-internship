package com.griddynamics;

import java.sql.SQLException;
import java.util.List;

import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.utils.ResultSetMappers;

public class PeopleQueries {
    
    private final DatabaseHandler db;
    
    public PeopleQueries(DatabaseHandler db) {
        this.db = db;
    }

    // 1. Person with the biggest number of citizenships (full person object)
    public List<Person> peopleWithTheBiggestNumberOfCitizenships() throws SQLException {
        return db.findMany("""
                SELECT id, name, age, citizenship, ( SELECT COUNT(*)
                    FROM citizenships
                    GROUP BY person_id
                    ORDER BY COUNT(*) DESC
                    LIMIT 1
                ) AS top_no_of_citizenships
                FROM people
                WHERE ARRAY_LENGTH(people.citizenship, 1) = top_no_of_citizenships
                """, Person.MAPPER);
    }

    // 2. All people who have no citizenship (full person object)
    public List<Person> peopleWithNoCitizenship() throws SQLException {
        return db.findMany("""
                SELECT id, name, age, citizenship
                FROM people
                WHERE ARRAY_LENGTH(people.citizenship, 1) = 0
                """, Person.MAPPER);
    }

    // 3. Country with the least people in Person table (country name)
    public List<String> countriesWithTheLeastPersons() throws SQLException {
        return db.findMany("""
                SELECT countries.name, ( SELECT COUNT(*)
                    FROM countries LEFT JOIN citizenships ON countries.id = citizenships.country_id
                    GROUP BY countries.id
                    ORDER BY COUNT(*)
                    LIMIT 1
                ) AS least_people
                FROM countries LEFT JOIN citizenships ON countries.id = citizenships.country_id
                GROUP BY countries.id
                HAVING COUNT(*) = least_people
                """, ResultSetMappers.STRING_MAPPER);
    }

    // 4. Continent with the most people in Person table (continent name)
    public List<String> countriesWithTheMostPersons() throws SQLException {
        return db.findMany("""
                SELECT countries.name, ( SELECT COUNT(*)
                    FROM countries LEFT JOIN citizenships ON countries.id = citizenships.country_id
                    GROUP BY countries.id
                    ORDER BY COUNT(*) DESC
                    LIMIT 1
                ) AS least_people
                FROM countries LEFT JOIN citizenships ON countries.id = citizenships.country_id
                GROUP BY countries.id
                HAVING COUNT(*) = least_people
                """, ResultSetMappers.STRING_MAPPER);
    }

    // 5. Continent, which has the biggest number of countries, which have at least one person in Person table (continent name)
    public List<String> continentWhichHasTheBiggestNumberOfPopulatedCountries() throws SQLException {
        return db.findMany("""
                SELECT continents.name
                FROM continents JOIN (citizenships LEFT JOIN countries ON citizenships.country_id = countries.id) ON countries.continent_id = continents.id
                GROUP BY continents.name
                ORDER BY COUNT(*) DESC
                LIMIT 1
                """, ResultSetMappers.STRING_MAPPER);
    }

}
