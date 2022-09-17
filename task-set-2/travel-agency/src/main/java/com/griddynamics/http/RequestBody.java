package com.griddynamics.http;

import com.griddynamics.flights.Flight;

public record RequestBody(String origin, String destination) {
    
    public RequestBody(Flight flight) {
        this(flight.origin().airportCode(), flight.destination().airportCode());
    }

}
