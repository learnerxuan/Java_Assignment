package com.atu.atc.model;

public class Tutor extends User {
    // Additional unique attributes for Tutor: subject and level
    // This implies a tutor is primarily associated with one subject and one level.
    // If a tutor can teach multiple, a List<Subject> or List<Classes> would be more appropriate.
    // However, adhering to the requested structure of simple String attributes.
    private String subject;
    private String level;

    /**
     * Constructor for the Tutor class, now including subject and level.
     * Matches the structure provided for Admin.java, plus the new fields.
     *
     * @param userId The unique ID of the tutor.
     * @param password The password for login.
     * @param fullName The full name of the tutor.
     * @param phoneNumber The tutor's phone number.
     * @param email The tutor's email address.
     * @param gender The tutor's gender.
     * @param subject The primary subject the tutor teaches.
     * @param level The primary academic level the tutor teaches.
     */
    public Tutor(String userId, String password, String fullName, String phoneNumber, String email, String gender, String subject, String level) {
        // Calls the constructor of the superclass (User)
        super(userId, password, "Tutor", fullName, phoneNumber, email, gender);
        this.subject = subject;
        this.level = level;
    }

    /**
     * Implements the login logic specific to a Tutor.
     * This method utilizes the userId and password inherited from the User class.
     *
     * @param enteredId The ID entered by the user.
     * @param enteredPassword The password entered by the user.
     * @return true if login is successful, false otherwise.
     */
    @Override
    public boolean login(String enteredId, String enteredPassword) {
        // Corrected getId() to getUserId()
        if (getId().equals(enteredId) && getPassword().equals(enteredPassword)) {
            System.out.println("Tutor " + getId() + " logged in successfully.");
            return true;
        } else {
            System.out.println("Tutor login failed. Invalid ID or password.");
            return false;
        }
    }

    /**
     * Overrides the updateProfile method from User to also update Tutor-specific fields.
     *
     * @param newId The new user ID.
     * @param newPassword The new password.
     * @param newFullName The new full name.
     * @param newPhoneNumber The new phone number.
     * @param newEmail The new email address.
     * @param newGender The new gender.
     */
    @Override
    public void updateProfile(String newId, String newPassword, String newFullName, String newPhoneNumber, String newEmail, String newGender) {
        // Call the superclass method to update common user details
        super.updateProfile(newId, newPassword, newFullName, newPhoneNumber, newEmail, newGender);
        // Add logic here if you want to update subject/level through this method,
        // or create specific setters for them.
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

    /**
     * Generates a comma-separated string representation of the Tutor object
     * suitable for writing to a text file.
     * Format: userId,password,fullName,phoneNumber,email,gender,subject,level
     *
     * @return A comma-delimited string of the tutor's core data.
     */
    public String toFileString() {
        // Corrected getId() to getUserId()
        return getId() + "," +
               getPassword() + "," +
               getFullName() + "," +
               getPhoneNumber() + "," +
               getEmail() + "," +
               getGender() + "," +
               subject + "," +
               level;
    }

    /**
     * Provides a human-readable string representation of the Tutor object for debugging.
     *
     * @return A formatted string showing tutor details.
     */
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
