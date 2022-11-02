package com.griddynamics.http;

import com.griddynamics.flights.Flight;

public record FlightPriceRequestBody(String origin, String destination) {
    
    public FlightPriceRequestBody(Flight flight) {
        this(flight.origin().airportCode(), flight.destination().airportCode());
    }

}
