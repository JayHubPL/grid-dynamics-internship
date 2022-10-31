package com.griddynamics;

import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestProcessor {
    
    private static final Pattern credentialsPattern = Pattern.compile("(?<username>\\w+):(?<password>\\w+)");
    private final HttpRequest request;
    private final List<HttpMethod> allowedMethods;
    private String authenticationHeaderName;
    private Credentials credentials;

    public HttpRequestProcessor(HttpRequest request, List<HttpMethod> allowedMethods) {
        this.request = request;
        this.allowedMethods = allowedMethods;
        authenticationHeaderName = "Authorization";
    }

    public CustomHttpResponse process() {
        CustomHttpResponse response = new CustomHttpResponse(request);
        if (!validateCredentials()) {
            response.setStatusCode(401);
            return response;
        }
        if (!validateMethod()) {
            response.setStatusCode(405);
            return response;
        }
        if (!validateContentType()) {
            response.setStatusCode(415);
            return response;
        }
        
        return response;
    }

    private boolean validateContentType() {
        var contentType = request.headers().firstValue("Content-Type");
        return (contentType.isPresent() && contentType.get().equals("application/json"));
    }

    private boolean validateMethod() {
        return allowedMethods.contains(HttpMethod.mapNameToEnum(request.method()));
    }

    private boolean validateCredentials() {
        Optional<String> auth = request.headers().firstValue(authenticationHeaderName);
        if (auth.isEmpty()) {
            return false;
        }
        try {
            String decodedAuth = Arrays.toString(Base64.getDecoder().decode(auth.get()));
            Matcher matcher = credentialsPattern.matcher(decodedAuth);
            if (matcher.find()) {
                String username = matcher.group("username");
                String password = matcher.group("password");
                credentials = new Credentials(username, password);
            }
        } catch (IllegalArgumentException illegalArgumentException) {

        }
        return true;
    }

    public void setAuthenticationHeaderName(String name) {
        authenticationHeaderName = name;
    }

    public void allowMethods(HttpMethod... methods) {
        for (var method : methods) {
            if (!allowedMethods.contains(method)) {
                allowedMethods.add(method);
            }
        }
    }

    public void forbidMethods(HttpMethod... methods) {
        for (var method : methods) {
            allowedMethods.remove(method);
        }
    }

}
