package com.atu.atc.service;

import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.ReceptionistRepository;
import com.atu.atc.data.EnrollmentRepository;
import com.atu.atc.data.PaymentRepository;
import com.atu.atc.data.ClassesRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.RequestRepository;
import com.atu.atc.model.Student;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Payment;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Request;
import com.atu.atc.model.Tutor;
import com.atu.atc.data.TutorRepository;
import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * @author henge
 */
public class ReceptionistService {
    private final StudentRepository studentRepo;
    private final ReceptionistRepository receptionistRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final PaymentRepository paymentRepo;
    private final ClassesRepository classesRepo;
    private final SubjectRepository subjectRepo;
    private final RequestRepository requestRepo;
    private final TutorRepository tutorRepo;

    public ReceptionistService(StudentRepository studentRepo, ReceptionistRepository receptionistRepo,
                               EnrollmentRepository enrollmentRepo, PaymentRepository paymentRepo,
                               ClassesRepository classesRepo, SubjectRepository subjectRepo, RequestRepository requestRepo,
                               TutorRepository tutorRepo){
        this.studentRepo = studentRepo;
        this.receptionistRepo = receptionistRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.classesRepo = classesRepo;
        this.subjectRepo = subjectRepo;
        this.requestRepo = requestRepo;
        this.tutorRepo = tutorRepo;
    }

    // Register a new student
    public void registerStudent(String name, String password, String phone, String email, String gender,
                                String icPassport, String address, String level, List<String> classIds){
        if (!Validator.isValidEmail(email)){
            System.err.println("Invalid email format.");
            return;
        }

        if (!Validator.isValidPhoneNumber(phone)) {
            System.err.println("Invalid phone number.");
            return;
        }
        if (!Validator.isFormValid(level)) {
            System.err.println("Invalid level. Must be between 1 and 5.");
            return;
        }

        String studentId = IDGenerator.generateUniqueId("S");
        String monthOfEnroll = LocalDate.now().toString().substring(0, 7);

        Student student = new Student(studentId, name, password, phone, email, gender, icPassport, address, monthOfEnroll, level);
        studentRepo.add(student);
        System.out.println("Student registered: " + studentId);

        // Enroll the student up to three subjects
        enrollStudentInSubjects(studentId, classIds);
    }

    // Enroll student into 1-3 subjects using class id DURING REGISTRATION ONLY
    public void enrollStudentInSubjects(String studentId, List<String> classIds){
        if (classIds.size() > 3){
            System.out.println("Cannot enroll in more than 3 subjects.");
            return;
        }

        Student student = studentRepo.getById(studentId);
        if (student == null){
            System.out.println("Student not found.");
            return;
        }

        String studentLevel = student.getLevel();

        // Count for successful enrollment to a class
        int successEnrollCount = 0;

        for (String classId : classIds){
            Classes cls = classesRepo.getById(classId).orElse(null);

            if (cls == null){
                System.out.println("Class " + classId + " not found.");
                continue;
            }

            String subjectId = cls.getSubjectId();
            var subject = subjectRepo.getSubjectById(subjectId).orElse(null);

            if (subject == null){
                System.out.println("Subject " + subjectId + " not found");
                continue;
            }

            if (!subject.getLevel().equalsIgnoreCase(studentLevel)){
                System.out.println("Level mismatch for class " + classId + ". Student level: " + studentLevel + ", subject level: " + subject.getLevel());
                continue;
}

            String enrollmentId = IDGenerator.generateUniqueId("CE");
            Enrollment enrollment = new Enrollment(enrollmentId, classId, studentId);
            enrollmentRepo.add(enrollment);
            successEnrollCount++;
        }

        System.out.println("Student " + studentId + " enrolled in " + successEnrollCount + " class(es).");
    }

    public List<Classes> getAllClasses(){
        return classesRepo.getAll();
    }

    // Enroll student in new subject (check enrollment < 3)
    public void addSubjectEnrollment(String studentId, String newClassId){
        List<Enrollment> current = enrollmentRepo.getByStudentId(studentId);

        if (current.size() >= 3){
            System.out.print("Student already enrolled in 3 subjects. Cannot add more.");
            return;
        }

        // Check if already enrolled in this class
        boolean alreadyEnrolled = current.stream().anyMatch(e -> e.getClassId().equals(newClassId));

        if (alreadyEnrolled){
            System.out.println("Student already enrolled in this subject.");
            return;
        }

        // Get student
        Student student = studentRepo.getById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        // Get class
        Classes cls = classesRepo.getById(newClassId).orElse(null);
        if (cls == null) {
            System.out.println("Class " + newClassId + " not found.");
            return;
        }

        // Get subject from class
        String subjectId = cls.getSubjectId();
        var subject = subjectRepo.getSubjectById(subjectId).orElse(null);
        if (subject == null) {
            System.out.println("Subject " + subjectId + " not found.");
            return;
        }

        // Check level match
        if (!subject.getLevel().equalsIgnoreCase(student.getLevel())) {
            System.out.println("Cannot enroll. Class level " + subject.getLevel() +
                    " does not match student level " + student.getLevel());
            return;
        }

        String newEnrollmentId = IDGenerator.generateUniqueId("CE");
        Enrollment newEnrollment = new Enrollment(newEnrollmentId, newClassId, studentId);
        enrollmentRepo.add(newEnrollment);
        System.out.println("Subject added to enrollment.");
    }

    public List<Classes> getAvailableClassesMatchingStudentLevel(String studentId) {
        Student student = studentRepo.getById(studentId);
        if (student == null) return Collections.emptyList();

        String studentLevel = student.getLevel();

        return classesRepo.getAll().stream()
                .filter(cls -> {
                    Subject subject = subjectRepo.getSubjectById(cls.getSubjectId()).orElse(null);
                    return subject != null && subject.getLevel().equals(studentLevel);
                })
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        return enrollmentRepo.getByStudentId(studentId);
    }

    public Optional<Classes> getClassById(String classId){
        return classesRepo.getById(classId);
    }

    public Optional<Subject> getSubjectById(String subjectId){
        return subjectRepo.getSubjectById(subjectId);
    }

    public Optional<Tutor> getTutorById(String tutorId){
        return Optional.ofNullable(tutorRepo.getById(tutorId));
    }

    // Withdraw from one subject
    public void withdrawSubject(String studentId, String classIdToRemove){
        List<Enrollment> current = enrollmentRepo.getByStudentId(studentId);

        for (Enrollment e : current){
            if (e.getClassId().equals(classIdToRemove)){
                enrollmentRepo.deleteById(e.getCourseEnrollmentId());
                System.out.println(("Subject withdrawn."));
                return;
            }
        }
        System.out.println("Subject not found in student's enrollment.");
    }

    public List<String> getEnrolledClassIds(String studentId){
        return enrollmentRepo.getByStudentId(studentId).stream().map(e -> e.getClassId()).collect(Collectors.toList());
    }

    // Accept a payment from student
    public String acceptPayment(String studentId, double amount, String method, String receptionistId){
        String paymentId = IDGenerator.generateUniqueId("P");
        LocalDate today = LocalDate.now();
        Payment payment = new Payment(paymentId, studentId, amount, today, method, "Paid", receptionistId);
        paymentRepo.add(payment);
        return generateReceipt(payment);
    }

    // Get all payments
    public List<Payment> getAllPayments(){
        return paymentRepo.getAll();
    }

    // Generate receipt
    private String generateReceipt(Payment payment) {
        StringBuilder sb = new StringBuilder();
        sb.append("        *** PAYMENT RECEIPT ***\n\n");
        sb.append(String.format(" %-20s: %s\n", "Payment ID", payment.getPaymentId()));
        sb.append(String.format(" %-20s: %s\n", "Student ID", payment.getStudentId()));
        sb.append(String.format(" %-20s: %s\n", "Date", payment.getDate()));
        sb.append(String.format(" %-20s: %s\n", "Payment Method", payment.getPaymentMethod()));
        sb.append(String.format(" %-20s: %s\n", "Status", payment.getStatus()));
        sb.append(String.format(" %-20s: %s\n", "Receptionist ID", payment.getReceptionistId()));
        sb.append("\n ----------------------------------------\n");
        sb.append(String.format(" %-20s: RM %.2f\n", "AMOUNT PAID", payment.getAmount()));
        sb.append(" ----------------------------------------\n");
        sb.append("\n              Thank you!\n");
        return sb.toString();
    }

    // Delete a student who have completed their studies
    public void deleteStudent(String studentId){
        boolean removed = studentRepo.delete(studentId);
        if (removed){
            List<Enrollment> enrollments = enrollmentRepo.getByStudentId((studentId));
            for (Enrollment e : enrollments){
                enrollmentRepo.deleteById(e.getCourseEnrollmentId());
            }
            System.out.println("Student " + studentId + " and enrollments removed.");
        }else{
            System.err.println("Student not found.");
        }
    }

    public List<Student> getAllStudents(){
        return studentRepo.getAll();
    }

    // Update receptionist's own profile
    public void updateProfile(Receptionist receptionist, String newPassword, String newFullName, String newPhoneNumber, String newEmail, String newGender){
        if (!Validator.isValidEmail(newEmail)) {
            System.err.println("Invalid email.");
            return;
        }

        if (!Validator.isValidPhoneNumber(newPhoneNumber)) {
            System.err.println("Invalid phone number.");
            return;
        }

        receptionist.updateProfile(receptionist.getId(), newPassword, newFullName, newPhoneNumber, newEmail, newGender);
        receptionistRepo.update(receptionist);
        System.out.println("Receptionist profile updated.");
    }

    // Get all pending requests
    public List<Request> getPendingRequests(){
        return requestRepo.getAll().stream().filter(r -> r.getStatus().equalsIgnoreCase("Pending")).toList();
    }

    // Approve request and update enrollment
    public String approveRequest(String requestId){
        Optional<Request> optRequest = requestRepo.getRequestById(requestId);
        if (optRequest.isEmpty()){
            return "Request not found.";
        }

        Request request = optRequest.get();

        if (!request.getStatus().equalsIgnoreCase("Pending")) {
            return "Request is not pending.";
        }

        // Get student and level
        Student student = studentRepo.getById(request.getStudentId());
        if (student == null) {
            return "Student not found.";
        }

        String studentLevel = student.getLevel();
        String currentSubjectId = request.getCurrentSubjectId();
        String requestedSubjectId = request.getRequestedSubjectId();

        // Check if already enrolled
        List<Enrollment> enrollments = enrollmentRepo.getByStudentId(student.getId());
        for (Enrollment e : enrollments) {
            String enrolledSubjectId = getSubjectIdFromClass(e.getClassId());
            if (enrolledSubjectId != null && enrolledSubjectId.equals(requestedSubjectId)) {
                return "Student is already enrolled in the requested subject.";
            }
        }

        // Find a matching class
        String newClassId = getClassIdForSubject(requestedSubjectId, studentLevel);
        if (newClassId == null) {
            return "No suitable class found for subject " + requestedSubjectId + " and level " + studentLevel;
        }

        boolean updated = false;
        for (Enrollment e : enrollments) {
            String enrolledSubjectId = getSubjectIdFromClass(e.getClassId());
            if (enrolledSubjectId != null && enrolledSubjectId.equals(currentSubjectId)) {
                e.setClassId(newClassId);
                enrollmentRepo.update(e);
                updated = true;
                break;
            }
        }

        if (updated) {
            request.setStatus("Approved");
            requestRepo.update(request);
            return "Request approved and enrollment updated.";
        } else {
            return "Failed to find matching current subject to update enrollment.";
        }
    }

    // Reject request
    public String rejectRequest(String requestId){
        Optional<Request> optRequest = requestRepo.getRequestById(requestId);
        if(optRequest.isPresent()){
            Request request = optRequest.get();
            if (!request.getStatus().equalsIgnoreCase("Pending")) {
                return "Request is not pending.";
            }
            request.setStatus("Rejected");
            requestRepo.update(request);
            return "Request rejected successfully.";
        } else {
            return "Request not found.";
        }
    }

    // Delete a request
    public String deleteRequest(String requestId){
        boolean deleted = requestRepo.delete(requestId);
        return deleted ? "Request deleted successfully." : "Failed to delete request.";
    }

    // Find the subjectId for a classId
    private String getSubjectIdFromClass(String classId){
        Optional<Classes> cls = classesRepo.getById(classId);
        return cls.map(Classes::getSubjectId).orElse(null);
    }

    // Get classId by studentId and student level
    private String getClassIdForSubject(String subjectId, String studentLevel){
        return classesRepo.getAll().stream()
                .filter(c -> c.getSubjectId().equals(subjectId))
                .filter(c -> {
                    Subject subject = subjectRepo.getSubjectById(c.getSubjectId()).orElse(null);
                    return subject != null && subject.getLevel().equalsIgnoreCase(studentLevel);
                }).map(Classes::getClassId).findFirst().orElse(null);
    }
}
