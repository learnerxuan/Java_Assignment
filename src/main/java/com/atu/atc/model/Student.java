// src/main/java/com/atu/atc/model/Student.java
package com.atu.atc.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String studentName;
    private String contactNumber;
    private String email;
    private String gender;
    private String schoolEnrollmentStatus;
    private String level;
    
    private List<Enrollment> enrollments;
    
    public Student(String studentID, String studentName, String password, String contactNumber,
                   String email, String gender, String schoolEnrollmentStatus, String level) {
        super(studentID, password, "Student");
        this.studentName = studentName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.gender = gender;
        this.schoolEnrollmentStatus = schoolEnrollmentStatus;
        this.level = level;
        this.enrollments = new ArrayList<>();
    }
 
    public Student(String studentID, String studentName, String password, String role, String contactNumber,
                  String email, String gender, String schoolEnrollmentStatus, String level) {
       super(studentID, password, role);
       this.studentName = studentName;
       this.contactNumber = contactNumber;
       this.email = email;
       this.gender = gender;
       this.schoolEnrollmentStatus = schoolEnrollmentStatus;
       this.level = level;
       this.enrollments = new ArrayList<>();

    }
    public String getStudentID(){
        return getUsername();
    }
    public String getStudentName(){
        return studentName;
    }
    public String getContactNumber(){
        return contactNumber;
    }
    public String getEmail(){
        return email;
    }
    public String getGender(){
        return gender;
    }
    public String getSchoolEnrollmentStatus(){
        return schoolEnrollmentStatus;
    }
    public String getLevel(){
        return level;
    }
    public List<Enrollment> getEnrollments(){
        return enrollments;
    }
    
    public void setStudentName(String studentName){
        this.studentName = studentName;
    }
    
        public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSchoolEnrollmentStatus(String schoolEnrollmentStatus) {
        this.schoolEnrollmentStatus = schoolEnrollmentStatus;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setEnrollments(List<Enrollment> enrollments){
        this.enrollments = enrollments;
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
    public boolean login(String enteredUsername, String enteredPassword) {
        return this.getUsername().equals(username) && getPassword().equals(enteredPassword);
    }

    public String toFileString() {
        return getUsername() + "," + 
               studentName + "," +
               getPassword() + "," + 
               getRole() + "," +     
               contactNumber + "," +
               email + "," +
               gender + "," +
               schoolEnrollmentStatus + "," +
               level;
    }

    private static class Enrollment {

        public Enrollment() {
        }
    }
}