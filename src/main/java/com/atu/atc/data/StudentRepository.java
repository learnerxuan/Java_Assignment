package com.atu.atc.data;

import com.atu.atc.model.Student;
import com.atu.atc.util.FileUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author farris
 */

public class StudentRepository extends UserRepository<Student> {
    private static final String filePath = "data/students.txt";
    private static final String HEADER = "student_id,student_name,password,phone_number,email,gender,IC/Passport,address,month_of_enroll,level";
    
    @Override
    public void load() {
        users.clear();
        List<String> lines = FileUtils.readDataLines(filePath);
        
        // Skip header line if present
        boolean isFirstLine = true;
        for (String line : lines) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            
            String[] parts = line.split(",", -1);
            if (parts.length == 10) {
                String id = parts[0].trim();
                String name = parts[1].trim();
                String password = parts[2].trim();
                String phone = parts[3].trim();
                String email = parts[4].trim();
                String gender = parts[5].trim();
                String ic = parts[6].trim();
                String address = parts[7].trim();
                String monthOfEnroll = parts[8].trim();
                String level = parts[9].trim();
                Student student = new Student(id, name, password, phone, email, gender, ic, address, monthOfEnroll, level);
                users.add(student);
            } else {
                System.err.println("Invalid student data format: " + line);
            }
        }
    }

    
    @Override
    public void save() {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Student s : users) {
            lines.add(s.toFileString());
        }
        FileUtils.writeLines(filePath, lines);
    }
    
    public Optional<Student> getByStudentId(String studentId) {
        for (Student s : users) {
            if (s.getId().equals(studentId)) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
    
    public boolean updateStudent(Student updatedStudent) {
        if (updatedStudent == null) {
            return false;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(updatedStudent.getId())) {
                users.set(i, updatedStudent);
                return true;
            }
        }
        return false;
    }
}