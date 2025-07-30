package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.User;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        updateUserContext(loggedInStudent);
    }
    
    private void initializeComponents() {
        setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout(20, 20));
        
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(25, 25, 112));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        viewScheduleButton = createStyledButton("View Class Schedule");
        requestSubjectChangeButton = createStyledButton("Request Subject Change");
        deletePendingRequestButton = createStyledButton("Delete Pending Subject Change Request");
        viewPaymentStatusButton = createStyledButton("View Payment Status");
        updateProfileButton = createStyledButton("Update My Profile");
        viewRequestStatusButton = createStyledButton("View Request Status");
        logoutButton = createLogoutButton("Logout");
    }
    
    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(65, 105, 225));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 85, 200)); // Slightly darker blue on hover
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225)); // Original color on exit
            }
        });
        
        return button;
    }
    
    private JButton createLogoutButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(255, 245, 245));
        button.setForeground(new Color(180, 65, 65));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 200, 200), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 235)); // Slightly darker red on hover
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 245, 245)); // Original color on exit
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        JLabel subtitleLabel = new JLabel("Student Dashboard", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 120, 140)); // Muted blue-gray
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Button Panel (Central Card)
        JPanel buttonCardPanel = new JPanel(new GridLayout(7, 1, 15, 15));
        buttonCardPanel.setBackground(Color.WHITE); // White background for the card
        buttonCardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true), // Light blue border, rounded corners
                new EmptyBorder(30, 80, 30, 80)
        ));
        
        buttonCardPanel.add(viewScheduleButton);
        buttonCardPanel.add(requestSubjectChangeButton);
        buttonCardPanel.add(deletePendingRequestButton);
        buttonCardPanel.add(viewPaymentStatusButton);
        buttonCardPanel.add(updateProfileButton);
        buttonCardPanel.add(viewRequestStatusButton);
        buttonCardPanel.add(logoutButton);
        
        // Add the button card panel to the center, wrapped in another panel for centering
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setBackground(new Color(240, 248, 255)); // Match main background
        centerWrapperPanel.add(buttonCardPanel);
        
        add(headerPanel, BorderLayout.NORTH);
        add(centerWrapperPanel, BorderLayout.CENTER);
    }
    
    private void setupEventListeners() {
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
    }
    
    @Override
    public void updateUserContext(User user) {
        if (user instanceof Student) {
            this.loggedInStudent = (Student) user;
            welcomeLabel.setText("Welcome back, " + loggedInStudent.getFullName() + "!");
            welcomeLabel.revalidate();
            welcomeLabel.repaint();
        } else {
            welcomeLabel.setText("Welcome, Student!");
        }
    }
}