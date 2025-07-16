package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;
import com.atu.atc.service.AuthService;
import com.atu.atc.service.StudentService;
import com.atu.atc.util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class StudentProfilePanel extends JPanel {
    private final MainFrame navigatorFrame;
    private final StudentService studentService;
    private final AuthService authService;
    private Student currentStudent; // The student whose profile is being viewed/edited
    
    private JTextField idField;
    private JTextField fullNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField genderField; // Consider JComboBox if gender is fixed
    private JTextField icPassportField;
    private JTextField addressField;
    private JTextField monthOfEnrollField;
    private JTextField levelField;
    private JPasswordField passwordField;
    private JLabel lblStatus;
    
    public StudentProfilePanel(MainFrame navigatorFrame, StudentService studentService, AuthService authService) {
        this.navigatorFrame = navigatorFrame;
        this.studentService = studentService;
        this.authService = authService;
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Labels and Text Fields
        JLabel lblId = new JLabel("Student ID:");
        idField = new JTextField(20);
        idField.setEditable(false); // ID should not be editable
        
        JLabel lblFullName = new JLabel("Full Name:");
        fullNameField = new JTextField(20);
        
        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        phoneNumberField = new JTextField(20);
        
        JLabel lblEmail = new JLabel("Email:");
        emailField = new JTextField(20);
        
        JLabel lblGender = new JLabel("Gender:");
        genderField = new JTextField(20);
        
        JLabel lblIcPassport = new JLabel("IC/Passport:");
        icPassportField = new JTextField(20);
        
        JLabel lblAddress = new JLabel("Address:");
        addressField = new JTextField(20);
        
        JLabel lblMonthOfEnroll = new JLabel("Month of Enrollment:");
        monthOfEnrollField = new JTextField(20);
        
        JLabel lblLevel = new JLabel("Level:");
        levelField = new JTextField(20);
        
        JLabel lblPassword = new JLabel("New Password (Leave blank to keep current):");
        passwordField = new JPasswordField(20);
        
        
        // Add components to formPanel
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblFullName, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(fullNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblPhoneNumber, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(phoneNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lblGender, gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(genderField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(lblIcPassport, gbc);
        gbc.gridx = 1; gbc.gridy = 5; formPanel.add(icPassportField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(lblAddress, gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(addressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7; formPanel.add(lblMonthOfEnroll, gbc);
        gbc.gridx = 1; gbc.gridy = 7; formPanel.add(monthOfEnrollField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; formPanel.add(lblLevel, gbc);
        gbc.gridx = 1; gbc.gridy = 8; formPanel.add(levelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9; formPanel.add(lblPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 9; formPanel.add(passwordField, gbc);
        
        lblStatus = new JLabel("");
        lblStatus.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2; formPanel.add(lblStatus, gbc);
        
        
        JButton btnUpdateProfile = new JButton("Update Profile");
        btnUpdateProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnUpdateProfile);
        buttonPanel.add(btnBack);
        
        add(new JLabel("Student Profile", SwingConstants.CENTER), BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void setStudent(Student student) {
        this.currentStudent = student;
        if (currentStudent != null) {
            idField.setText(currentStudent.getId());
            fullNameField.setText(currentStudent.getFullName());
            phoneNumberField.setText(currentStudent.getPhoneNumber());
            emailField.setText(currentStudent.getEmail());
            genderField.setText(currentStudent.getGender());
            icPassportField.setText(currentStudent.getIcPassport());
            addressField.setText(currentStudent.getAddress());
            monthOfEnrollField.setText(currentStudent.getMonthOfEnroll());
            levelField.setText(currentStudent.getLevel());
            passwordField.setText(""); // Clear password field for security
            lblStatus.setText("");
        }
    }
    
    private void updateProfile() {
        if (currentStudent == null) {
            lblStatus.setText("Error: No student selected.");
            return;
        }
        
        String newFullName = fullNameField.getText().trim();
        String newPhoneNumber = phoneNumberField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newGender = genderField.getText().trim();
        String newIcPassport = icPassportField.getText().trim();
        String newAddress = addressField.getText().trim();
        String newMonthOfEnroll = monthOfEnrollField.getText().trim();
        String newLevel = levelField.getText().trim();
        String newPassword = new String(passwordField.getPassword());
        
        if (newFullName.isEmpty() || newPhoneNumber.isEmpty() || newEmail.isEmpty() ||
                newGender.isEmpty() || newIcPassport.isEmpty() || newAddress.isEmpty() ||
                newMonthOfEnroll.isEmpty() || newLevel.isEmpty()) {
            lblStatus.setText("All fields except new password are required.");
            return;
        }
        
        if (!Validator.isValidEmail(newEmail)) {
            lblStatus.setText("Invalid email format.");
            return;
        }
        if (!Validator.isValidPhoneNumber(newPhoneNumber)) {
            lblStatus.setText("Invalid phone number format.");
            return;
        }
        
        // Update current student object
        currentStudent.setFullName(newFullName);
        currentStudent.setPhoneNumber(newPhoneNumber);
        currentStudent.setEmail(newEmail);
        currentStudent.setGender(newGender);
        currentStudent.setIcPassport(newIcPassport);
        currentStudent.setAddress(newAddress);
        currentStudent.setMonthOfEnroll(newMonthOfEnroll);
        currentStudent.setLevel(newLevel);
        
        boolean profileUpdated = studentService.updateStudentProfile(currentStudent);
        
        boolean passwordUpdated = true;
        if (!newPassword.isEmpty()) {
            // Attempt to change password using AuthService
            passwordUpdated = authService.changePassword(currentStudent.getId(), newPassword);
        }
        
        if (profileUpdated && passwordUpdated) {
            lblStatus.setText("Profile updated successfully!");
            lblStatus.setForeground(Color.BLUE);
            // Re-fetch student to ensure all changes are reflected, including password hash if it was changed
            Optional<Student> updatedStudentOpt = authService.authenticateStudent(currentStudent.getId(), currentStudent.getPassword());
            // Update the current student reference
            updatedStudentOpt.ifPresent(student -> currentStudent = student);
        } else {
            lblStatus.setText("Failed to update profile. Check inputs.");
            lblStatus.setForeground(Color.RED);
        }
    }
}