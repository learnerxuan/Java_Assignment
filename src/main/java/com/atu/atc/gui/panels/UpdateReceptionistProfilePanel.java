package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;
import com.atu.atc.util.Validator;

import javax.swing.*;
import java.awt.*;

/**
 * @author henge
 */
public class UpdateReceptionistProfilePanel extends JPanel{
    private final ReceptionistService receptionistService;
    private Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    public UpdateReceptionistProfilePanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Update My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JTextField nameField = new JTextField(receptionist.getFullName());
        JTextField phoneField = new JTextField(receptionist.getPhoneNumber());
        JTextField emailField = new JTextField(receptionist.getEmail());
        JTextField genderField = new JTextField(receptionist.getGender());
        JPasswordField passwordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();

        passwordField.setEchoChar('*');
        newPasswordField.setEchoChar('*');

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);

        formPanel.add(new JLabel("Current Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("New Password:"));
        formPanel.add(newPasswordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("back");

        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        updateBtn.addActionListener(e -> {
            String enteredPassword = new String(passwordField.getPassword());
            String actualPassword = receptionist.getPassword();

            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String gender = genderField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || gender.isEmpty() || enteredPassword.isEmpty() || newPassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if(!Validator.isValidEmail(email)){
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }

            if(!Validator.isValidPhoneNumber(phone)){
                JOptionPane.showMessageDialog(this, "Invalid phone number");
                return;
            }

            if (!enteredPassword.equals(actualPassword)) {
                JOptionPane.showMessageDialog(this, "Incorrect password. Please enter your current password to confirm changes.");
                return;
            }

            receptionistService.updateProfile(receptionist, newPassword, name, phone, email, gender);
            JOptionPane.showMessageDialog(this, "Profile updated successfully.");

            nameField.setText("");
            phoneField.setText("");
            emailField.setText("");
            genderField.setText("");
            passwordField.setText("");
            newPasswordField.setText("");
        });

        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist);
        });
    }
}
