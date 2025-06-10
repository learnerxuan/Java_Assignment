// src/main/java/com/atu/atc/model/Student.java
package com.atu.atc.model;

public class Student extends User {
    // Student-specific attributes
    private String icPassport;
    private String email;
    private String contactNumber;
    private String address;
    private String level; // Form 1-5
    // You'll likely need a List<Enrollment> here later
    // private List<Enrollment> enrollments;

    public Student(String username, String password, String icPassport, String email, String contactNumber, String address, String level) {
        super(username, password, "Student");
        this.icPassport = icPassport;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.level = level;
        // this.enrollments = new ArrayList<>();
    }

    @Override
    public boolean login(String enteredUsername, String enteredPassword) {
        // Basic login for now
        return getUsername().equals(enteredUsername) && getPassword().equals(enteredPassword);
    }

    // Getters for student-specific attributes
    public String getIcPassport() { return icPassport; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
    public String getLevel() { return level; }

    // Placeholder for Student-specific methods
    public void viewClassSchedule() { /* ... */ }
    public void sendSubjectChangeRequest() { /* ... */ }
    // Add all methods from your assignment description for Student
}