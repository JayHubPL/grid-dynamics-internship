package com.griddynamics;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NumberFormats {

    /**
     * This program takes N numbers written in different formats
     * and sorts them without changing the inital format in which they
     * were given. First number N must be an integer and after it
     * there should be N numbers to be sorted and displayed.
     * Standard input stream is used to provide numbers.
     */
    public static void main(String[] args) {
        
        int numberOfInputNumbers = 0;
        List<InputNumber> numbers = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            numberOfInputNumbers = scanner.nextInt();
            for (int i = 0; i < numberOfInputNumbers; i++) {
                String nextNumber = scanner.next();
                numbers.add(new InputNumber(nextNumber));
            }
        } catch (InputMismatchException inputMismatchException) {
            System.err.println("Invalid number format!");
            System.exit(1);
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Not enough numbers in an input stream!");
            System.exit(1);
        }

        // stable sort
        numbers = numbers.stream()
            .sorted()
            .collect(Collectors.toList());

        // display
        System.out.println("Output:");
        String output = numbers.stream()
            .map(InputNumber::getStringRepresentation)
            .collect(Collectors.joining(", "));
        System.out.println(output);
    }
}
