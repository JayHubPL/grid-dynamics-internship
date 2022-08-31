package com.griddynamics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MessagesSplit {

    /**
     * This program takes as an input a path to the text file
     * containing one line message and and a maximum length of the line
     * in order to split the message to multiple lines which are
     * no longer than specified length.
     * Path to the input file can be given as an argument.
     * Default path to the input file is {@code input.txt}.
     * @throws WordTooLongException when one of the words in the message
     * cannot be splitted due to being longer than maximum line length.
     */
    public static void main(String[] args) throws WordTooLongException {
        
        // parse arguments, get path to input file
        Path pathToInputFile = Path.of("input.txt");
        if (args.length > 1) {
            try {
                pathToInputFile = Path.of(args[1]);
            } catch (InvalidPathException invalidPathException) {
                System.err.println("Invalid path format: " + args[1]);
            }
        }

        // parse input data...
        InputData input = parseInputFile(pathToInputFile);

        // generate message lines from words
        List<String> splittedMessage = splitMessageToMaxLenLines(input.words(), input.maxLineLength());

        // display splitted message with line breaks
        splittedMessage.stream().forEach(System.out::println);
    }

    /**
     * Takes a path to an input file and extracts all words
     * as separate strings and a maximum line length.
     * Data is wrapped in {@code InputData} object.
     * @param path path to the input file
     * @return {@code InputData} contains parsed info.
     */
    private static InputData parseInputFile(Path path) {
        List<String> words = Collections.emptyList();
        int lineMaxLength = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            words = Arrays.asList(reader.readLine().split("\\s+"));
            lineMaxLength = Integer.parseInt(reader.readLine());
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Failed to open the text file: " + path);
            System.exit(1);
        } catch (IOException ioException) {
            System.err.println(ioException.getLocalizedMessage());
            System.exit(1);
        }
        return new InputData(words, lineMaxLength);
    }

    /**
     * Generates message line by line out of {@code words} such that
     * no line is longer than {@code maxLineLength}.
     * @param words list of words to be assembled into a message.
     * @param maxLineLength maximum line length.
     * @return {@code List<String>} message lines no longer than {@code maxLineLength}.
     * @throws WordTooLongException when any word is longer than {@code maxLineLength}.
     */
    private static List<String> splitMessageToMaxLenLines(List<String> words, int maxLineLength)
    throws WordTooLongException {
        for (String word : words) {
            if (word.length() > maxLineLength) {
                throw new WordTooLongException(word, maxLineLength);
            }
        }
        List<String> splittedMessage = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            if (word.length() + currentLine.length() > maxLineLength) {
                splittedMessage.add(currentLine.toString().trim());
                currentLine.setLength(0); // clear line
            }
            currentLine.append(word + " ");
        }
        if (currentLine.length() != 0) {
            splittedMessage.add(currentLine.toString().trim());
        }
        return splittedMessage;
    }
}
