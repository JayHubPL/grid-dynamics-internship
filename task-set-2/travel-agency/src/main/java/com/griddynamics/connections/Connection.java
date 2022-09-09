package com.griddynamics.connections;

public class Connection {

    private City origin;
    private City destination;

    public Connection(City origin, City destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public City origin() {
        return origin;
    }

    public City destination() {
        return destination;
    }

}
