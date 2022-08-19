package com.griddynamics;

public record Country (String name, Continent continent, long area, long population) {

    public double getDensity() {
        return (double) population / area;
    }
}
