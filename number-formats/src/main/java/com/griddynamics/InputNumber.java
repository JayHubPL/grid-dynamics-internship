package com.griddynamics;

public class InputNumber implements Comparable<InputNumber> {

    private final String stringRepresentation;
    private final double value;

    public InputNumber(String numberAsString) throws NumberFormatException {
        this.stringRepresentation = numberAsString;
        this.value = Double.parseDouble(numberAsString);
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    @Override
    public int compareTo(InputNumber o) {
        return (int) Math.signum(this.value - o.value);
    }
}
