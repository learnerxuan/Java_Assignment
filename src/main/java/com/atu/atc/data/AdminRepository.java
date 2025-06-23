/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.data;

/**
 *
 * @author User
 */

import com.atu.atc.model.Admin;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminRepository extends UserRepository<Admin>{
    
    private static final String FILE_PATH = "data/admins.txt";
    
    private static final String HEADER = "admin_id,admin_name,password,phone_number,email,gender";
    
    @Override
    public void load(){
        // Clear the inherited 'users' list to ensure a fresh load.
        users.clear();
        
        // Read all lines from the file and skip the header.
        List<String> lines = FileUtils.readDataLines(FILE_PATH);
        
        // Loop throught each line
        for (String line: lines) {
            String[] parts = line.split(",", -1); // Split by comma and -1 ensures trailing empty strings.
            
            if (parts.length == 6){
                try {
                    String id = parts[0].trim();
                    String fullName = parts[1].trim();
                    String password = parts[2].trim();
                    String phoneNumber = parts[3].trim();
                    String email = parts[4].trim();
                    String gender = parts[5].trim();
                    
                    // Create a new Admin object 
                    Admin admin = new Admin(id, fullName, password, phoneNumber, email, gender);
                    
                    users.add(admin);
                    
                } catch (Exception e){
                    // Catch error
                    System.err.println("AdminRepository: Error parsing line for Admin: " + line + " - "+ e.getMessage());
                }
            } else {
                // Output error if a lines does not have the expected number of fields.
                System.err.println("AdminRepository: Malformed line - " + FILE_PATH);
            }
        }
        System.out.println("AdminRepository: Loaded " + users.size() + " admins into memory from " + FILE_PATH);
    }
    
    
    @Override
    public void save(){
        List<String> linesToSave = new ArrayList<>();
        linesToSave.add(HEADER);
        
        // Loop through each Admin object 
        for (Admin admin: users){
            linesToSave.add(admin.toFileString());
        }
        
        boolean success = FileUtils.writeLines(FILE_PATH, linesToSave);
        if (!success) {
            System.err.println("AdminRepository: Failed to save admin data to " + FILE_PATH);
        } else {
            System.out.println("AdminRepository: Saved " + users.size() + " admins to " + FILE_PATH);
        }
    }
}
