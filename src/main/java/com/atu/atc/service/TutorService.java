package com.atu.atc.service;

import com.atu.atc.data.*;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.Course;
import com.atu.atc.model.Student;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Subject;
import com.atu.atc.util.IDGenerator;
import com.atu.atc.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TutorService {

    private final TutorRepository tutorRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public TutorService(TutorRepository tutorRepository,
                        SubjectRepository subjectRepository,
                        StudentRepository studentRepository,
                        EnrollmentRepository enrollmentRepository,
                        ClassesRepository classesRepository,
                        IDGenerator idGenerator) {
        this.tutorRepository = tutorRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public void updateTutorProfile(Tutor tutor, String newPassword, String newFullName, String newPhoneNumber, String newEmail, String newGender) {
            
        if (!Validator.isValidPhoneNumber(newPhoneNumber)) {
            System.err.println("TutorService: Update failed - Invalid phone number format.");
            return;
        }
        if (!Validator.isValidEmail(newEmail)) {
            System.err.println("TutorService: Update failed - Invalid email format.");
            return;
        }

        tutor.updateProfile(tutor.getId(), newPassword, newFullName, newPhoneNumber, newEmail, newGender);
        
        tutorRepository.update(tutor);
        System.out.println("TutorService: Profile for tutor " + tutor.getId() + " updated successfully.");
    }

    public boolean addClassInformation(String subjectId, String level, double charges, String schedule, String tutorId) {
        System.err.println("TutorService: Cannot add class information. CourseRepository is not available.");
        return false;
    }

    public boolean updateClassInformation(String courseId, String newSubjectId, String newLevel,
                                          double newCharges, String newSchedule, String newTutorId) {
        System.err.println("TutorService: Cannot update class information. CourseRepository is not available.");
        return false;
    }

    public boolean deleteClassInformation(String courseId) {
        System.err.println("TutorService: Cannot delete class information. CourseRepository is not available.");
        return false;
    }

    public List<String> viewStudentsEnrolledInMySubjects(String tutorId) {
        System.err.println("TutorService: Cannot view enrolled students by subject. CourseRepository and/or EnrollmentRepository dependencies are not fully set up.");
        return new ArrayList<>();
    }
}

