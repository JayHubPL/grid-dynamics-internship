package com.griddynamics.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.file.Path;
import java.time.Duration;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.griddynamics.connections.Connection;

public class HttpRequestHandler {
    
    private final URI lambdaUri;
    private final String token;
    private final Duration timeout;
    private final Logger logger;

    public HttpRequestHandler(Path tokenPath) throws IOException, FileNotFoundException {
        logger = LogManager.getLogger(HttpRequestHandler.class);
        lambdaUri = URI.create("https://34wrh7kcoe3dwrggfw7ulm4tyq0htvgs.lambda-url.eu-west-2.on.aws/");
        token = readTokenFromFile(tokenPath);
        timeout = Duration.ofSeconds(10);
    }

    public BigDecimal makePriceRequest(Connection connection) throws InvalidTokenException, IOException {
        HttpRequest request = buildHttpRequest(connection);
        HttpResponse<String> response = null;
        BigDecimal price = null;
        int requestAttemptsLeft = 3;

        while (requestAttemptsLeft > 0 && (response == null || response.statusCode() != HTTP_OK || price == null)) {
            try {
                response = sendRequest(request);
                int statusCode = response.statusCode();
                switch (statusCode) {
                    case HTTP_OK -> {
                        logger.info("Response received successfully");
                        price = new Gson().fromJson(response.body(), ResponseBody.class).price();
                        if (price == null) {
                            logger.warn("Price is null");
                        } else {
                            logger.info(String.format("Received valid price: %s", price));
                        }
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
            throw logger.throwing(Level.FATAL, new NoValidResponseException(connection));
        }
        return price;
    }

    private HttpRequest buildHttpRequest(Connection connection) {
        return HttpRequest.newBuilder(lambdaUri)
            .headers("x-authentication", token, "Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new RequestBody(connection))))
            .timeout(timeout)
            .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request)
    throws IOException, HttpTimeoutException, InterruptedException {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String readTokenFromFile(Path path) throws IOException, FileNotFoundException {
        String token = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            token = reader.readLine().trim();
        } catch (FileNotFoundException fileNotFoundException) {
            logger.fatal("No token file found");
            throw logger.throwing(Level.FATAL, fileNotFoundException);
        } catch (IOException ioException) {
            throw logger.throwing(Level.FATAL, ioException);
        }
        return token;
    }

}
