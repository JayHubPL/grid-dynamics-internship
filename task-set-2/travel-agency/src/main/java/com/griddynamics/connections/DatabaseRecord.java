package com.griddynamics.connections;

public class DatabaseRecord {
    
    private String origin;
    private String originCode;
    private String destination;
    private String destinationCode;

    public Connection mapToConnection() {
        City originCity = new City(origin, originCode);
        City destinationCity = new City(destination, destinationCode);
        return new Connection(originCity, destinationCity);
    }

}
