package com.griddynamics.connections;

public class Connection {

    private Airport origin;
    private Airport destination;

    public Connection(Airport origin, Airport destination) {
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
        return String.format("%s -> %s", origin, destination);
    }

}
