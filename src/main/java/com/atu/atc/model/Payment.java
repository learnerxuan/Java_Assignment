package com.atu.atc.model;

import java.time.LocalDate;

/**
 * @author henge
 */

public class Payment {
    private String paymentId;
    private String studentId;
    private double amount;
    private LocalDate date;
    private String paymentMethod;
    private String status;
    private String receptionistId;

    public Payment(String paymentId, String studentId, double amount, LocalDate date,
                   String paymentMethod, String status, String receptionistId) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.receptionistId = receptionistId;
    }

    // Getters
    public String getPaymentId() {
        return paymentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public String getReceptionistId() {
        return receptionistId;
    }

    // Setters
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReceptionistId(String receptionistId) {
        this.receptionistId = receptionistId;
    }

    // For saving to file
    public String toFileString() {
        return paymentId + "," + studentId + "," + amount + "," + date + "," +
                paymentMethod + "," + status + "," + receptionistId;
    }
}
