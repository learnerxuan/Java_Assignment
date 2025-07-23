package com.atu.atc.service;

import com.atu.atc.data.*;
import com.atu.atc.model.*;
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

        this.tutorRepository.load();
        this.subjectRepository.load();
        this.studentRepository.load();
        this.enrollmentRepository.load();
        this.classesRepository.load();
    }

    public String updateTutorProfile(Tutor tutor, String newPassword, String fullName, String phone, String email, String gender) {
    if (!Validator.isValidPhoneNumber(phone)) {
        return "Invalid phone number.";
    }
    if (!Validator.isValidEmail(email)) {
        return "Invalid email.";
    }

    tutor.setPassword(newPassword);
    tutor.setFullName(fullName);
    tutor.setPhoneNumber(phone);
    tutor.setEmail(email);
    tutor.setGender(gender);

    boolean success = tutorRepository.update(tutor);
    if (success) {
        tutorRepository.save();
        return "Profile updated successfully.";
    }
    return "Tutor not found.";
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

        String nextClassId = idGenerator.generateClassId();
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
                        result.add(String.join(",",
                            s.getId(),
                            s.getFullName(),
                            s.getIcPassport(),
                            s.getEmail(),
                            s.getPhoneNumber(),
                            s.getLevel(),
                            s.getMonthOfEnroll()
                        ));
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

    public List<String[]> getClassesByTutorId(String tutorId) {
        List<String[]> result = new ArrayList<>();
        List<Classes> classes = classesRepository.getByTutorId(tutorId);
        for (Classes c : classes) {
            result.add(new String[]{
                c.getClassId(),
                c.getSubjectId(),
                c.getDay(),
                c.getStartTime(),
                c.getEndTime()
            });
        }
        return result;
    }

    public void loadLatestTutorDataInto(String tutorId, Tutor target) {
        tutorRepository.load(); // Refresh from file
        Tutor fresh = tutorRepository.getById(tutorId);
        if (fresh != null) {
            target.setFullName(fresh.getFullName());
            target.setPassword(fresh.getPassword());
            target.setPhoneNumber(fresh.getPhoneNumber());
            target.setEmail(fresh.getEmail());
            target.setGender(fresh.getGender());
            target.setSubject(fresh.getSubject());
            target.setLevel(fresh.getLevel());
        }
    }
}
