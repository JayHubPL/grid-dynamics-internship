package com.griddynamics;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.griddynamics.jdbcutil.ConnectionAttributes;
import com.griddynamics.jdbcutil.PGDataSource;

public class Main {
    
    public static final String DATABASE_NAME = "countries_stats";
    public static final String USER = "postgres";
    public static final String PASSWORD = "docker";
    public static final int PORT = 5432;
    public static final InetAddress IP = InetAddress.getLoopbackAddress();
    public static final ConnectionAttributes CONN_ATTRIBUTES = new ConnectionAttributes(IP, PORT, DATABASE_NAME, USER, PASSWORD);
    public static final Path pathToCountriesFile = Paths.get("app", "src", "main", "resources", "countries.csv");

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        List<Country> countries = readCountryDataFromCSV(pathToCountriesFile);
        CountriesDBHandler db = new CountriesDBHandler(new PGDataSource(CONN_ATTRIBUTES), countries);
        // countries = db.findMany("""
        //         SELECT * FROM countries
        //         """, Country.mapper);
        // countries.forEach(System.out::println);
        List<Person> people = db.findMany("""
                SELECT id, name, age, ARRAY_AGG(citizenships.country_id) AS citizenship
                FROM people LEFT JOIN citizenships ON citizenships.person_id = people.id
                GROUP BY people.id
                """, Person.MAPPER);
        people.forEach(System.out::println);
        db.close();
    }
    
    private static List<Country> readCountryDataFromCSV(Path path) throws FileNotFoundException, NoSuchElementException {
        List<Country> countries = new ArrayList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(path.toFile()))) {
            scanner.useDelimiter(";|\\r\\n");
            while (scanner.hasNext()) {
                String name = scanner.next();
                Continent continent = Continent.getContinentFromName(scanner.next());
                int area = Integer.parseInt(scanner.next());
                int population = Integer.parseInt(scanner.next());
                countries.add(new Country(null, name, population, area, continent));
            }
        }
        return countries;
    }


}
