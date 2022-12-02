package com.griddynamics.helperclasses;

import com.griddynamics.annotations.JsonSerializable;

@SuppressWarnings("unused")
@JsonSerializable
public class SimplePOJO {
    
    private Object obj;

    public SimplePOJO(Object obj) {
        this.obj = obj;
    }

}
