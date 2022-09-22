package com.griddynamics.flights;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class FlightDatabaseTest {
    
    public static Gson gsonInstance;
    public static Path resourcesPath;

    public static Airport WAW;
    public static Airport WMI;
    public static Airport WRO;
    public static Airport KRK;
    public static Airport MXP;

    @BeforeAll
    public static void init() {
        gsonInstance = new Gson();
        resourcesPath = Path.of("src", "test", "resources");
        WAW = new Airport("Warsaw", "WAW");
        WMI = new Airport("Warsaw", "WMI");
        WRO = new Airport("Wroclaw", "WRO");
        KRK = new Airport("Krakow", "KRK");
        MXP = new Airport("Milan", "MXP");
    }

    @Test
    public void getTravelDestinationsFrom_Krakow_ReturnAllDestinationsCorrectly() throws IOException {
        FlightDatabase db = new FlightDatabase(resourcesPath.resolve("data_1.json"), gsonInstance);

        List<Airport> expected = List.of(WAW, WMI, WRO, MXP);

        List<Airport> actual = db.getTravelDestinationsFrom(KRK);

        assertThat(expected).containsExactlyInAnyOrderElementsOf(actual);
    }

    // TODO MORE TESTS COMMING SOON

}
