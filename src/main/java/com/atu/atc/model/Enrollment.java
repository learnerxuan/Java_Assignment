package com.atu.atc.model;

/**
 * @author henge
 */

public class Enrollment {
    private String courseEnrollmentId;
    private String classId;
    private String studentId;

    public Enrollment(String courseEnrollmentId, String classId, String studentId){
        this.courseEnrollmentId = courseEnrollmentId;
        this.classId = classId;
        this.studentId = studentId;
    }

    // Getters
    public String getCourseEnrollmentId(){
        return courseEnrollmentId;
    }

    public String getClassId(){
        return classId;
    }

    public String getStudentId(){
        return studentId;
    }

    // Setters
    public void setCourseEnrollmentId(String courseEnrollmentId) {
        this.courseEnrollmentId = courseEnrollmentId;
    }

    public void setClassId(String classId){
        this.classId = classId;
    }

    public void setStudentId(String studentId){
        this.studentId = studentId;
    }

    // For writing to enrollments.txt
    public String toFileString(){
        return courseEnrollmentId + "," + classId + "," + studentId;
    }
}
