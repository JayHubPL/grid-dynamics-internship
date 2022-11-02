package com.griddynamics.flights;

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

public class FlightDatabase {

    private final List<Flight> flights;
    private final Gson gsonInstance;
    private final Logger logger;
    
    public FlightDatabase(Path pathToFlightDatabase, Gson gsonInstance) throws NoSuchFieldError, IOException {
        logger = LogManager.getLogger(FlightDatabase.class);
        this.gsonInstance = gsonInstance;
        flights = loadDatabaseFromFile(pathToFlightDatabase);
    }

    public List<Airport> getTravelDestinationsFrom(Airport airport) {
        return flights.stream()
            .filter(f -> f.origin().equals(airport))
            .map(Flight::destination)
            .collect(Collectors.toList());
    }

    public List<Airport> getOriginCityAirports(String originCityName) {
        return flights.stream()
            .map(Flight::origin)
            .filter(a -> a.cityName().equals(originCityName))
            .distinct()
            .collect(Collectors.toList());
    }

    private List<Flight> loadDatabaseFromFile(Path path) throws JsonSyntaxException, IOException {
        List<Flight> flights = Collections.emptyList();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            DatabaseRecord[] records = gsonInstance.fromJson(reader, DatabaseRecord[].class);
            flights = Arrays.stream(records)
                .map(DatabaseRecord::mapToFlight)
                .collect(Collectors.toList());
        } catch(FileNotFoundException fileNotFoundException) {
            throw logger.throwing(Level.FATAL, fileNotFoundException);
        } catch (JsonSyntaxException jsonSyntaxException) {
            throw logger.throwing(Level.FATAL, jsonSyntaxException);
        } catch (IOException ioException) {
            throw logger.throwing(Level.FATAL, ioException);
        }
        return flights;
    }

}
