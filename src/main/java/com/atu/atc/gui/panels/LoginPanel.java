/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame; 
import com.atu.atc.service.AuthService; 
import com.atu.atc.model.User; 
import java.util.Optional;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Xuan
 */
public class LoginPanel extends JPanel {

    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    // Dependencies
    private final AuthService authService;
    private final MainFrame.PanelNavigator navigator;

    public LoginPanel(AuthService authService, MainFrame.PanelNavigator navigator) {
        this.authService = authService;
        this.navigator = navigator;

        // Panel Layout and Styling 
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Padding around the panel

        // Input Panel for User ID and Password 
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns, with gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Inner padding

        // User ID Row
        inputPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField(20);
        inputPanel.add(userIdField);

        // Password Row
        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        inputPanel.add(passwordField);

        // Empty cell for alignment before the button
        inputPanel.add(new JLabel(""));

        // Login Button
        loginButton = new JButton("Login");
        // Attach the inner LoginButtonListener to the login button
        loginButton.addActionListener(new LoginButtonListener());
        inputPanel.add(loginButton);

        // Add the inputPanel to the center of the LoginPanel
        add(inputPanel, BorderLayout.CENTER);

        // Message Label 
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        messageLabel.setForeground(Color.RED); // Default color for error messages
        add(messageLabel, BorderLayout.SOUTH); // Add message label to the bottom of the LoginPanel
    }

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

            // Clear previous message
            messageLabel.setText("");
            messageLabel.setForeground(Color.RED); 

            // Call authenticateUser 
            Optional<User> authenticatedUserOptional = authService.authenticateUser(userId, password);

            if (authenticatedUserOptional.isPresent()) {
                User loggedInUser = authenticatedUserOptional.get();
                messageLabel.setText("Login successful! Welcome, " + loggedInUser.getFullName());
                messageLabel.setForeground(Color.BLUE); 
                clearFields(); // Clear fields after successful login

                // Request MainFrame to navigate to the appropriate dashboard
                navigator.navigateTo(loggedInUser.getRole() + "Dashboard", loggedInUser);
            } else {
                messageLabel.setText("Login failed. Invalid User ID or Password, or account locked.");
                messageLabel.setForeground(Color.RED); 
                clearFields(); 
            }
        }
    }
}

