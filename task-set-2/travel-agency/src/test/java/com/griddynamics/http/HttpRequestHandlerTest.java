package com.griddynamics.http;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;
import com.griddynamics.flights.Airport;
import com.griddynamics.flights.Flight;

@ExtendWith(MockitoExtension.class)
public class HttpRequestHandlerTest {

    public static Gson gsonInstance;
    public static Flight dummyFlight;
    public HttpRequestHandler httpRequestHandler;

    @BeforeAll
    public static void init() {
        gsonInstance = new Gson();
        dummyFlight = new Flight(new Airport("A", "AAA"), new Airport("B", "BBB"));
    }

    @Mock
    public HttpResponse<String> mockedResponse;

    @Mock
    public HttpClient mockedHttpClient;

    @BeforeEach
    public void initBeforeEach() {
        httpRequestHandler = new HttpRequestHandler(mockedHttpClient, "token", gsonInstance);
    }

    @Test
    public void makePriceRequest_ResponseCodeIsUnauthorized_ThrowException() throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        doReturn(HTTP_UNAUTHORIZED).when(mockedResponse).statusCode();

        assertThrows(InvalidTokenException.class, () -> {
            httpRequestHandler.makePriceRequest(dummyFlight);
        });
    }

    @Test
    public void makePriceRequest_ResponseCodeIsBadRequest_ThrowException() throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        doReturn(HTTP_BAD_REQUEST).when(mockedResponse).statusCode();

        assertThrows(NoValidResponseException.class, () -> {
            httpRequestHandler.makePriceRequest(dummyFlight);
        });
    }

    @Test
    public void makePriceRequest_ResponseCodeIsUnavailable_ThrowException() throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        doReturn(HTTP_UNAVAILABLE).when(mockedResponse).statusCode();

        assertThrows(NoValidResponseException.class, () -> {
            httpRequestHandler.makePriceRequest(dummyFlight);
        });
    }

    @Test
    public void makePriceRequest_ResponseCodeIsOK_ReturnPriceCorrectly() throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        doReturn(HTTP_OK).when(mockedResponse).statusCode();
        doReturn("{\"price\": 69.420}").when(mockedResponse).body();

        assertEquals(new BigDecimal("69.420"), httpRequestHandler.makePriceRequest(dummyFlight));
    }
    
    @ParameterizedTest
    @CsvSource({"400", "503"})
    public void makePriceRequest_ResponseCodeIsNotOK_RetryThreeTimes_ReturnPriceCorrectly(int statusCode) throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        when(mockedResponse.statusCode()).thenReturn(statusCode).thenReturn(statusCode).thenReturn(HTTP_OK);
        doReturn("{\"price\": 69.420}").when(mockedResponse).body();

        assertEquals(new BigDecimal("69.420"), httpRequestHandler.makePriceRequest(dummyFlight));
    }

    @Test
    public void makePriceRequest_PriceIsInvalid_RetryThreeTimes_ReturnPriceCorrectly() throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        doReturn(HTTP_OK).when(mockedResponse).statusCode();
        when(mockedResponse.body()).thenReturn("{\"price\": 0}").thenReturn("{\"price\": null}").thenReturn("{\"price\": 69.420}");

        assertEquals(new BigDecimal("69.420"), httpRequestHandler.makePriceRequest(dummyFlight));
    }

    @Test
    public void makePriceRequest_PriceIsInvalid_RetryThreeTimesAndFail_ThrowException() throws Exception {
        doReturn(mockedResponse).when(mockedHttpClient).send(any(), any());
        doReturn(HTTP_OK).when(mockedResponse).statusCode();
        when(mockedResponse.body()).thenReturn("{\"price\": 0}").thenReturn("{\"price\": null}").thenReturn("{\"price\": null}");

        assertThrows(NoValidResponseException.class, () -> {
            httpRequestHandler.makePriceRequest(dummyFlight);
        });
    }

}
