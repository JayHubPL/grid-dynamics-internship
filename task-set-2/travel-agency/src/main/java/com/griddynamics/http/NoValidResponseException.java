package com.griddynamics.http;

import com.griddynamics.flights.Flight;

public class NoValidResponseException extends RuntimeException {
    
    public NoValidResponseException(Flight flight) {
        super(String.format("Failed to receive valid response for flight %s", flight));
    }
    
}
