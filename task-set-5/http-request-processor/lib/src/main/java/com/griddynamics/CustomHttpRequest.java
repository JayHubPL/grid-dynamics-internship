package com.griddynamics;

import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomHttpRequest extends HttpRequest {
    static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;        
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;        
    }

    public void setMethod(HttpMethod method) {
        this.method = method;        
    }

    public void setURI(URI uri) {
        this.uri = uri;        
    }

    /**
     * Returns request's body encoding, if present.
     * @return {@code Optinal} caontaing body encoding as {@code String} or an {@code Optional.empty()}
     * if no encoding is specified in the request's {@code CONTENT_ENCODING_HEADER} header.
     */
    public Optional<String> getBodyEncoding() {
        return headers.firstValue(CONTENT_ENCODING_HEADER);
    }
    
}
