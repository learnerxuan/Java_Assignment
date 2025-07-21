/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AuthService;
import com.atu.atc.model.User;
import com.atu.atc.model.Admin; 
import com.atu.atc.model.Receptionist; 
import com.atu.atc.model.Tutor;
import com.atu.atc.model.Student; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * 
 * @author Xuan 
 */
public class LoginPanel extends JPanel {

    // UI Components 
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel; // For displaying login status messages

    // Dependencies 
    private final AuthService authService;
    private final MainFrame.PanelNavigator navigator;

    public LoginPanel(AuthService authService, MainFrame.PanelNavigator navigator) {
        this.authService = authService;
        this.navigator = navigator;

        // Panel Styling 
        setBackground(new Color(240, 248, 255)); // Alice Blue
        setLayout(new GridBagLayout()); // Use GridBagLayout for centering the login form

        // Main Login Card Panel
        JPanel loginCardPanel = new JPanel(new GridBagLayout());
        loginCardPanel.setBackground(Color.WHITE); // White background for the card
        loginCardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true), // Light blue border, rounded corners
                new EmptyBorder(30, 40, 30, 40) // Inner padding
        ));
        loginCardPanel.setPreferredSize(new Dimension(450, 400));
        loginCardPanel.setMaximumSize(new Dimension(450, 400)); 
        loginCardPanel.setMinimumSize(new Dimension(400, 350)); 

        // GridBagConstraints for layout within loginCardPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL; // Components fill horizontal space

        int row = 0;

        // Title Label 
        JLabel titleLabel = new JLabel("Welcome to ATC System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Larger, bolder font
        titleLabel.setForeground(new Color(25, 25, 112)); // Midnight Blue 
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; // Span across two columns
        gbc.weighty = 0.1;
        loginCardPanel.add(titleLabel, gbc);
        row++;

        // User ID Input 
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userIdLabel.setForeground(new Color(70, 130, 180)); // Steel Blue
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1; // Reset to 1 column
        gbc.anchor = GridBagConstraints.WEST; // Align label to the left
        loginCardPanel.add(userIdLabel, gbc);

        userIdField = new JTextField(20);
        userIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        userIdField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1), // Light blue border
                new EmptyBorder(5, 10, 5, 10) 
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Allow text field to take extra horizontal space
        loginCardPanel.add(userIdField, gbc);
        row++;

        // Password Input 
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(70, 130, 180)); // Steel Blue
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0; // Reset weight for label
        loginCardPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1), // Light blue border
                new EmptyBorder(5, 10, 5, 10) // Inner padding
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        loginCardPanel.add(passwordField, gbc);
        row++;

        // Message Label 
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.RED); // Default color for error messages
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; 
        gbc.weighty = 0.1; 
        loginCardPanel.add(messageLabel, gbc);
        row++;

        // Login Button 
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(65, 105, 225)); // Royal Blue
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setFocusPainted(false); // Remove focus border
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true), // Matching border, rounded
                new EmptyBorder(10, 25, 10, 25) // Padding
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        // Add a subtle hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(50, 85, 200)); // Slightly darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(65, 105, 225)); // Original color on exit
            }
        });

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; // Span across both columns
        gbc.weighty = 0.2; // Give it more vertical weight to push it down
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        loginCardPanel.add(loginButton, gbc);

        add(loginCardPanel, new GridBagConstraints()); 

        // Attach Action Listener 
        loginButton.addActionListener(new LoginButtonListener());
    }

    // Clears the User ID and Password fields.
    public void clearFields() {
        userIdField.setText("");
        passwordField.setText("");
    }
    
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get user input from the View's components
            String userId = userIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Clear previous message and reset color
            messageLabel.setText("");
            messageLabel.setForeground(Color.RED);

            // Basic client-side validation for empty fields
            if (userId.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter User ID and Password.");
                return;
            }

            // Call authentication service
            Optional<User> authenticatedUserOptional = authService.authenticateUser(userId, password);

            if (authenticatedUserOptional.isPresent()) {
                User loggedInUser = authenticatedUserOptional.get();
                messageLabel.setText("Login successful! Welcome, " + loggedInUser.getFullName() + "!");
                messageLabel.setForeground(new Color(0, 128, 0)); // Green for success

                clearFields(); // Clear fields after successful login

                navigator.navigateTo(loggedInUser.getRole() + "Dashboard", loggedInUser);
            } else {
                messageLabel.setText("Login failed. Invalid User ID or Password, or account locked.");
                messageLabel.setForeground(Color.RED);
                clearFields(); // Clear fields on failed login attempt
            }
        }
    }
}


