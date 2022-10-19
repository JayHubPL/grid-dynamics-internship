package com.griddynamics;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Continent {
    AFRICA,
    ANTARCTICA,
    ASIA,
    AUSTRALIA,
    EUROPE,
    NORTH_AMERICA,
    SOUTH_AMERICA;

    public static Continent getContinentFromName(String continentName) {
        return switch (continentName) {
            case "Asia" -> ASIA;
            case "Europe" -> EUROPE;
            case "Africa" -> AFRICA;
            case "Australia" -> AUSTRALIA;
            case "Antarctica" -> ANTARCTICA;
            case "North America" -> NORTH_AMERICA;
            case "South America" -> SOUTH_AMERICA;
            default -> throw new RuntimeException("Continent " + continentName + " does not exist");
        };
    }

    public String getPrettyName() {
        return Arrays.stream(name().split("_"))
            .map(s -> {
                s = s.toLowerCase();
                return s.substring(0, 1).toUpperCase() + s.substring(1);
            })
            .collect(Collectors.joining(" "));
    }
}
