// src/main/java/com/atu/atc/model/User.java
// This is an abstract class, serving as a blueprint for all types of users in the system.
// It demonstrates Abstraction by defining common properties and behaviors for all users.
// We cannot create an object directly from the User class.

package com.atu.atc.model;

public abstract class User {
    // Attributes common to all users
    String id;
    private String password;
    private String role; // e.g., "Admin", "Receptionist", "Tutor", "Student"
    private String fullName;
    private String phoneNumber;
    private String email;
    private String gender;


    // Constructor to initialize a User object
    public User(String id, String password, String role, String fullName, String phoneNumber, String email, String gender) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    // Setters
    // Encapsulation: We provide methods to set attributes,
    // allowing us to add validation logic if needed in the future.
    public void setId(String id) {
        this.id = id;
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // --- Common User Behaviors (Methods) ---

    // Abstract method for login.
    // Each subclass (Admin, Receptionist, etc.) MUST provide its own implementation
    // of how a user logs in. This enforces a common behavior and demonstrates Abstraction.
    public abstract boolean login(String enteredId, String enteredPassword);

    // Common method for updating profile.
    // For simplicity, this example just prints a message. In a real system,
    // it would allow changing attributes like username, password, etc.
    // This demonstrates Encapsulation by allowing controlled modification of object state.
    public void updateProfile(String newId, String newPassword, String newFullName,
                              String newPhoneNumber, String newEmail, String newGender) {
        this.id = newId;
        this.password = newPassword;
        this.fullName = newFullName;
        this.phoneNumber = newPhoneNumber;
        this.email = newEmail;
        this.gender = newGender;

        System.out.println(this.role + " profile updated for " + this.id);
    }

    // A simple method to display user details (common to all users)
    public void displayUserDetails() {
        System.out.println("Username: " + id);
        System.out.println("Full Name: " + fullName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Gender: " + gender);
        System.out.println("Role: " + role);
        // Note: We don't display password for security reasons
    }
}