// src/main/java/com/atu/atc/model/Student.java
package com.atu.atc.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Student extends User {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String gender;
    private final String icPassport;
    private String address;
    private final LocalDate monthOfEnroll;
    private int level;

    // Constructor
    public Student(String studentId, String password, String fullName, String phoneNumber,
                   String email, String gender, String icPassport, String address,
                   LocalDate monthOfEnroll, int level) {
        super(studentId, password, "Student", fullName, phoneNumber, email, gender); // Inherited from User
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.icPassport = icPassport;
        this.address = address;
        this.monthOfEnroll = monthOfEnroll;
        this.level = level;
    }

    // --- Getters ---
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

    public String getIcPassport() {
        return icPassport;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getMonthOfEnroll() {
        return monthOfEnroll;
    }

    public int getLevel() {
        return level;
    }

    // --- Setters (For editable fields) ---
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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    public void viewClassSchedule() {
        System.out.println("Viewing class schedule...");
    }

    public void sendSubjectChangeRequest() {
        System.out.println("Sending subject change request...");
    }

    public void deletePendingRequest() {
        System.out.println("Deleting pending request...");
    }

    public void viewPaymentStatus() {
        System.out.println("Viewing payment status...");
    }
        

    @Override
    public boolean login(String enteredId, String enteredPassword) {
        return this.getId().equals(enteredId) && getPassword().equals(enteredPassword);
    }

    public String toFileString() {
        return getId() + "," +
               fullName + "," +
               getPassword() + "," + 
               getRole() + "," +     
               phoneNumber + "," +
               email + "," +
               gender + "," +
               "," +
               level;
    }
}