package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDashboardPanel extends JPanel {
    private final MainFrame navigatorFrame;
    private Student currentStudent;
    
    private JLabel welcomeLabel;
    
    public StudentDashboardPanel(MainFrame navigatorFrame) {
        this.navigatorFrame = navigatorFrame;
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomeLabel = new JLabel("Welcome, Student!");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Dashboard Buttons
        JButton btnViewProfile = new JButton("View/Update Profile");
        JButton btnViewSchedule = new JButton("View Class Schedule");
        JButton btnViewPaymentHistory = new JButton("View Payment History");
        JButton btnViewRequestStatus = new JButton("View Subject Change Request Status");
        JButton btnSubmitSubjectChange = new JButton("Submit Subject Change Request");
        JButton btnLogout = new JButton("Logout");
        
        // Add action listeners
        btnViewProfile.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.STUDENT_PROFILE_PANEL, currentStudent));
        btnViewSchedule.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.VIEW_SCHEDULE_PANEL, currentStudent));
        btnViewPaymentHistory.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.VIEW_PAYMENT_HISTORY_PANEL, currentStudent));
        btnViewRequestStatus.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.VIEW_REQUEST_STATUS_PANEL, currentStudent));
        btnSubmitSubjectChange.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.SUBMIT_SUBJECT_CHANGE_REQUEST_PANEL, currentStudent));
        btnLogout.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.LOGIN_PANEL, null)); // Logout, no user needed for login panel
        
        // Layout buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(btnViewProfile, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(btnViewSchedule, gbc);
        
        gbc.gridy = 2;
        centerPanel.add(btnViewPaymentHistory, gbc);
        
        gbc.gridy = 3;
        centerPanel.add(btnViewRequestStatus, gbc);
        
        gbc.gridy = 4;
        centerPanel.add(btnSubmitSubjectChange, gbc);
        
        gbc.gridy = 5;
        centerPanel.add(btnLogout, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    public void setStudent(Student student) {
        this.currentStudent = student;
        if (currentStudent != null) {
            welcomeLabel.setText("Welcome, " + currentStudent.getFullName() + " (ID: " + currentStudent.getId() + ")!");
        } else {
            welcomeLabel.setText("Welcome, Student!"); // Fallback
        }
    }
}