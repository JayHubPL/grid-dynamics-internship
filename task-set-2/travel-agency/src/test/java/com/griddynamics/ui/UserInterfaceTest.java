package com.griddynamics.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UserInterfaceTest {

    @Test
    public void showMsg_ShowsMessageCorrectly() {
        String input = "This is a message";
        StringWriter output = new StringWriter();
        UserInterface UI = new UserInterface(null, new PrintWriter(output));

        UI.showMsg(input);

        assertEquals("This is a message\n", output.toString());
    }

    @Test
    public void requestOriginCityName_ReadCityNameCorreclyAndIgnoreTheRest() throws IOException {
        String input = "   Krakow   blablabla ";
        StringWriter output = new StringWriter();
        UserInterface UI = new UserInterface(new BufferedReader(new StringReader(input)), new PrintWriter(output));

        String actual = UI.requestOriginCityName();

        assertEquals("Please enter your origin city name: ", output.toString());
        assertEquals("Krakow", actual);
    }

    @Test
    public void requestElementFromList_OneAttemptWithinRange_ReturnsCorrectly() throws IOException {
        String input = "1";
        StringWriter output = new StringWriter();
        UserInterface UI = new UserInterface(new BufferedReader(new StringReader(input)), new PrintWriter(output));

        List<Integer> list = Arrays.asList(1, 2, 3);

        String prompt = "prompt";
        Integer actualReturn = UI.requestElementFromList(list, prompt);

        String expectedOutput = prompt + "\n   1. 1\n   2. 2\n   3. 3\nChoose index from range 1 to 3: ";

        assertEquals(list.get(0), actualReturn);
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void requestElementFromList_AttemptNotWithinRange_ReturnsCorrectlyAfterSecondTry() throws IOException {
        String input = "4\n1";
        StringWriter output = new StringWriter();
        UserInterface UI = new UserInterface(new BufferedReader(new StringReader(input)), new PrintWriter(output));

        List<Integer> list = Arrays.asList(1, 2, 3);

        String prompt = "prompt";
        Integer actualReturn = UI.requestElementFromList(list, prompt);

        String expectedOutput = prompt + "\n   1. 1\n   2. 2\n   3. 3\nChoose index from range 1 to 3: Index is not from specified range. Please try again.\nChoose index from range 1 to 3: ";

        assertEquals(list.get(0), actualReturn);
        assertEquals(expectedOutput, output.toString());
    }

    @ParameterizedTest
    @CsvSource({"1,true", "2,false"})
    public void askIfWantsToExit_MakesAChoice_ShowsMessageAndReturnsCorrectly(String input, Boolean shouldExit) throws IOException {
        StringWriter output = new StringWriter();
        UserInterface UI = new UserInterface(new BufferedReader(new StringReader(input)), new PrintWriter(output));

        boolean wantsToExit = UI.askIfWantsToExit();

        String expectedOutput = "Do you wish to continue?\n   1. No, exit the program.\n   2. Yes, check another flight.\nChoose index from range 1 to 2: ";

        assertEquals(expectedOutput, output.toString());
        assertEquals(shouldExit, wantsToExit);
    }

    @ParameterizedTest
    @CsvSource({"1,USD", "2,EUR", "3,PLN", "4,ZWD"})
    public void requestCurrency(String input, String currencyName) throws IOException {
        StringWriter output = new StringWriter();
        UserInterface UI = new UserInterface(new BufferedReader(new StringReader(input)), new PrintWriter(output));

        String actualCurrencyName = UI.requestCurrency().name();

        String expectedOutput = "Choose currency:\n   1. USD\n   2. EUR\n   3. PLN\n   4. ZWD\nChoose index from range 1 to 4: ";

        assertEquals(expectedOutput, output.toString());
        assertEquals(currencyName, actualCurrencyName);
    }

}