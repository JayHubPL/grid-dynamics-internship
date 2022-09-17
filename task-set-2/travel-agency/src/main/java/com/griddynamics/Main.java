package com.griddynamics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.griddynamics.flights.FlightDatabase;
import com.griddynamics.http.HttpRequestHandler;
import com.griddynamics.http.InvalidTokenException;
import com.griddynamics.ui.UserInterface;
import com.griddynamics.utils.FileParser;

public class Main {
    
    public static void main(String[] args) {
        UserInterface UI = new UserInterface();
        Gson gson = new Gson();

        if (args.length == 0) {
            UI.showMsg("No database file was provided. Please provide a path to the file containing flights information.");
            System.exit(-1);
        }

        try {
            Path databasePath = Path.of(args[0]);
            Path tokenPath = args.length > 1 ? Path.of(args[1]) : Path.of("token.txt");
            
            String token = FileParser.parseTokenFile(tokenPath);

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequestHandler httpRequestHandler = new HttpRequestHandler(httpClient, token, gson);
            FlightDatabase flightDB = new FlightDatabase(databasePath, gson);

            TravelAgencyClient travelAgencyClient = new TravelAgencyClient(UI, flightDB, httpRequestHandler);
            travelAgencyClient.runApplication();

            System.exit(0);
        } catch (InvalidPathException invalidPathException) {
            UI.showMsg(String.format("Provided path is invalid: %s. Please provide a valid path.", args[0]));
        } catch (InvalidTokenException invalidTokenException) {
            UI.showMsg("API token is invalid and/or does not give neccesary authorization level. Please contact the administrator.");
        } catch (FileNotFoundException fileNotFoundException) {
            String missingFile = fileNotFoundException.getMessage().split(" ", 2)[0];
            switch (missingFile) {
                case "token.txt" -> UI.showMsg("Token file was not found. Please ensure there is a \"token.txt\" file in program directory. You can also provide custom path to the token through the second argument.");
                default -> UI.showMsg(String.format("%s was not found. Please ensure there is a %s file in the specified directory or the directory is correct.", missingFile));
            }
        } catch (JsonSyntaxException jsonSyntaxException) {
            UI.showMsg("Errors in database format. Please esure that the data is valid and in json format.");
        } catch (IOException ioException) {
            UI.showMsg("Application has encountered irrecoverable error and will now exit.");
        }

        System.exit(-1);
    }
}
