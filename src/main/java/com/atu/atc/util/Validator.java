/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.util;

import java.util.regex.Pattern;

/**
 *
 * @author henge
 */
public class Validator {
    
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
        "^[0-9\\s\\-()]{7,20}$"
    );
    
    // Email validation using regex
    public static boolean isValidEmail(String email){
        if (email == null || email.isEmpty()) return false;
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, email);
    }
    
    // Phone number validation
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }
    
    // Checks if a string is a numeric value
    public static boolean isNumeric(String text){
        if(text == null || text.isEmpty()) return false;
        return text.matches("\\d+"); // Accepts only whole number digits
    }

    // Validate form using method overloading
    // Method 1: Accepts a String (e.g. "3")
    public static boolean isFormValid(String formText) {
        try {
            int level = Integer.parseInt(formText); // Convert String to int
            return isFormValid(level); // Java sees int, calls Method 2 below
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method 2: Accepts an int (e.g., 1, 2, 3, 4, 5)
    // This method is run when int is passed
    public static boolean isFormValid(int level) {
        return level >= 1 && level <= 5;
    }
}
