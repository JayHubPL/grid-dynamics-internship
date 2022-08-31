package com.griddynamics;

public enum Continent {
    AFRICA,
    ANTARCTICA,
    ASIA,
    AUSTRALIA,
    EUROPE,
    NORTH_AMERICA,
    SOUTH_AMERICA;

    public static Continent getContinentFromName(String continentName) throws InvalidContinentException {
        return switch (continentName) {
            case "Asia" -> Continent.ASIA;
            case "Europe" -> Continent.EUROPE;
            case "Africa" -> Continent.AFRICA;
            case "Australia" -> Continent.AUSTRALIA;
            case "Antarctica" -> Continent.ANTARCTICA;
            case "North America" -> Continent.NORTH_AMERICA;
            case "South America" -> Continent.SOUTH_AMERICA;
            default -> throw new InvalidContinentException(continentName);
        };
    }
}
