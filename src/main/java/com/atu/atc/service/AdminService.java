/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.service;

import com.atu.atc.data.AdminRepository;
import com.atu.atc.data.ReceptionistRepository;
import com.atu.atc.data.TutorRepository;
import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.ClassesRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.EnrollmentRepository;
import com.atu.atc.data.PaymentRepository;
import com.atu.atc.data.RequestRepository;

import com.atu.atc.model.Admin;
import com.atu.atc.model.Student;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Payment;
import com.atu.atc.model.Classes; 
import com.atu.atc.model.User;
import com.atu.atc.model.Enrollment;

import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;
import com.atu.atc.util.FileUtils;

import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Xuan
 */
public class AdminService {
    private final AdminRepository adminRepository;
    private final ReceptionistRepository receptionistRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final ClassesRepository classesRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final RequestRepository requestRepository;
    
    private final IDGenerator idGenerator; 
    private final Validator validator;  
    
    // Constructor 
    public AdminService(AdminRepository adminRepository,
                        ReceptionistRepository receptionistRepository,
                        TutorRepository tutorRepository,
                        StudentRepository studentRepository,
                        ClassesRepository classesRepository,
                        SubjectRepository subjectRepository,
                        EnrollmentRepository enrollmentRepository,
                        PaymentRepository paymentRepository,
                        RequestRepository requestRepository,
                        IDGenerator idGenerator,
                        Validator validator) {
        this.adminRepository = adminRepository;
        this.receptionistRepository = receptionistRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
        this.classesRepository = classesRepository;
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
        this.requestRepository = requestRepository;
        this.idGenerator = idGenerator;
        this.validator = validator;
    }
    
    public Optional<Tutor> registerTutor(String fullName, String password, String phoneNumber, String email, String gender, String subject, String level) {

        // Input Validation 
        if (!Validator.isValidEmail(email)) { 
            System.err.println("AdminService Error: Invalid email format for new tutor.");
            return Optional.empty();
        }

        if (!Validator.isValidPhoneNumber(phoneNumber)) { 
            System.err.println("AdminService Error: Invalid phone number format for new tutor.");
            return Optional.empty();
        }

        String tutorId = idGenerator.generateUniqueId("T");

        if (tutorId == null) {
            System.err.println("AdminService Error: Failed to generate ID for new tutor.");
            return Optional.empty();
        }

        // Create new Tutor object
        Tutor newTutor = new Tutor(tutorId, password, fullName, phoneNumber, email, gender, subject, level);

        // Add the new tutor to the repository
        tutorRepository.add(newTutor);

        System.out.println("AdminService: Successfully registered new tutor with ID: " + tutorId);

        return Optional.of(newTutor); 
    }
    
    public boolean deleteTutor(String tutorId) {
        
        if (Optional.ofNullable(tutorRepository.getById(tutorId)).isEmpty()){
            System.err.println("AdminService: tutorId not found.");
            return false;
        }
        
        // Check if the tutor is currenctly assigned to any active classes
        List<Classes> assignedClasses = classesRepository.getByTutorId(tutorId);
        
        if (!assignedClasses.isEmpty()){
            System.err.println("AdminService: Tutor cannot be deleted as they are currently assigned to classes.");
            return false;
        }
        
        boolean deleted = tutorRepository.delete(tutorId);
        
        if (deleted){
            System.out.println("Successfully deleted tutor: "+tutorId);
        } else {
            System.err.println("Failed to detele tutor: "+ tutorId);
        }
        
        return deleted;
    }
    
    
    public boolean registerReceptionist(String fullName, String password, String phoneNumber, String email, String gender){
        
        if (!validator.isValidEmail(email)) {
            System.err.println("AdminService Error: Invalid email format.");
            return false;
        }
        
        if (!validator.isValidPhoneNumber(phoneNumber)){
            System.err.println("AdminService Error: Invalid phone number format.");
            return false;
        }
        
        String recepId = idGenerator.generateUniqueId("R");
        
        if (recepId == null){
            System.err.println("AdminService Error: Failed to generate ID for Receptionist.");
            return false;
        }
        
        Receptionist newRecep = new Receptionist(recepId, password, fullName, phoneNumber, email, gender);
        
        receptionistRepository.add(newRecep);
        
        System.out.println("AdminService: Successfully registered new Receptionist.");
        
        return true;
    }
    
    public boolean deleteReceptionist(String receptionistId) {
        
        if (Optional.ofNullable(receptionistRepository.getById(receptionistId)).isEmpty()){
            System.out.println("Receptionist with ID : "+ receptionistId + " not found for deletion.");
            return false;
        }
        
        boolean deleted = receptionistRepository.delete(receptionistId);
        if (deleted){
            System.out.println("Successfully deleted tutor: "+receptionistId);
        } else {
            System.err.println("Failed to detele tutor: "+ receptionistId);
        }
        
        return deleted;
    }
    
    public boolean updateAdminProfile(Admin updatedAdmin) {
        
        if (Optional.ofNullable(adminRepository.getById(updatedAdmin.getId())).isEmpty()) {
            System.err.println("Admin with ID '" + updatedAdmin.getId() + "' not found for update.");
            return false;
        }
        
        // Input Validation for updated fields.
        if (!validator.isValidEmail(updatedAdmin.getEmail()) || !validator.isValidPhoneNumber(updatedAdmin.getPhoneNumber())) {
            System.err.println("AdminService Error: Invalid email or phone number in updated profile for '" + updatedAdmin.getId() + "'. Update failed.");
            return false;
        }
        if (updatedAdmin.getFullName() == null || updatedAdmin.getFullName().trim().isEmpty() ||
            updatedAdmin.getPassword() == null || updatedAdmin.getPassword().trim().isEmpty() ||
            updatedAdmin.getGender() == null || updatedAdmin.getGender().trim().isEmpty()) {
            System.err.println("AdminService Error: Missing or invalid required fields in updated profile for '" + updatedAdmin.getId() + "'. Update failed.");
            return false;
        }

        // Delegate to repository for actual update and persistence.
        boolean updated = adminRepository.update(updatedAdmin);
        if (updated) {
            System.out.println("AdminService: Admin profile updated successfully for ID: " + updatedAdmin.getId());
        } else {
            System.err.println("AdminService Error: Failed to update admin profile for ID: '" + updatedAdmin.getId() + "' at repository level.");
        }
        return updated;
    }
    
    public double viewMonthlyIncomeReport(int year, int intMonth, String filterSubjectId, String filterFormLevel){
        // Input Validation
        // Validate year
        if (year < 2000 || year > LocalDate.now().getYear() + 1){
            System.err.println("Invalid year !");
            return 0.0;
        }
        // Validate Month
        if (intMonth < 1 || intMonth > 12) {
            System.err.println("Invalid Month entered: Month must be between 1 and 12.");
            return 0.0;
        }
        // Validate form level if provided
        if (filterFormLevel != null && !filterFormLevel.trim().isEmpty() && !validator.isFormValid(filterFormLevel)) {
            System.err.println("Invalid form level: Form level must be between 1 and 5.");
            return 0.0;
        }
        // Validate subject ID if provided
        if (filterSubjectId != null && !filterSubjectId.trim().isEmpty()) {
            if (subjectRepository.getSubjectById(filterSubjectId).isEmpty()) {
                System.err.println("Subject with ID '" + filterSubjectId + "' not found. Report cannot be generated for this subject.");
                return 0.0;
            }
        }
        
        Month monthEnum = Month.of(intMonth);
        
        List<Payment> allPayments = paymentRepository.getAll();
        
        if (allPayments.isEmpty()) {
            System.out.println("No payments found in the system for report.");
            return 0.0;
        }

        // Filter and Calculate
        double totalIncome = allPayments.stream()
            // Filter by Date and Status (only 'Completed' payments for the specified month/year)
            .filter(payment -> payment.getDate() != null &&
                               payment.getDate().getYear() == year &&
                               payment.getDate().getMonthValue() == intMonth &&
                               payment.getStatus().equalsIgnoreCase("Completed"))
            .filter(payment -> {
                // Filter by Student's Form Level and Subject
                Optional<Student> studentOpt = Optional.ofNullable(studentRepository.getById(payment.getStudentId()));
                if (studentOpt.isEmpty()) {
                    System.err.println("Student with ID '" + payment.getStudentId() + "' for payment '" + payment.getPaymentId() + "' not found. Skipping payment from report.");
                    return false; // Skip this payment if student is not found
                }
                
                Student student = studentOpt.get();

                // Apply form level filter if specified
                boolean matchesFormLevel = (filterFormLevel == null || filterFormLevel.trim().isEmpty()) || student.getLevel().equalsIgnoreCase(filterFormLevel);
                
                if (!matchesFormLevel) {
                    return false; 
                }

                // Apply subject filter if specified
                boolean matchesSubject = true; 
                
                if (filterSubjectId != null && !filterSubjectId.trim().isEmpty()) {
                    
                    List<Enrollment> studentEnrollments = enrollmentRepository.getByStudentId(student.getId());
                    
                    // Check if any of the student's enrollments are for a class with the filtered subject.
                    matchesSubject = studentEnrollments.stream()
                        .anyMatch(enrollment -> {
                            Optional<Classes> classOpt = classesRepository.getById(enrollment.getClassId());
                            if (classOpt.isPresent()) {
                                Classes enrolledClass = classOpt.get();
                                return enrolledClass.getSubjectId().equalsIgnoreCase(filterSubjectId);
                            }
                            return false; // Class not found for enrollment
                        });
                }
                
                return matchesFormLevel && matchesSubject; // Payment must match both form and subject filters
            })
            .mapToDouble(Payment::getAmount) // Extract the 'amount' from each filtered Payment object
            .sum(); // Sum all the extracted amounts

        System.out.println("AdminService: Monthly income report for " + monthEnum + ", " + year + " (Subject: " + (filterSubjectId != null && !filterSubjectId.isEmpty() ? filterSubjectId : "All") +
                           ", Level: " + (filterFormLevel != null && !filterFormLevel.isEmpty() ? filterFormLevel : "All") +
                           "): $" + String.format("%.2f", totalIncome));
        return totalIncome;
    }
    
    public boolean assignTutorToClass(String classId, String tutorId){
        
        Optional<Classes> classOpt = classesRepository.getById(classId);
        Optional<Tutor> tutorOpt = Optional.ofNullable(tutorRepository.getById(tutorId));
        
        if (classOpt.isEmpty()) {
            System.err.println("Class with ID '" + classId + "' not found.");
            return false;
        }
        if (tutorOpt.isEmpty()) {
            System.err.println("Tutor with ID '" + tutorId + "' not found.");
            return false;
        }

        Classes cls = classOpt.get();
        
        cls.setTutorId(tutorId);

        boolean updated = classesRepository.update(cls);

        if (updated) {
            System.out.println("Successfully assigned tutor '" + tutorId + "' to class '" + classId + "'.");
        } else {
            System.err.println("Failed to update class '" + classId + "' with new tutor at repository level. Assignment failed.");
        }
        return updated;
    } 
}


