// src/main/java/com/atu/atc/util/FileUtils.java

package com.atu.atc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// This utility class provides helper methods for reading from and writing to text files.
// It handles the basic file I/O operations, centralizing them for easier management.
public class FileUtils {

    /**
     * Reads all lines from a specified text file.
     * If the file does not exist, an empty list is returned.
     *
     * @param filePath The full path to the text file (e.g., "data/users.txt").
     * @return A List of String, where each String is a line from the file.
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        // Using try-with-resources ensures that the FileReader and BufferedReader are
        // automatically closed, even if errors occur. This is good practice.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            // If the file doesn't exist or there's another IO error,
            // we print the stack trace and return an empty list.
            // In a real application, you might want more sophisticated error handling.
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            // For a new file, this just means it doesn't exist yet, which is fine.
        }
        return lines;
    }

    /**
     * Writes a list of strings to a specified text file, overwriting existing content.
     *
     * @param filePath The full path to the text file.
     * @param lines    A List of String, where each String will be written as a new line.
     * @return true if the write operation was successful, false otherwise.
     */
    public static boolean writeLines(String filePath, List<String> lines) {
        // Using try-with-resources for FileWriter and BufferedWriter.
        // 'false' in FileWriter means overwrite; 'true' would mean append.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Writes a new line character after each line
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
}