package com.griddynamics;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.griddynamics.interfaces.BodyDecoder;
import com.griddynamics.interfaces.BusinessLogicProcessor;
import com.griddynamics.interfaces.CredentialsValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestProcessor {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE = "application/json";
    private static final String AUTHORIZATION_DEFAULT_HEADER = "Authorization";
    
    private final List<HttpMethod> allowedMethods;
    private final CredentialsValidator credValidator;
    private final BodyDecoder bodyDecoder;
    private final BusinessLogicProcessor blg;
    private String authenticationHeaderName;

    private CustomHttpRequest request;
    private Credentials credentials;

    public HttpRequestProcessor(List<HttpMethod> allowedMethods, CredentialsValidator credValidator, BodyDecoder bodyDecoder,
    BusinessLogicProcessor blp, String authenticationHeaderName) {
        this.allowedMethods = allowedMethods;
        this.credValidator = credValidator;
        this.bodyDecoder = bodyDecoder;
        this.blg = blp;
        this.authenticationHeaderName = authenticationHeaderName;
    }

    public HttpRequestProcessor(List<HttpMethod> allowedMethods, CredentialsValidator credValidator, BodyDecoder bodyDecoder,
    BusinessLogicProcessor blp) {
        this(allowedMethods, credValidator, bodyDecoder, blp, AUTHORIZATION_DEFAULT_HEADER);
    }

    public HttpResponse<String> process(CustomHttpRequest request) {
        this.request = request;
        CustomHttpResponse response = new CustomHttpResponse(request);

        // validate request parameters
        if (!validateAndAcquireCredentials()) {
            response.setStatusCode(HttpResponseStatus.UNAUTHORIZED);
            return response;
        }
        if (!validateMethod()) {
            response.setStatusCode(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return response;
        }
        if (!validateContentType()) {
            response.setStatusCode(HttpResponseStatus.WRONG_CONTENT_TYPE);
            return response;
        }

        // decode body if encoding is present
        Optional<String> bodyEncoding = request.getBodyEncoding();
        if (bodyEncoding.isPresent()) {
            request.setBody(bodyDecoder.decode(request.getBody(), bodyEncoding.get()));
        }

        // log request contents
        log.info("Method: " + request.method());
        log.info("URI: " + request.uri().toASCIIString());
        log.info("Body: " + request.getBody());
        log.info("Username: " + credentials.getUsername());
        
        // apply business logic
        try {
            return blg.applyLogic(request);
        } catch (Exception exception) {
            response.setStatusCode(HttpResponseStatus.SERVER_ERROR);
        }
        return response;
    }

    private boolean validateContentType() {
        var contentType = request.headers().firstValue(CONTENT_TYPE_HEADER);
        return (contentType.isPresent() && contentType.get().equals(CONTENT_TYPE));
    }

    private boolean validateMethod() {
        return allowedMethods.contains(HttpMethod.mapNameToEnum(request.method()));
    }

    private boolean validateAndAcquireCredentials() {
        Optional<String> auth = request.headers().firstValue(authenticationHeaderName);
        if (auth.isEmpty()) {
            return false;
        }
        try {
            credentials = credValidator.checkCredentials(Arrays.toString(Base64.getDecoder().decode(auth.get())));
            return credentials.isAuthenticated();
        } catch (IllegalArgumentException illegalArgumentException) { // if auth was not base64
            return false;
        }
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
