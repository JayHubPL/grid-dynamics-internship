package com.griddynamics.http;

public enum ResponseStatus {

    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    SERVICE_DOWN(503),
    UNKNOWN(-1);

    private final int statusCode;

    private ResponseStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static ResponseStatus mapStatusCodeToEnum(int statusCode) {
        return switch (statusCode) {
            case 200 -> OK;
            case 400 -> BAD_REQUEST;
            case 401 -> UNAUTHORIZED;
            case 503 -> SERVICE_DOWN;
            default -> UNKNOWN;
        };
    }

}
