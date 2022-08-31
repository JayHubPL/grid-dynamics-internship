package com.griddynamics;

public class InvalidContinentException extends Exception {

    private final String continent;

    InvalidContinentException(String continent) {
        this.continent = continent;
    }

    @Override
    public String getMessage() {
        return "Continent " + continent + " does not exist!";
    }
    
    
}
