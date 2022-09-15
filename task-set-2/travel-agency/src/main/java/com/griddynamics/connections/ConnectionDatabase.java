package com.griddynamics.connections;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ConnectionDatabase {

    private final List<Connection> connections;
    private final Logger logger;
    
    public ConnectionDatabase(Path pathToConnectionsDatabase) throws NoSuchFieldError, IOException {
        logger = LogManager.getLogger(ConnectionDatabase.class);
        connections = loadDatabaseFromFile(pathToConnectionsDatabase);
    }

    public List<Airport> getTravelDestinationsFrom(Airport airport) {
        return connections.stream()
            .filter(c -> c.origin().equals(airport))
            .map(c -> c.destination())
            .collect(Collectors.toList());
    }

    public List<Airport> getOriginCityAirports(String originCityName) {
        return connections.stream()
            .map(c -> c.origin())
            .filter(a -> a.cityName().equals(originCityName))
            .distinct()
            .collect(Collectors.toList());
    }

    private List<Connection> loadDatabaseFromFile(Path path) throws NoSuchFieldError, JsonSyntaxException, IOException {
        List<Connection> connections = Collections.emptyList();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            DatabaseRecord[] records = new Gson().fromJson(reader, DatabaseRecord[].class);
            connections = Arrays.asList(records).stream()
                .map(DatabaseRecord::mapToConnection)
                .collect(Collectors.toList());
        } catch(FileNotFoundException fileNotFoundException) {
            logger.fatal("No databse file found");
            throw logger.throwing(Level.FATAL, fileNotFoundException);
        } catch (JsonSyntaxException jsonSyntaxException) {
            throw logger.throwing(Level.FATAL, jsonSyntaxException);
        } catch (IOException ioException) {
            throw logger.throwing(Level.FATAL, ioException);
        }
        return connections;
    }

}
