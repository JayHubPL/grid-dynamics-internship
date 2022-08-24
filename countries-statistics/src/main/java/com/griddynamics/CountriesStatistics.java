package com.griddynamics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CountriesStatistics {
    
    /**
     * This program solves Countries Statistics tasks
     * utilizing Stream API as much as possible.
     * It takes as an parameter a path to a CSV file
     * containing country data.
     */
    public static void main(String[] args) {

        // parse arguments, get path to input file
        Path pathToInputFile = Path.of("countries.csv");
        if (args.length > 1) {
            try {
                pathToInputFile = Path.of(args[1]);
            } catch (InvalidPathException invalidPathException) {
                System.err.println("Invalid path format: " + args[1]);
            }
        }

        // parse .csv and get a list of countries
        List<Country> countries = Collections.emptyList();
        try {
            countries = readCountryDataFromCSV(pathToInputFile);
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Failed to open the text file: " + pathToInputFile);
            System.exit(1);
        } catch (InvalidContinentException invalidContinentException) {
            System.err.println(invalidContinentException.getMessage());
            System.exit(1);
        }

        // 1. Sort countries by population in the descending order
        countries.stream()
            .sorted(Collections.reverseOrder(Comparator.comparingInt(Country::population)))
            .forEach(System.out::println);
        
        // 2. Find territories with a maximum and minimum area
        Comparator<Country> compareByAreaAscending = Comparator.comparingInt(Country::area);
        Country minAreaCountry = countries.stream()
            .min(compareByAreaAscending)
            .orElseThrow();
        Country maxAreaCountry = countries.stream()
            .max(compareByAreaAscending)
            .orElseThrow();
        System.out.println("Minimum area: " + minAreaCountry);
        System.out.println("Maximum area: " + maxAreaCountry);

        // 3. Sort countries by continent and area in an ascending order (firstly countries are
        // sorted by continent; if countries have the same continent, they are sorted by area)
        countries.stream()
            .sorted(Comparator.comparing(Country::continent)
                .thenComparing(Country::area))
            .forEach(System.out::println);
        
        // 4. For a given continent find a country with a maximum population density
        // (population divided by area)
        Continent givenContinent = Continent.Asia;
        Comparator<Country> compareByPopDensityAscending =  new Comparator<Country>() {
            public int compare(Country o1, Country o2) {
                return (int) Math.signum(o1.getDensity() - o2.getDensity());
            }
        };
        Country maxPopDensityCountry = countries.stream()
            .filter(country -> country.continent().equals(givenContinent))
            .max(compareByPopDensityAscending)
            .orElseThrow();
        System.out.println("Maximum population density: " + maxPopDensityCountry);

        // 5. Calculate total area of each continent
        countries.stream()
            .collect(Collectors.groupingBy(Country::continent, Collectors.summingInt(Country::area)))
            .forEach((continent, totalArea) -> {
                System.out.println(continent + "'s total area: " + totalArea);
            });

        // 6. Find all continents, which have a total area greater than 15 million km2
        countries.stream()
            .collect(Collectors.groupingBy(Country::continent, Collectors.summingInt(Country::area)))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 15000000)
            .map(entry -> entry.getKey())
            .forEach(System.out::println);

        // 7. Group countries by the first letter of its name
        Map<Character, List<Country>> countriesGroupedByFirstLetter = countries.stream()
            .collect(Collectors.groupingBy(country -> country.name().charAt(0)));
        countriesGroupedByFirstLetter.entrySet().stream()
            .forEach(entry -> {
                entry.getValue().stream()
                    .forEach(System.out::println);
            });

        // 8. Find 95th percentile for population density
        double percentile =  countries.stream()
            .sorted(compareByPopDensityAscending)
            .skip((int)(.95 * countries.size()))
            .findFirst()
            .orElseThrow()
            .getDensity();
        System.out.println("95th percentile for population density: " + percentile);

        // 9. Using result from subtask 8, find all countries with
        // population density exceeding 95th percentile
        countries.stream()
            .filter(country -> country.getDensity() > percentile)
            .forEach(System.out::println);
        
    }

    private static List<Country> readCountryDataFromCSV(Path path)
    throws FileNotFoundException, InvalidContinentException, NoSuchElementException {
        List<Country> countries = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(path.toFile()))) {
            scanner.useDelimiter(";|\\r\\n");
            while (scanner.hasNext()) {
                String name = scanner.next();
                Continent continent = Continent.getContinentFromName(scanner.next());
                int area = Integer.parseInt(scanner.next());
                int population = Integer.parseInt(scanner.next());
                countries.add(new Country(name, continent, area, population));
            }
        }
        return countries;
    }
}
