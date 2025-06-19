/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.data;

import com.atu.atc.model.Student;
import com.atu.atc.util.FileUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author farris
 */
public final class StudentRepository {

    private static final String FILE_PATH = "data/students.txt";
    private final List<Student> students;

    public StudentRepository() {
        this.students = new ArrayList<>();
        loadStudents();
    }
    public void loadStudents() {
        students.clear();
        List<String> lines = FileUtils.readAllLines(FILE_PATH);
        for (String line : lines) {
            try {
                String[] parts = line.split(",");
                if (parts.length != 8) if (parts.length == 9) {
                    String studentID = parts[0];
                    String studentName = parts[1];
                    String password = parts[2];
                    String role = parts[3];
                    String contactNumber = parts[4];
                    String email = parts[5];
                    String gender = parts[6];
                    String schoolEnrollmentStatus = parts[7];
                    String level = parts[8];

                    Student student = new Student(studentID, studentName, password, role,
                            contactNumber, email, gender, schoolEnrollmentStatus, level);
                    students.add(student);
                }
                else {
                    System.err.println("Skipping malformed student data line (wrong number of parts): " + line);
                } else {
                    String studentID = parts[0];
                    String studentName = parts[1];
                    String password = parts[2];
                    String contactNumber = parts[3];
                    String email = parts[4];
                    String gender = parts[5];
                    String schoolEnrollmentStatus = parts[6];
                    String level = parts[7];
                    
                    Student student = new Student(studentID, studentName, password, "Student",
                            contactNumber, email, gender, schoolEnrollmentStatus, level);
                    students.add(student);
                }
            } catch (Exception e) {
                System.err.println("Error parsing student data line: " + line + " - " + e.getMessage());
            }
        }
        System.out.println("Loaded " + students.size() + " students from " + FILE_PATH);
    }

    public void saveStudents() {
        List<String> lines = new ArrayList<>();
        for (Student student : students) {
            lines.add(student.toFileString());
        }
        FileUtils.writeAllLines(FILE_PATH, lines);
        System.out.println("Saved " + students.size() + " students to " + FILE_PATH);
    }

    public void addStudent(Student student) {
        if (getStudentById(student.getStudentID()).isPresent()) {
            System.out.println("Error: Student with ID " + student.getStudentID() + " already exists.");
            return;
        }
        this.students.add(student);
        saveStudents();
    }
    public void updateStudent(Student updatedStudent) {
        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentID().equals(updatedStudent.getStudentID())) {
                students.set(i, updatedStudent);
                found = true;
                break;
            }
        }
        if (found) {
            saveStudents();
            System.out.println("Student " + updatedStudent.getStudentID() + " updated successfully.");
        } else {
            System.out.println("Error: Student " + updatedStudent.getStudentID() + " not found for update.");
        }
    }

    public void deleteStudent(String studentId) {
        Iterator<Student> iterator = students.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Student student = iterator.next();
            if (student.getStudentID().equals(studentId)) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            saveStudents();
            System.out.println("Student " + studentId + " deleted successfully.");
        } else {
            System.out.println("Error: Student " + studentId + " not found for deletion.");
        }
    }

    public Optional<Student> getStudentById(String studentId) {
        return students.stream()
                       .filter(student -> student.getStudentID().equals(studentId))
                       .findFirst();
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students); // Return a copy to prevent external modification
    }
}
