package com.griddynamics.people;

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
        // return db.findMany("""
        //         SELECT id, name, age, citizenship, ( SELECT COUNT(*)
        //             FROM citizenships
        //             GROUP BY person_id
        //             ORDER BY COUNT(*) DESC
        //             LIMIT 1
        //         ) AS top_no_of_citizenships
        //         FROM people
        //         WHERE ARRAY_LENGTH(people.citizenship, 1) = top_no_of_citizenships
        //         """, Person.MAPPER);
        return db.findMany("""
                SELECT *
                FROM people JOIN (
                    SELECT citizenships.person_id AS pid, COUNT(*) AS no_of_citizenships
                    FROM citizenships
                    GROUP BY pid
                ) AS subquery ON people.id = subquery.pid
                WHERE subquery.no_of_citizenships = (
                    SELECT COUNT(citizenships.id) AS count
                    FROM citizenships
                    GROUP BY citizenships.person_id
                    ORDER BY count DESC
                    LIMIT 1
                )
                """, Person.MAPPER);
    }

    // 2. All people who have no citizenship (full person object)
    public List<Person> peopleWithNoCitizenship() throws SQLException {
        return db.findMany("""
                SELECT *
                FROM people LEFT JOIN citizenships ON people.id = citizenships.person_id
                WHERE citizenships.id IS NULL
                """, Person.MAPPER);
    }

    // 3. Country with the least people in Person table (country name)
    public List<String> countriesWithTheLeastPersons() throws SQLException {
        return db.findMany("""
                SELECT countries.name
                FROM countries
                LEFT JOIN citizenships ON countries.id = citizenships.country_id
                GROUP BY countries.name
                HAVING COUNT(citizenships.id) = ( 
                    SELECT COUNT(citizenships.id) AS count
                    FROM countries
                    LEFT JOIN citizenships ON countries.id = citizenships.country_id
                    GROUP BY countries.name
                    ORDER BY count
                    LIMIT 1
                )
                """, ResultSetMappers.STRING_MAPPER);
    }

    // 4. Continent with the most people in Person table (continent name)
    public String continentWithTheBiggestNumberOfPersons() throws SQLException {
        return db.findOne("""
                SELECT continents.name
                FROM continents
                JOIN countries ON continents.id = countries.continent_id
                RIGHT JOIN citizenships ON countries.id = citizenships.country_id
                GROUP BY continents.name
                ORDER BY COUNT(citizenships.id) DESC
                LIMIT 1
                """, ResultSetMappers.STRING_MAPPER).get();
    }

    // 5. Continent, which has the biggest number of countries, which have at least one person in Person table (continent name)
    public String continentWhichHasTheBiggestNumberOfPopulatedCountries() throws SQLException {
        return db.findOne("""
                SELECT continents.name
                FROM continents JOIN (citizenships LEFT JOIN countries ON citizenships.country_id = countries.id) ON countries.continent_id = continents.id
                GROUP BY continents.name
                ORDER BY COUNT(countries.id) DESC
                LIMIT 1
                """, ResultSetMappers.STRING_MAPPER).get();
    }

}
