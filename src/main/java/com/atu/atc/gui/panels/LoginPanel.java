/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AuthService;
import com.atu.atc.service.AuthResult;

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
    private JLabel messageLabel;

    // Dependencies
    private final AuthService authService;
    private final MainFrame.PanelNavigator navigator;

    public LoginPanel(AuthService authService, MainFrame.PanelNavigator navigator) {
        this.authService = authService;
        this.navigator = navigator;

        // Panel Styling
        setBackground(new Color(240, 248, 255)); 
        setLayout(new GridBagLayout()); 

        // Main Login Card Panel
        JPanel loginCardPanel = new JPanel(new GridBagLayout());
        loginCardPanel.setBackground(Color.WHITE); 
        loginCardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true), 
                new EmptyBorder(30, 40, 30, 40) 
        ));
        loginCardPanel.setPreferredSize(new Dimension(450, 400));
        loginCardPanel.setMaximumSize(new Dimension(450, 400));
        loginCardPanel.setMinimumSize(new Dimension(400, 350));

        // GridBagConstraints for layout within loginCardPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        int row = 0;

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to ATC System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2; 
        gbc.weighty = 0.1;
        loginCardPanel.add(titleLabel, gbc);
        row++;

        // User ID Input 
        JLabel userIdLabel = new JLabel("User ID:"); 
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userIdLabel.setForeground(new Color(70, 130, 180)); 
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1; 
        gbc.anchor = GridBagConstraints.WEST; 
        loginCardPanel.add(userIdLabel, gbc); 

        userIdField = new JTextField(20);
        userIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        userIdField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0; 
        loginCardPanel.add(userIdField, gbc);
        row++;

        // Password Input
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(70, 130, 180)); 
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0; 
        loginCardPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        loginCardPanel.add(passwordField, gbc);
        row++;

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        messageLabel.setForeground(Color.RED);
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
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(50, 85, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(65, 105, 225));
            }
        });

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
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
            String userId = userIdField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            messageLabel.setText(""); // Clear previous message
            messageLabel.setForeground(Color.RED);

            if (userId.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter User ID and Password."); 
                return;
            }

            AuthResult result = authService.authenticateUser(userId, password);

            messageLabel.setText(result.getMessage());
            if (result.isSuccess()) {
                messageLabel.setForeground(new Color(0, 128, 0)); // Green for success
                clearFields(); // Clear fields after successful login
                navigator.navigateTo(result.getAuthenticatedUser().get().getRole() + "Dashboard", result.getAuthenticatedUser().get());
            } else {
                messageLabel.setForeground(Color.RED); // Red for failure
                clearFields();
            }
        }
    }
}

