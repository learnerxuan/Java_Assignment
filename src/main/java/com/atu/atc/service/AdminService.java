//// src/main/java/com/atu/atc/service/AdminService.java
//
//package com.atu.atc.service;
//
//import com.atu.atc.data.CourseRepository; // Will be needed for assigning tutors
//import com.atu.atc.data.EnrollmentRepository; // Will be needed for reports
//import com.atu.atc.data.PaymentRepository;    // Will be needed for reports
//import com.atu.atc.data.UserRepository; // Import UserRepository to manage users
//import com.atu.atc.model.Admin;
//import com.atu.atc.model.Course;
//import com.atu.atc.model.Receptionist;
//import com.atu.atc.model.Student; // Potentially needed for reports
//import com.atu.atc.model.Tutor;
//import com.atu.atc.model.User; // To return User objects (polymorphically)
//
//import java.util.List; // For lists of objects
//import java.time.Month; // For monthly reports (Java 8+ Date/Time API is good practice)
//
//// The AdminService class handles the business logic for Admin operations.
//// It interacts with the data layer (repositories) to perform tasks.
//public class AdminService {
//
//    private UserRepository userRepository; // Dependency: AdminService needs a UserRepository
//    // private TutorRepository tutorRepository;     // Will need specific repositories for tutors, courses, etc.
//    // private ReceptionistRepository receptionistRepository;
//    // private CourseRepository courseRepository;
//    // private PaymentRepository paymentRepository;
//    // private EnrollmentRepository enrollmentRepository;
//
//    // Constructor for AdminService. It receives a UserRepository instance.
//    // This is called "Dependency Injection" - passing required dependencies.
//    public AdminService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        // Initialize other repositories here once they are created
//        // this.tutorRepository = new TutorRepository();
//        // this.receptionistRepository = new ReceptionistRepository();
//        // this.courseRepository = new CourseRepository();
//        // this.paymentRepository = new PaymentRepository();
//        // this.enrollmentRepository = new EnrollmentRepository();
//    }
//
//    /**
//     * Registers a new Tutor in the system.
//     * This method performs the business logic and delegates saving to the repository.
//     *
//     * @param username The username for the new tutor.
//     * @param password The password for the new tutor.
//     * @return true if the tutor was registered successfully, false otherwise.
//     */
//    public boolean registerTutor(String username, String password) {
//        // Business rule: Check if a user with this username already exists.
//        if (userRepository.getUserByUsername(username) != null) {
//            System.err.println("Registration failed: Username '" + username + "' already exists.");
//            return false;
//        }
//        Tutor newTutor = new Tutor(username, password);
//        // Delegate saving to the UserRepository.
//        // In a full system, you might have a dedicated TutorRepository for tutor-specific data.
//        // For now, since Tutor extends User, UserRepository can handle it.
//        boolean success = userRepository.addUser(newTutor);
//        if (success) {
//            System.out.println("AdminService: Tutor " + username + " registered successfully.");
//        } else {
//            System.out.println("AdminService: Failed to register tutor " + username + " (data layer issue).");
//        }
//        return success;
//    }
//
//    /**
//     * Deletes a Tutor from the system.
//     *
//     * @param username The username of the tutor to delete.
//     * @return true if the tutor was deleted successfully, false otherwise.
//     */
//    public boolean deleteTutor(String username) {
//        // Business rule: Ensure the user exists and is actually a Tutor.
//        User userToDelete = userRepository.getUserByUsername(username);
//        if (userToDelete == null) {
//            System.err.println("Deletion failed: Tutor '" + username + "' not found.");
//            return false;
//        }
//        if (!(userToDelete instanceof Tutor)) {
//            System.err.println("Deletion failed: User '" + username + "' is not a Tutor.");
//            return false;
//        }
//        // Delegate deletion to the UserRepository.
//        boolean success = userRepository.deleteUser(username);
//        if (success) {
//            System.out.println("AdminService: Tutor " + username + " deleted successfully.");
//        } else {
//            System.out.println("AdminService: Failed to delete tutor " + username + " (data layer issue).");
//        }
//        return success;
//    }
//
//    /**
//     * Registers a new Receptionist in the system.
//     *
//     * @param username The username for the new receptionist.
//     * @param password The password for the new receptionist.
//     * @return true if the receptionist was registered successfully, false otherwise.
//     */
//    public boolean registerReceptionist(String username, String password) {
//        if (userRepository.getUserByUsername(username) != null) {
//            System.err.println("Registration failed: Username '" + username + "' already exists.");
//            return false;
//        }
//        Receptionist newReceptionist = new Receptionist(username, password);
//        boolean success = userRepository.addUser(newReceptionist);
//        if (success) {
//            System.out.println("AdminService: Receptionist " + username + " registered successfully.");
//        } else {
//            System.out.println("AdminService: Failed to register receptionist " + username + " (data layer issue).");
//        }
//        return success;
//    }
//
//    /**
//     * Deletes a Receptionist from the system.
//     *
//     * @param username The username of the receptionist to delete.
//     * @return true if the receptionist was deleted successfully, false otherwise.
//     */
//    public boolean deleteReceptionist(String username) {
//        User userToDelete = userRepository.getUserByUsername(username);
//        if (userToDelete == null) {
//            System.err.println("Deletion failed: Receptionist '" + username + "' not found.");
//            return false;
//        }
//        if (!(userToDelete instanceof Receptionist)) {
//            System.err.println("Deletion failed: User '" + username + "' is not a Receptionist.");
//            return false;
//        }
//        boolean success = userRepository.deleteUser(username);
//        if (success) {
//            System.out.println("AdminService: Receptionist " + username + " deleted successfully.");
//        } else {
//            System.out.println("AdminService: Failed to delete receptionist " + username + " (data layer issue).");
//        }
//        return success;
//    }
//
//    /**
//     * Assigns a tutor to a specific course.
//     * (This method will need CourseRepository and actual Course/Tutor objects fully implemented later)
//     *
//     * @param tutorUsername The username of the tutor to assign.
//     * @param courseId The ID of the course to assign.
//     * @return true if assignment is successful, false otherwise.
//     */
//    public boolean assignTutorToCourse(String tutorUsername, String courseId) {
//        // For full implementation, you'd load Tutor and Course objects from their specific repositories.
//        // Tutor tutor = tutorRepository.getTutorByUsername(tutorUsername);
//        // Course course = courseRepository.getCourseById(courseId);
//
//        // Placeholder logic for now:
//        System.out.println("AdminService: Attempting to assign tutor '" + tutorUsername + "' to course '" + courseId + "'.");
//        // In a real scenario, you'd add the course to the tutor's assigned courses
//        // and set the tutor on the course object, then save both.
//        // Example:
//        // if (tutor != null && course != null) {
//        //     tutor.addAssignedCourse(course);
//        //     course.setAssignedTutor(tutor);
//        //     tutorRepository.updateTutor(tutor);
//        //     courseRepository.updateCourse(course);
//        //     return true;
//        // }
//        return false; // Return false until fully implemented
//    }
//
//    /**
//     * Views the monthly income report based on level and subject.
//     * (This method will need PaymentRepository, EnrollmentRepository, and full Payment/Enrollment models)
//     *
//     * @param level The educational level (e.g., "Form 4").
//     * @param subject The subject name (e.g., "Mathematics").
//     * @return A formatted String representing the report, or an empty string if no data.
//     */
//    public String viewMonthlyIncomeReport(String level, String subject) {
//        // Placeholder logic for now:
//        System.out.println("AdminService: Generating monthly income report for Level: " + level + ", Subject: " + subject);
//        // In a real scenario:
//        // List<Payment> allPayments = paymentRepository.loadPayments();
//        // List<Enrollment> allEnrollments = enrollmentRepository.loadEnrollments();
//        // Filter payments/enrollments by month, level, subject, and sum up.
//        // Construct a detailed report string.
//
//        return "Report for " + level + " " + subject + ": (Data not yet calculated)"; // Return placeholder
//    }
//
//    /**
//     * Updates the profile of a given Admin user.
//     *
//     * @param updatedAdmin The Admin object with the new profile details.
//     * @return true if the profile was updated successfully, false otherwise.
//     */
//    public boolean updateAdminProfile(Admin updatedAdmin) {
//        // Delegate updating to the UserRepository
//        boolean success = userRepository.updateUser(updatedAdmin);
//        if (success) {
//            System.out.println("AdminService: Admin profile for " + updatedAdmin.getUsername() + " updated successfully.");
//        } else {
//            System.out.println("AdminService: Failed to update admin profile for " + updatedAdmin.getUsername() + ".");
//        }
//        return success;
//    }
//}