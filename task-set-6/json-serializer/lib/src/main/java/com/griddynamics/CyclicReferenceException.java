package com.griddynamics;

public class CyclicReferenceException extends RuntimeException {
    
    public CyclicReferenceException(String msg) {
        super(msg);
    }

}
