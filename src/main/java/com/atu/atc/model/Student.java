// src/main/java/com/atu/atc/model/Student.java
package com.atu.atc.model;

public class Student extends User {
    
    private String student_id;
    private String student_name;
    private String phone_number;
    private String email;
    private String gender;
    private final String icPassport;
    private String address;
    private final String month_of_enroll;
    private String level;

    public Student(String student_id, String student_name, String password, String phone_number,
                   String email, String gender, String icPassport, String address,
                   String month_of_enroll, String level) {
        super(student_id, password, "Student", student_name, phone_number, email, gender);
        this.student_name = student_name;
        this.phone_number = phone_number;
        this.email = email;
        this.gender = gender;
        this.icPassport = icPassport;
        this.address = address;
        this.month_of_enroll = month_of_enroll;
        this.level = level;
    }
//getters
    public String getStudent_id() {
        return student_id;
    }
    
    public String getStudent_name() {
        return student_name;
    }

    public String getPhone_number() {
        return phone_number;
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

    public String getMonthOfEnroll() {
        return month_of_enroll;
    }

    public String getLevel() {
        return level;
    }
//setters
    public void setStudent_name(String fullName) {
        this.student_name = fullName;
    }

    public void setPhone_number(String phoneNumber) {
        this.phone_number = phoneNumber;
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

    public void setLevel(String level) {
        this.level = level;
    }
// std functions    
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
        return student_id + "," + student_name + "," + getPassword() + "," + 
               phone_number + "," + email + "," + gender + "," + 
               icPassport + "," + address + "," + month_of_enroll + "," +
               level;
    }
}