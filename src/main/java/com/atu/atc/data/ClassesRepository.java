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
    
//    public static void main(String[] args) {
//        System.out.println("--- Starting ClassesRepository Test ---");
//
//        // 1. Create a dummy classes.txt file for testing.
//        String dummyFilePath = "data/classes.txt"; 
//        List<String> dummyContent = new ArrayList<>();
//        dummyContent.add(HEADER); 
//        dummyContent.add("C001,S001,T001,Monday,08:00,10:00");
//        dummyContent.add("C002,S002,T002,Tuesday,09:30,11:30");
//        dummyContent.add("C003,S001,T003,Wednesday,14:00,16:00");
//        FileUtils.writeLines(dummyFilePath, dummyContent);
//        System.out.println("Created dummy file: " + dummyFilePath);
//
//        // --- CRITICAL CHANGE: Create an INSTANCE of ClassesRepository to call its non-static methods ---
//        ClassesRepository classesRepo = new ClassesRepository(); 
//
//        // 2. Load classes from the dummy file
//        System.out.println("\n--- Loading Classes ---");
//        classesRepo.load(); // Call load() on the instance
//        System.out.println("Classes loaded: " + classesRepo.getAll().size()); 
//        for (Classes cls : classesRepo.getAll()) {
//            System.out.println(cls); 
//        }
//
//        // 3. Test adding a new class
//        System.out.println("\n--- Adding New Class ---");
//        Classes newClass = new Classes("C004", "S003", "T004", "Friday", "10:00", "12:00");
//        classesRepo.add(newClass); // Call add() on the instance
//        System.out.println("Current classes after add: " + classesRepo.getAll().size());
//
//
//        // 6. Test updating a class
//        System.out.println("\n--- Updating Class (C001) ---");
//        Classes updatedClass = new Classes("C001", "S001", "T005", "Monday", "08:30", "10:30"); // Reassigning tutor, changing time
//        boolean updated = classesRepo.update(updatedClass); 
//        System.out.println("Class C001 updated: " + updated);
//        System.out.println("Classes after update:");
//        for (Classes cls : classesRepo.getAll()) {
//            System.out.println(cls);
//        }
//
//        // 7. Test deleting a class
//        System.out.println("\n--- Deleting Class (C003) ---");
//        boolean deleted = classesRepo.delete("C003"); 
//        System.out.println("Class C003 deleted: " + deleted);
//        System.out.println("Classes after delete: " + classesRepo.getAll().size());
//        for (Classes cls : classesRepo.getAll()) {
//            System.out.println(cls);
//        }
//
//        // 8. Verify file content by reloading (after previous operations auto-saved)
//        System.out.println("\n--- Reloading Classes to Verify Persistence ---");
//        classesRepo.load(); 
//        System.out.println("Classes reloaded: " + classesRepo.getAll().size());
//        for (Classes cls : classesRepo.getAll()) {
//            System.out.println(cls);
//        }
//
//        System.out.println("\n--- ClassesRepository Test Complete ---");
//   }
}
