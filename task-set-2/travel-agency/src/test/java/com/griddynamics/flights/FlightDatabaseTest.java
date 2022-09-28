package com.griddynamics.flights;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class FlightDatabaseTest {
    
    public final Gson gsonInstance = new Gson();
    public final Path resourcesPath = Path.of("src", "test", "resources");

    public Airport WAW = new Airport("Warsaw", "WAW");
    public Airport WMI = new Airport("Warsaw", "WMI");
    public Airport WRO = new Airport("Wroclaw", "WRO");
    public Airport KRK = new Airport("Krakow", "KRK");
    public Airport MXP = new Airport("Milan", "MXP");

    @Test
    public void getTravelDestinationsFrom_Krakow_ReturnAllDestinationsCorrectly() throws IOException {
        FlightDatabase db = new FlightDatabase(resourcesPath.resolve("data_1.json"), gsonInstance);

        List<Airport> expected = List.of(WAW, WMI, WRO, MXP);

        List<Airport> actual = db.getTravelDestinationsFrom(KRK);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void getOriginCityAirports_Warsaw_ReturnAllAirports() throws IOException {
        FlightDatabase db = new FlightDatabase(resourcesPath.resolve("data_2.json"), gsonInstance);

        List<Airport> expected = List.of(WAW, WMI);

        List<Airport> actual = db.getOriginCityAirports("Warsaw");

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void getOriginCityAirports_UnknownCityName_ReturnEmptyList() throws IOException {
        FlightDatabase db = new FlightDatabase(resourcesPath.resolve("data_2.json"), gsonInstance);

        List<Airport> actual = db.getOriginCityAirports("unknownCityName");

        assertThat(actual).isEmpty();
    }

}
