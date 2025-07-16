package com.atu.atc.gui.panels;

import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;
import com.atu.atc.util.Validator;
import com.atu.atc.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author henge
 */
public class RegisterStudentPanel extends JPanel{
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    public RegisterStudentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Register New Student");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField icField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField levelField = new JTextField();

        JTextField class1Field = new JTextField();
        JTextField class2Field = new JTextField();
        JTextField class3Field = new JTextField();

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Password: "));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Phone Number: "));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);

        formPanel.add(new JLabel("IC/Passport:"));
        formPanel.add(icField);

        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        formPanel.add(new JLabel("Level (1-5):"));
        formPanel.add(levelField);

        formPanel.add(new JLabel("Class ID #1:"));
        formPanel.add(class1Field);

        formPanel.add(new JLabel("Class ID #2 (optional):"));
        formPanel.add(class2Field);

        formPanel.add(new JLabel("Class ID #3 (optional):"));
        formPanel.add(class3Field);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton submitBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");
        btnPanel.add(submitBtn);
        btnPanel.add(backBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // Submit Action
        submitBtn.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String gender = genderField.getText().trim();
            String ic = icField.getText().trim();
            String address = addressField.getText().trim();
            String level = levelField.getText().trim();

            List<String> classIds = new ArrayList<>();

            if (!class1Field.getText().trim().isEmpty()){
                classIds.add(class1Field.getText().trim());
            }
            if (!class2Field.getText().trim().isEmpty()){
                classIds.add(class2Field.getText().trim());
            }
            if (!class3Field.getText().trim().isEmpty()){
                classIds.add(class3Field.getText().trim());
            }

            // Basic validations
            if (name.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty()
            || gender.isEmpty() || ic.isEmpty() || address.isEmpty() || level.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            if (!Validator.isValidEmail(email)){
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }

            if (!Validator.isValidPhoneNumber(phone)){
                JOptionPane.showMessageDialog(this, "Invalid phone number.");
                return;
            }

            if (!Validator.isFormValid(level)){
                JOptionPane.showMessageDialog(this, "Level must be between 1 and 5.");
                return;
            }

            if (classIds.size() > 3){
                JOptionPane.showMessageDialog(this, "You can only enroll in up to 3 classes.");
                return;
            }

            // Call service
            receptionistService.registerStudent(name, password, phone, email, gender, ic, address, level, classIds);
            JOptionPane.showMessageDialog(this, "Student registered successfully.");

            // Clear fields
            nameField.setText("");
            passwordField.setText("");
            phoneField.setText("");
            emailField.setText("");
            genderField.setText("");
            icField.setText("");
            addressField.setText("");
            levelField.setText("");
            class1Field.setText("");
            class2Field.setText("");
            class3Field.setText("");
        });

        // Back action
        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist);
        });
    }
}
