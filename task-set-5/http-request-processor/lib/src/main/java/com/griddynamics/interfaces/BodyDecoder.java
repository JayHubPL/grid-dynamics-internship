package com.griddynamics.interfaces;

public interface BodyDecoder {
    
    /**
     * Decodes String using given encoding
     * @param body string to decode using 
     * @param encoding name of encoding used on the body
     * @return a decoded {@code body} string
     */
    String decode(String body, String encoding);

}
