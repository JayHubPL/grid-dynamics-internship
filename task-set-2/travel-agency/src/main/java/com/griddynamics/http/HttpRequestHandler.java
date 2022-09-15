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

        while (response == null || response.statusCode() != ResponseStatus.OK.getStatusCode() || price == null) {
            try {
                response = sendRequest(request);
                switch (ResponseStatus.mapStatusCodeToEnum(response.statusCode())) {
                    case OK -> {
                        logger.info("Response recieved successfully");
                        price = new Gson().fromJson(response.body(), ResponseBody.class).price();
                        if (price == null) {
                            logger.warn("Price is null, retrying sending request...");
                        } else {
                            logger.info(String.format("Recieved valid price: %s", price));
                        }
                    } 
                    case UNAUTHORIZED -> {
                        throw logger.throwing(Level.FATAL, new InvalidTokenException(token));
                    }
                    case BAD_REQUEST -> {
                        logger.warn(String.format("Bad request status code recieved: %d, retrying sending request...", response.statusCode())); // TODO
                    }
                    case SERVICE_DOWN -> {
                        logger.warn("Service is down, retrying sending request...");
                    }
                    case UNKNOWN -> {
                        logger.warn(String.format("Unknown request status code: %d, retrying sending request...", response.statusCode()));
                    }
                }
            } catch (InterruptedException interruptedException) {
                logger.warn("Sending the request was interrupted, retrying sending request...");
            } catch (HttpTimeoutException httpTimeoutException) {
                logger.warn("Request timed out, retrying sending request...");
            } catch (JsonSyntaxException jsonSyntaxException) {
                logger.warn("Response is not a valid json, retrying sending request...");
            } catch (IOException ioException) {
                throw logger.throwing(Level.FATAL, ioException);
            }
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
