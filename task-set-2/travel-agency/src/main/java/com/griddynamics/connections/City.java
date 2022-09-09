package com.griddynamics.connections;

public class City {

    private String name;
    private String code;

    public City(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String name() {
        return name;
    }

    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", name, code);
    }
    
}
