package com.atu.atc.service;

import com.atu.atc.model.*;
import com.atu.atc.data.*;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {
    private final String filePath = "data/students.txt";
    
    public void add(Student student) {
        List<Student> students = getAll();
        students.add(student);
        saveAll(students);
    }
    
    public List<Student> getAll() {
        List<String> lines = FileUtils.readDataLines(filePath);
        List<Student> students = new ArrayList<>();
        for (String line : lines) {
            Student student = Student.fromFileString(line);
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }
    
    public Student getById(String studentId) {
        return getAll().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
    
    public boolean delete(String studentId) {
        List<Student> students = getAll();
        boolean removed = students.removeIf(s -> s.getId().equals(studentId));
        saveAll(students);
        return removed;
    }
    
    public void update(Student updatedStudent) {
        List<Student> students = getAll().stream()
                .map(s -> s.getId().equals(updatedStudent.getId()) ? updatedStudent : s)
                .collect(Collectors.toList());
        saveAll(students);
    }
    
    public void saveAll(List<Student> students) {
        List<String> lines = students.stream()
                .map(Student::toFileString)
                .collect(Collectors.toList());
        FileUtils.writeLines(filePath, lines);
    }
    
    public Optional<Student> findById(String id) {
        return getAll().stream().filter(s -> s.getId().equals(id)).findFirst();
    }
}
