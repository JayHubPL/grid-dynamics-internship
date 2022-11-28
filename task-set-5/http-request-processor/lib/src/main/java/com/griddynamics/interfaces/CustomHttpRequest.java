package com.griddynamics.interfaces;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.Optional;

import com.griddynamics.HttpMethod;

public interface CustomHttpRequest {
    static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    String getBody();
    void setBody(String body);
    void setHeaders(HttpHeaders headers);
    void setMethod(HttpMethod method);
    void setURI(URI uri);
    /**
     * Returns request's body encoding, if present.
     * @return {@code Optinal} caontaing body encoding as {@code String} or an {@code Optional.empty()}
     * if no encoding is specified in the request's {@code CONTENT_ENCODING_HEADER} header.
     */
    Optional<String> getBodyEncoding();
}
