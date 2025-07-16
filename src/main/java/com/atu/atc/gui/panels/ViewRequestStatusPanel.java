package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Request;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewRequestStatusPanel extends JPanel {
    private final MainFrame navigatorFrame;
    private final StudentService studentService;
    private Student currentStudent;
    
    private DefaultTableModel tableModel;
    
    public ViewRequestStatusPanel(MainFrame navigatorFrame, StudentService studentService) {
        this.navigatorFrame = navigatorFrame;
        this.studentService = studentService;
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        JLabel titleLabel = new JLabel("Subject Change Request Status", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"Request ID", "Current Subject ID", "Requested Subject ID", "Status", "Request Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        JTable requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent));
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadRequestData());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void setStudent(Student student) {
        this.currentStudent = student;
        loadRequestData();
    }
    
    private void loadRequestData() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "No student selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0); // Clear existing data
        
        List<Request> requests = studentService.getStudentRequests(currentStudent.getId());
        
        if (requests.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subject change requests found for this student.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Request request : requests) {
            Object[] rowData = {
                    request.getRequestId(),
                    request.getCurrentSubjectId(),
                    request.getRequestedSubjectId(),
                    request.getStatus(),
                    request.getRequestDate().toString()
            };
            tableModel.addRow(rowData);
        }
    }
}
