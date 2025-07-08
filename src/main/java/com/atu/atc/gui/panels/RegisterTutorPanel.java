/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AdminService; 
import com.atu.atc.model.Admin; 
import com.atu.atc.model.Tutor; 

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 *
 * @author Xuan
 */

public class RegisterTutorPanel extends JPanel {

    // UI Components 
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox; 
    private JComboBox<String> subjectComboBox; 
    private JComboBox<String> levelComboBox; 
    private JButton registerButton;
    private JButton backButton;
    private JLabel messageLabel; 

    // Dependencies 
    private final AdminService adminService;
    private final MainFrame.PanelNavigator navigator;

    // State 
    private final Admin loggedInAdmin; 

    public RegisterTutorPanel(AdminService adminService, MainFrame.PanelNavigator navigator, Admin loggedInAdmin) {
        this.adminService = adminService;
        this.navigator = navigator;
        this.loggedInAdmin = loggedInAdmin; 

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); 

        JLabel titleLabel = new JLabel("Register New Tutor", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        int row = 0;

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(25);
        formPanel.add(fullNameField, gbc);
        row++;

        // Password
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(25);
        formPanel.add(passwordField, gbc);
        row++;

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(25);
        formPanel.add(phoneField, gbc);
        row++;

        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(25);
        formPanel.add(emailField, gbc);
        row++;

        // Gender
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        // Added empty string for initial selection
        String[] genders = {"", "Male", "Female"}; 
        genderComboBox = new JComboBox<>(genders);
        formPanel.add(genderComboBox, gbc);
        row++;

        // Subject 
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        String[] subjects = {"", "Mathematics", "Science", "English", "History", "Biology"}; 
        subjectComboBox = new JComboBox<>(subjects);
        formPanel.add(subjectComboBox, gbc);
        row++;

        // Level 
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 1;
        String[] levels = {"", "1", "2", "3", "4", "5"}; 
        levelComboBox = new JComboBox<>(levels);
        formPanel.add(levelComboBox, gbc);
        row++;

 
        add(formPanel, BorderLayout.CENTER);

        // Control Panel 
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        registerButton = new JButton("Register Tutor");
        backButton = new JButton("Back to Dashboard");

        controlPanel.add(registerButton);
        controlPanel.add(backButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.RED); 

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; 
        formPanel.add(messageLabel, gbc);

        // Attach Action Listeners 
        registerButton.addActionListener(e -> registerTutor());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));
    }

    private void registerTutor() {
        // Clear previous messages
        messageLabel.setText("");
        messageLabel.setForeground(Color.RED);

        // Get input from UI components
        String fullName = fullNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        
        // Get selected String values from JComboBox
        String gender = (String) genderComboBox.getSelectedItem();
        String subject = (String) subjectComboBox.getSelectedItem();
        String level = (String) levelComboBox.getSelectedItem();

        // Validation
        if (fullName.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() ||
            gender == null || gender.isEmpty() || 
            subject == null || subject.isEmpty() || 
            level == null || level.isEmpty()) { 
            messageLabel.setText("Please fill in all fields.");
            return;
        }

       
        Optional<Tutor> registeredTutorOptional = adminService.registerTutor(fullName, password, phone, email, gender, subject, level);

        // Handle Service Result
        if (registeredTutorOptional.isPresent()) {
            Tutor newTutor = registeredTutorOptional.get();
            messageLabel.setText("Tutor '" + newTutor.getFullName() + "' registered successfully! User ID: " + newTutor.getId());
            messageLabel.setForeground(new Color(0, 100, 0)); 
            clearFields(); // Clear the form after successful registration
        } else {
            messageLabel.setText("Tutor registration failed. Check inputs or refer to console.");
        }
    }

    // Clears all input fields on the form.
    private void clearFields() {
        fullNameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        emailField.setText("");
        genderComboBox.setSelectedIndex(0); 
        subjectComboBox.setSelectedIndex(0); 
        levelComboBox.setSelectedIndex(0); 
    }
}
