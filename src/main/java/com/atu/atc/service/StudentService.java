package com.atu.atc.service;

import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.RequestRepository;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Request;
import java.util.Optional;
import java.time.LocalDate;

public class StudentService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final RequestRepository requestRepository;
    
    public StudentService(StudentRepository studentRepository,
                          SubjectRepository subjectRepository,
                          RequestRepository requestRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.requestRepository = requestRepository;
    }
    
    /**
     * Retrieves a student by their ID.
     * @param studentId The ID of the student.
     * @return An Optional containing the Student if found, or empty if not.
     */
    public Optional<Student> getStudentDetails(String studentId) {
        return studentRepository.getByStudentId(studentId);
    }
    
    public Optional<Subject> getSubjectDetails(String subjectId) {
        return subjectRepository.getSubjectById(subjectId);
    }
    

    public boolean submitSubjectChangeRequest(String studentId, String currentSubjectId, String requestedSubjectId, String status) {
        // First, validate if student and subjects exist
        Optional<Student> studentOpt = studentRepository.getByStudentId(studentId);
        Optional<Subject> currentSubjectOpt = subjectRepository.getSubjectById(currentSubjectId);
        Optional<Subject> requestedSubjectOpt = subjectRepository.getSubjectById(requestedSubjectId);
        
        if (!studentOpt.isPresent()) {
            System.err.println("StudentService: Student with ID " + studentId + " not found.");
            return false;
        }
        if (!currentSubjectOpt.isPresent()) {
            System.err.println("StudentService: Current Subject with ID " + currentSubjectId + " not found.");
            return false;
        }
        if (!requestedSubjectOpt.isPresent()) {
            System.err.println("StudentService: Requested Subject with ID " + requestedSubjectId + " not found.");
            return false;
        }
        
        // Generate a simple request ID
        String newRequestId = "REQ-" + System.currentTimeMillis();
        
        // Create the Request object
        Request newRequest = new Request(newRequestId, studentId, currentSubjectId, requestedSubjectId, status);
        
        // Add the request using the RequestRepository
        requestRepository.add(newRequest);
        System.out.println("StudentService: Subject change request " + newRequestId + " submitted successfully for " + studentId);
        return true;
    }

}