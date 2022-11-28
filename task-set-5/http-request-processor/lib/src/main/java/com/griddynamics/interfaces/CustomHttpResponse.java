package com.griddynamics.interfaces;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;

import com.griddynamics.HttpResponseStatus;

public interface CustomHttpResponse<T> extends HttpResponse<T> {
    void setStatusCode(HttpResponseStatus status);
    void setHeaders(HttpHeaders headers);
    void setBody(String body);
}
