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
                getId(),
                getFullName(),
                getPassword(),
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
        String[] parts = fileString.split(",", -1);
        if (parts.length == 10) {
            return new Student(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6],
                    parts[7],
                    parts[8],
                    parts[9]
            );
        }
        return null;
    }

    @Override
    public void updateProfile(String newId, String newPassword, String newFullName, String newPhoneNumber, String newEmail, String newGender) {
        super.updateProfile(newId, newPassword, newFullName, newPhoneNumber, newEmail, newGender);
        System.out.println("Student " + getId() + ": Profile updated locally (persistence handled by service layer).");
    }
}
