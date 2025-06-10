// src/main/java/com/atu/atc/data/TestRepository.java
package com.atu.atc.data;

import com.atu.atc.model.Admin;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Student;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;

import java.util.List;

public class TestRepository {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        System.out.println("--- 1. Adding initial users ---");
        // Add an Admin
        User admin = new Admin("admin1", "adminpass");
        userRepository.addUser(admin);

        // Add a Receptionist
        User receptionist = new Receptionist("receptionist1", "recpass");
        userRepository.addUser(receptionist);

        // Add a Tutor
        User tutor = new Tutor("tutor1", "tutorpass");
        userRepository.addUser(tutor);

        // Add a Student (with more detailed info as per Student class)
        Student student = new Student("student1", "studpass", "990101-14-1234", "student1@example.com", "0123456789", "123 Main St", "Form 4");
        userRepository.addUser(student);

        System.out.println("\n--- 2. Loading all users ---");
        List<User> loadedUsers = userRepository.loadUsers();
        for (User u : loadedUsers) {
            System.out.println("Loaded: " + u.getUsername() + " | Role: " + u.getRole() + " | Password: " + u.getPassword());
            if (u instanceof Student) {
                Student s = (Student) u;
                System.out.println("  (Student) IC: " + s.getIcPassport() + ", Email: " + s.getEmail());
            }
        }

        System.out.println("\n--- 3. Testing getUserbyUsername ---");
        User foundAdmin = userRepository.getUserByUsername("admin1");
        if (foundAdmin != null) {
            System.out.println("Found Admin: " + foundAdmin.getUsername());
        } else {
            System.out.println("Admin not found.");
        }

        User notFoundUser = userRepository.getUserByUsername("nonexistent");
        if (notFoundUser == null) {
            System.out.println("Nonexistent user correctly not found.");
        }

        System.out.println("\n--- 4. Updating a user ---");
        // Change admin's password
        if (foundAdmin != null) {
            foundAdmin.setPassword("newadminpass");
            boolean updated = userRepository.updateUser(foundAdmin);
            System.out.println("Admin password updated: " + updated);
        }

        System.out.println("\n--- 5. Loading again to confirm update ---");
        loadedUsers = userRepository.loadUsers();
        for (User u : loadedUsers) {
            System.out.println("Loaded (after update): " + u.getUsername() + " | Role: " + u.getRole() + " | Password: " + u.getPassword());
        }

        System.out.println("\n--- 6. Deleting a user ---");
        boolean deleted = userRepository.deleteUser("receptionist1");
        System.out.println("Receptionist deleted: " + deleted);

        System.out.println("\n--- 7. Loading again to confirm deletion ---");
        loadedUsers = userRepository.loadUsers();
        for (User u : loadedUsers) {
            System.out.println("Loaded (after deletion): " + u.getUsername() + " | Role: " + u.getRole());
        }

        // You should now see a 'users.txt' file created in your project's 'data' folder!
    }
}