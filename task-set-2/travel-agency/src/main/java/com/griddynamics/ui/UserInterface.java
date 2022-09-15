package com.griddynamics.ui;

import java.io.Console;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import com.robustsoft.currencies.Currency;

public class UserInterface {

    private final Console console;

    public UserInterface() {
        console = System.console();
    }
    
    public void showMsg(String msg) {
        console.printf("%s\n", msg);
    }

    public String requestOriginCityName() {
        String prompt = "Please enter your origin city name: ";
        String cityName = console.readLine(prompt).trim();
        console.flush();
        return cityName;
    }

    public <T extends Comparable<T>> T requestElementFromList(List<T> list, String prompt) {
        showMsg(prompt);
        list.sort(Comparator.naturalOrder());
        IntStream.range(0, list.size())
            .mapToObj(i -> String.format("%4d. %s", i + 1, list.get(i)))
            .forEach(System.out::println);
        String indexInfo = String.format("Choose index from range 1 to %d: ", list.size());
        Integer index = null;
        while (index == null || index < 1 || index > list.size()) {
            try {
                index = Integer.valueOf(console.readLine(indexInfo).trim());
                if (index < 1 || index > list.size()) {
                    showMsg("Index is not from specified range. Please try again.");
                }
            } catch (NumberFormatException numberFormatException) {
                showMsg("Index is invalid. Please try again.");
            }
            console.flush();
        }
        return list.get(index - 1);
    }

    public boolean askIfWantsToExit() {
        String yes = "Yes, check another connection.";
        String no = "No, exit the program.";
        List<String> options = Arrays.asList(yes, no);
        return requestElementFromList(options, "Do you wish to continue?").equals(no);
    }

    public Currency requestCurrency() {
        LinkedList<Currency> currencies = new LinkedList<>(Arrays.asList(Currency.values()));
        return requestElementFromList(currencies, "Choose currency:");
    }

}
