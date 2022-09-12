package com.griddynamics.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import com.griddynamics.connections.Connection;
import com.griddynamics.ui.UserInterface;

public class HttpRequestHandler {
    
    private final URI lambdaUri;
    private final String token;
    private final Duration timeout;
    private Logger logger;

    public HttpRequestHandler(Path tokenPath) {
        lambdaUri = URI.create("https://34wrh7kcoe3dwrggfw7ulm4tyq0htvgs.lambda-url.eu-west-2.on.aws/");
        token = readTokenFromFile(tokenPath);
        timeout = Duration.ofSeconds(10);
        logger = LogManager.getLogger(HttpRequestHandler.class); // TODO this can be null
    }

    public double makePriceRequest(Connection connection) {
        HttpRequest request = buildHttpRequest(connection);
        HttpResponse<String> response = null;

        while (response == null || response.statusCode() != ResponseStatus.OK.getStatusCode()) {
            try {
                response = sendRequest(request);
                switch (ResponseStatus.mapStatusCodeToEnum(response.statusCode())) {
                    case OK -> {
                        logger.info("Response recieved successfully");
                    } 
                    case UNAUTHORIZED -> {
                        logger.fatal("Authorization not acquired, invalid token");
                        UserInterface.showMsg("API token is invalid and/or does not give neccesary authorization level. Please check the token and restart the application.");
                        System.exit(-1);
                    }
                    case BAD_REQUEST -> { // this behaviour may change
                        logger.fatal("Bad request status code recieved");
                        UserInterface.showMsg("Application has encountered irrecoverable error and will now exit.");
                        System.exit(-1);
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
            } catch (HttpTimeoutException httpTimeoutException){
                logger.warn("Request timed out, retrying sending request...");
            } catch (IOException ioException) {
                logger.catching(Level.FATAL, ioException);
                UserInterface.showMsg("Application has encountered irrecoverable error and will now exit.");
                System.exit(-1);
            }
        }

        return new Gson().fromJson(response.body(), ResponseBody.class).price();
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

    private String readTokenFromFile(Path path) {
        String token = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            token = reader.readLine().trim();
        } catch (FileNotFoundException fileNotFoundException){
            System.err.println(fileNotFoundException.getMessage());
        } catch (IOException ioException) { // TODO token.txt might not exist or data is invalid

        }
        return token;
    }

}
