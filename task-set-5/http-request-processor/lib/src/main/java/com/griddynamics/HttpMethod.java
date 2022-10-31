package com.griddynamics;

public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    PATCH;

    public static HttpMethod mapNameToEnum(String methodName) {
        return switch (methodName) {
            case "GET" -> HttpMethod.GET;
            case "HEAD" -> HttpMethod.HEAD;
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            case "DELETE" -> HttpMethod.DELETE;
            case "CONNECT" -> HttpMethod.CONNECT;
            case "OPTIONS" -> HttpMethod.OPTIONS;
            case "TRACE" -> HttpMethod.TRACE;
            case "PATCH" -> HttpMethod.PATCH;
            default -> throw new IllegalArgumentException("Invalid HTTP method " + methodName);
        };
    }

}
