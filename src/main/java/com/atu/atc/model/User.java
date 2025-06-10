// src/main/java/com/atu/atc/model/User.java
// This is an abstract class, serving as a blueprint for all types of users in the system.
// It demonstrates Abstraction by defining common properties and behaviors for all users.
// We cannot create an object directly from the User class.

package com.atu.atc.model; // This must match your package structure

public abstract class User {
    // Attributes common to all users
    private String username;
    private String password;
    private String role; // e.g., "Admin", "Receptionist", "Tutor", "Student"

    // Constructor to initialize a User object
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- Getters ---
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // --- Setters ---
    // Encapsulation: We provide methods to set attributes,
    // allowing us to add validation logic if needed in the future.
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // The role is typically set during creation and might not be directly changeable
    // via a public setter if user roles are fixed. But for profile updates,
    // we'll allow setting.
    public void setRole(String role) {
        this.role = role;
    }

    // --- Common User Behaviors (Methods) ---

    // Abstract method for login.
    // Each subclass (Admin, Receptionist, etc.) MUST provide its own implementation
    // of how a user logs in. This enforces a common behavior and demonstrates Abstraction.
    public abstract boolean login(String enteredUsername, String enteredPassword);

    // Common method for updating profile.
    // For simplicity, this example just prints a message. In a real system,
    // it would allow changing attributes like username, password, etc.
    // This demonstrates Encapsulation by allowing controlled modification of object state.
    public void updateProfile(String newUsername, String newPassword) {
        this.username = newUsername;
        this.password = newPassword;
        System.out.println(this.role + " profile updated for " + this.username);
    }

    // A simple method to display user details (common to all users)
    public void displayUserDetails() {
        System.out.println("Username: " + username);
        System.out.println("Role: " + role);
        // Note: We don't display password for security reasons
    }
}