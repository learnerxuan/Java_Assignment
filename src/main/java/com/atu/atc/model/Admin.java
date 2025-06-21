// src/main/java/com/atu/atc/model/Admin.java (REVERTED/SIMPLIFIED FOR PHASE 2/3)

package com.atu.atc.model;

// IMPORTANT: Do NOT import AdminService here for now.
// The AdminService and data layer are not ready yet.

public class Admin extends User {
    // Admin-specific attributes (if any)
    // For now, no unique attributes beyond what User provides.

    // No AdminService field needed here yet in Phase 2/3.
    // private AdminService adminService;

    // Constructor for Admin.
    public Admin(String username, String password,  String fullName, String phoneNumber, String email, String gender) {
        super(username, password, "Admin", fullName, phoneNumber, email, gender);
    }

    // No setter for AdminService needed yet.
    // public void setAdminService(AdminService adminService) { ... }

    @Override
    public boolean login(String enteredUsername, String enteredPassword) {
        // This basic login check remains specific to the User model.
        // AuthenticationService will orchestrate the full login process later.
        if (getUsername().equals(enteredUsername) && getPassword().equals(enteredPassword)) {
            System.out.println("Admin " + getUsername() + " logged in successfully.");
            return true;
        } else {
            System.out.println("Admin login failed. Invalid username or password.");
            return false;
        }
    }

    // --- Admin-specific functionalities (TEMPORARILY SIMPLIFIED/PLACEHOLDER) ---
    // These methods will just print a message for now.
    // They will be updated later in Phase 5 to delegate to AdminService.

    public boolean registerTutor(String username, String password) {
        System.out.println("Admin " + getUsername() + ": Attempting to register tutor " + username + " (logic not yet implemented).");
        // Placeholder for future logic: This would eventually call AdminService.registerTutor
        return false; // For now, assume failure
    }

    public boolean deleteTutor(String username) {
        System.out.println("Admin " + getUsername() + ": Attempting to delete tutor " + username + " (logic not yet implemented).");
        return false;
    }

    public boolean registerReceptionist(String username, String password) {
        System.out.println("Admin " + getUsername() + ": Attempting to register receptionist " + username + " (logic not yet implemented).");
        return false;
    }

    public boolean deleteReceptionist(String username) {
        System.out.println("Admin " + getUsername() + ": Attempting to delete receptionist " + username + " (logic not yet implemented).");
        return false;
    }

    public boolean assignTutorToCourse(String tutorUsername, String courseId) {
        System.out.println("Admin " + getUsername() + ": Attempting to assign tutor " + tutorUsername + " to course " + courseId + " (logic not yet implemented).");
        return false;
    }

    public String viewMonthlyIncomeReport(String level, String subject) {
        System.out.println("Admin " + getUsername() + ": Attempting to view report for " + level + ", " + subject + " (logic not yet implemented).");
        return "Report: Not yet available.";
    }

    @Override
    public void updateProfile(String newUsername, String newPassword, String newFullName,
                              String newPhoneNumber, String newEmail, String newGender) {
        super.updateProfile(newUsername, newPassword, newFullName, newPhoneNumber, newEmail, newGender); // Calls the base User method
        System.out.println("Admin " + getUsername() + ": Profile updated locally (persistence not yet implemented).");
        // Placeholder for future logic: This would eventually call AdminService.updateAdminProfile
    }
}