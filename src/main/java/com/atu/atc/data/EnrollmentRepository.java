package com.atu.atc.data;

/**
 * @author henge
 */

import com.atu.atc.model.Enrollment;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentRepository {
    private static final String FILE_PATH = "data/enrollments.txt";
    private static final String HEADER = "course_enrollment_id,class_id,student_id";
    private List<Enrollment> enrollments = new ArrayList<>();

    // Load all enrollments from file
    public void load(){
        enrollments.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);

        for (String line : lines){
            String[] parts = line.split(",", -1);
            if (parts.length == 3){
                Enrollment e = new Enrollment(
                        parts[0].trim(), //courseEnrollmentId
                        parts[1].trim(), //classId
                        parts[2].trim()  // studentId
                );
                enrollments.add(e);
            }else{
                System.err.println("Invalid enrollment record: " + line);
            }
        }
    }

    // Save all enrollments to file
    public void save(){
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Enrollment e : enrollments){
            lines.add(e.toFileString());
        }
        FileUtils.writeLines(FILE_PATH, lines);
    }

    // Add a new enrollment and save
    public boolean add(Enrollment enrollment){
        enrollments.add(enrollment);
        save();
        return false;
    }

    // Get all enrollments
    public List<Enrollment> getAll(){
        return enrollments;
    }

    // Get all enrollments for a specific student
    public List<Enrollment> getByStudentId(String studentId){
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments){
            if (e.getStudentId().equals(studentId)){
                result.add(e);
            }
        }
        return result;
    }

    // Delete an enrollment by courseEnrollmentId
    public boolean deleteById(String courseEnrollmentId){
        boolean removed = enrollments.removeIf(e -> e.getCourseEnrollmentId().equals(courseEnrollmentId));
        if (removed) save();
        return removed;
    }

    // Update enrollment by Id
    public boolean update(Enrollment updated){
        for (int i =0; i < enrollments.size(); i++){
            if (enrollments.get(i).getCourseEnrollmentId().equals((updated.getCourseEnrollmentId()))){
                enrollments.set(i, updated);
                save();
                return true;
            }
        }
        return false;
    }
}
