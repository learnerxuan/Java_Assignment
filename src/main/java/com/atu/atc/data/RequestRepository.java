package com.atu.atc.data;

import com.atu.atc.model.Request;
import com.atu.atc.util.FileUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Optional;

/**
 *
 * @author farris
 */
public class RequestRepository {
    private static final String FILE_PATH = "data/requests.txt";
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
                System.err.println("RequestRepository: Invalid data format for line: " + line);
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
        if (getRequestById(request.getRequest_id()).isPresent()) {
            System.err.println("RequestRepository: Request with ID " + request.getRequest_id() + " already exists.");
            return;
        }
        requests.add(request);
        save();
    }
    
    public boolean update(Request updatedRequest) {
        if (updatedRequest == null) return false;
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).getRequest_id().equals(updatedRequest.getRequest_id())) {
                requests.set(i, updatedRequest);
                save();
                System.out.println("RequestRepository: Request updated: " + updatedRequest.getRequest_id());
                return true;
            }
        }
        System.err.println("RequestRepository: Request with ID " + updatedRequest.getRequest_id() + " not found for update.");
        return false;
    }
    
    public boolean delete(String requestId) {
        Iterator<Request> iterator = requests.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getRequest_id().equals(requestId)) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            save();
            System.out.println("RequestRepository: Request deleted: " + requestId);
            return true;
        }
        System.err.println("RequestRepository: Request with ID " + requestId + " not found for deletion.");
        return false;
    }
    
    public List<Request> getAll() {
        return new ArrayList<>(requests);
    }

    public Optional<Request> getRequestById(String requestId) {
        return requests.stream()
                .filter(r -> r.getRequest_id().equals(requestId))
                .findFirst();
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