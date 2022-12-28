package com.griddynamics;

public enum HttpResponseStatus {
    UNAUTHORIZED(401),
    METHOD_NOT_ALLOWED(405),
    WRONG_CONTENT_TYPE(415),
    SERVER_ERROR(500),
    BAD_REQUEST(400),
    OK(200);

    private final int statusCode;

    private HttpResponseStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
