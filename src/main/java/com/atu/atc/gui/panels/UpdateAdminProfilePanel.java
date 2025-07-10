/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame; 
import com.atu.atc.service.AdminService; 
import com.atu.atc.model.Admin;
import com.atu.atc.model.User; 

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Xuan
 */
public class UpdateAdminProfilePanel extends JPanel{
    // UI Components
    private JTextField idField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox;
    private JButton updateButton;
    private JButton backButton;
    private JLabel messageLabel; 
    
    // Dependencies
    private final AdminService adminService;
    private final MainFrame.PanelNavigator navigator;
    
    // The current logged-in Admin 
    private Admin loggedInAdmin; 

    public UpdateAdminProfilePanel(AdminService adminService, MainFrame.PanelNavigator navigator, Admin loggedInAdmin) {
        this.adminService = adminService;
        this.navigator = navigator;
        this.loggedInAdmin = loggedInAdmin; 

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("Update My Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ID
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Admin ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(25);
        idField.setEditable(false);
        idField.setBackground(Color.LIGHT_GRAY); 
        formPanel.add(idField, gbc);
        row++;

        // Full Name
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(25);
        formPanel.add(fullNameField, gbc);
        row++;

        // Password
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(25);
        formPanel.add(passwordField, gbc);
        row++;

        // Phone Number
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(25);
        formPanel.add(phoneField, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(25);
        formPanel.add(emailField, gbc);
        row++;

        // Gender 
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        String[] genders = {"", "Male", "Female"};
        genderComboBox = new JComboBox<>(genders);
        formPanel.add(genderComboBox, gbc);
        row++;

        // Add formPanel to the center
        add(formPanel, BorderLayout.CENTER);

        // Control Panel (SOUTH)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        updateButton = new JButton("Update Profile");
        backButton = new JButton("Back to Dashboard");

        controlPanel.add(updateButton);
        controlPanel.add(backButton);
        add(controlPanel, BorderLayout.SOUTH);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.RED); // Red for errors

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; 
        formPanel.add(messageLabel, gbc);

        // Attach Action Listeners 
        updateButton.addActionListener(e -> updateProfile());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));

        // Populate fields with current admin data on panel load
        populateFields();
    }

    private void populateFields() {
        if (loggedInAdmin != null) {
            idField.setText(loggedInAdmin.getId()); 
            fullNameField.setText(loggedInAdmin.getFullName());
            passwordField.setText(loggedInAdmin.getPassword()); 
            phoneField.setText(loggedInAdmin.getPhoneNumber());
            emailField.setText(loggedInAdmin.getEmail());

            for (int i = 0; i < genderComboBox.getItemCount(); i++) {
                if (genderComboBox.getItemAt(i).equalsIgnoreCase(loggedInAdmin.getGender())) {
                    genderComboBox.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            messageLabel.setText("Error: Admin data not loaded.");
            messageLabel.setForeground(Color.RED);
        }
    }

    private void updateProfile() {
        messageLabel.setText("");
        messageLabel.setForeground(Color.RED);

        // Get input from UI components
        String newFullName = fullNameField.getText().trim();
        String newPassword = new String(passwordField.getPassword()).trim();
        String newPhone = phoneField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newGender = (String) genderComboBox.getSelectedItem();

        // Validation
        if (newFullName.isEmpty() || newPassword.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty() ||
            newGender == null || newGender.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        // Create an updated Admin object
        Admin updatedAdmin = new Admin(
            loggedInAdmin.getId(), 
            newPassword,
            newFullName,
            newPhone,
            newEmail,
            newGender
        );

        boolean success = adminService.updateAdminProfile(updatedAdmin);

        if (success) {
            this.loggedInAdmin = updatedAdmin;
            messageLabel.setText("Profile updated successfully!");
            messageLabel.setForeground(new Color(0, 100, 0)); 
            populateFields();
        } else {
            messageLabel.setText("Profile update failed. Check inputs or console for details.");
        }
    }
}
