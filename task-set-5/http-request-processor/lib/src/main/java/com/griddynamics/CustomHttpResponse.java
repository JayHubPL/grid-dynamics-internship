package com.griddynamics;

import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import javax.net.ssl.SSLSession;

public class CustomHttpResponse implements com.griddynamics.interfaces.CustomHttpResponse<String> {
    private HttpRequest request;
    private HttpHeaders headers;
    private int statusCode;
    private String body;

    public CustomHttpResponse(HttpRequest request) {
        this.request = request;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public void setStatusCode(HttpResponseStatus status) {
        statusCode = status.getStatusCode();
    }
    
    @Override
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public void setBody(String body) {
        this.body = body;        
    }

    @Override
    public HttpRequest request() {
        return request;
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public Version version() {
        return null;
    }

}
