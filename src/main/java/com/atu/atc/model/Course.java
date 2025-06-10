// src/main/java/com/atu/atc/model/Course.java
package com.atu.atc.model;

// A basic Course class
public class Course {
    private String courseId;
    private String subjectName;
    private String level;
    private double charges;
    private String schedule;
    // private Tutor assignedTutor; // You'll eventually add a Tutor object here

    public Course(String courseId, String subjectName, String level, double charges, String schedule) {
        this.courseId = courseId;
        this.subjectName = subjectName;
        this.level = level;
        this.charges = charges;
        this.schedule = schedule;
    }

    // Getters for Course attributes
    public String getCourseId() { return courseId; }
    public String getSubjectName() { return subjectName; }
    public String getLevel() { return level; }
    public double getCharges() { return charges; }
    public String getSchedule() { return schedule; }

    // You can add setters and other methods as needed for course management
    // e.g., updateCourseDetails()
}