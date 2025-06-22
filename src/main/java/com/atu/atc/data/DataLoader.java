package com.atu.atc.data;

/**
 * @author henge
 */
public class DataLoader {
    private final StudentRepository studentRepo;
    private final ReceptionistRepository receptionistRepo;
    private final TutorRepository tutorRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final PaymentRepository paymentRepo;
    private final RequestRepository requestRepo;

    public DataLoader(StudentRepository studentRepo,
                      ReceptionistRepository receptionistRepo,
                      TutorRepository tutorRepo,
                      CourseRepository courseRepo,
                      EnrollmentRepository enrollmentRepo,
                      PaymentRepository paymentRepo,
                      RequestRepository requestRepo) {
        this.studentRepo = studentRepo;
        this.receptionistRepo = receptionistRepo;
        this.tutorRepo = tutorRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.requestRepo = requestRepo;
    }

    public void loadAllData() {
        studentRepo.load();
        receptionistRepo.load();
        tutorRepo.load();
        courseRepo.load();
        enrollmentRepo.load();
        paymentRepo.load();
        requestRepo.load();
        System.out.println("All data loaded.");
    }
}
