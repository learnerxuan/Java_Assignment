package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;
import com.atu.atc.util.Validator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author henge
 */
public class UpdateReceptionistProfilePanel extends JPanel {
    private final ReceptionistService receptionistService;
    private Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    // UI Components
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;

    public UpdateReceptionistProfilePanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
        loadProfileData(); // Load data when panel is initialized
    }

    private void initUI() {
        setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Update My Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 25, 112));
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Profile Update Form
        JPanel profileFormPanel = new JPanel(new GridBagLayout());
        profileFormPanel.setBackground(Color.WHITE);
        profileFormPanel.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});

        addField(profileFormPanel, gbc, 0, "Full Name:", nameField);
        addField(profileFormPanel, gbc, 1, "Phone Number:", phoneField);
        addField(profileFormPanel, gbc, 2, "Email:", emailField);
        addField(profileFormPanel, gbc, 3, "Gender:", genderComboBox);

        JButton updateProfileButton = new JButton("Update Profile");
        styleButton(updateProfileButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        profileFormPanel.add(updateProfileButton, gbc);

        // Password Change Form
        JPanel passwordFormPanel = new JPanel(new GridBagLayout());
        passwordFormPanel.setBackground(Color.WHITE);
        passwordFormPanel.setBorder(BorderFactory.createTitledBorder("Change Password"));
        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.insets = new Insets(10, 10, 10, 10);
        pgbc.fill = GridBagConstraints.HORIZONTAL;

        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);

        addField(passwordFormPanel, pgbc, 0, "Current Password:", currentPasswordField);
        addField(passwordFormPanel, pgbc, 1, "New Password:", newPasswordField);

        JButton changePasswordButton = new JButton("Change Password");
        styleButton(changePasswordButton);
        pgbc.gridx = 0;
        pgbc.gridy = 2;
        pgbc.gridwidth = 2;
        pgbc.anchor = GridBagConstraints.CENTER;
        passwordFormPanel.add(changePasswordButton, pgbc);

        mainPanel.add(profileFormPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        mainPanel.add(passwordFormPanel);

        add(mainPanel, BorderLayout.CENTER);

        // Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255));
        JButton backBtn = new JButton("Back");
        styleButton(backBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        updateProfileButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all profile fields.");
                return;
            }

            if (!Validator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }

            if (!Validator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number");
                return;
            }

            receptionistService.updateProfile(receptionist, receptionist.getPassword(), name, phone, email, gender);
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");
            loadProfileData(); // Refresh UI after update
        });

        changePasswordButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in both password fields.");
                return;
            }

            if (!currentPassword.equals(receptionist.getPassword())) {
                JOptionPane.showMessageDialog(this, "Incorrect current password.");
                return;
            }

            receptionistService.updateProfile(receptionist, newPassword, receptionist.getFullName(), receptionist.getPhoneNumber(), receptionist.getEmail(), receptionist.getGender());
            JOptionPane.showMessageDialog(this, "Password changed successfully.");
            loadProfileData(); // Refresh UI after password change
        });

        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist));
    }

    private void loadProfileData() {
        nameField.setText(receptionist.getFullName());
        phoneField.setText(receptionist.getPhoneNumber());
        emailField.setText(receptionist.getEmail());
        genderComboBox.setSelectedItem(receptionist.getGender());
        currentPasswordField.setText(""); // Clear password fields
        newPasswordField.setText("");
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(65, 105, 225));
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
}