// src/main/java/com/atu/atc/data/UserRepository.java

package com.atu.atc.data;

import com.atu.atc.model.Admin;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Student;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.util.FileUtils; // Import our new FileUtils

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // For stream operations later

// This class manages the persistence of User objects (Admin, Receptionist, Tutor, Student)
// to and from a text file (users.txt). It uses FileUtils for basic file operations.
public class UserRepository {

    private static final String FILE_PATH = "data/users.txt"; // Path to your users.txt file

    /**
     * Loads all user data from the users.txt file and converts them into User objects.
     * This method handles the deserialization (String to Object).
     *
     * @return A List of User objects representing all users in the system.
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        List<String> lines = FileUtils.readLines(FILE_PATH); // Use FileUtils to read lines

        for (String line : lines) {
            // Assuming a simple CSV format: username,password,role,other_data...
            String[] parts = line.split(","); // Split by comma
            if (parts.length >= 3) {
                String username = parts[0].trim();
                String password = parts[1].trim();
                String role = parts[2].trim();

                // This is where Polymorphism comes into play during object creation.
                // We instantiate the correct subclass based on the 'role' field.
                switch (role) {
                    case "Admin":
                        users.add(new Admin(username, password));
                        break;
                    case "Receptionist":
                        users.add(new Receptionist(username, password));
                        break;
                    case "Tutor":
                        users.add(new Tutor(username, password));
                        break;
                    case "Student":
                        // For student, we need more parts.
                        // This part will need to be expanded when Student has more attributes.
                        // For now, let's assume minimum to avoid errors.
                        String icPassport = parts.length > 3 ? parts[3].trim() : "";
                        String email = parts.length > 4 ? parts[4].trim() : "";
                        String contactNumber = parts.length > 5 ? parts[5].trim() : "";
                        String address = parts.length > 6 ? parts[6].trim() : "";
                        String level = parts.length > 7 ? parts[7].trim() : "";
                        users.add(new Student(username, password, icPassport, email, contactNumber, address, level));
                        break;
                    default:
                        System.err.println("Unknown user role encountered: " + role);
                        break;
                }
            }
        }
        return users;
    }

    /**
     * Saves a list of User objects to the users.txt file.
     * This method handles the serialization (Object to String).
     *
     * @param users The List of User objects to be saved.
     * @return true if the save operation was successful, false otherwise.
     */
    public boolean saveUsers(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            // Convert each User object back into a string format for saving.
            // This is a simple CSV-like format.
            String userData = user.getUsername() + "," + user.getPassword() + "," + user.getRole();
            // If student, append its specific attributes
            if (user instanceof Student) {
                Student student = (Student) user; // Downcasting to access Student-specific getters
                userData += "," + student.getIcPassport() + "," + student.getEmail() + "," +
                            student.getContactNumber() + "," + student.getAddress() + "," + student.getLevel();
            }
            lines.add(userData);
        }
        return FileUtils.writeLines(FILE_PATH, lines); // Use FileUtils to write lines
    }

    /**
     * Retrieves a User object by its username.
     *
     * @param username The username of the user to find.
     * @return The User object if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        List<User> users = loadUsers(); // Load all users to find one
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    /**
     * Adds a new user to the system (and saves to file).
     *
     * @param newUser The User object to add.
     * @return true if added successfully, false otherwise (e.g., username already exists).
     */
    public boolean addUser(User newUser) {
        List<User> users = loadUsers();
        // Check if username already exists
        if (users.stream().anyMatch(user -> user.getUsername().equals(newUser.getUsername()))) {
            System.err.println("Error: Username '" + newUser.getUsername() + "' already exists.");
            return false;
        }
        users.add(newUser);
        return saveUsers(users);
    }

    /**
     * Updates an existing user's information (and saves to file).
     *
     * @param updatedUser The User object with updated information.
     * @return true if updated successfully, false if the user was not found.
     */
    public boolean updateUser(User updatedUser) {
        List<User> users = loadUsers();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser); // Replace the old user object with the updated one
                found = true;
                break;
            }
        }
        if (found) {
            return saveUsers(users);
        } else {
            System.err.println("Error: User '" + updatedUser.getUsername() + "' not found for update.");
            return false;
        }
    }

    /**
     * Deletes a user from the system (and saves to file).
     *
     * @param username The username of the user to delete.
     * @return true if deleted successfully, false if the user was not found.
     */
    public boolean deleteUser(String username) {
        List<User> users = loadUsers();
        boolean removed = users.removeIf(user -> user.getUsername().equals(username));
        if (removed) {
            return saveUsers(users);
        } else {
            System.err.println("Error: User '" + username + "' not found for deletion.");
            return false;
        }
    }
}