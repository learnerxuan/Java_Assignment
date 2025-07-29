package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UpdateTutorProfilePanel extends JPanel {

    private final MainFrame.PanelNavigator navigator;
    private final TutorService tutorService;
    private Tutor loggedInTutor;

    private JLabel tutorIdLabel;
    private JTextField fullNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField genderField;
    private JPasswordField passwordField;
    private JButton saveButton;
    private JButton backButton;
    private JLabel messageLabel;

    public UpdateTutorProfilePanel(TutorService tutorService, MainFrame.PanelNavigator navigator, Tutor loggedInTutor) {
        this.tutorService = tutorService;
        this.navigator = navigator;
        this.loggedInTutor = loggedInTutor;

        setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout());

        initUI();
        addListeners();
        populateFields();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("✏️ Update My Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 240), 2, true),
            new EmptyBorder(30, 50, 30, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; formPanel.add(new JLabel("Tutor ID:"), gbc);
        tutorIdLabel = new JLabel("N/A");
        gbc.gridx = 1; formPanel.add(tutorIdLabel, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Full Name:"), gbc);
        fullNameField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Phone Number:"), gbc);
        phoneNumberField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(phoneNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("Gender:"), gbc);
        genderField = new JTextField(20);
        gbc.gridx = 1; formPanel.add(genderField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; formPanel.add(new JLabel("New Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2;
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(messageLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        saveButton = createStyledButton("💾 Save Changes", new Color(70, 130, 180));
        backButton = createStyledButton("🔙 Back", new Color(220, 150, 150));
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(new Color(245, 250, 255));
        centerWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        centerWrapper.add(formPanel, BorderLayout.CENTER);
        centerWrapper.add(buttonPanel, BorderLayout.SOUTH);

        add(centerWrapper, BorderLayout.CENTER);
    }

    private void populateFields() {
        if (loggedInTutor != null) {
            tutorIdLabel.setText(loggedInTutor.getId());
            fullNameField.setText(loggedInTutor.getFullName());
            phoneNumberField.setText(loggedInTutor.getPhoneNumber());
            emailField.setText(loggedInTutor.getEmail());
            genderField.setText(loggedInTutor.getGender());
            passwordField.setText("");
            messageLabel.setText(" ");
        } else {
            showMessage("❌ Error: No tutor loaded.", Color.RED);
        }
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.darker(), 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private void addListeners() {
        saveButton.addActionListener(e -> saveProfileChanges());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));
    }

    private void saveProfileChanges() {
        if (loggedInTutor == null) {
            showMessage("❌ Error: No tutor loaded.", Color.RED);
            return;
        }

        String newPassword = new String(passwordField.getPassword());
        String newFullName = fullNameField.getText().trim();
        String newPhone = phoneNumberField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newGender = genderField.getText().trim();

        if (newFullName.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty() || newGender.isEmpty()) {
            showMessage("⚠️ Fields cannot be empty.", Color.RED);
            return;
        }

        String passwordToUse = newPassword.isEmpty() ? loggedInTutor.getPassword() : newPassword;

        String result = tutorService.updateTutorProfile(
            loggedInTutor, passwordToUse, newFullName, newPhone, newEmail, newGender
        );

        if (result.equals("Profile updated successfully.")) {
            loggedInTutor.setPassword(passwordToUse);
            loggedInTutor.setFullName(newFullName);
            loggedInTutor.setPhoneNumber(newPhone);
            loggedInTutor.setEmail(newEmail);
            loggedInTutor.setGender(newGender);

            passwordField.setText("");
            showMessage("✅ " + result, new Color(0, 128, 0));
        } else {
            showMessage("❌ " + result, Color.RED);
        }
    }

    private void showMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
}
