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
import com.atu.atc.model.Tutor;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Payment;
import com.atu.atc.model.Classes; 
import com.atu.atc.model.User;

import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;
import com.atu.atc.util.FileUtils;
import java.util.Optional;

import java.util.List;
/**
 *
 * @author User
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
    
    public boolean registerTutor(String fullName, String password, String phoneNumber, String email, String gender, String level, String subject){
        
        if (!validator.isValidEmail(email)) {
            System.err.println("AdminService Error: Invalid email format.");
            return false;
        }
        
        if (!validator.isValidPhoneNumber(phoneNumber)){
            System.err.println("AdminService Error: Invalid phone number format.");
            return false;
        }
        
        String tutorId = idGenerator.generateUniqueId("T");
        
        if (tutorId == null){
            System.err.println("AdminService Error: Failed to generate ID for tutor.");
            return false;
        }
        
        Tutor newTutor = new Tutor(tutorId, password, fullName, phoneNumber, email, gender, level, subject);
        
        tutorRepository.add(newTutor);
        
        System.out.println("AdminService: Successfully registered new tutor.");
        
        return true;
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
    
}


