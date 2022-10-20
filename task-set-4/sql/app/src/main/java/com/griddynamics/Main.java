package com.griddynamics;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import com.griddynamics.people.Person;
import com.griddynamics.utils.CountriesDBHandler;

// TODO remove this class
public class Main {
    
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        CountriesDBHandler db = CountriesDBHandler.getDefaultDBHandler();
        List<Person> people = db.findMany("""
                SELECT id, name, age, ARRAY_AGG(citizenships.country_id) AS citizenship
                FROM people LEFT JOIN citizenships ON citizenships.person_id = people.id
                GROUP BY people.id
                """, Person.MAPPER);
        people.forEach(System.out::println);
        db.close();
    }

}
