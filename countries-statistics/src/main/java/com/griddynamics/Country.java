package com.griddynamics;

public record Country(String name, Continent continent, int area, int population) {

    public double getDensity() {
        return (double) population / area;
    }

    @Override
    public String toString() {
        return String.format("%-24s %-12s Area: %-8d Pop: %-10d", name, continent, area, population);
    }

}
