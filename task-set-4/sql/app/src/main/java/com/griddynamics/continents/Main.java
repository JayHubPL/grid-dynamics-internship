package com.griddynamics.continents;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.utils.CountriesDBHandler;

public class Main {

    private static final String DIV = "-".repeat(50);

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        DatabaseHandler db = CountriesDBHandler.getDefaultDBHandler();
        ContinentQueries cq = new ContinentQueries(db);
        
        List<Pair<Integer, String>> res1 = cq.numberOfCountriesOnEachContinent();
        System.out.printf(
            "%s%n1. Number of countries on each continent:%n%-15s%-15s%n",
        DIV, "Continent", "Count");
        res1.forEach(p -> System.out.printf("%-15s%-15s%n", p.getValue1(), p.getValue0()));

        List<Pair<Integer, String>> res2 = cq.continentsArea();
        System.out.printf(
            "%s%n2. Total area of each continent, descending:%n%-15s%-15s%n",
            DIV, "Continent", "Area (km2)");
        res2.forEach(p -> System.out.printf("%-15s%-15s%n", p.getValue1(), p.getValue0()));

        List<Pair<Integer, String>> res3 = cq.continentsAveragePopulationDensity();
        System.out.printf(
            "%s%n3. Average population density per continent:%n%-15s%-15s%n",
            DIV, "Continent", "Average density");
        res3.forEach(p -> System.out.printf("%-15s%-15s%n", p.getValue1(), p.getValue0()));

        List<Triplet<String, String, Integer>> res4 = cq.smallestCountryOnTheContinent();
        System.out.printf(
            "%s%n4. The smallest country on each continent:%n%-15s%-20s%-15s%n",
            DIV, "Continent", "Country", "Area (km2)");
        res4.forEach(p -> System.out.printf("%-15s%-20s%-15s%n", p.getValue0(), p.getValue1(), p.getValue2()));

        List<String> res5 = cq.continentsWithAreaGreaterThan(15000000);
        System.out.printf(
            "%s%n5. Continents with area greater than 15M km2:%n%-15s%n",
            DIV, "Continent");
        res5.forEach(s -> System.out.printf("%-15s%n", s));

        List<String> res6 = cq.continentsWithAverageCountryPopulationLessThan(20000000);
        System.out.printf(
            "%s%n6. Continents which have average country population less than 20M:%n%-15s%n",
            DIV, "Continent");
        res6.forEach(s -> System.out.printf("%-15s%n", s));

        db.close();
    }

}
