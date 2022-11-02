package com.griddynamics.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileParser {

    private static final Logger logger;

    static {
        logger = LogManager.getLogger(FileParser.class);
    }

    public static String parseTokenFile(Path path) throws FileNotFoundException, IOException {
        String token = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            token = reader.readLine().trim();
        } catch (FileNotFoundException fileNotFoundException) {
            logger.fatal("No token file found");
            throw logger.throwing(Level.FATAL, fileNotFoundException);
        } catch (IOException ioException) {
            throw logger.throwing(Level.FATAL, ioException);
        }
        return token;
    }

}
