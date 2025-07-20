package com.atu.atc.gui.panels;

import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;
import com.atu.atc.gui.MainFrame.PanelNavigator;
import com.atu.atc.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UpdateStudentProfilePanel extends JPanel {
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JPasswordField passwordField;
    private JButton saveButton;
    private JButton backButton;
    private Student currentStudent;
    private PanelNavigator navigator;
    private StudentService studentService;
    
    public UpdateStudentProfilePanel(StudentService studentService, PanelNavigator navigator, Student student) {
        this.studentService = studentService;
        this.currentStudent = student;
        this.navigator = navigator;
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Update Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(currentStudent.getPhoneNumber());
        formPanel.add(phoneField);
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(currentStudent.getEmail());
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField(currentStudent.getAddress());
        formPanel.add(addressField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(currentStudent.getPassword());
        formPanel.add(passwordField);
        
        saveButton = new JButton("Save");
        backButton = new JButton("Back");
        
        formPanel.add(backButton);
        formPanel.add(saveButton);
        
        add(formPanel, BorderLayout.CENTER);
        
        saveButton.addActionListener((ActionEvent e) -> {
            String newPhone = phoneField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newAddress = addressField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            
            studentService.updateProfile(currentStudent, newPassword, newPhone, newEmail, newAddress);
            
            JOptionPane.showMessageDialog(this, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        
        backButton.addActionListener((ActionEvent e) -> {
            navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent);
        });
    }
}
