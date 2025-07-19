package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;
import com.atu.atc.data.StudentRepository;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import java.awt.*;

public class UpdateStudentProfilePanel extends JPanel {
    private final StudentService studentService;
    private final Student student;
    private final MainFrame.PanelNavigator navigator;
    
    public UpdateStudentProfilePanel(StudentService studentService, Student student, MainFrame.PanelNavigator navigator) {
        this.studentService = studentService;
        this.student = student;
        this.navigator = navigator;
        
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Update My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));
        
        JTextField phoneField = new JTextField(student.getPhoneNumber());
        JTextField emailField = new JTextField(student.getEmail());
        JTextField addressField = new JTextField(student.getAddress());
        JPasswordField passwordField = new JPasswordField(student.getPassword());
        JPasswordField newPasswordField = new JPasswordField();
        
        passwordField.setEchoChar('*');
        newPasswordField.setEchoChar('*');
        
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);
        
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        
        formPanel.add(new JLabel("Current Password:"));
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("New Password:"));
        formPanel.add(newPasswordField);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("Back");
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        updateBtn.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();
            String currentPassword = new String(passwordField.getPassword()).trim();
            String newPassword = new String(newPasswordField.getPassword()).trim();
            
            if (phone.isEmpty() || email.isEmpty() || address.isEmpty() || currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
            
            if (!student.getPassword().equals(currentPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect password. Please enter your current password to confirm changes.");
                return;
            }
            
            student.setPhoneNumber(phone);
            student.setEmail(email);
            student.setAddress(address);
            student.setPassword(newPassword);
            
            if (studentService.update(student)) {
                studentRepository.save();
                JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile.");
            }
        });
        
        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, student);
        });
    }
}
