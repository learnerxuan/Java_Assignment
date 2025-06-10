// src/main/java/com/atu/atc/model/Admin.java
// This class extends the abstract User class, demonstrating Inheritance.
// It provides specific functionalities for an Admin user.

package com.atu.atc.model; // This must match your package structure

import java.util.ArrayList; // Not directly used in this snippet but commonly needed
import java.util.List;    // Not directly used in this snippet but commonly needed

public class Admin extends User {
    // Admin-specific attributes can be added here if needed (e.g., an admin ID number).
    // For now, Admin doesn't have unique attributes beyond what User provides.

    // Constructor for Admin. It calls the User class's constructor using 'super()'.
    public Admin(String username, String password) {
        // "super" refers to the parent class (User). We pass the username, password,
        // and explicitly set the role to "Admin".
        super(username, password, "Admin");
    }

    // Implementation of the abstract login method from the User class.
    // This demonstrates Polymorphism, as different user types will have their
    // own login logic (though basic for now).
    @Override // It's good practice to use @Override annotation for clarity
    public boolean login(String enteredUsername, String enteredPassword) {
        // Basic login check: username and password must match.
        // In a real system, this would involve hashing passwords (see PasswordHasher in utils)
        // and checking against stored data from a repository.
        if (getUsername().equals(enteredUsername) && getPassword().equals(enteredPassword)) {
            System.out.println("Admin " + getUsername() + " logged in successfully.");
            return true;
        } else {
            System.out.println("Admin login failed. Invalid username or password.");
            return false;
        }
    }

    // --- Admin-specific functionalities based on the assignment ---

    // Example method: Register a new Tutor
    // (Note: This method would interact with a TutorRepository in a full system)
    public void registerTutor(Tutor tutor) {
        System.out.println("Admin " + getUsername() + " is registering new tutor: " + tutor.getUsername());
        // Placeholder for actual logic: Save this tutor object to a file via a repository.
    }

    // Example method: Delete a Tutor
    // (Note: This method would interact with a TutorRepository)
    public void deleteTutor(String tutorId) {
        System.out.println("Admin " + getUsername() + " is deleting tutor with ID: " + tutorId);
        // Placeholder for actual logic: Remove this tutor from your data storage.
    }

    // Example method: Register a new Receptionist
    // (Note: This method would interact with a ReceptionistRepository)
    public void registerReceptionist(Receptionist receptionist) {
        System.out.println("Admin " + getUsername() + " is registering new receptionist: " + receptionist.getUsername());
        // Placeholder for actual logic: Save receptionist to file.
    }

    // Example method: Delete a Receptionist
    // (Note: This method would interact with a ReceptionistRepository)
    public void deleteReceptionist(String receptionistId) {
        System.out.println("Admin " + getUsername() + " is deleting receptionist with ID: " + receptionistId);
        // Placeholder for actual logic: Remove receptionist from file.
    }

    // Example method: Assign a tutor to a subject/course
    // (Requires Course and Tutor classes to be fully defined and repositories to manage them)
    public void assignTutorToCourse(Tutor tutor, Course course) {
        System.out.println("Admin " + getUsername() + " is assigning tutor " + tutor.getUsername() + " to course " + course.getSubjectName() + " (Level: " + course.getLevel() + ")");
        // Placeholder for actual logic: Update tutor's assigned courses and course's assigned tutor in data storage.
    }

    // Example method: View monthly income report
    // (Requires Payment and Enrollment data from repositories)
    public void viewMonthlyIncomeReport(String level, String subject) {
        System.out.println("Admin " + getUsername() + " is viewing monthly income report for level: " + level + ", subject: " + subject);
        // Placeholder for actual logic: This would involve reading payment/enrollment data from files
        // and calculating the total income for the specified level and subject.
    }
}