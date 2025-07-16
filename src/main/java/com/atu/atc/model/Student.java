package com.atu.atc.model;

public class Student extends User {
    private String icPassport;
    private String address;
    private String monthOfEnroll;
    private String level; // Student's academic level (e.g., Year 1, Year 2, or Level 1-5)
    
    public Student(String id, String password, String role, String fullName,
                   String phoneNumber, String email, String gender,
                   String icPassport, String address, String monthOfEnroll, String level) {
        super(id, password, role, fullName, phoneNumber, email, gender);
        this.icPassport = icPassport;
        this.address = address;
        this.monthOfEnroll = monthOfEnroll;
        this.level = level;
    }
    
    // Getters specific to Student
    public String getIcPassport() {
        return icPassport;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getMonthOfEnroll() {
        return monthOfEnroll;
    }
    
    public String getLevel() {
        return level;
    }
    
    // Setters specific to Student [ADDITION]
    public void setIcPassport(String icPassport) {
        this.icPassport = icPassport;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setMonthOfEnroll(String monthOfEnroll) {
        this.monthOfEnroll = monthOfEnroll;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    
    @Override
    public boolean login(String enteredId, String enteredPassword) {
        return false;
    }
    
    @Override
    public String toFileString() {
        return String.join(";",
                getId(),
                getPassword(),
                getRole(),
                getFullName(),
                getPhoneNumber(),
                getEmail(),
                getGender(),
                icPassport,
                address,
                monthOfEnroll,
                level
        );
    }
    
    public static Student fromFileString(String fileString) {
        String[] parts = fileString.split(";");
        if (parts.length == 11) {
            return new Student(
                    parts[0], // id
                    parts[1], // password
                    parts[2], // role
                    parts[3], // fullName
                    parts[4], // phoneNumber
                    parts[5], // email
                    parts[6], // gender
                    parts[7], // icPassport
                    parts[8], // address
                    parts[9], // monthOfEnroll
                    parts[10] // level
            );
        }
        return null; // Or throw an exception for malformed string
    }
}