package com.griddynamics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

import com.griddynamics.jdbcutil.DatabaseConnectionProvider;
import com.griddynamics.jdbcutil.DatabaseHandler;

public class CountriesDBHandler extends DatabaseHandler {

    public static final int DEFAULT_POPULATION_SIZE = 10000;
    public static final int DEFAULT_MAX_CITIZENSHIPS = 4;
    public static Map<Integer, Continent> continentIdMapping = Collections.emptyMap(); // this is not the ideal solution but it does the job
    private static final Function<ResultSet, Pair<Integer, Continent>> mapper = rs -> {
        try {
            return new Pair<Integer,Continent>(
                rs.getInt("id"),
                Continent.getContinentFromName(rs.getString("name"))
            );
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    public CountriesDBHandler(DatabaseConnectionProvider provider, List<Country> countries, int noOfPeople, int maxCitizenships) throws SQLException {
        super(provider);
        initialize(countries, noOfPeople);
        populate(noOfPeople, maxCitizenships);
        continentIdMapping = createContinentIdMapping();
    }

    public CountriesDBHandler(DatabaseConnectionProvider provider, List<Country> countries, int noOfPeople) throws SQLException {
        this(provider, countries, noOfPeople, DEFAULT_MAX_CITIZENSHIPS);
    }

    public CountriesDBHandler(DatabaseConnectionProvider provider, List<Country> countries) throws SQLException {
        this(provider, countries, DEFAULT_POPULATION_SIZE, DEFAULT_MAX_CITIZENSHIPS);
    }

    private Map<Integer, Continent> createContinentIdMapping() throws SQLException {
        Map<Integer, Continent> mapping = new HashMap<>();
        String query = """
                SELECT id, name
                FROM continents
                """;
        findMany(query, mapper).forEach(p -> mapping.put(p.getFirst(), p.getSecond()));
        return mapping;
    }

    private void initialize(List<Country> countries, int noOfPeople) throws SQLException {
        createTables();
        initializeContinentTable();
        initializeCountryTable(countries);
    }

    public void createTables() throws SQLException {
        String query = """
                CREATE TABLE IF NOT EXISTS continents
                ( id SERIAL PRIMARY KEY
                , name varchar(50)
                )
                """;
        execute(query);
        query = """
                CREATE TABLE IF NOT EXISTS countries
                ( id SERIAL PRIMARY KEY
                , name varchar(50) NOT NULL
                , population int NOT NULL
                , area int NOT NULL
                , continent_id int REFERENCES continents
                )
                """;
        execute(query);
        query = """
                CREATE TABLE IF NOT EXISTS people
                ( id SERIAL PRIMARY KEY
                , name varchar(50) NOT NULL
                , age int NOT NULL
                , citizenship int[]
                )
                """;
                /*
                 * citizenship int[] REFERENCES countries (id)
                 * or 
                 * citizenship int[] ELEMENT REFERENCES countries
                 * 
                 * This is not possible in PostgreSQL
                 * https://stackoverflow.com/questions/41054507/postgresql-array-of-elements-that-each-are-a-foreign-key
                 * 
                 * TODO find another solution to create relation between each citizenship and country
                 * 
                 * IDEA: additional table citizenships (person_id, country_id)
                 */
        execute(query);
    }

    private void populate(int noOfPeople, int maxCitizenships) throws SQLException {
        Random rng = new Random();
        int maxCountryIndex = findOne("SELECT MAX(id) AS max_id FROM countries", rs -> {
            try {
                return rs.getInt("max_id");
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        }).get();
        List<List<Object>> argsList = new ArrayList<>();
        for (int i = 0; i < noOfPeople; i++) {
            int noOfCitizenships = rng.nextInt(maxCitizenships + 1);
            List<Integer> citizenships = new ArrayList<>();
            for (int j = 0; j < noOfCitizenships; j++) {
                citizenships.add(rng.nextInt(1, maxCountryIndex + 1));
            }
            argsList.add(
                List.<Object>of(String.format("Name%d", i + 1),
                rng.nextInt(100),
                citizenships.toArray(new Integer[noOfCitizenships]))
            );
        }
        batchExecute("""
                INSERT INTO people (name, age, citizenship)
                VALUES (?, ?, ?)
                """, argsList);
    }

    private void batchExecute(String query, List<List<Object>> argsList) throws SQLException {
        try (PreparedStatement prepStmt = conn.prepareStatement(query)) {
            for (var args : argsList) {
                for (int i = 0; i < args.size(); i++) {
                    prepStmt.setObject(i + 1, args.get(i));
                }
                prepStmt.addBatch();
            }
            prepStmt.executeBatch();
            conn.commit();
        }
    }

    private void initializeContinentTable() throws SQLException {
        List<List<Object>> argsList = Arrays.stream(Continent.values())
            .map(Continent::getPrettyName)
            .map(List::<Object>of)
            .collect(Collectors.toList());
        batchExecute("INSERT INTO continents (name) VALUES (?)", argsList);
    }

    private void initializeCountryTable(List<Country> countries) throws SQLException {
        List<List<Object>> argsList = countries.stream()
            .map(c -> List.<Object>of(c.name(), c.population(), c.area(), c.continent().getPrettyName()))
            .collect(Collectors.toList());
        batchExecute("""
                INSERT INTO countries (name, population, area, continent_id)
                SELECT ?, ?, ?, continents.id
                FROM continents
                WHERE continents.name = ?
                """,
                argsList);
    }
    
}
