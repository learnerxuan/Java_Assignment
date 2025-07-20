package com.atu.atc.gui.panels;

import com.atu.atc.model.Request;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;
import com.atu.atc.gui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class DeletePendingRequestPanel extends JPanel {
    
    private final StudentService studentService;
    private final MainFrame.PanelNavigator navigator;
    private final Student student;
    
    private JTable requestTable;
    private DefaultTableModel tableModel;
    
    public DeletePendingRequestPanel(StudentService studentService, MainFrame.PanelNavigator navigator, Student student) {
        this.studentService = studentService;
        this.navigator = navigator;
        this.student = student;
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Delete Pending Requests");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columnNames = {"Request ID", "Current Subject", "Requested Subject", "Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JButton deleteButton = new JButton("Delete Selected Request");
        deleteButton.addActionListener(e -> deleteSelectedRequest());
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, student));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        loadRequests();
    }
    
    private void loadRequests() {
        tableModel.setRowCount(0);
        List<Request> requests = studentService.getRequestsByStudentId(student.getId());
        for (Request r : requests) {
            if ("Pending".equalsIgnoreCase(r.getStatus())) {
                tableModel.addRow(new Object[]{
                        r.getRequestId(),
                        r.getCurrentSubjectId(),
                        r.getRequestedSubjectId(),
                        r.getStatus(),
                        r.getRequestDate()
                });
            }
        }
    }
    
    private void deleteSelectedRequest() {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String requestId = Objects.toString(tableModel.getValueAt(selectedRow, 0), "");
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this request?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentService.deletePendingRequest(requestId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Request deleted successfully.");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the request.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
