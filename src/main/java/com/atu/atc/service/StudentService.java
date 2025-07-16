package com.atu.atc.service;

import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.RequestRepository;
import com.atu.atc.data.EnrollmentRepository;
import com.atu.atc.data.PaymentRepository;
import com.atu.atc.data.ClassesRepository;

import com.atu.atc.model.Student;
import com.atu.atc.model.Request;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Payment;
import com.atu.atc.model.Classes;

import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final RequestRepository requestRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final ClassesRepository classesRepository;
    
    private final IDGenerator idGenerator;
    private final Validator validator;
    
    public StudentService(StudentRepository studentRepository,
                          SubjectRepository subjectRepository,
                          RequestRepository requestRepository,
                          EnrollmentRepository enrollmentRepository,
                          PaymentRepository paymentRepository,
                          ClassesRepository classesRepository,
                          IDGenerator idGenerator,
                          Validator validator) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.requestRepository = requestRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentRepository = paymentRepository;
        this.classesRepository = classesRepository;
        
        this.idGenerator = idGenerator;
        this.validator = validator;
    }
    
    // [ADDITION] Getters for repositories used by GUI panels
    public SubjectRepository getSubjectRepository() {
        return subjectRepository;
    }
    
    public ClassesRepository getClassesRepository() {
        return classesRepository;
    }
    
    public Student registerStudent(String fullName, String password, String phoneNumber, String email,
                                   String gender, String icPassport, String address, String monthOfEnroll,
                                   String level) {
        String newStudentId = idGenerator.generateStudentId();
        
        if (!Validator.isValidEmail(email) || !Validator.isValidPhoneNumber(phoneNumber)) {
            System.err.println("StudentService: Invalid email or phone number format.");
            return null;
        }
        
        Student newStudent = new Student(newStudentId, password, "Student", fullName,
                phoneNumber, email, gender, icPassport,
                address, monthOfEnroll, level);
        studentRepository.add(newStudent);
        System.out.println("StudentService: Student " + newStudentId + " registered.");
        return newStudent;
    }
    
    public boolean updateStudentProfile(Student updatedStudent) {
        if (updatedStudent == null || !Validator.isValidEmail(updatedStudent.getEmail()) ||
                !Validator.isValidPhoneNumber(updatedStudent.getPhoneNumber())) {
            System.err.println("StudentService: Invalid student data for update.");
            return false;
        }
        boolean success = studentRepository.update(updatedStudent);
        if (success) {
            System.out.println("StudentService: Profile for student " + updatedStudent.getId() + " updated.");
        } else {
            System.err.println("StudentService: Failed to update profile for student " + updatedStudent.getId());
        }
        return success;
    }
    
    public boolean submitSubjectChangeRequest(String studentId, String currentSubjectId,
                                              String requestedSubjectId, String status) {
        String requestId = idGenerator.generateRequestId();
        
        Request newRequest = new Request(requestId, studentId, currentSubjectId,
                requestedSubjectId, status, LocalDate.now());
        requestRepository.add(newRequest);
        System.out.println("StudentService: Subject change request " + requestId + " submitted for student " + studentId);
        return true;
    }
    
    public Optional<Subject> getSubjectDetails(String subjectId) {
        return subjectRepository.getSubjectById(subjectId);
    }
    
    public List<Request> getStudentRequests(String studentId) {
        return requestRepository.getByStudentId(studentId);
    }
    
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.getByStudentId(studentId);
    }
    
    public List<String[]> getStudentClassSchedule(String studentId) {
        List<Enrollment> studentEnrollments = enrollmentRepository.getByStudentId(studentId);
        List<String[]> scheduleDetails = new ArrayList<>();
        
        scheduleDetails.add(new String[]{"Class ID", "Subject Name", "Tutor ID", "Day", "Start Time", "End Time"});
        
        for (Enrollment enrollment : studentEnrollments) {
            Optional<Classes> classOpt = classesRepository.getById(enrollment.getClassId());
            if (classOpt.isPresent()) {
                Classes cls = classOpt.get();
                Optional<Subject> subjectOpt = subjectRepository.getSubjectById(cls.getSubjectId());
                String subjectName = subjectOpt.isPresent() ? subjectOpt.get().getName() : "N/A";
                
                scheduleDetails.add(new String[]{
                        cls.getClassId(),
                        subjectName,
                        cls.getTutorId(),
                        cls.getDay(),
                        cls.getStartTime(),
                        cls.getEndTime()
                });
            } else {
                System.err.println("StudentService (Enrollment): Class with ID " + enrollment.getClassId() + " not found for enrollment " + enrollment.getCourseEnrollmentId());
            }
        }
        return scheduleDetails;
    }
    
    public boolean addEnrollment(Enrollment enrollment) {
        if (enrollmentRepository.add(enrollment)) {
            System.out.println("StudentService (Enrollment): Enrollment added successfully for student " + enrollment.getStudentId() + " in class " + enrollment.getClassId());
            return true;
        }
        System.err.println("StudentService (Enrollment): Failed to add enrollment.");
        return false;
    }
    
    public List<Payment> getPaymentsByStudentId(String studentId) {
        return paymentRepository.getByStudentId(studentId);
    }
    
    public void recordPayment(Payment payment) {
        paymentRepository.add(payment);
        System.out.println("StudentService (Payment): Payment recorded for student " + payment.getStudentId() + " amount: " + payment.getAmount());
    }
}