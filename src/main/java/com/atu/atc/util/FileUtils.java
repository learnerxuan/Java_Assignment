// src/main/java/com/atu/atc/util/FileUtils.java

package com.atu.atc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// This utility class provides helper methods for reading from and writing to text files.
// It handles the basic file I/O operations, centralizing them for easier management.
public class FileUtils {
    
    // Read lines from a specific file
    public static List<String> readLines(String file_path) {
        // Define a list 
        List<String> lines = new ArrayList<>();
            
        // Read lines from the file. try-catch block is used to handle exceptions when reading the file.
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))){
            // Define a variable called line for temporary use.
            String line;
            
            // If the line is not null, line will be added to the list variable.
            while ((line = reader.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e) {
            // Catch exception and print the error message out.
            System.err.println("Error reading file: " + file_path + " - " + e.getMessage());
        }
        
        return lines;
    }
    
    // Write lines for a specific file, return True if success, False if failure.
    public static boolean writeLines(String filePath, List<String> lines) {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Add a new line after each line
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }
    
//    public static void main(String[] args) {
//        // Replace with an actual file path on your system
//        String testFilePath = "src/main/resources/data/classes.txt"; 
//        List<String> linesToWrite = Arrays.asList(
//            "This is the first line written by writeLines.",
//            "This is the second line.",
//            "And here is the third line, overriding previous content."
//        );
//
//        // Calling the static method without creating an object
//        List<String> lines = readLines(testFilePath);
//
//        // Printing the results
//        System.out.println("File contents:");
//        for (String line : lines) {
//            System.out.println(line);
//        }
//        
//        boolean writeSuccess = writeLines(testFilePath, linesToWrite);
//    }

    public static List<String> readAllLines(String FILE_PATH) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void writeAllLines(String FILE_PATH, List<String> lines) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}