package com.atu.atc.model;

public class Student extends User {
    private String icPassport;
    private String address;
    private String monthOfEnroll;
    private String level;
    
    public Student(String id, String fullName, String password,
                   String phoneNumber, String email, String gender,
                   String icPassport, String address, String monthOfEnroll, String level) {
        super(id, password, "Student", fullName, phoneNumber, email, gender);
        this.icPassport = icPassport;
        this.address = address;
        this.monthOfEnroll = monthOfEnroll;
        this.level = level;
    }
    
    // Getters
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
    
    // Setters
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
        return getId().equals(enteredId) && getPassword().equals(enteredPassword);
    }
    
    @Override
    public String toFileString() {
        return String.join(",",
                getId(),             // student_id
                getFullName(),       // student_name
                getPassword(),       // password
                getPhoneNumber(),    // phone_number
                getEmail(),          // email
                getGender(),         // gender
                icPassport,          // IC/Passport
                address,             // address
                monthOfEnroll,       // month_of_enroll
                level                // level
        );
    }
    
    public static Student fromFileString(String fileString) {
        String[] parts = fileString.split(",", -1);
        if (parts.length == 10) {
            return new Student(
                    parts[0], // student_id
                    parts[1], // student_name
                    parts[2], // password
                    parts[3], // phone_number
                    parts[4], // email
                    parts[5], // gender
                    parts[6], // IC/Passport
                    parts[7], // address
                    parts[8], // month_of_enroll
                    parts[9]  // level
            );
        }
        return null;
    }
}
