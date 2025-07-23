package com.atu.atc.model;

public class Tutor extends User {
    // Additional unique attributes for Tutor: subject and level
    private String subject;
    private String level;

    public Tutor(String userId, String fullName, String password, String phoneNumber, String email, String gender, String level, String subject) {
        // Calls the constructor of the superclass (User)
        super(userId, password, "Tutor", fullName, phoneNumber, email, gender);
        this.subject = subject;
        this.level = level;
    }

    @Override
    public boolean login(String enteredId, String enteredPassword) {
    if (enteredId == null || enteredPassword == null) {
        System.out.println("Tutor login failed: ID or Password is null.");
        return false;
    }

    String trimmedId = enteredId.trim();
    String trimmedPassword = enteredPassword.trim();

    boolean idMatch = getId().trim().equalsIgnoreCase(trimmedId);
    boolean pwMatch = getPassword().trim().equals(trimmedPassword);

    if (idMatch && pwMatch) {
        System.out.println("Tutor " + getId() + " logged in successfully.");
        return true;
    } else {
        System.out.println("Tutor login failed. ID match: " + idMatch + ", PW match: " + pwMatch);
        return false;
    }
}


    @Override
    public void updateProfile(String newId, String newFullName, String newPassword,  String newPhoneNumber, String newEmail, String newGender) {
        super.updateProfile(newId, newFullName, newPassword,  newPhoneNumber, newEmail, newGender);
        System.out.println("Tutor " + getId() + ": Profile updated locally (persistence handled by service layer).");
    }

    // --- Getters and Setters for subject and level ---
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String toFileString() {
        // Corrected getId() to getUserId()
        return getId() + "," +
                getFullName() + "," +
                getPassword() + "," +
                getPhoneNumber() + "," +
                getEmail() + "," +
                getGender() + "," +
                level + "," +
                subject;
    }
    
    @Override
    public String toString() {
        // Corrected getId() to getUserId()
        return "Tutor{" +
               "userId='" + getId() + '\'' +
               ", role='" + getRole() + '\'' +
               ", fullName='" + getFullName() + '\'' +
               ", phoneNumber='" + getPhoneNumber() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", gender='" + getGender() + '\'' +
               ", subject='" + subject + '\'' +
               ", level='" + level + '\'' +
               '}';
    }

    // --- Placeholder methods for Tutor-specific functionalities (from assignment) ---
    public void addClassInformation() {
        System.out.println("Tutor: Adding class information (logic will be in TutorService).");
    }

    public void viewStudentsEnrolledInMySubjects() {
        System.out.println("Tutor: Viewing students enrolled in my subjects (logic will be in TutorService).");
    }
}
