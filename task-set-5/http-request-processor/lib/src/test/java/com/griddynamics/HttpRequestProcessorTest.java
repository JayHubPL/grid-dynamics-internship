package com.griddynamics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.Test;

import com.griddynamics.interfaces.BodyDecoder;
import com.griddynamics.interfaces.BusinessLogicProcessor;
import com.griddynamics.stubs.StubBodyDecoder;
import com.griddynamics.stubs.StubBusinessLogicProcessor;

public class HttpRequestProcessorTest {
    
    private static final BiPredicate<String, String> PREDICATE = new BiPredicate<>() {
        @Override
        public boolean test(String t, String u) {
            return true;
        }
    };
    private static final HttpHeaders VALID_HEADERS = HttpHeaders.of(Map.of(
            "Authorization", List.of(Base64.getEncoder().encodeToString("user:pass".getBytes())),
            "Content-Type", List.of("application/json")
        ), PREDICATE);
    private final BodyDecoder decoder = new StubBodyDecoder();
    private final BusinessLogicProcessor blp = new StubBusinessLogicProcessor();
    private final CustomHttpRequest request = new CustomHttpRequest(null, null, HttpMethod.GET, URI.create("uri"));

    @Test
    public void process_AuthHeaderIsAbsent_Return401() {
        request.setHeaders(HttpHeaders.of(Collections.emptyMap(), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(401, response.statusCode());
    }

    @Test
    public void process_AuthHeaderIsNotBase64_Return401() {
        request.setHeaders(HttpHeaders.of(Map.of("Authorization", List.of("hubert:mazur")), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(401, response.statusCode());
    }

    @Test
    public void process_AuthHeaderIsNotOfValidFormat_Return401() {
        request.setHeaders(HttpHeaders.of(Map.of(
            "Authorization", List.of(Base64.getEncoder().encodeToString("invalid_format".getBytes()))
        ), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(401, response.statusCode());
    }

    @Test
    public void process_MethodIsNotAllowed_Return405() {
        request.setHeaders(VALID_HEADERS);
        var allowedMethods = List.of(HttpMethod.PUT);
        var requestMethod = HttpMethod.GET;
        request.setMethod(requestMethod);
        assertThat(requestMethod).isNotIn(allowedMethods);
        var processor = new HttpRequestProcessor(allowedMethods, decoder, blp);
        var response = processor.process(request);
        assertEquals(405, response.statusCode());
    }

    @Test
    public void process_MethodIsAllowed_Return200() {
        request.setHeaders(VALID_HEADERS);
        var allowedMethods = List.of(HttpMethod.GET);
        var requestMethod = HttpMethod.GET;
        request.setMethod(requestMethod);
        assertThat(requestMethod).isIn(allowedMethods);
        var processor = new HttpRequestProcessor(allowedMethods, decoder, blp);
        var response = processor.process(request);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void process_ContentTypeHeaderIsAbsent_Return415() {
        request.setHeaders(HttpHeaders.of(Map.of(
            "Authorization", List.of(Base64.getEncoder().encodeToString("user:pass".getBytes()))
        ), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(415, response.statusCode());
    }

    @Test
    public void process_ContentTypeIsNotApplicationJson_Return415() {
        request.setHeaders(HttpHeaders.of(Map.of(
            "Authorization", List.of(Base64.getEncoder().encodeToString("user:pass".getBytes())),
            "Content-Type", List.of("text/plain")
        ), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(415, response.statusCode());
    }

    @Test
    public void process_ContentTypeIsApplicationJson_Return200() {
        request.setHeaders(HttpHeaders.of(Map.of(
            "Authorization", List.of(Base64.getEncoder().encodeToString("user:pass".getBytes())),
            "Content-Type", List.of("application/json")
        ), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void process_RequestBodyIsEncoded_DecodeAndReturn200() {
        String body = "body";
        request.setBody(body);
        request.setHeaders(HttpHeaders.of(Map.of(
            "Authorization", List.of(Base64.getEncoder().encodeToString("user:pass".getBytes())),
            "Content-Type", List.of("application/json"),
            "Content-Encoding", List.of("encoding")
        ), PREDICATE));
        var processor = new HttpRequestProcessor(List.of(HttpMethod.GET), decoder, blp);
        var response = processor.process(request);
        assertEquals(body + StubBodyDecoder.DECODE_SUFFIX, response.body());
        assertEquals(200, response.statusCode());
    }

}
