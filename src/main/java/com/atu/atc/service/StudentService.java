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
    
    // Repositories for access by panels like ViewSchedulePanel
    private final EnrollmentRepository enrollmentRepository;
    private final ClassesRepository classesRepository;
    private final SubjectRepository subjectRepository;
    private final TutorRepository tutorRepository;
    
    public StudentService(EnrollmentRepository enrollmentRepository,
                          ClassesRepository classesRepository,
                          SubjectRepository subjectRepository,
                          TutorRepository tutorRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.classesRepository = classesRepository;
        this.subjectRepository = subjectRepository;
        this.tutorRepository = tutorRepository;
    }
    
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
    
    public List<Request> getRequestsByStudentId(String studentId) {
        String requestFilePath = "data/requests.txt";
        List<String> lines = FileUtils.readDataLines(requestFilePath);
        List<Request> requests = new ArrayList<>();
        
        for (String line : lines) {
            Request request = Request.fromFileString(line);
            if (request != null && request.getStudentId().equals(studentId)) {
                requests.add(request);
            }
        }
        
        return requests;
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
    
    // 👇 Add getters for repositories so the panel can access them
    
    public EnrollmentRepository getEnrollmentRepository() {
        return enrollmentRepository;
    }
    
    public ClassesRepository getClassesRepository() {
        return classesRepository;
    }
    
    public SubjectRepository getSubjectRepository() {
        return subjectRepository;
    }
    
    public TutorRepository getTutorRepository() {
        return tutorRepository;
    }
}
