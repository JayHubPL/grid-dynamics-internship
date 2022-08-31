package com.griddynamics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LettersStatistics {

    /**
     * 
     *  This program takes as an imput a path to the text file
     *  and outputs the frequency of the letters in it.
     *  Ony 10 most frequent letters are shown.
     *  Path to the input file can be given as an argument.
     *  Default path to the input file is {@code input.txt}.
     * 
     */
    public static void main(String[] args) {

        // parse arguments, get path to input file
        Path pathToInputFile = Path.of("input.txt");
        if (args.length > 1) {
            try {
                pathToInputFile = Path.of(args[1]);
            } catch (InvalidPathException invalidPathException) {
                System.err.println("Invalid path format: " + args[1]);
            }
        }

        // read input file contents
        String fileContents = readFileContents(pathToInputFile);

        // get char frequencies
        Map<Character, Long> frequency = frequencyOfCharsFromString(fileContents);

        // sort in non ascending order by frequency, take top 10 items and display them
        displayCharFrequency(frequency, 10);
    }

    /**
     * 
     *  Reads a file given by {@code path} and returns
     *  its contents as a singular string.
     * 
     */
    private static String readFileContents(Path path) {
        String fileContents = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            fileContents = reader.lines().collect(Collectors.joining());
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Failed to open the text file: " + path);
            System.exit(1);
        } catch (IOException ioException) {
            System.err.println("Error while closing the reader!");
            System.exit(1);
        }
        return fileContents;
    }

    /**
     * 
     *  Takes a string and produces a multiset
     *  by counting character occurences in the string.
     *  It is case insensitive and counts only letters ignoring
     *  white spaces, special characters etc.
     * 
     */
    private static Map<Character, Long> frequencyOfCharsFromString(String str) {
        return str.chars()
            .mapToObj(c -> (char) c)
            .filter(Character::isLetter)
            .map(Character::toLowerCase)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    /**
     * 
     *  Takes a frequency of chars and displays some of
     *  the most frequent ones.
     *  @param maximumItemsToDisplay tells what is the maximum amount
     *  of elements that should be displayed.
     * 
     */
    private static void displayCharFrequency(Map<Character, Long> frequency, int maximumItemsToDisplay) {
        frequency.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(maximumItemsToDisplay)
            .forEach(entry -> 
                System.out.println(entry.getKey() + ": " + entry.getValue())
            );
    }
}
