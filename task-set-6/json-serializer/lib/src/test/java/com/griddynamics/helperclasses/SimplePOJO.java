package com.griddynamics.helperclasses;

import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

@JsonSerializable
public class SimplePOJO {
    
    @JsonAttribute
    private Object obj;

    public SimplePOJO(Object obj) {
        this.obj = obj;
    }

}
