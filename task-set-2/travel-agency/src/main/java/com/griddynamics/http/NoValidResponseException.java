package com.griddynamics.http;

import com.griddynamics.flights.Flight;

public class NoValidResponseException extends RuntimeException {

    private final String msg;

    public NoValidResponseException(Flight flight) {
        msg = String.format("Failed to receive valid response for flight %s", flight);
    }

    @Override
    public String getMessage() {
        return msg;
    }
    
}
