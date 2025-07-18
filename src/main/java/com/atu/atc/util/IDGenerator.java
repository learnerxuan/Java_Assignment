/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.atu.atc.util.FileUtils;
import java.util.ArrayList;

public class IDGenerator {
    // File path where the current id number is stored
    private static final String ID_COUNTER_FILE_PATH = "src/main/resources/data/id_counters.txt";
    
    // A static map to store the last number for each ID prefix
    private static Map<String, Integer> lastIds = new HashMap();
    
    // Static initializer block, runs only once when the IDGenerator class is called.
    static {
        loadLastIds();
    }
    
    public static synchronized String generateUniqueId(String prefix){
        // Get the current latest ID
        int currentCount = lastIds.getOrDefault(prefix, 0);
        currentCount++;
        
        // Get the current count for this prefix. If no count exists, start from 0.
        lastIds.put(prefix, currentCount);
        
        //Store the latest count into the map
        saveLastIds();
        
        // Format the ID with leading zeros (e.g., 1 becomes 001, 12 becomes 012).
        // %03d means format as an integer, padded with leading zeros to a width of 3.
        return String.format("%s%03d", prefix, currentCount);
    }
    
    private static void loadLastIds(){
        // Read content of the id_counters.txt file
        List<String> lines = FileUtils.readLines(ID_COUNTER_FILE_PATH);
        
        for (String line: lines) {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                try {
                    String prefix = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    lastIds.put(prefix, count); // Example: {"U": 3}
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in ID counter file: " + line + " - " + e.getMessage());
                }
            } else {
                System.err.println("IDGenerator: Malformed line in ID counter file: " + line);
            }
        }      
        // For debugging purpose
        System.out.println("Loaded last IDs: "+ lastIds);
    }
    
    private static void saveLastIds(){
        List<String> linesToSave = new ArrayList<>();
        
        for (Map.Entry<String, Integer> entry : lastIds.entrySet()) {
            linesToSave.add(entry.getKey() + "=" + entry.getValue());
        }
        // Use FileUtils to write the lines to the file.
        boolean success = FileUtils.writeLines(ID_COUNTER_FILE_PATH, linesToSave);
        if (!success) {
            System.err.println("IDGenerator: Failed to save ID counters to file.");
        } else {
            System.out.println("IDGenerator: Saved last IDs: " + lastIds); // For debugging
        } 
    }
    
    // Specific ID generation methods (calling generateUniqueId internally)
    public String generateStudentId() {
        return generateUniqueId("S");
    }
    
    public String generateRequestId() {
        return generateUniqueId("REQ");
    }
    
    public String generateTutorId() {
        return generateUniqueId("T");
    }
    
    public String generateReceptionistId() {
        return generateUniqueId("R");
    }
    
    public String generateAdminId() {
        return generateUniqueId("A");
    }
    
    public String generateClassId() {
        return generateUniqueId("C"); // Assuming 'C' for Class
    }
    
    public String generateEnrollmentId() {
        return generateUniqueId("E"); // Assuming 'E' for Enrollment
    }
    
    public String generatePaymentId() {
        return generateUniqueId("P"); // Assuming 'P' for Payment
    }
    
    // [ADDITION] Methods to set starting counters for specific ID types (for DataLoader)
    public void setLastStudentId(String lastId) {
        setLastId("S", lastId);
    }
    
    public void setLastRequestId(String lastId) {
        setLastId("REQ", lastId);
    }
    
    public void setLastTutorId(String lastId) {
        setLastId("T", lastId);
    }
    
    public void setLastReceptionistId(String lastId) {
        setLastId("R", lastId);
    }
    
    public void setLastAdminId(String lastId) {
        setLastId("A", lastId);
    }
    
    public void setLastClassId(String lastId) {
        setLastId("C", lastId);
    }
    
    public void setLastEnrollmentId(String lastId) {
        setLastId("E", lastId);
    }
    
    public void setLastPaymentId(String lastId) {
        setLastId("P", lastId);
    }
    
    // Generic helper to set the last ID from a loaded string
    private void setLastId(String prefix, String lastId) {
        if (lastId != null && lastId.startsWith(prefix) && lastId.length() > prefix.length()) {
            try {
                int idNum = Integer.parseInt(lastId.substring(prefix.length()));
                // Only update if the loaded ID number is greater than current
                if (idNum > lastIds.getOrDefault(prefix, 0)) {
                    lastIds.put(prefix, idNum);
                    System.out.println("IDGenerator: Set last ID for " + prefix + " to " + idNum);
                }
            } catch (NumberFormatException e) {
                System.err.println("IDGenerator: Invalid last ID format for " + prefix + ": " + lastId);
            }
        }
    }
    
//        public static void main(String[] args) {
//          System.out.println("--- Testing IDGenerator ---");

        // First run: Should start from 1 for each new prefix
//        System.out.println("Generating IDs for the first time (or continuing from last saved):");
//        System.out.println("Class ID: " + IDGenerator.generateUniqueId("CLS"));
//        System.out.println("Course ID: " + IDGenerator.generateUniqueId("CE"));
//        System.out.println("Student ID: " + IDGenerator.generateUniqueId("S"));
//
//
//        System.out.println("\n--- Testing complete. Close the program and run again to see persistence. ---");
//        System.out.println("Check 'data/id_counters.txt' in your project folder.");
        // Simulate a second run (you would close and re-run the main method)
        // If you run this file multiple times, you should see the counters increment.
        // For example, if the first run generated U001, U002, U003,
        // the second run (after restarting the app) should generate U004, U005, etc.
//    }
}
