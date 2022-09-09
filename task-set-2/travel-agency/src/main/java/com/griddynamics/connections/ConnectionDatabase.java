package com.griddynamics.connections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class ConnectionDatabase {

    private final List<Connection> connections;
    
    public ConnectionDatabase(Path pathToConnectionsDatabase) {
        connections = loadDatabaseFromFile(pathToConnectionsDatabase);
    }

    public List<City> getTravelDestinationsFrom(String originCityName) {
        return connections.stream()
            .filter(c -> c.origin().name().equals(originCityName))
            .map(c -> c.destination())
            .collect(Collectors.toList());
    }

    private List<Connection> loadDatabaseFromFile(Path path) {
        List<Connection> connections = Collections.emptyList();
        try {
            String content = Files.readString(path);
            DatabaseRecord[] records = new Gson().fromJson(content, DatabaseRecord[].class);
            connections = Arrays.asList(records).stream()
                .map(DatabaseRecord::mapToConnection)
                .collect(Collectors.toList());
        } catch(NoSuchFileException noSuchFileException) {
            System.err.println(noSuchFileException.getMessage());
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }
        return connections;
    }

}
