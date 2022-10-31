package com.griddynamics;

import java.net.http.HttpRequest;

public class CustomHttpResponse {
    
    private final HttpRequest request;
    private int statusCode;

    public CustomHttpResponse(HttpRequest request, int statusCode) {
        this.request = request;
        this.statusCode = statusCode;
    }

    public CustomHttpResponse(HttpRequest request) {
        this(request, 500);
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
