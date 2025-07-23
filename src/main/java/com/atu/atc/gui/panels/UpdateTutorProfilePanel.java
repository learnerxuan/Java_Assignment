package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import java.awt.*;

public class UpdateTutorProfilePanel extends JPanel implements DashboardPanelInterface {

    private MainFrame.PanelNavigator navigator;
    private TutorService tutorService;
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

    public UpdateTutorProfilePanel(TutorService tutorService, MainFrame.PanelNavigator navigator) {
        this.tutorService = tutorService;
        this.navigator = navigator;
        buildUI();
        addListeners();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Update My Profile");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        gbc.gridwidth = 1;

        addRow(gbc, 1, "Tutor ID:", tutorIdLabel = new JLabel("N/A"));
        addRow(gbc, 2, "Full Name:", fullNameField = new JTextField(20));
        addRow(gbc, 3, "Phone Number:", phoneNumberField = new JTextField(20));
        addRow(gbc, 4, "Email:", emailField = new JTextField(20));
        addRow(gbc, 5, "Gender:", genderField = new JTextField(20));
        addRow(gbc, 6, "New Password (leave blank to keep current):", passwordField = new JPasswordField(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save Changes");
        backButton = new JButton("Back to Dashboard");
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Message
        gbc.gridy++;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        add(messageLabel, gbc);
    }

    private void addRow(GridBagConstraints gbc, int row, String labelText, JComponent input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(input, gbc);
    }

    private void addListeners() {
        saveButton.addActionListener(e -> saveProfileChanges());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));
    }

    @Override
    public void updateUserContext(User user) {
        if (!(user instanceof Tutor)) {
            messageLabel.setText("Error: Invalid user context.");
            return;
        }

        this.loggedInTutor = (Tutor) user;
        tutorIdLabel.setText(loggedInTutor.getId());
        fullNameField.setText(loggedInTutor.getFullName());
        phoneNumberField.setText(loggedInTutor.getPhoneNumber());
        emailField.setText(loggedInTutor.getEmail());
        genderField.setText(loggedInTutor.getGender());
        passwordField.setText("");
        messageLabel.setText(" ");
    }

    private void saveProfileChanges() {
    String newPassword = new String(passwordField.getPassword());
    String newFullName = fullNameField.getText().trim();
    String newPhone = phoneNumberField.getText().trim();
    String newEmail = emailField.getText().trim();
    String newGender = genderField.getText().trim();

    if (newFullName.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty() || newGender.isEmpty()) {
        messageLabel.setText("Fields cannot be empty.");
        messageLabel.setForeground(Color.RED);
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

        messageLabel.setText(result);
        messageLabel.setForeground(new Color(0, 128, 0));
        passwordField.setText("");
    } else {
        messageLabel.setText(result);
        messageLabel.setForeground(Color.RED);
    }
}

    private void showMessage(String msg, Color color) {
        messageLabel.setText(msg);
        messageLabel.setForeground(color);
    }
}
