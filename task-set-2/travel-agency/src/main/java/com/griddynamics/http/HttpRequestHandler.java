package com.griddynamics.http;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.griddynamics.flights.Flight;

public class HttpRequestHandler {
    
    private final URI lambdaUri;
    private final String token;
    private final Duration timeout;
    private final Logger logger;
    private final HttpClient httpClient;
    private final Gson gsonInstance;

    public HttpRequestHandler(HttpClient httpClient, String token, Gson gsonInstance, Duration timeout) {
        logger = LogManager.getLogger(HttpRequestHandler.class);
        lambdaUri = URI.create("https://34wrh7kcoe3dwrggfw7ulm4tyq0htvgs.lambda-url.eu-west-2.on.aws/");
        this.httpClient = httpClient;
        this.token = token;
        this.gsonInstance = gsonInstance;
        this.timeout = timeout;
    }

    public HttpRequestHandler(HttpClient httpClient, String token, Gson gsonInstance) {
        this(httpClient, token, gsonInstance, Duration.ofSeconds(10));
    }

    public BigDecimal makePriceRequest(Flight flight) throws InvalidTokenException, IOException {
        HttpRequest request = buildHttpRequest(flight);
        HttpResponse<String> response = null;
        BigDecimal price = null;
        int requestAttemptsLeft = 3;

        while (requestAttemptsLeft > 0 && !validPriceInfoReceieved(response, price)) {
            try {
                response = sendRequest(request);
                int statusCode = response.statusCode();
                switch (statusCode) {
                    case HTTP_OK -> {
                        logger.info("Response received successfully");
                        price = getPriceFormResponseBody(response.body());
                    } 
                    case HTTP_UNAUTHORIZED -> {
                        throw logger.throwing(Level.FATAL, new InvalidTokenException(token));
                    }
                    case HTTP_BAD_REQUEST -> {
                        logger.warn(String.format("Bad request status code received: %d", statusCode));
                    }
                    case HTTP_UNAVAILABLE -> {
                        logger.warn("Service is down");
                    }
                    default -> {
                        logger.warn(String.format("Unknown request status code: %d", statusCode));
                    }
                }
            } catch (InterruptedException interruptedException) {
                logger.warn("Sending the request was interrupted");
            } catch (HttpTimeoutException httpTimeoutException) {
                logger.warn("Request timed out");
            } catch (JsonSyntaxException jsonSyntaxException) {
                logger.warn("Response is not a valid json");
            } catch (IOException ioException) {
                throw logger.throwing(Level.FATAL, ioException);
            }
            if (price == null && requestAttemptsLeft > 0) {
                requestAttemptsLeft--;
                logger.info(String.format("Retrying sending the request. Attempts left: %d", requestAttemptsLeft));
            }
        }
        if (price == null) {
            throw logger.throwing(Level.FATAL, new NoValidResponseException(flight));
        }
        return price;
    }

    private boolean validPriceInfoReceieved(HttpResponse<?> response, BigDecimal price) {
        return response != null && response.statusCode() == HTTP_OK && price != null && !price.equals(BigDecimal.ZERO);
    }

    private BigDecimal getPriceFormResponseBody(String body) throws JsonSyntaxException {
        BigDecimal price = gsonInstance.fromJson(body, ResponseBody.class).price();
        if (price == null || price.equals(BigDecimal.ZERO) ) {
            logger.info(String.format("Received invalid price: %s", price));
        } else {
            logger.info(String.format("Received valid price: %s", price));
        }
        return price;
    }

    private HttpRequest buildHttpRequest(Flight flight) {
        return HttpRequest.newBuilder(lambdaUri)
            .headers("x-authentication", token, "Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(gsonInstance.toJson(new RequestBody(flight))))
            .timeout(timeout)
            .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request)
    throws IOException, HttpTimeoutException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
