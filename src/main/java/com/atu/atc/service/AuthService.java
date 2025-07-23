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

    // Modified to return AuthResult
    public AuthResult authenticateUser(String userId, String password) {

        // Identify whether is Admin user or not.
        boolean isAdminAttempt = userId.toUpperCase().startsWith("A");

        if (!isAdminAttempt && failedLoginAttempts.getOrDefault(userId, 0) >= MAX_ATTEMPTS) {
            // Concise message for locked account
            String message = "Account locked for ID: " + userId;
            System.err.println("Authentication failed for userId: " + userId + " . " + message);
            return new AuthResult(message); // Account is locked.
        }

        User authenticatedUser = null;

        // Attempt to authenticate as Admin
        Optional<Admin> adminOpt = Optional.ofNullable(adminRepository.getById(userId));

        if (adminOpt.isPresent()) {
            if (adminOpt.get().login(userId, password)) {
                authenticatedUser = adminOpt.get();
            }
        }

        // If not authenticated yet, try Receptionist
        if (authenticatedUser == null) {
            Optional<Receptionist> recepOpt = Optional.ofNullable(receptionistRepository.getById(userId));

            if (recepOpt.isPresent()) {
                if (recepOpt.get().login(userId, password)) {
                    authenticatedUser = recepOpt.get();
                }
            }
        }

        // If not authenticated yet, try Tutor
        if (authenticatedUser == null) {
            Optional<Tutor> tutorOpt = Optional.ofNullable(tutorRepository.getById(userId));

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
            String message = "Login successful! Welcome, " + authenticatedUser.getFullName() + "!";
            System.out.println("AuthService: User '" + userId + "' authenticated successfully as " + authenticatedUser.getRole() + ".");

            // Reset failed attempts for this user upon successful login.
            failedLoginAttempts.remove(userId);
            return new AuthResult(authenticatedUser, message); // Return success result.

        } else {
            // Authentication failed
            String message;
            if (!isAdminAttempt) {
                // Increment count, or initialize to 1 if first failed attempt.
                failedLoginAttempts.put(userId, failedLoginAttempts.getOrDefault(userId, 0) + 1);

                int currentAttempts = failedLoginAttempts.get(userId);

                if (currentAttempts >= MAX_ATTEMPTS) {
                    message = "Account locked for ID: " + userId;
                } else {
                    message = "Invalid ID/Password. Attempts: " + currentAttempts + "/" + MAX_ATTEMPTS;
                }
            } else {
                message = "Invalid ID or Password.";
            }
            System.err.println("AuthService: Authentication failed for user '" + userId + "'. " + message);
            return new AuthResult(message); // Return failure result.
        }
    }

    public Optional<Student> authenticateStudent(String id, String password) {
        Optional<Student> student = Optional.ofNullable(studentRepository.getById(id));
        if (student.isPresent() && student.get().getPassword().equals(password)) {
            return student;
        }
        return Optional.empty();
    }

    public boolean changePassword(String userId, String newPassword) {
        // Find the user by ID across all repositories and update password
        Optional<Admin> adminOpt = Optional.ofNullable(adminRepository.getById(userId));
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setPassword(newPassword);
            return adminRepository.update(admin);
        }

        Optional<Receptionist> receptionistOpt = Optional.ofNullable(receptionistRepository.getById(userId));
        if (receptionistOpt.isPresent()) {
            Receptionist receptionist = receptionistOpt.get();
            receptionist.setPassword(newPassword);
            return receptionistRepository.update(receptionist);
        }

        Optional<Tutor> tutorOpt = Optional.ofNullable(tutorRepository.getById(userId));
        if (tutorOpt.isPresent()) {
            Tutor tutor = tutorOpt.get();
            tutor.setPassword(newPassword);
            return tutorRepository.update(tutor);
        }

        Optional<Student> studentOpt = Optional.ofNullable(studentRepository.getById(userId));
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setPassword(newPassword);
            return studentRepository.update(student);
        }

        System.err.println("AuthService: User with ID " + userId + " not found for password change.");
        return false;
    }
}


