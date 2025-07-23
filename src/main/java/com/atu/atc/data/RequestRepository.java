package com.atu.atc.data;

import com.atu.atc.model.Request;
import com.atu.atc.util.FileUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class RequestRepository {
    private static final String filePath = "src/main/resources/data/requests.txt";
    private List<Request> requests = new ArrayList<>();
    
    public RequestRepository() {
        load();
    }
    
    public void load() {
        requests.clear();
        List<String> lines = FileUtils.readDataLines(filePath);
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("request_id")) continue;
            Request r = Request.fromFileString(line);
            if (r != null) {
                requests.add(r);
            } else {
                System.err.println("RequestRepository: Invalid data format for line: " + line);
            }
        }
    }
    
    public void save() {
        List<String> lines = new ArrayList<>();
        lines.add("request_id;student_id;current_subject_id;requested_subject_id;status;request_date");
        for (Request r : requests) {
            lines.add(r.toFileString());
        }
        FileUtils.writeLines(filePath, lines);
    }
    
    public List<Request> getAll() {
        return new ArrayList<>(requests);
    }
    
    public Optional<Request> getRequestById(String requestId) {
        return requests.stream()
                .filter(r -> r.getRequestId().equals(requestId))
                .findFirst();
    }
    
    public List<Request> getByStudentId(String studentId) {
        List<Request> result = new ArrayList<>();
        for (Request r : requests) {
            if (r.getStudentId().equals(studentId)) {
                result.add(r);
            }
        }
        return result;
    }
    
    public void add(Request request) {
        if (getRequestById(request.getRequestId()).isPresent()) {
            System.err.println("RequestRepository: Request with ID " + request.getRequestId() + " already exists.");
            return;
        }
        requests.add(request);
        save();
    }
    
    public boolean update(Request updatedRequest) {
        if (updatedRequest == null) return false;
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getRequestId().equals(updatedRequest.getRequestId())) {
                requests.set(i, updatedRequest);
                save();
                System.out.println("RequestRepository: Request updated: " + updatedRequest.getRequestId());
                return true;
            }
        }
        System.err.println("RequestRepository: Request with ID " + updatedRequest.getRequestId() + " not found.");
        return false;
    }
    
    public boolean delete(String requestId) {
        Iterator<Request> iterator = requests.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getRequestId().equals(requestId)) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            save();
            System.out.println("RequestRepository: Request deleted: " + requestId);
            return true;
        } else {
            System.err.println("RequestRepository: Request with ID " + requestId + " not found.");
            return false;
        }
    }
}
