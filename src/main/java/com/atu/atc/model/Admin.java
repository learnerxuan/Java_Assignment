// src/main/java/com/atu/atc/model/Admin.java (REVERTED/SIMPLIFIED FOR PHASE 2/3)

package com.atu.atc.model;


public class Admin extends User {

    // Constructor for Admin.
    public Admin(String id, String password,  String fullName, String phoneNumber, String email, String gender) {
        super(id, password, "Admin", fullName, phoneNumber, email, gender);
    }

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
        super.updateProfile(newId, newPassword, newFullName, newPhoneNumber, newEmail, newGender); 
        System.out.println("Admin " + getId() + ": Profile updated locally (persistence not yet implemented).");
    }
    
    public String toFileString(){
        return getId() + "," +        
               getPassword() + "," +  
               getFullName() + "," +  
               getPhoneNumber() + "," +
               getEmail() + "," +
               getGender();
    }
    
    @Override
    public String toString() {
        return "Admin{" +
               "id='" + getId() + '\'' +
               ", role='" + getRole() + '\'' +
               ", fullName='" + getFullName() + '\'' +
               ", phoneNumber='" + getPhoneNumber() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", gender='" + getGender() + '\'' +
               '}';
    }
}