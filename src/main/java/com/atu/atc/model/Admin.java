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
    public Admin(String id, String password,  String fullName, String phoneNumber, String email, String gender) {
        super(id, password, "Admin", fullName, phoneNumber, email, gender);
    }

    // No setter for AdminService needed yet.
    // public void setAdminService(AdminService adminService) { ... }

    @Override
    public boolean login(String enteredId, String enteredPassword) {
        // This basic login check remains specific to the User model.
        // AuthenticationService will orchestrate the full login process later.
        if (getId().equals(enteredId) && getPassword().equals(enteredPassword)) {
            System.out.println("Admin " + getId() + " logged in successfully.");
            return true;
        } else {
            System.out.println("Admin login failed. Invalid username or password.");
            return false;
        }
    }

    // --- Admin-specific functionalities (TEMPORARILY SIMPLIFIED/PLACEHOLDER) ---
    // These methods will just print a message for now.
    // They will be updated later in Phase 5 to delegate to AdminService.

    public boolean registerTutor(String id, String password) {
        System.out.println("Admin " + getId() + ": Attempting to register tutor " + id + " (logic not yet implemented).");
        // Placeholder for future logic: This would eventually call AdminService.registerTutor
        return false; // For now, assume failure
    }

    public boolean deleteTutor(String id) {
        System.out.println("Admin " + getId() + ": Attempting to delete tutor " + id + " (logic not yet implemented).");
        return false;
    }

    public boolean registerReceptionist(String id, String password) {
        System.out.println("Admin " + getId() + ": Attempting to register receptionist " + id + " (logic not yet implemented).");
        return false;
    }

    public boolean deleteReceptionist(String id) {
        System.out.println("Admin " + getId() + ": Attempting to delete receptionist " + id + " (logic not yet implemented).");
        return false;
    }

    public boolean assignTutorToCourse(String tutorId, String courseId) {
        System.out.println("Admin " + getId() + ": Attempting to assign tutor " + tutorId + " to course " + courseId + " (logic not yet implemented).");
        return false;
    }

    public String viewMonthlyIncomeReport(String level, String subject) {
        System.out.println("Admin " + getId() + ": Attempting to view report for " + level + ", " + subject + " (logic not yet implemented).");
        return "Report: Not yet available.";
    }

    @Override
    public void updateProfile(String newId, String newPassword, String newFullName,
                              String newPhoneNumber, String newEmail, String newGender) {
        super.updateProfile(newId, newPassword, newFullName, newPhoneNumber, newEmail, newGender); // Calls the base User method
        System.out.println("Admin " + getId() + ": Profile updated locally (persistence not yet implemented).");
        // Placeholder for future logic: This would eventually call AdminService.updateAdminProfile
    }
}