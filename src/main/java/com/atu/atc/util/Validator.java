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
    
    // Email validation using regex
    public static boolean isValidEmail(String email){
        if (email == null || email.isEmpty()) return false;
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, email);
    }
    
    // Checks if a string is a numeric value
    public static boolean isNumeric(String text){
        if(text == null || text.isEmpty()) return false;
        return text.matches("\\d+"); // Accepts only whole number digits
    }
    
    // Check is the form is valid
    public static boolean isFormValid(String form){
        if (form == null) return false;
        return form.equalsIgnoreCase("Form 1") ||
               form.equalsIgnoreCase("Form 2") ||
               form.equalsIgnoreCase("Form 3") ||
               form.equalsIgnoreCase("Form 4") ||
               form.equalsIgnoreCase("Form 5");
    }
}
