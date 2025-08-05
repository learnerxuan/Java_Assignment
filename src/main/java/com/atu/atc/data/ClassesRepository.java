/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.data;

import com.atu.atc.model.Classes; 
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Optional;
/**
 *
 * @author User
 */
public class ClassesRepository {

    private static final String FILE_PATH = "src/main/resources/data/classes.txt";

    private static final String HEADER = "class_id,subject_id,tutor_id,day,start_time,end_time";

    private List<Classes> classes = new ArrayList<>(); // [Classes{classId='C001', subjectId='S001', tutorId='T001', day='Monday', startTime='08:00', endTime='10:00'}, Classes{classId='C002', subjectId='S002', tutorId='T002', day='Tuesday', startTime='09:30', endTime='11:30'}]


    public void load() {
        classes.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);

        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length == 6) {
                try {
                    String classId = parts[0].trim();
                    String subjectId = parts[1].trim();
                    String tutorId = parts[2].trim();
                    String day = parts[3].trim();
                    LocalTime startTime = LocalTime.parse(parts[4].trim());
                    LocalTime endTime = LocalTime.parse(parts[5].trim());

                    Classes cls = new Classes(classId, subjectId, tutorId, day, startTime.toString(), endTime.toString()); // Convert LocalTime back to String for constructor
                    classes.add(cls);

                } catch (Exception e) {
                    // Catch error
                    System.err.println("ClassesRepository: Error parsing line: " + line + " - " + e.getMessage());
                }
            } else {
                System.err.println("ClassesRepository: Malformed line: " + line);
            }
        }
    }

    public void save() {
        List<String> linesToSave = new ArrayList<>();
        linesToSave.add(HEADER);

        for (Classes cls : classes) {
            linesToSave.add(cls.toFileString());
        }

        boolean success = FileUtils.writeLines(FILE_PATH, linesToSave);
        if (!success) {
            System.err.println("ClassesRepository: Failed to save class data to " + FILE_PATH);
        } else {
            System.out.println("ClassesRepository: Saved " + classes.size() + " classes to " + FILE_PATH);
        }
    }

    public void add(Classes cls) {
        classes.add(cls);
        save();
    }

    // Retrieve class ID
    public Optional<Classes> getById(String classId) {

        for (Classes cls : classes) {
            if (cls.getClassId().equalsIgnoreCase(classId)) {
                return Optional.of(cls);
            }
        }

        return Optional.empty();
    }

    // Returns a list of all Classes objects currently in memory.
    public List<Classes> getAll() {
        return new ArrayList<>(classes);
    }

    // Get tutorId
    public List<Classes> getByTutorId(String tutorId) {

        List<Classes> assignedClasses = new ArrayList<>();

        for (Classes cls : classes) {
            if (cls.getTutorId() != null && !cls.getTutorId().trim().isEmpty() &&
                    cls.getTutorId().equalsIgnoreCase(tutorId)) {
                assignedClasses.add(cls);
            }
        }
        return assignedClasses;
    }

    public boolean update(Classes updatedClass) {

        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getClassId().equals(updatedClass.getClassId())) {
                classes.set(i, updatedClass);
                save();
                System.out.println("ClassesRepository: Class updated in memory: " + updatedClass.getClassId());
                return true;
            }
        }

        System.err.println("ClassesRepository: Class with ID " + updatedClass.getClassId() + " not found for update.");
        return false;
    }

    public boolean delete(String classId) {

        Iterator<Classes> iterator = classes.iterator();

        while (iterator.hasNext()) {
            Classes cls = iterator.next();
            if (cls.getClassId().equals(classId)) {
                iterator.remove();
                save();
                System.out.println("ClassesRepository: Class deleted from memory: " + classId);
                return true;
            }
        }

        System.err.println("ClassesRepository: Class with ID " + classId + " not found for deletion.");
        return false;
    }
}
