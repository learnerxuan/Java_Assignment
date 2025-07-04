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
    public List<Classes> viewStudentClasses(String studentId) {
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return List.of(); // to return an empty list the student id is not found
        }
        
        List<Enrollment> studentEnrollments = enrollmentRepo.getByStudentId(studentId);
        if (studentEnrollments == null) {
            return List.of();
        }
        
        return studentEnrollments.stream()
                .map(enrollment -> classesRepo.getClassById(enrollment.getClassId()))
                .filter(java.util.Objects::nonNull) // Filter out any null classes (if a class ID doesn't match an existing class)
                .collect(Collectors.toList());
    }
    
    // View Student Payments
    
    public List<Payment> viewStudentPayments(String studentId) {
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return List.of(); // to return an empty list the student id is not found
        }
        List<Payment> payments = paymentRepo.getPaymentsByStudentId(studentId);
        if (payments == null) {
            return List.of();
        }
        return payments;
    }
    
    // Submit Requests
    
    public boolean submitRequest(String studentId, String requestType, String requestDetails) {
        Student student = studentRepo.getByStudentId(studentId);
        if (student == null) {
            System.out.println("The Student ID " + studentId + " is not found.");
            return false; // to return an empty list the student id is not found
        }
        if (requestType == null || requestType.trim().isEmpty() || requestDetails == null || requestDetails.trim().isEmpty()) {
            System.out.println("Ensure that all required fields are filled to submit. Try Again.");
            return false;
        }
        
        System.out.println("\nRequest has been submitted.");
        System.out.println("Student ID: " + studentId);
        System.out.println("Request Type: " + requestType);
        System.out.println("Request Details: " + requestDetails);
        System.out.println("Submission Date : " + LocalDate.now());
        
        return true;
    }
    
    // Update Student Profile
    
    public boolean updateStudentProfile(String studentId, Student updatedStudent) {
        if (updatedStudent == null) {
            System.out.println("Object Cannot be Null");
            return false; // to return an empty list the student id is not found
        }
        if (!studentId.equals(updatedStudent.getId())) {
            System.out.println("Unsuccessful. Students IDs are mismatched. Please try again.");
            return false;
        }
        Student existingStudent = studentRepo.getByStudentId(studentId);
        if (existingStudent == null) {
            System.out.println("Student ID " + studentId + " is not found.");
            return false;
        }
        existingStudent.setAddress(updatedStudent.getAddress());
        existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
        existingStudent.setLevel(updatedStudent.getLevel());
        existingStudent.setEmail(updatedStudent.getEmail());

        
        boolean success = studentRepo.updateStudent(existingStudent);
        if (success) {
            System.out.println("Student Profile has been Updated.");
        } else {
            System.out.println("Student Profile update is unsuccessful.");
        }
        return success;
    }
}
