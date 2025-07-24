/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AdminService;
import com.atu.atc.model.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Update My Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(25, 25, 112));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel (Central Card)
        JPanel formCardPanel = new JPanel(new GridBagLayout());
        formCardPanel.setBackground(Color.WHITE);
        formCardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true),
                new EmptyBorder(25, 30, 25, 30)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ID
        JLabel idLabel = new JLabel("Admin ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        idLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formCardPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        idField = new JTextField(25);
        idField.setEditable(false);
        idField.setBackground(new Color(230, 230, 230));
        idField.setFont(new Font("Arial", Font.PLAIN, 16));
        idField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formCardPanel.add(idField, gbc);
        row++;

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        fullNameLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formCardPanel.add(fullNameLabel, gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(25);
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        fullNameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formCardPanel.add(fullNameField, gbc);
        row++;

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formCardPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formCardPanel.add(passwordField, gbc);
        row++;

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        phoneLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formCardPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(25);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 16));
        phoneField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formCardPanel.add(phoneField, gbc);
        row++;

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formCardPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        emailField = new JTextField(25);
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formCardPanel.add(emailField, gbc);
        row++;

        // Gender
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        genderLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formCardPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        String[] genders = {"", "Male", "Female"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        genderComboBox.setBackground(Color.WHITE);
        formCardPanel.add(genderComboBox, gbc);
        row++;

        // Message Label (placed within the formCardPanel for better positioning)
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formCardPanel.add(messageLabel, gbc);
        row++;

        // Add formCardPanel to the center, wrapped in a panel for centering
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setBackground(new Color(240, 248, 255));
        centerWrapperPanel.add(formCardPanel);
        add(centerWrapperPanel, BorderLayout.CENTER);

        // Control Panel (SOUTH)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(240, 248, 255));
        updateButton = new JButton("Update Profile");
        backButton = new JButton("Back to Dashboard");

        // Apply button styling
        styleButton(updateButton);
        styleButton(backButton);

        controlPanel.add(updateButton);
        controlPanel.add(backButton);
        add(controlPanel, BorderLayout.SOUTH);

        // Attach Action Listeners
        updateButton.addActionListener(e -> updateProfile());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));

        // Populate fields with current admin data on panel load
        populateFields();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(65, 105, 225)); // Royal Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 85, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225));
            }
        });
    }

    private void populateFields() {
        if (loggedInAdmin != null) {
            idField.setText(loggedInAdmin.getId()); // Assuming getUserId() for ID
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
            loggedInAdmin.getId(), // Keep existing ID
            newPassword,
            newFullName,
            newPhone,
            newEmail,
            newGender
        );

        boolean success = adminService.updateAdminProfile(updatedAdmin);

        if (success) {
            this.loggedInAdmin = updatedAdmin; // Update the local reference to the new Admin object
            messageLabel.setText("Profile updated successfully!");
            messageLabel.setForeground(new Color(0, 128, 0)); // Green for success

            // Navigate back to the dashboard with the updated Admin object
            // This ensures the DashboardPanel is re-initialized with the latest data
            navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, this.loggedInAdmin);
            System.out.println("Admin profile updated. Navigating back to dashboard.");

        } else {
            messageLabel.setText("Profile update failed. Check inputs or console for details.");
        }
    }
}


