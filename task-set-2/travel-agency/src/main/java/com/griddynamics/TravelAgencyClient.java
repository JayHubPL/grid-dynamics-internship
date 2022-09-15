package com.griddynamics;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.griddynamics.connections.Airport;
import com.griddynamics.connections.Connection;
import com.griddynamics.connections.ConnectionDatabase;
import com.griddynamics.http.HttpRequestHandler;
import com.griddynamics.http.InvalidTokenException;
import com.griddynamics.ui.UserInterface;

import com.robustsoft.currencies.Currency;

public class TravelAgencyClient {
    
    private final ConnectionDatabase connectionDB;
    private final HttpRequestHandler httpRequestHandler;
    private final UserInterface UI;

    public TravelAgencyClient(UserInterface UI, Path databasePath, Path tokenPath) throws IOException, JsonSyntaxException, FileNotFoundException {
        this.UI = UI;
        connectionDB = new ConnectionDatabase(databasePath);
        httpRequestHandler = new HttpRequestHandler(tokenPath);
    }

    public void run() throws InvalidTokenException, IOException {
        UI.showMsg("Welcome to Flight Price Checker!");

        boolean shouldExitProgram = false;
        while (!shouldExitProgram) {
            List<Airport> originAirports = connectionDB.getOriginCityAirports(UI.requestOriginCityName());
            while (originAirports.isEmpty()) {
                UI.showMsg("No such origin city exists in the database. Check for spelling or try another city.");
                originAirports = connectionDB.getOriginCityAirports(UI.requestOriginCityName());
            }

            Airport origin = originAirports.get(0);
            if (originAirports.size() > 1) {
                origin = UI.requestElementFromList(originAirports, "There are multiple airports in this city. Please choose one of them:");
            }

            List<Airport> destinations = connectionDB.getTravelDestinationsFrom(origin);
            
            Airport destination = UI.requestElementFromList(destinations, "Choose your destination:");

            UI.showMsg("Checking the price...");
            BigDecimal price = httpRequestHandler.makePriceRequest(new Connection(origin, destination));

            Currency currency = UI.requestCurrency();
            UI.showMsg(String.format("The price of the flight from %s to %s is %s", origin, destination, currency.of(price)));

            shouldExitProgram = UI.askIfWantsToExit();
        }

        UI.showMsg("Thank you and goodbye!");
    }

}
