package com.griddynamics.flights;

public class DatabaseRecord {
    
    private String origin;
    private String originCode;
    private String destination;
    private String destinationCode;

    public Flight mapToFlight() {
        Airport originAirport = new Airport(origin, originCode);
        Airport destinationAirport = new Airport(destination, destinationCode);
        return new Flight(originAirport, destinationAirport);
    }

}
