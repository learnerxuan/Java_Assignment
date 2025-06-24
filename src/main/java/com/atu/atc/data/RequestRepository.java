/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.data;

import com.atu.atc.model.Request;
import com.atu.atc.util.FileUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author farris
 */
public class RequestRepository {
    private static final String FILE_PATH = "data/students.txt";
    private static final String HEADER = "request_id, student_id, current_subject_id, requested_subject_id, status";
    private List<Request> requests = new ArrayList<>();

    public void load() {
        requests.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);
        
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length == 5) {
                String request_id = parts[0].trim();
                String student_id = parts[1].trim();
                String current_subject_id = parts[2].trim();
                String requested_subject_id = parts[3].trim();
                String status = parts[4].trim();
                
                Request r = new Request(request_id, student_id, current_subject_id, requested_subject_id, status);
                requests.add(r);
            } else {
                System.err.println("Invalid.");
            }
        }
    }         

    public void save() {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Request r : requests) {
            lines.add(r.toFileString());
        }
        FileUtils.writeLines(FILE_PATH, lines);
    }
    
    public void add(Request request) {
        requests.add(request);
        save();
    }
        
    public List<Request> getAll() {
        return requests;
    }    
        
    public List<Request> getByStudentId(String studentId) {
        List<Request> result = new ArrayList<>();
        for (Request r : requests) {
            if (r.getStudent_id().equals(studentId)) {
                result.add(r);
            }
        }
        return result;
    }
}
