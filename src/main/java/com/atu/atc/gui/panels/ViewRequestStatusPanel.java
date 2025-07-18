package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewRequestStatusPanel extends JPanel {
    
    private JTable requestTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton backButton;
    private JLabel infoLabel;
    
    private final StudentService studentService;
    private final MainFrame.PanelNavigator navigator;
    private final Student loggedInStudent;
    
    public ViewRequestStatusPanel(StudentService studentService, Student loggedInStudent, MainFrame.PanelNavigator navigator) {
        this.studentService = studentService;
        this.navigator = navigator;
        this.loggedInStudent = loggedInStudent;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel titleLabel = new JLabel("View Request Status", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Request ID", "Current Subject", "Requested Subject", "Status", "Date Submitted"};
        tableModel = new DefaultTableModel(columns, 0);
        requestTable = new JTable(tableModel);
        requestTable.setEnabled(false);
        add(new JScrollPane(requestTable), BorderLayout.CENTER);
        
        infoLabel = new JLabel("Below are your submitted subject change requests.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        add(infoLabel, BorderLayout.SOUTH);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back to Dashboard");
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        refreshButton.addActionListener(e -> loadRequestData());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, loggedInStudent));
        
        loadRequestData();
    }
    
    private void loadRequestData() {
        tableModel.setRowCount(0); // Clear existing rows
        
        try {
            List<String> requestLines = studentService.getSubjectChangeRequests(loggedInStudent.getId());
            
            if (requestLines.isEmpty()) {
                infoLabel.setText("You have no subject change requests.");
                return;
            }
            
            for (String line : requestLines) {
                String[] parts = line.split(",", -1); // Allow empty fields
                if (parts.length >= 5) {
                    tableModel.addRow(new Object[]{
                            parts[0].trim(), // Request ID
                            parts[1].trim(), // Current Subject
                            parts[2].trim(), // Requested Subject
                            parts[3].trim(), // Status
                            parts[4].trim()  // Date Submitted
                    });
                } else {
                    System.err.println("Invalid request record: " + line);
                }
            }
            
            infoLabel.setText("Showing " + requestLines.size() + " request(s).");
            
        } catch (Exception ex) {
            infoLabel.setText("Error loading data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
