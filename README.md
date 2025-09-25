# Tuition Centre Management System

**Note:** This is a university group assignment.

This is a Java-based desktop application for managing a tuition centre. The system provides different functionalities for four types of users: Admin, Receptionist, Tutor, and Student.

## Features

### Admin
*   Register new tutors and receptionists.
*   Delete existing tutors and receptionists.
*   View all registered tutors and receptionists.
*   Update their own profile.
*   View monthly income reports.
*   Assign tutors to classes.

### Receptionist
*   Register new students.
*   Manage student enrollments in classes.
*   Accept student payments.
*   Delete existing students.
*   Update their own profile.
*   Manage student requests for subject changes.

### Tutor
*   View their assigned classes.
*   View students enrolled in their classes.
*   Manage their classes.
*   Update their own profile.

### Student
*   View their class schedule.
*   Submit requests to change subjects.
*   Delete their pending requests.
*   View the status of their requests.
*   View their payment status and total balance.
*   Update their own profile.

## Technologies Used

*   Java
*   Swing for the graphical user interface
*   Maven for project management

## How to Run

1.  Clone the repository.
2.  Open the project in your favorite Java IDE.
3.  Make sure you have Maven installed and configured.
4.  Build the project using Maven to download dependencies.
5.  Run the `Tuition_Centre_Management_System` class located in `src/main/java/com/mycompany/tuition_centre_management_system/`.

## File Structure

The project is organized into the following main packages:

*   `com.atu.atc.data`: Contains all the repository classes responsible for data access. Data is stored in text files in the `src/main/resources/data` directory.
*   `com.atu.atc.gui`: Contains the main `MainFrame` and all the UI panels for different user roles and functionalities.
*   `com.atu.atc.model`: Contains the model classes for all the entities in the system, such as `User`, `Student`, `Tutor`, etc.
*   `com.atu.atc.service`: Contains the service classes that implement the business logic of the application.
*   `com.atu.atc.util`: Contains utility classes for tasks like ID generation and input validation.

## User Roles and Functionalities

### Admin

The admin has the highest level of authority in the system. They are responsible for managing the staff (tutors and receptionists) and have access to financial reports.

### Receptionist

The receptionist is responsible for managing student-related tasks, such as registration, enrollment, and payments. They also handle student requests for changing subjects.

### Tutor

Tutors can manage their assigned classes and view the students enrolled in them. They can also update their own profile information.

### Student

Students can view their schedule, manage their enrollments, and make payments. They can also request to change subjects and track the status of their requests.