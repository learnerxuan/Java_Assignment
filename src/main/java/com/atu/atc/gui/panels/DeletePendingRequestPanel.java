package com.atu.atc.gui.panels;

import com.atu.atc.data.RequestRepository;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.model.Request;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class DeletePendingRequestPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    
    private RequestRepository requestRepository;
    private SubjectRepository subjectRepository;
    private Student currentStudent;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    public DeletePendingRequestPanel(RequestRepository requestRepository, SubjectRepository subjectRepository, Student currentStudent) {
        this.requestRepository = requestRepository;
        this.subjectRepository = subjectRepository;
        this.currentStudent = currentStudent;
        
        setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel(new Object[]{"Request ID", "Current Subject", "Requested Subject", "Status", "Request Date"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        deleteButton = new JButton("Delete Selected Request");
        deleteButton.addActionListener(e -> deleteSelectedRequest());
        
        add(scrollPane, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.SOUTH);
        
        loadPendingRequests();
    }
    
    private void loadPendingRequests() {
        tableModel.setRowCount(0);
        
        List<Request> requests = requestRepository.getAll();
        for (Request request : requests) {
            if (request.getStudentId().equals(currentStudent.getId()) && "Pending".equalsIgnoreCase(request.getStatus())) {
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
    
    private void deleteSelectedRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this request?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = requestRepository.delete(requestId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Request deleted successfully.");
                loadPendingRequests();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete request.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
