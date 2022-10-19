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
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Pair;

import com.griddynamics.jdbcutil.DatabaseConnectionProvider;
import com.griddynamics.jdbcutil.DatabaseHandler;

public class CountriesDBHandler extends DatabaseHandler {

    private static final Function<ResultSet, Integer> MAX_ID_MAPPER = rs -> {
        try {
            return rs.getInt("max_id");
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    };

    public static final int DEFAULT_POPULATION_SIZE = 100;
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
        deleteTables();
        createTables();
        initializeContinentsTable();
        continentIdMapping = createContinentIdMapping();
        initializeCountriesTable(countries);
        initializePeopleTable(noOfPeople);
        initializeCitizenshipsTable(maxCitizenships);
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

    private void deleteTables() throws SQLException {
        execute("DROP TABLE IF EXISTS citizenships");
        execute("DROP TABLE IF EXISTS countries");
        execute("DROP TABLE IF EXISTS people");
        execute("DROP TABLE IF EXISTS continents");
    }

    private void createTables() throws SQLException {
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
                 * IDEA: additional table citizenships (person_id, country_id)
                 */
        execute(query);
        query = """
                CREATE TABLE IF NOT EXISTS citizenships
                ( person_id int REFERENCES people (id)
                , country_id int REFERENCES countries (id)
                )
                """;
        execute(query);
    }

    private void initializePeopleTable(int noOfPeople) throws SQLException {
        Random rng = new Random();
        List<List<Object>> argsList = new ArrayList<>();
        for (int i = 0; i < noOfPeople; i++) {
            argsList.add(List.<Object>of(
                String.format("Name%d", i + 1),
                rng.nextInt(100)
            ));
        }
        batchExecute("""
                INSERT INTO people (name, age)
                VALUES (?, ?)
                """, argsList);
    }

    private void initializeCitizenshipsTable(int maxCitizenships) throws SQLException {
        Random rng = new Random();
        List<List<Object>> argsList = new ArrayList<>();
        int maxCountryIndex = getMaxIDInTable("countries");
        int maxPersonID = getMaxIDInTable("people");
        for (int personID = 1; personID <= maxPersonID; personID++) {
            int noOfCitizenships = rng.nextInt(maxCitizenships + 1);
            Set<Integer> countryIDs = new TreeSet<>();
            while (countryIDs.size() < noOfCitizenships) {
                countryIDs.add(rng.nextInt(1, maxCountryIndex + 1));
            }
            for (var countryID : countryIDs) {
                argsList.add(List.<Object>of(personID, countryID));
            }
        }
        batchExecute("""
                INSERT INTO citizenships (person_id, country_id)
                VALUES (?, ?)
                """, argsList);
    }

    private int getMaxIDInTable(String tableName) throws SQLException {
        return findOne( "SELECT MAX(id) AS max_id FROM " + tableName, MAX_ID_MAPPER).get();
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

    private void initializeContinentsTable() throws SQLException {
        List<List<Object>> argsList = Arrays.stream(Continent.values())
            .map(Continent::getPrettyName)
            .map(List::<Object>of)
            .collect(Collectors.toList());
        batchExecute("INSERT INTO continents (name) VALUES (?)", argsList);
    }

    private void initializeCountriesTable(List<Country> countries) throws SQLException {
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
