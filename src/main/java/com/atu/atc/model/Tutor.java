// src/main/java/com/atu/atc/model/Tutor.java
package com.atu.atc.model;

public class Tutor extends User {
    public Tutor(String username, String password) {
        super(id, password, "Student", fullName, phone, email, gender);;
    }

    @Override
    public boolean login(String enteredUsername, String enteredPassword) {
        // Basic login for now
        return getUsername().equals(enteredUsername) && getPassword().equals(enteredPassword);
    }

    // Placeholder for Tutor-specific methods
    public void addClassInformation() { /* ... */ }
    public void viewStudentsEnrolledInMySubjects() { /* ... */ }
    // Add all methods from your assignment description for Tutor
}