package com.griddynamics.flights;

public class Flight {

    private final Airport origin;
    private final Airport destination;

    public Flight(Airport origin, Airport destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Airport origin() {
        return origin;
    }

    public Airport destination() {
        return destination;
    }

    @Override
    public String toString() {
        return String.format("from %s to %s", origin, destination);
    }

}
