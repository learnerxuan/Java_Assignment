/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.service;

import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.ReceptionistRepository;
import com.atu.atc.data.EnrollmentRepository;
import com.atu.atc.data.PaymentRepository;
import com.atu.atc.data.ClassesRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.model.Student;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Payment;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Subject;
import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

/**
 *
 * @author farris
 */

public class StudentService {
    private final StudentRepository studentRepo;
    private final ReceptionistRepository receptionistRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final PaymentRepository paymentRepo;
    private final ClassesRepository classesRepo;
    private final SubjectRepository subjectRepo;

    public StudentService(StudentRepository studentRepo, ReceptionistRepository receptionistRepo,
                               EnrollmentRepository enrollmentRepo, PaymentRepository paymentRepo,
                               ClassesRepository classesRepo, SubjectRepository subjectRepo){
        this.studentRepo = studentRepo;
        this.receptionistRepo = receptionistRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.classesRepo = classesRepo;
        this.subjectRepo = subjectRepo;
    }
    
    // View Student Classes
    public List<Classes> viewStudentClasses (String studentId) {
        if (!Validator.isValidId(studentId)) {
            System.out.println("Invalid Student ID Format");
            return List.of(); // to return an empty list if there is an invalid format
        }
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return List.of(); // to return an empty list the student id is not found
        }
        return studentEnrollments.stream()
                .map(enrollment -> classesRepo.getClassById(enrollment.getClassId()))
                .filter(java.util.Objects::nonNull) // Filter out any null classes (if a class ID doesn't match an existing class)
                .collect(Collectors.toList());
    }
    
    // View Student Payments
    
    public List<Payment> viewStudentPayments(String studentId) {
        if (!Validator.isValidId(studentId)) {
            System.out.println("Invalid Student ID Format");
            return List.of(); // to return an empty list if there is an invalid format
        }
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return List.of(); // to return an empty list the student id is not found
        }
        return paymentRepo.getPaymentsByStudentId(studentId);
    }
    
    // Submit Requests

    public boolean submitRequest(String studentId, String requestType, String requestDetails) {
        if (!Validator.isValidId(studentId)) {
            System.out.println("Invalid Student ID Format");
            return false; // to return an empty list if there is an invalid format
        }
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return false; // to return an empty list the student id is not found
        }
        if (requestType == null || requestType.trim().isEmpty() || requestDetails == null || requestDetails.trim().isEmpty()) {
            System.out.println("Ensure that all required fields are filled to submit. Try Again.");
            return False;
        }
        
        System.out.println("\nRequest has been submitted.");
        System.out.println("Student ID: " + studentId);
        System.out.println("Request Type: " + requestType);
        System.out.println("Request Details: " + requestDetails);
        System.out.println("Submission Date : " + LocalDate.now());
    
    // Update Student Profile

    public boolean updateStudentProfile(String studentId, Student updatedStudent) {
        if (!Validator.isValidId(studentId)) {
            System.out.println("Invalid Student ID Format");
            return false; // to return an empty list if there is an invalid format
        }
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return false; // to return an empty list the student id is not found
        }
        return
    }

}
