package com.griddynamics.stubs;

import com.griddynamics.interfaces.BodyDecoder;

public class StubBodyDecoder implements BodyDecoder {

    public static final String DECODE_SUFFIX = "_decoded";

    @Override
    public String decode(String body, String encoding) {
        return body + DECODE_SUFFIX;
    }
    
}
