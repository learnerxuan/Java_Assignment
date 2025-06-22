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

    @Override
    public void updateProfile(String newId, String newPassword, String newFullName,String newPhoneNumber, String newEmail, String newGender) {
        super.updateProfile(newId, newPassword, newFullName, newPhoneNumber, newEmail, newGender); // Calls the base User method
        System.out.println("Admin " + getId() + ": Profile updated locally (persistence not yet implemented).");
        // Placeholder for future logic: This would eventually call AdminService.updateAdminProfile
    }
    
    @Override
    public String toString() {
        // You can choose to call super.toString() if User has a good toString(),
        // or manually include the inherited attributes using their getters.
        return "Admin{" +
               "id='" + getId() + '\'' +
               ", role='" + getRole() + '\'' +
               ", fullName='" + getFullName() + '\'' +
               ", phoneNumber='" + getPhoneNumber() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", gender='" + getGender() + '\'' +
               // Note: Password is intentionally excluded for security when printing.
               '}';
    }
}