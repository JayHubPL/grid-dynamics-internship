package com.griddynamics.helperclasses;

import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

@JsonSerializable
public class ContainsAllPrimitives {

    @JsonAttribute
    private byte b;
    @JsonAttribute
    private short s;
    @JsonAttribute
    private int i;
    @JsonAttribute
    private long l;
    @JsonAttribute
    private float f;
    @JsonAttribute
    private double d;
    @JsonAttribute
    private boolean bool;
    @JsonAttribute
    private char c;

}
