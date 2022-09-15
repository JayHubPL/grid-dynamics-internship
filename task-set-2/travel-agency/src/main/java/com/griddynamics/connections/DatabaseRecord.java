package com.griddynamics.connections;

public class DatabaseRecord {
    
    private String origin;
    private String originCode;
    private String destination;
    private String destinationCode;

    public Connection mapToConnection() {
        Airport originCity = new Airport(origin, originCode);
        Airport destinationCity = new Airport(destination, destinationCode);
        return new Connection(originCity, destinationCity);
    }

}
