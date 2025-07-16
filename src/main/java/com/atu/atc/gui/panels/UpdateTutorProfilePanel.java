/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GUI panel for updating a Tutor's profile.
 * Allows the logged-in tutor to change their personal details.
 * Implements DashboardPanelInterface for context updates.
 *
 * @author haoshuan (adapted from UpdateAdminProfilePanel structure)
 */
public class UpdateTutorProfilePanel extends JPanel implements DashboardPanelInterface {

    private MainFrame.PanelNavigator navigator;
    private TutorService tutorService;
    private Tutor loggedInTutor; // The tutor whose profile is being updated

    // UI Components
    private JLabel tutorIdLabel;
    private JTextField fullNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField genderField;
    private JPasswordField passwordField; // For new password
    private JButton saveButton;
    private JButton backButton;
    private JLabel messageLabel;

    /**
     * Constructor for UpdateTutorProfilePanel.
     *
     * @param tutorService The service layer for tutor-specific business logic.
     * @param navigator The PanelNavigator instance from MainFrame for screen transitions.
     */
    public UpdateTutorProfilePanel(TutorService tutorService, MainFrame.PanelNavigator navigator) {
        this.tutorService = tutorService;
        this.navigator = navigator;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Update My Profile");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        // Tutor ID (Non-editable)
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Tutor ID:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        tutorIdLabel = new JLabel("N/A"); // Will be updated by updateUserContext
        add(tutorIdLabel, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        fullNameField = new JTextField(20);
        add(fullNameField, gbc);

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        phoneNumberField = new JTextField(20);
        add(phoneNumberField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        emailField = new JTextField(20);
        add(emailField, gbc);

        // Gender
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        genderField = new JTextField(20);
        add(genderField, gbc);

        // New Password
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("New Password (leave blank to keep current):"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save Changes");
        backButton = new JButton("Back to Dashboard");
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        // Message Label
        gbc.gridy++;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        add(messageLabel, gbc);

        // --- Action Listeners ---
        saveButton.addActionListener(e -> saveProfileChanges());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));
    }

    /**
     * Populates the fields with the current tutor's data when the panel is shown.
     * @param user The currently logged-in Tutor object.
     */
    @Override
    public void updateUserContext(User user) {
        if (user instanceof Tutor) {
            this.loggedInTutor = (Tutor) user;
            tutorIdLabel.setText(loggedInTutor.getId());
            fullNameField.setText(loggedInTutor.getFullName());
            phoneNumberField.setText(loggedInTutor.getPhoneNumber());
            emailField.setText(loggedInTutor.getEmail());
            genderField.setText(loggedInTutor.getGender());
            passwordField.setText(""); // Always clear password field for security
            messageLabel.setText(""); // Clear any previous messages
        } else {
            messageLabel.setText("Error: Invalid user context provided.");
            messageLabel.setForeground(Color.RED);
            // Optionally disable fields if context is wrong
        }
    }

    private void saveProfileChanges() {
        String newPassword = new String(passwordField.getPassword());
        String newFullName = fullNameField.getText().trim();
        String newPhoneNumber = phoneNumberField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newGender = genderField.getText().trim();

        // Client-side validation for non-password fields
        if (newFullName.isEmpty() || newPhoneNumber.isEmpty() || newEmail.isEmpty() || newGender.isEmpty()) {
            messageLabel.setText("Full Name, Phone Number, Email, and Gender cannot be empty.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        // Determine password to use: new one if provided, else current one
        String passwordToUpdate = newPassword.isEmpty() ? loggedInTutor.getPassword() : newPassword;

        // Call the service layer to update the profile
        // The service method will perform further validation and persistence
        tutorService.updateTutorProfile(loggedInTutor, passwordToUpdate, newFullName, newPhoneNumber, newEmail, newGender);

        // After service call, update the in-memory loggedInTutor object
        // This is crucial so the dashboard reflects the latest data without reloading
        loggedInTutor.setPassword(passwordToUpdate);
        loggedInTutor.setFullName(newFullName);
        loggedInTutor.setPhoneNumber(newPhoneNumber);
        loggedInTutor.setEmail(newEmail);
        loggedInTutor.setGender(newGender);

        messageLabel.setText("Profile updated successfully!");
        messageLabel.setForeground(new Color(0, 128, 0)); // Green for success
        passwordField.setText(""); // Clear password field for security
    }
}

