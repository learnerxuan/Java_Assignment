package com.atu.atc.data;

/**
 *
 * @author haoshuan
 */
import com.atu.atc.model.Tutor;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class TutorRepository extends UserRepository<Tutor> {
    private static final String FILE_PATH = "src/main/resources/data/tutors.txt";
    private static final String HEADER ="userId,fullName,password,phoneNumber,email,gender,level,subject";
    @Override
public void load() {
    users.clear();
    List<String> lines = FileUtils.readDataLines(FILE_PATH);

    for (String line : lines) {
        String[] parts = line.split(",", -1);

        if (parts.length == 8) {
            try {
                String userId = parts[0].trim();
                String fullName = parts[2].trim();
                String password = parts[1].trim();
                String phoneNumber = parts[3].trim();
                String email = parts[4].trim();
                String gender = parts[5].trim();
                String level = parts[7].trim();
                String subject = parts[6].trim();

                Tutor tutor = new Tutor(userId, fullName, password, phoneNumber, email, gender, level, subject);
                users.add(tutor);
            } catch (Exception e) {
                System.err.println("TutorRepository: Error parsing line for Tutor: " + line + " - " + e.getMessage());
            }
        } else {
            System.err.println("TutorRepository: Malformed line - " + line);
        }
    }

    System.out.println("TutorRepository: Loaded " + users.size() + " Tutors into memory from " + FILE_PATH);
}
    @Override
    public void save(){
        List<String> linesToSave = new ArrayList<>();
        linesToSave.add(HEADER);
        
        // Loop through each Tutor object 
        for (Tutor tutor: users){
            linesToSave.add(tutor.toFileString());
        }
        
        boolean success = FileUtils.writeLines(FILE_PATH, linesToSave);
        if (!success) {
            System.err.println("TutorRepository: Failed to save tutor data to " + FILE_PATH);
        } else {
            System.out.println("TutorRepository: Saved " + users.size() + " tutor to " + FILE_PATH);
        }
    }
}
