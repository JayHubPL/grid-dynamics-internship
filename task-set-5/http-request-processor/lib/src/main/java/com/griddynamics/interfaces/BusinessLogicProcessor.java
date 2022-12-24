package com.griddynamics.interfaces;

import java.net.http.HttpResponse;

import com.griddynamics.CustomHttpRequest;

public interface BusinessLogicProcessor {
    
    /**
     * Apllies business logic to the HTTP request and generates an HTTP response.
     * @param request an HTTP request to have business logic apllied to it
     * @return an HTTP response
     */
    HttpResponse<String> applyLogic(CustomHttpRequest request);

}
