package com.griddynamics.helperclasses;

import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

@JsonSerializable
public class ContainsUnmarkedFields {
    
    @JsonAttribute
    private Object marked;
    @SuppressWarnings("unused")
    private Object unmarked;

}
