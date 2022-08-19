package com.griddynamics;

public enum Continent {
    Africa,
    Antarctica,
    Asia,
    Australia,
    Europe,
    NorthAmerica,
    SouthAmerica;

    public static Continent getContinentFromName(String continentName) throws InvalidContinentException {
        return switch (continentName) {
            case "Asia" -> Continent.Asia;
            case "Europe" -> Continent.Europe;
            case "Africa" -> Continent.Africa;
            case "Australia" -> Continent.Australia;
            case "Antarctica" -> Continent.Antarctica;
            case "North America" -> Continent.NorthAmerica;
            case "South America" -> Continent.SouthAmerica;
            default -> throw new InvalidContinentException(continentName);
        };
    }
}
