package com.atu.atc.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Request {
    private String requestId;
    private String studentId;
    private String currentSubjectId;
    private String requestedSubjectId;
    private String status; // e.g., "Pending", "Approved", "Rejected"
    private LocalDate requestDate;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    public Request(String requestId, String studentId, String currentSubjectId,
                   String requestedSubjectId, String status, LocalDate requestDate) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.currentSubjectId = currentSubjectId;
        this.requestedSubjectId = requestedSubjectId;
        this.status = status;
        this.requestDate = requestDate;
    }
    
    // Getters
    public String getRequestId() {
        return requestId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getCurrentSubjectId() {
        return currentSubjectId;
    }
    
    public String getRequestedSubjectId() {
        return requestedSubjectId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public LocalDate getRequestDate() {
        return requestDate;
    }
    
    // Setters (for mutable fields only)
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public void setCurrentSubjectId(String currentSubjectId) {
        this.currentSubjectId = currentSubjectId;
    }
    
    public void setRequestedSubjectId(String requestedSubjectId) {
        this.requestedSubjectId = requestedSubjectId;
    }
    
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }
    
    // Persistence methods
    public String toFileString() {
        return String.join(";",
                requestId,
                studentId,
                currentSubjectId,
                requestedSubjectId,
                status,
                requestDate.format(DATE_FORMATTER)
        );
    }
    
    public static Request fromFileString(String fileString) {
        String[] parts = fileString.split(";");
        if (parts.length == 6) {
            return new Request(
                    parts[0], // requestId
                    parts[1], // studentId
                    parts[2], // currentSubjectId
                    parts[3], // requestedSubjectId
                    parts[4], // status
                    LocalDate.parse(parts[5], DATE_FORMATTER) // requestDate
            );
        }
        return null;
    }
}
