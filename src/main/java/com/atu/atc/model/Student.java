package com.atu.atc.model;

public class Student extends User {

    private final String icPassport;
    private String address;
    private final String monthOfEnroll;
    private String level;

    public Student(String id, String password, String fullName, String phoneNumber,
                   String email, String gender, String icPassport, String address,
                   String monthOfEnroll, String level) {
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
    public void setAddress(String address) {
        this.address = address;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean login(String enteredId, String enteredPassword) {
        return getId().equals(enteredId) && getPassword().equals(enteredPassword);
    }

    public String toFileString() {
        return getId() + "," + getFullName() + "," + getPassword() + "," +
                getPhoneNumber() + "," + getEmail() + "," + getGender() + "," +
                icPassport + "," + address + "," + monthOfEnroll + "," + level;
    }
}
