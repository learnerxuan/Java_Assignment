package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.User;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends JPanel implements DashboardPanelInterface {
    
    // UI Components
    private JLabel welcomeLabel;
    private JButton viewScheduleButton;
    private JButton requestSubjectChangeButton;
    private JButton deletePendingRequestButton;
    private JButton viewPaymentStatusButton;
    private JButton updateProfileButton;
    private JButton logoutButton;
    private JButton viewRequestStatusButton;
    
    
    // Dependencies
    private final StudentService studentService;
    private final MainFrame.PanelNavigator navigator;
    
    // State
    private Student loggedInStudent;
    
    public StudentDashboardPanel(StudentService studentService, Student loggedInStudent, MainFrame.PanelNavigator navigator) {
        this.studentService = studentService;
        this.loggedInStudent = loggedInStudent;
        this.navigator = navigator;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome Label
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        viewScheduleButton = new JButton("View Class Schedule");
        requestSubjectChangeButton = new JButton("Request Subject Change");
        deletePendingRequestButton = new JButton("Delete Pending Subject Change Request");
        viewPaymentStatusButton = new JButton("View Payment Status");
        updateProfileButton = new JButton("Update My Profile");
        logoutButton = new JButton("Logout");
        viewRequestStatusButton = new JButton("View Request Status");
        
        buttonPanel.add(viewScheduleButton);
        buttonPanel.add(requestSubjectChangeButton);
        buttonPanel.add(deletePendingRequestButton);
        buttonPanel.add(viewPaymentStatusButton);
        buttonPanel.add(updateProfileButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(viewRequestStatusButton);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        // Button Actions
        viewScheduleButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.VIEW_SCHEDULE_PANEL, loggedInStudent);
            System.out.println("Student: Navigating to View Schedule Panel.");
        });
        
        requestSubjectChangeButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.SUBJECT_CHANGE_REQUEST_PANEL, loggedInStudent);
            System.out.println("Student: Navigating to Subject Change Request Panel.");
        });
        
        deletePendingRequestButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.DELETE_PENDING_REQUEST_PANEL, loggedInStudent);
            System.out.println("Student: Navigating to Delete Pending Subject Change Request Panel.");
        });
        
        viewPaymentStatusButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.VIEW_PAYMENT_STATUS_PANEL, loggedInStudent);
            System.out.println("Student: Navigating to View Payment Status Panel.");
        });
        
        updateProfileButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.UPDATE_STUDENT_PROFILE_PANEL, loggedInStudent);
            System.out.println("Student: Navigating to Update Profile Panel.");
        });
        
        viewRequestStatusButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.VIEW_REQUEST_STATUS_PANEL, loggedInStudent);
            System.out.println("Student: Navigating to View Request Status Panel.");
        });
        
        logoutButton.addActionListener(e -> {
            navigator.logout();
        });
        
        updateUserContext(loggedInStudent);
    }
    
    @Override
    public void updateUserContext(User user) {
        if (user instanceof Student) {
            this.loggedInStudent = (Student) user;
            welcomeLabel.setText("Welcome, " + loggedInStudent.getFullName() + "!");
        } else {
            welcomeLabel.setText("Welcome, Student!");
        }
    }
}
