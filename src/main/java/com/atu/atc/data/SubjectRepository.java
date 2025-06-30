package com.atu.atc.data;

/**
 *
 * @author haoshuan
 */
import com.atu.atc.model.Subject;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Optional;
public class SubjectRepository {
    private static final String FILE_PATH = "data/subjects.txt";
    // Define the header matching the Subject.toFileString() format
    private static final String HEADER = "subject_Id|subject_Name|tutor_Id|level"; // Using | as delimiter as per Subject.toFileString()
    private List<Subject> subjects = new ArrayList<>();
    
    public void load() {
        subjects.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);

        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length == 4) { 
                try {
                    String subjectId = parts[0].trim();
                    String subjectName = parts[1].trim();
                    String tutorId = parts[2].trim();
                    String level = parts[3].trim();

                    Subject subject = new Subject(subjectId, subjectName, tutorId, level);
                        subjects.add(subject);
                } catch (Exception e) {
                    // Catch error
                    System.err.println("SubjectRepository: Error parsing line: " + line + " - " + e.getMessage());
                }
            } else {
                System.err.println("SubjetcRepository: Malformed line: " + line);
            }
        }
    }
    public void save() {
        List<String> linesToSave = new ArrayList<>();
        linesToSave.add(HEADER);
        
        for (Subject subject : subjects) {
            linesToSave.add(subject.toFileString());
        }
        
        boolean success = FileUtils.writeLines(FILE_PATH, linesToSave);
        if (!success) {
            System.err.println("SubjectRepository: Failed to save class data to " + FILE_PATH);
        } else {
            System.out.println("SubjectRepository: Saved " + subjects.size() + " classes to " + FILE_PATH);
        }
    }
    public void add(Subject subject) {
        subjects.add(subject);
        save();
    }
    public boolean update(Subject updatedSubject) { 
        
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getSubjectId().equals(updatedSubject.getSubjectId())) {
                subjects.set(i, updatedSubject); 
                save(); 
                System.out.println("SubjectRepository: Class updated in memory: " + updatedSubject.getSubjectId());
                return true;
            }
        }
        
        System.err.println("SubjectRepository: Class with ID " + updatedSubject.getSubjectId() + " not found for update.");
        return false;
    } 
    public boolean delete(String subjectId) { 
        
        Iterator<Subject> iterator = subjects.iterator();
        
        while (iterator.hasNext()) {
            Subject subject = iterator.next();
            if (subject.getSubjectId().equals(subjectId)) {
                iterator.remove(); 
                save(); 
                System.out.println("SubjectRepository: Subject deleted from memory: " + subjectId);
                return true;
            }
        }
        
        System.err.println("SubjectRepository: Subject with ID " + subjectId + " not found for deletion.");
        return false;
    }
    public Optional<Subject> getSubjectById(String subjectId) {
        return subjects.stream()
                       .filter(subject -> subject.getSubjectId().equals(subjectId))
                       .findFirst();
    }
    
    public List<Subject> getAllSubjects() {
        return new ArrayList<>(subjects);
    }
}
