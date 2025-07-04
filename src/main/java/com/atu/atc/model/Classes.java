/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.model;

/**
 *
 * @author User
 */
public class Classes {
    // Define attributes
    private String classId;
    private String subjectId;
    private String tutorId;
    private String day;
    private String startTime;
    private String endTime;
    
    // Constructor
    public Classes(String classId, String subjectId, String tutorId, String day, String startTime, String endTime) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.tutorId = tutorId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters
    public String getClassId() {
        return classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }   
    
    // Setters
    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    // This method provides a string representation of the Classes object,
    @Override
    public String toString() {
        return "Classes{" +
               "classId='" + classId + '\'' +
               ", subjectId='" + subjectId + '\'' +
               ", tutorId='" + tutorId + '\'' +
               ", day='" + day + '\'' +
               ", startTime='" + startTime + '\'' +
               ", endTime='" + endTime + '\'' +
               '}';
    }
    
    public String toFileString() {
        return classId + "," + subjectId + "," + tutorId + "," + day + "," +
                startTime + "," + endTime;
    }
}
