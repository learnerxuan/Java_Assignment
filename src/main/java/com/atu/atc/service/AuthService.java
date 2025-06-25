/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.service;

import com.atu.atc.data.AdminRepository;
import com.atu.atc.data.ReceptionistRepository;
import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.TutorRepository;
import com.atu.atc.model.User; 
import com.atu.atc.model.Admin; 
import com.atu.atc.model.Receptionist; 
import com.atu.atc.model.Tutor; 
import com.atu.atc.model.Student; 

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 *
 * @author Xuan
 */
public class AuthService {
    
    private final AdminRepository adminRepository;
    private final ReceptionistRepository receptionistRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    
    // userId as key and failed attempt count as value, example: {"S002":2, "S001":1}
    private static final Map<String, Integer> failedLoginAttempts = new HashMap<>();
    private static final int MAX_ATTEMPTS = 3;
    
    // Constructor for AuthService
    public AuthService(AdminRepository adminRepository, ReceptionistRepository receptionistRepository, TutorRepository tutorRepository, StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.receptionistRepository = receptionistRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
    }
    
    public Optional<User> authenticateUser(String userId, String password){
        
        // Identify whether is Admin user or not.
        boolean isAdminAttempt = userId.toUpperCase().startsWith("A");
        
        if (!isAdminAttempt && failedLoginAttempts.getOrDefault(userId, 0) >= MAX_ATTEMPTS){
            System.err.println("Authentication failed for userId: "+ userId + " .Account locked due to too many failed attempts.");
            
            return Optional.empty(); // Account is locked.
        }
        
        User authenticatedUser = null;
        
        // Attempt to authenticate as Admin, creates an Optional.empty() if the userId is not found.
        Optional<Admin> adminOpt = Optional.ofNullable(adminRepository.getById(userId));
        
        if (adminOpt.isPresent()){
            if (adminOpt.get().login(userId, password)){
                authenticatedUser = adminOpt.get();
            }
        }
        
        // If not authenticated yet, try Receptionist
        if (authenticatedUser == null){
            Optional<Receptionist> recepOpt = Optional.ofNullable(receptionistRepository.getById(userId));
            
            if (recepOpt.isPresent()){
                if (recepOpt.get().login(userId, password)) {
                    authenticatedUser = recepOpt.get();
                }               
            }
        }
        
        // If not authenticated yet, try Tutor
        if (authenticatedUser == null) {
            Optional<Tutor> tutorOpt = tutorRepository.getById(userId);
            
            if (tutorOpt.isPresent()) {
                if (tutorOpt.get().login(userId, password)) {
                    authenticatedUser = tutorOpt.get();
                }
            }
        }

        // If not authenticated yet, try Student
        if (authenticatedUser == null) {
            Optional<Student> studentOpt = Optional.ofNullable(studentRepository.getById(userId));
            
            if (studentOpt.isPresent()) {
                if (studentOpt.get().login(userId, password)) {
                    authenticatedUser = studentOpt.get();
                }
            }
        }
        
        if (authenticatedUser != null) {
            // Authentication successful
            System.out.println("AuthService: User '" + userId + "' authenticated successfully as " + authenticatedUser.getRole() + ".");
            
            // Reset failed attempts for this user upon successful login.
            failedLoginAttempts.remove(userId);
            return Optional.of(authenticatedUser); // Return the authenticated User object.
            
        } else {
            // Authentication failed
            System.out.println("AuthService: Authentication failed for user '" + userId + "'. Invalid ID or password.");

            // Increment failed attempts only for non-Admin users.
            // Admins are typically not locked out to prevent denial of service.
            if (!isAdminAttempt) {
                // Increment count, or initialize to 1 if first failed attempt.
                failedLoginAttempts.put(userId, failedLoginAttempts.getOrDefault(userId, 0) + 1);
                
                int currentAttempts = failedLoginAttempts.get(userId);
                
                System.err.println("AuthService: Failed attempts for '" + userId + "': " + currentAttempts + "/" + MAX_ATTEMPTS);
                
                if (currentAttempts >= MAX_ATTEMPTS) {
                    System.err.println("AuthService: Account '" + userId + "' has reached the maximum failed attempts and is now locked.");
                }
            }
            return Optional.empty(); // Return empty Optional to indicate failure.
        }
    }
}
