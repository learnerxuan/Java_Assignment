// src/main/java/com/atu/atc/model/Tutor.java
package com.atu.atc.model;

public class Tutor extends User {
    public Tutor(String id, String password, String fullName, String phoneNumber, String email, String gender) {
        super(id, password, "Student", fullName, phoneNumber, email, gender);;
    }

    @Override
    public boolean login(String enteredId, String enteredPassword) {
        // Basic login for now
        return getId().equals(enteredId) && getPassword().equals(enteredPassword);
    }

    // Placeholder for Tutor-specific methods
    public void addClassInformation() { /* ... */ }
    public void viewStudentsEnrolledInMySubjects() { /* ... */ }
    // Add all methods from your assignment description for Tutor
}