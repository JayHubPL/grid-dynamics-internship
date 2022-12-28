package com.griddynamics;

import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import javax.net.ssl.SSLSession;

public class CustomHttpResponse implements HttpResponse<String> {
    private final HttpRequest request;
    private HttpHeaders headers;
    private HttpResponseStatus statusCode;
    private String body;

    public CustomHttpResponse(CustomHttpRequest request) {
        this.request = request;
        statusCode = HttpResponseStatus.OK;
        headers = request.headers();
        body = request.getBody();
    }

    @Override
    public int statusCode() {
        return statusCode.getStatusCode();
    }

    public void setStatusCode(HttpResponseStatus status) {
        statusCode = status;
    }
    
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

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
