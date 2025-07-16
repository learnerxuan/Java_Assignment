package com.atu.atc.data;

/**
 * @author henge
 */
public class DataLoader {
    private final StudentRepository studentRepo;
    private final ReceptionistRepository receptionistRepo;
    private final TutorRepository tutorRepo;
    private final ClassesRepository classRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final PaymentRepository paymentRepo;
    private final RequestRepository requestRepo;
    private final AdminRepository adminRepo;
    private final SubjectRepository subjectRepo;
    
    public DataLoader(StudentRepository studentRepo,
                      ReceptionistRepository receptionistRepo,
                      TutorRepository tutorRepo,
                      ClassesRepository classRepo,
                      EnrollmentRepository enrollmentRepo,
                      PaymentRepository paymentRepo,
                      RequestRepository requestRepo,
                      AdminRepository adminRepo,
                      SubjectRepository subjectRepo) {
        this.studentRepo = studentRepo;
        this.receptionistRepo = receptionistRepo;
        this.tutorRepo = tutorRepo;
        this.classRepo = classRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.paymentRepo = paymentRepo;
        this.requestRepo = requestRepo;
        this.adminRepo = adminRepo;
        this.subjectRepo = subjectRepo;
    }

    public void loadAllData() {
        studentRepo.load();
        receptionistRepo.load();
        tutorRepo.load();
        classRepo.load();
        enrollmentRepo.load();
        paymentRepo.load();
        requestRepo.load();
        adminRepo.load();
        subjectRepo.load();
        System.out.println("All data loaded.");
    }
}
