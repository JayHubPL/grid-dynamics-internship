package com.griddynamics.people;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.utils.CountriesDBHandler;

public class Main {

    private static final String DIV = "-".repeat(50);
    
    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException, SQLException {
        DatabaseHandler db = CountriesDBHandler.getDefaultDBHandler();
        PeopleQueries pq = new PeopleQueries(db);

        List<Person> res1 = pq.peopleWithTheBiggestNumberOfCitizenships();
        System.out.printf(
            "%s%n1. People with the highest number of citizenships:%n%-6s%-11s%-4s%n",
        DIV, "ID", "Name", "Age");
        res1.forEach(System.out::println);

        List<Person> res2 = pq.peopleWithNoCitizenship();
        System.out.printf(
            "%s%n2. People with no citizenships:%n%-6s%-11s%-4s%n",
        DIV, "ID", "Name", "Age");
        res2.forEach(System.out::println);

        List<String> res3 = pq.countriesWithTheLeastPersons();
        System.out.printf(
            "%s%n3. Country with the least people in Person table:%n%-25s%n",
        DIV, "Country");
        res3.forEach(System.out::println);

        String res4 = pq.continentWithTheBiggestNumberOfPersons();
        System.out.printf(
            "%s%n4. Continent with the most people in Person table:%n%-25s%n",
        DIV, "Continent");
        System.out.println(res4);

        String res5 = pq.continentWhichHasTheBiggestNumberOfPopulatedCountries();
        System.out.printf(
            "%s%n5. Continent, which has the biggest number of countries, which have at least one person in Person table:%n%-25s%n",
        DIV, "Continent");
        System.out.println(res5);

        db.close();
    }

}
