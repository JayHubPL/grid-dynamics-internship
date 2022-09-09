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

import com.google.gson.Gson;
import com.griddynamics.connections.Connection;

public class HttpRequestHandler {
    
    private final URI lambdaUri;
    private final String token;

    public HttpRequestHandler(Path tokenPath) {
        lambdaUri = URI.create("https://34wrh7kcoe3dwrggfw7ulm4tyq0htvgs.lambda-url.eu-west-2.on.aws/");
        token = readTokenFromFile(tokenPath);
    }

    public double makePriceRequest(Connection connection) {
        var request = HttpRequest.newBuilder(lambdaUri)
            .headers("x-authentication", token, "Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new RequestBody(connection), RequestBody.class)))
            .timeout(Duration.ofSeconds(10)) // TODO this throws later
            .build();
            
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Failure getting response, status code: " + response.statusCode());
                System.exit(1);
            }
            // TODO check status code
        } catch (InterruptedException interruptedException) {
            System.err.println(interruptedException.getMessage());
        } catch (HttpTimeoutException httpTimeoutException){
            
        } catch (IOException ioException) {
            System.err.println(ioException.getMessage());
        }

        System.out.println(response.body());

        return new Gson().fromJson(response.body(), ResponseBody.class).price();

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
