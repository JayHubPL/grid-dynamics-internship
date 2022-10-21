package com.griddynamics.countries;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.javatuples.Pair;

import com.griddynamics.jdbcutil.DatabaseHandler;
import com.griddynamics.utils.CountriesDBHandler;

public class Main {
    
    private static final String DIV = "-".repeat(50);

    public static void main(String[] args) throws FileNotFoundException, NoSuchElementException, SQLException {
        DatabaseHandler db = CountriesDBHandler.getDefaultDBHandler();
        CountryQueries cq = new CountryQueries(db);

        int res1 = cq.biggestPopulation();
        System.out.printf(
            "%s%n1. Biggest population for any country:%n%-15s%n",
        DIV, "Population");
        System.out.printf("%-15d%n", res1);

        Pair<Integer, String> res2 = cq.countryWithTheBiggestPopulation();
        System.out.printf(
            "%s%n2. Country with the biggest population:%n%-5s%-15s%n",
        DIV, "ID", "Country");
        System.out.printf("%-5d%-15s%n", res2.getValue0(), res2.getValue1());

        Pair<Integer, String> res3 = cq.countryWithTheBiggestDensity();
        System.out.printf(
            "%s%n3. Country with the biggest population density:%n%-5s%-15s%n",
        DIV, "ID", "Country");
        System.out.printf("%-5d%-15s%n", res3.getValue0(), res3.getValue1());

        int bottomN = 10;
        List<String> res4 = cq.countriesWithLowestDensity(bottomN);
        System.out.printf(
            "%s%n4. Top %d countries with the lowest population density:%n%-15s%n",
        DIV, bottomN, "Country");
        res4.forEach(s -> System.out.printf("%-15s%n", s));

        List<String> res5 = cq.countriesWithDensityAboveAverage();
        System.out.printf(
            "%s%n5. Countries with population density higher than average across all countries:%n%-15s%n",
        DIV, "Country");
        res5.forEach(s -> System.out.printf("%-15s%n", s));

        String res6 = cq.countryWithDensityClosestToTheAverage();
        System.out.printf(
            "%s%n6. Country which has a population, closest to the average population of all countries:%n%-15s%n",
        DIV, "Country");
        System.out.printf("%-15s%n", res6);

        List<Pair<Integer, String>> res7 = cq.countCountriesWithGreaterArea();
        System.out.printf(
            "%s%n7. Number of countries with area greater area than their own:%n%-25s%-15s%n",
        DIV, "Country", "Bigger countries (area)");
        res7.forEach(p -> System.out.printf("%-25s%-15s%n", p.getValue1(), p.getValue0()));

        List<String> res8 = cq.countriesWithTheLongestName();
        System.out.printf(
            "%s%n8. Countries with the longest name:%n%-25s%n",
        DIV, "Country");
        res8.forEach(s -> System.out.printf("%-25s%n", s));

        Character startingLetter = 'A';
        List<String> res9 = cq.countriesNamesStartingWithLetter(startingLetter);
        System.out.printf(
            "%s%n9. All countries with the name starting with the letter %c:%n%-15s%n",
        DIV, startingLetter, "Country");
        res9.forEach(s -> System.out.printf("%-15s%n", s));

        Character containsLetter = 'F';
        List<String> res10 = cq.countriesContainingLetter(containsLetter);
        System.out.printf(
            "%s%n10. Countries containing letter %c in the name, in alphabetical order:%n%-15s%n",
        DIV, containsLetter, "Country");
        res10.forEach(s -> System.out.printf("%-15s%n", s));

        db.close();
    }

}
