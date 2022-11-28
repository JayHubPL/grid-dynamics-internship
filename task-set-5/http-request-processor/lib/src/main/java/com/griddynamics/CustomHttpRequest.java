package com.griddynamics;

import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Optional;

public class CustomHttpRequest extends HttpRequest implements com.griddynamics.interfaces.CustomHttpRequest {
    private String body;
    private HttpHeaders headers;
    private HttpMethod method;
    private URI uri;

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        return Optional.of(BodyPublishers.ofString(body));
    }

    @Override
    public String method() {
        return method.name();
    }

    @Override
    public Optional<Duration> timeout() {
        return Optional.empty();
    }

    @Override
    public boolean expectContinue() {
        return false;
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public Optional<Version> version() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;        
    }

    @Override
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;        
    }

    @Override
    public void setMethod(HttpMethod method) {
        this.method = method;        
    }

    @Override
    public void setURI(URI uri) {
        this.uri = uri;        
    }

    @Override
    public Optional<String> getBodyEncoding() {
        return headers.firstValue(CONTENT_ENCODING_HEADER);
    }
    
}
