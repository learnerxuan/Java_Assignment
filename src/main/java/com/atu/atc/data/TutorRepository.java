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
    private static final String FILE_PATH = "data/tutor.txt";
    private static final String HEADER ="userId,password,fullName,phoneNumber,email,gender,subject,level";
    @Override
    public void load(){
        // Clear the inherited 'users' list to ensure a fresh load.
        users.clear();
        
        // Read all lines from the file and skip the header.
        List<String> lines = FileUtils.readDataLines(FILE_PATH);
        
        // Loop throught each line
        for (String line: lines) {
            String[] parts = line.split(",", -1); // Split by comma and -1 ensures trailing empty strings.
            
            if (parts.length == 8) {
                    try {
                        String userId = parts[0].trim();
                        String password = parts[1].trim();
                        String fullName = parts[2].trim();
                        String phoneNumber = parts[3].trim();
                        String email = parts[4].trim();
                        String gender = parts[5].trim();
                        String subject = parts[6].trim(); // New field for Tutor
                        String level = parts[7].trim();   // New field for Tutor
                        
                        // Create a new Tutor object (Corrected from Admin to Tutor)
                        Tutor tutor = new Tutor(userId, password, fullName, phoneNumber, email, gender, subject, level);
                        
                        users.add(tutor);
                        } catch (Exception e){
                    // Catch error
                    System.err.println("TutorRepository: Error parsing line for Tutor: " + line + " - "+ e.getMessage());
                }
            } else {
                // Output error if a lines does not have the expected number of fields.
                System.err.println("TutorRepository: Malformed line - " + FILE_PATH);
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