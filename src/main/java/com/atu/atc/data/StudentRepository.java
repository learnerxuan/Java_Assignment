/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.data;

import com.atu.atc.model.Student;
import com.atu.atc.util.FileUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farris
 */
public class StudentRepository {
    private static final String FILE_PATH = "data/students.txt";
    private static final String HEADER = "student_id,student_name,password,phone_number,email,gender,IC/Passport,address,month_of_enroll,level";
    private List<Student> students = new ArrayList<>();

    public void load() {
        students.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);
        
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length == 10) {
                String student_id = parts[0].trim();
                String student_name = parts[1].trim();
                String password = parts[2].trim();
                String phone_number = parts[3].trim();
                String email = parts[4].trim();
                String gender = parts[5].trim();
                String icPassport = parts[6].trim();
                String address = parts[7].trim();
                String month_of_enroll = parts[8].trim();
                String level = parts[9].trim();

                Student s = new Student(student_id, student_name, password, phone_number, email, gender, icPassport, address, month_of_enroll, level);
                students.add(s);
            } else {
                System.err.println("Invalid.");
            }
        }
    }         

    public void save() {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Student s : students) {
            lines.add(s.toFileString());
        }
        FileUtils.writeLines(FILE_PATH, lines);
    }
    
        public void add(Student student) {
        students.add(student);
        save();
    }
        
    public List<Student> getAll() {
        return students;
    }    
        
    public List<Student> getByStudentId(String studentId) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getStudent_id().equals(studentId)) {
                result.add(s);
            }
        }
        return result;
    }
}