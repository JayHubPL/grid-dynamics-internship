package com.griddynamics.flights;

public class Airport implements Comparable<Airport> {

    private String cityName;
    private String airportCode;

    public Airport(String name, String code) {
        this.cityName = name;
        this.airportCode = code;
    }

    public String cityName() {
        return cityName;
    }

    public String airportCode() {
        return airportCode;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", cityName, airportCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Airport other = (Airport) obj;
        if (airportCode == null) {
            if (other.airportCode != null)
                return false;
        } else if (!airportCode.equals(other.airportCode))
            return false;
        if (cityName == null) {
            if (other.cityName != null)
                return false;
        } else if (!cityName.equals(other.cityName))
            return false;
        return true;
    }

    @Override
    public int compareTo(Airport o) {
        return this.toString().compareTo(o.toString());
    }
    
}
