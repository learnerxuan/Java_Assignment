// src/main/java/com/atu/atc/model/Receptionist.java
package com.atu.atc.model;

public class Receptionist extends User {
    public Receptionist(String username, String password) {
        super(username, password, "Receptionist");
    }

    @Override
    public boolean login(String enteredUsername, String enteredPassword) {
        // Basic login for now
        return getUsername().equals(enteredUsername) && getPassword().equals(enteredPassword);
    }

    // Placeholder for Receptionist-specific methods
    public void registerStudent() { /* ... */ }
    public void acceptPayment() { /* ... */ }
    // Add all methods from your assignment description for Receptionist
}