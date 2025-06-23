package com.atu.atc.data;

/**
 * @author henge
 */
public class DataSaver {
    private final StudentRepository studentRepo;
    private final ReceptionistRepository receptionistRepo;
    private final TutorRepository tutorRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final PaymentRepository paymentRepo;
    private final RequestRepository requestRepo;
    private final AdminRepository adminRepo;

    public DataSaver(StudentRepository studentRepo,
                     ReceptionistRepository receptionistRepo,
                     TutorRepository tutorRepo,
                     CourseRepository courseRepo,
                     EnrollmentRepository enrollmentRepo,
                     PaymentRepository paymentRepo,
                     RequestRepository requestRepo,
                     AdminRepository adminRepo) {
        this.studentRepo = studentRepo;
        this.receptionistRepo = receptionistRepo;
        this.tutorRepo = tutorRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.requestRepo = requestRepo;
        this.adminRepo = adminRepo;
    }

    public void saveAllData() {
        studentRepo.save();
        receptionistRepo.save();
        tutorRepo.save();
        courseRepo.save();
        enrollmentRepo.save();
        paymentRepo.save();
        requestRepo.save();
        adminRepo.save();
        System.out.println("All data saved.");
    }
}
