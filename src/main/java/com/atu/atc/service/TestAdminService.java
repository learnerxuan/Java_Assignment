//// src/main/java/com/atu/atc/service/TestAdminService.java
//package com.atu.atc.service;
//
//import com.atu.atc.data.UserRepository; // We need a UserRepository instance
//import com.atu.atc.model.Admin;
//import com.atu.atc.model.Receptionist; // To instantiate and register
//import com.atu.atc.model.Tutor;         // To instantiate and register
//import com.atu.atc.model.User;          // To observe users list
//
//import java.util.List;
//
//public class TestAdminService {
//
//    public static void main(String[] args) {
//        // First, instantiate the UserRepository (data layer)
//        UserRepository userRepository = new UserRepository();
//
//        // Then, instantiate the AdminService, passing the UserRepository (dependency injection)
//        AdminService adminService = new AdminService(userRepository);
//
//        // --- A. Prepare some initial data for testing ---
//        // Let's ensure we have an admin user in the system to begin with.
//        // For this test, we'll manually add one if it doesn't exist.
//        if (userRepository.getUserByUsername("superadmin") == null) {
//            userRepository.addUser(new Admin("superadmin", "securepass"));
//            System.out.println("Added 'superadmin' for testing purposes.");
//        }
//        if (userRepository.getUserByUsername("testadmin") == null) {
//            userRepository.addUser(new Admin("testadmin", "admin123"));
//            System.out.println("Added 'testadmin' for testing purposes.");
//        }
//        System.out.println("--- Current Users in System ---");
//        userRepository.loadUsers().forEach(u -> System.out.println("  " + u.getUsername() + " (" + u.getRole() + ")"));
//        System.out.println("-------------------------------\n");
//
//        // --- B. Simulate Admin actions using AdminService ---
//
//        System.out.println("--- 1. Registering a new Tutor ---");
//        boolean success = adminService.registerTutor("Dr. Smith", "tutorpass");
//        System.out.println("Register 'Dr. Smith' successful: " + success);
//        success = adminService.registerTutor("Dr. Smith", "tutorpass2"); // Attempt to register existing
//        System.out.println("Attempt to re-register 'Dr. Smith' successful: " + success); // Should be false
//
//        System.out.println("\n--- 2. Registering a new Receptionist ---");
//        success = adminService.registerReceptionist("Jane Doe", "recpass");
//        System.out.println("Register 'Jane Doe' successful: " + success);
//
//        System.out.println("\n--- 3. Verifying registered users (loading from file) ---");
//        List<User> usersAfterRegistration = userRepository.loadUsers();
//        System.out.println("Users after registration:");
//        for (User u : usersAfterRegistration) {
//            System.out.println("  " + u.getUsername() + " | Role: " + u.getRole());
//        }
//
//        System.out.println("\n--- 4. Deleting a Tutor ---");
//        success = adminService.deleteTutor("Dr. Smith");
//        System.out.println("Delete 'Dr. Smith' successful: " + success);
//        success = adminService.deleteTutor("NonExistentTutor"); // Attempt to delete non-existent
//        System.out.println("Attempt to delete 'NonExistentTutor' successful: " + success); // Should be false
//
//        System.out.println("\n--- 5. Deleting a Receptionist ---");
//        success = adminService.deleteReceptionist("Jane Doe");
//        System.out.println("Delete 'Jane Doe' successful: " + success);
//
//        System.out.println("\n--- 6. Verifying users after deletion ---");
//        List<User> usersAfterDeletion = userRepository.loadUsers();
//        System.out.println("Users after deletion:");
//        for (User u : usersAfterDeletion) {
//            System.out.println("  " + u.getUsername() + " | Role: " + u.getRole());
//        }
//
//        System.out.println("\n--- 7. Testing Admin profile update ---");
//        Admin testAdmin = (Admin) userRepository.getUserByUsername("testadmin");
//        if (testAdmin != null) {
//            testAdmin.setAdminService(adminService); // Set the service for the model object
//            testAdmin.updateProfile("new_testadmin_name", "newpass123");
//            System.out.println("Check 'users.txt' for 'new_testadmin_name' and 'newpass123'.");
//            // Reload to confirm changes were persisted
//            User reloadedAdmin = userRepository.getUserByUsername("new_testadmin_name");
//            if (reloadedAdmin != null) {
//                System.out.println("Reloaded admin: " + reloadedAdmin.getUsername() + ", password: " + reloadedAdmin.getPassword());
//            }
//        }
//
//
//        System.out.println("\n--- 8. Testing placeholder methods ---");
//        System.out.println(adminService.assignTutorToCourse("TutorX", "CourseY"));
//        System.out.println(adminService.viewMonthlyIncomeReport("Form 5", "Physics"));
//
//        // IMPORTANT: Remember to clear or reset your users.txt manually if you want
//        // to run these tests from a clean slate each time, otherwise, users will accumulate.
//    }
//}