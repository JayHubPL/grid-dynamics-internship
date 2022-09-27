package com.griddynamics.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import com.robustsoft.currencies.Currency;

public class UserInterface {

    private final BufferedReader input;
    private final PrintWriter output;

    public UserInterface(BufferedReader input, PrintWriter output) {
        this.input = input;
        this.output = output;
    }

    public UserInterface() {
        this(new BufferedReader(System.console().reader()), System.console().writer());
    }
    
    public void showMsg(String msg, boolean withoutNewLine) {
        output.printf(withoutNewLine ? "%s" : "%s\n", msg);
        output.flush();
    }

    public void showMsg(String msg) {
        showMsg(msg, false);
    }

    public String requestOriginCityName() throws IOException {
        return readInputWithPrompt("Please enter your origin city name: ");
    }

    public <T extends Comparable<T>> T requestElementFromList(List<T> list, String prompt) throws IOException {
        showMsg(prompt);
        list.sort(Comparator.naturalOrder());
        IntStream.range(0, list.size())
            .mapToObj(i -> String.format("%4d. %s", i + 1, list.get(i)))
            .forEach(i -> showMsg(i));
        String indexInfo = String.format("Choose index from range 1 to %d: ", list.size());
        Integer index = null;
        while (index == null || index < 1 || index > list.size()) {
            try {
                index = Integer.valueOf(readInputWithPrompt(indexInfo));
                if (index < 1 || index > list.size()) {
                    showMsg("Index is not from specified range. Please try again.");
                }
            } catch (NumberFormatException numberFormatException) {
                showMsg("Index is invalid. Please try again.");
            }
        }
        return list.get(index - 1);
    }

    public boolean askIfWantsToExit() throws IOException {
        String yes = "Yes, check another flight.";
        String no = "No, exit the program.";
        List<String> options = Arrays.asList(yes, no);
        return requestElementFromList(options, "Do you wish to continue?").equals(no);
    }

    public Currency requestCurrency() throws IOException {
        LinkedList<Currency> currencies = new LinkedList<>(Arrays.asList(Currency.values()));
        return requestElementFromList(currencies, "Choose currency:");
    }

    private String readInputWithPrompt(String prompt) throws IOException {
        showMsg(prompt, true);
        return input.readLine().trim().split("\\s+")[0];
    }

}
