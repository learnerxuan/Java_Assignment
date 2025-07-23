package com.atu.atc.data;

/**
 * @author henge
 */

import com.atu.atc.model.Receptionist;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class ReceptionistRepository extends UserRepository<Receptionist> {
    private static final String FILE_PATH = "src/main/resources/data/receptionists.txt";
    private static final String HEADER = "receptionist_id,receptionist_name,password,phone_number,email,gender";

    @Override
    public void load(){
        users.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);
        for (String line : lines){
            String[] parts = line.split(",", -1); // -1: Include trailing empty string
            if (parts.length == 6) {
                Receptionist r = new Receptionist(
                        parts[0].trim(), //id
                        parts[2].trim(), // password
                        parts[1].trim(), // full name
                        parts[3].trim(), // phone number
                        parts[4].trim(), // email
                        parts[5].trim()  // gender
                );
                users.add(r);
            }else{
                System.err.println("Invalid receptionist data: "  + line);
            }
        }
    }

    @Override
    public void save(){
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Receptionist r : users){
            lines.add(r.toFileString());
        }
        FileUtils.writeLines(FILE_PATH, lines);
    }
}
