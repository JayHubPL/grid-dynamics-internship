package com.griddynamics;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.griddynamics.flights.Airport;
import com.griddynamics.flights.Flight;
import com.griddynamics.flights.FlightDatabase;
import com.griddynamics.http.HttpRequestHandler;
import com.griddynamics.http.InvalidTokenException;
import com.griddynamics.ui.UserInterface;

import com.robustsoft.currencies.Currency;

public class TravelAgencyClient {
    
    private final FlightDatabase connectionDB;
    private final HttpRequestHandler httpRequestHandler;
    private final UserInterface UI;

    public TravelAgencyClient(UserInterface UI, FlightDatabase connectionDB, HttpRequestHandler httpRequestHandler) {
        this.UI = UI;
        this.connectionDB = connectionDB;
        this.httpRequestHandler = httpRequestHandler;
    }

    public void runApplication() throws InvalidTokenException, IOException {
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
            BigDecimal price = httpRequestHandler.makePriceRequest(new Flight(origin, destination));

            Currency currency = UI.requestCurrency();
            UI.showMsg(String.format("The price of the flight from %s to %s is %s", origin, destination, currency.of(price)));

            shouldExitProgram = UI.askIfWantsToExit();
        }

        UI.showMsg("Thank you and goodbye!");
    }

}
