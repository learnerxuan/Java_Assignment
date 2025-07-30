package com.atu.atc.service;

import com.atu.atc.data.*;
import com.atu.atc.model.*;
import com.atu.atc.util.Validator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import com.atu.atc.util.IDGenerator;

public class StudentService {
    private final StudentRepository studentRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final ClassesRepository classesRepo;
    private final RequestRepository requestRepo;
    private final PaymentRepository paymentRepo;
    private final SubjectRepository subjectRepo;
    private final TutorRepository tutorRepo;
    
    public StudentService(StudentRepository studentRepo,
                          EnrollmentRepository enrollmentRepo,
                          ClassesRepository classesRepo,
                          RequestRepository requestRepo,
                          PaymentRepository paymentRepo,
                          SubjectRepository subjectRepo,
                          TutorRepository tutorRepo) {
        this.studentRepo = studentRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.classesRepo = classesRepo;
        this.requestRepo = requestRepo;
        this.paymentRepo = paymentRepo;
        this.subjectRepo = subjectRepo;
        this.tutorRepo = tutorRepo;
    }
    
    // View class schedule based on student ID.
    public List<Classes> getSchedule(String studentId) {
        List<String> enrolledClassIds = enrollmentRepo.getByStudentId(studentId).stream()
                .map(Enrollment::getClassId)
                .collect(Collectors.toList());
        
        return enrolledClassIds.stream()
                .map(cid -> classesRepo.getById(cid).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    // Send a subject change request to receptionist.
    public void submitSubjectChangeRequest(String studentId, String currentSubjectId, String requestedSubjectId) {
        List<Request> existingRequests = requestRepo.getByStudentId(studentId);
        String requestId = IDGenerator.generateUniqueId("RQ");
        Request newRequest = new Request(
                requestId,
                studentId,
                currentSubjectId,
                requestedSubjectId,
                "Pending",
                LocalDate.now()
        );
        requestRepo.add(newRequest);
    }
    
    // Delete a pending subject change request.
    public boolean deletePendingRequest(String requestId) {
        Optional<Request> request = requestRepo.getRequestById(requestId);
        if (request.isPresent() && request.get().getStatus().equalsIgnoreCase("Pending")) {
            return requestRepo.delete(requestId);
        }
        return false;
    }
    
    // View all requests sent by the student.
    public List<Request> getRequestsByStudentId(String studentId) {
        return requestRepo.getAll().stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
    
    // View payment records of student.
    public List<Payment> getPayments(String studentId) {
        return paymentRepo.getAll().stream()
                .filter(p -> p.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }
    
    // Calculate total unpaid balance.
    public double getTotalBalance(String studentId) {
        return getPayments(studentId).stream()
                .filter(p -> !p.getStatus().equalsIgnoreCase("Paid"))
                .mapToDouble(Payment::getAmount)
                .sum();
    }
    
    // Update profile details of student.
    public void updateProfile(Student student, String newPassword, String newPhone, String newEmail, String newAddress) {
        if (!Validator.isValidEmail(newEmail)) {
            System.err.println("Invalid email format.");
            return;
        }
        
        if (!Validator.isValidPhoneNumber(newPhone)) {
            System.err.println("Invalid phone number.");
            return;
        }
        
        student.setPassword(newPassword);
        student.setPhoneNumber(newPhone);
        student.setEmail(newEmail);
        student.setAddress(newAddress);
        
        studentRepo.update(student);
        System.out.println("Student profile updated.");
    }
    
    public ClassesRepository getClassesRepository() {
        return classesRepo;
    }
    
    public EnrollmentRepository getEnrollmentRepository() {
        return enrollmentRepo;
    }
    
    public SubjectRepository getSubjectRepository() {
        return subjectRepo;
    }
    
    public TutorRepository getTutorRepository() {
        return tutorRepo;
    }
}
