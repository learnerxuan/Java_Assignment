package com.atu.atc.gui.panels;

import com.atu.atc.data.RequestRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.model.Request;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ViewRequestStatusPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    
    private RequestRepository requestRepository;
    private SubjectRepository subjectRepository;
    private Student currentStudent;
    
    public ViewRequestStatusPanel(RequestRepository requestRepository, SubjectRepository subjectRepository, Student currentStudent) {
        this.requestRepository = requestRepository;
        this.subjectRepository = subjectRepository;
        this.currentStudent = currentStudent;
        
        setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel(new Object[]{"Request ID", "Current Subject", "Requested Subject", "Status"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(scrollPane, BorderLayout.CENTER);
        loadAllRequests();
    }
    
    private void loadAllRequests() {
        tableModel.setRowCount(0);
        
        List<Request> requests = requestRepository.getAll();
        for (Request request : requests) {
            if (request.getStudentId().equals(currentStudent.getId())) {
                Optional<Subject> currentSubjectOpt = subjectRepository.getSubjectById(request.getCurrentSubjectId());
                Optional<Subject> requestedSubjectOpt = subjectRepository.getSubjectById(request.getRequestedSubjectId());
                
                String currentSubjectName = currentSubjectOpt.map(Subject::getName).orElse("Unknown");
                String requestedSubjectName = requestedSubjectOpt.map(Subject::getName).orElse("Unknown");
                
                tableModel.addRow(new Object[]{
                        request.getRequestId(),
                        request.getCurrentSubjectId() + " - " + currentSubjectName,
                        request.getRequestedSubjectId() + " - " + requestedSubjectName,
                        request.getStatus()
                });
            }
        }
    }
}
