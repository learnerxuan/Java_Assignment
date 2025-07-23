package com.atu.atc.service;

import com.atu.atc.data.TutorRepository;
import com.atu.atc.data.StudentRepository;
import com.atu.atc.data.EnrollmentRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.ClassesRepository;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.Classes;
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
    private final ClassesRepository classesRepository;
    private final IDGenerator idGenerator;

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
        this.classesRepository = classesRepository;
        this.idGenerator = idGenerator;
    }

    public String updateTutorProfile(Tutor tutor, String newPassword, String newFullName, String newPhoneNumber, String newEmail, String newGender) {
        if (!Validator.isValidPhoneNumber(newPhoneNumber)) {
            return "Invalid phone number.";
        }
        if (!Validator.isValidEmail(newEmail)) {
            return "Invalid email address.";
        }

        tutor.updateProfile(tutor.getId(), newPassword, newFullName, newPhoneNumber, newEmail, newGender);
        boolean updated = tutorRepository.update(tutor);
        if (updated) {
            tutorRepository.save();
        }
        return updated ? "Profile updated successfully." : "Failed to update profile.";
    }
    
    public boolean addClassInformation(String subjectId, String level, double charges, String schedule, String tutorId) {
    if (subjectId == null || tutorId == null || schedule == null ||
        subjectId.isBlank() || tutorId.isBlank() || schedule.isBlank()) {
        return false;
    }

    String[] parts = schedule.trim().split("\\s+");
    if (parts.length != 2) return false;

    String day = parts[0].trim();
    String[] times = parts[1].split("-");
    if (times.length != 2) return false;

    String startTime = times[0].trim();
    String endTime = times[1].trim();

    String nextClassId = idGenerator.generateClassId(); // This should now generate CLS001, CLS002, etc.
    Classes newClass = new Classes(nextClassId, subjectId, tutorId, day, startTime, endTime);
    classesRepository.add(newClass);
    classesRepository.save();

    return true;
}


    


    public boolean updateClassInformation(String classId, String subjectId, String level, double charges, String schedule, String tutorId) {
        Optional<Classes> optionalClass = classesRepository.getById(classId);
        if (optionalClass.isEmpty()) return false;

        Classes existing = optionalClass.get();

        String[] parts = schedule.split(" ");
        if (parts.length != 2) return false;

        String day = parts[0];
        String[] times = parts[1].split("-");
        if (times.length != 2) return false;

        String startTime = times[0];
        String endTime = times[1];

        Classes updated = new Classes(
            existing.getClassId(),
            subjectId,
            tutorId,
            day,
            startTime,
            endTime
        );

        classesRepository.update(updated);
        classesRepository.save();
        return true;
    }

    public boolean deleteClassInformation(String classId) {
        boolean removed = classesRepository.delete(classId);
        if (removed) {
            classesRepository.save();
        }
        return removed;
    }

    public List<String> viewStudentsEnrolledInMySubjects(String tutorId) {
        List<String> result = new ArrayList<>();
        List<Enrollment> enrollments = enrollmentRepository.getAll();
        for (Enrollment e : enrollments) {
            Optional<Classes> optionalClass = classesRepository.getById(e.getClassId());
            if (optionalClass.isPresent()) {
                Classes c = optionalClass.get();
                if (tutorId.equalsIgnoreCase(c.getTutorId())) {
                    Student s = studentRepository.getById(e.getStudentId());
                    if (s != null) {
                        result.add(s.getFullName() + " - " + s.getId());
                    } else {
                        System.err.println("⚠️ Skipped: Student not found: " + e.getStudentId());
                    }
                }
            } else {
                System.err.println("⚠️ Skipped: Class not found: " + e.getClassId());
            }
        }
        return result;
    }
}
