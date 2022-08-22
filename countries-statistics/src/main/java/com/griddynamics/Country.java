package com.griddynamics;

public record Country (String name, Continent continent, int area, int population) {

    public double getDensity() {
        return (double) population / area;
    }
}
