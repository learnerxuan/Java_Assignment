package com.atu.atc.gui.panels;

import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;
import com.atu.atc.util.Validator;
import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * @author henge
 */
public class RegisterStudentPanel extends JPanel {
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    private final JComboBox<String> class1Dropdown = new JComboBox<>();
    private final JComboBox<String> class2Dropdown = new JComboBox<>();
    private final JComboBox<String> class3Dropdown = new JComboBox<>();

    public RegisterStudentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    private void initUI() {
        setBackground(new Color(240, 248, 255));
        setLayout(new GridBagLayout());

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true),
                new EmptyBorder(20, 30, 20, 30)
        ));
        cardPanel.setPreferredSize(new Dimension(550, 800));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel titleLabel = new JLabel("Register New Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        cardPanel.add(titleLabel, gbc);

        JTextField nameField = createInputField();
        JPasswordField passwordField = createPasswordField();
        JTextField phoneField = createInputField();
        JTextField emailField = createInputField();
        JComboBox<String> genderDropdown = new JComboBox<>(new String[]{"Male", "Female"});
        styleComboBox(genderDropdown);
        JTextField icField = createInputField();
        JTextField addressField = createInputField();
        JComboBox<String> levelDropdown = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});

        styleComboBox(levelDropdown);

        addField(cardPanel, gbc, row++, "Full Name:", nameField);
        addField(cardPanel, gbc, row++, "Password:", passwordField);
        addField(cardPanel, gbc, row++, "Phone Number:", phoneField);
        addField(cardPanel, gbc, row++, "Email:", emailField);
        addField(cardPanel, gbc, row++, "Gender:", genderDropdown);
        addField(cardPanel, gbc, row++, "IC/Passport:", icField);
        addField(cardPanel, gbc, row++, "Address:", addressField);
        addField(cardPanel, gbc, row++, "Form (1-5):", levelDropdown);
        addField(cardPanel, gbc, row++, "Class 1:", class1Dropdown);
        addField(cardPanel, gbc, row++, "Class 2 (optional):", class2Dropdown);
        addField(cardPanel, gbc, row++, "Class 3 (optional):", class3Dropdown);

        loadAvailableClassesToDropdowns();

        JButton submitBtn = createButton("Register", new Color(65, 105, 225));
        JButton backBtn = createButton("Back", Color.GRAY);

        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        cardPanel.add(backBtn, gbc);
        gbc.gridx = 1;
        cardPanel.add(submitBtn, gbc);

        GridBagConstraints rootGbc = new GridBagConstraints();
        rootGbc.gridx = 0;
        rootGbc.gridy = 0;
        rootGbc.weightx = 1.0;
        rootGbc.weighty = 1.0;
        rootGbc.fill = GridBagConstraints.BOTH;
        add(cardPanel, rootGbc);

        submitBtn.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String gender = (String) genderDropdown.getSelectedItem();
            String ic = icField.getText().trim();
            String address = addressField.getText().trim();
            String level = (String) levelDropdown.getSelectedItem();

            List<String> classIds = new ArrayList<>();
            for (JComboBox<String> cb : List.of(class1Dropdown, class2Dropdown, class3Dropdown)) {
                String selected = (String) cb.getSelectedItem();
                if (selected != null && !selected.isEmpty()) {
                    classIds.add(selected.split(" - ")[0]);
                }
            }

            if (name.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty()
                    || gender.isEmpty() || ic.isEmpty() || address.isEmpty() || level == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            if (!Validator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Invalid email format.");
                return;
            }

            if (!Validator.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number.");
                return;
            }

            if (!Validator.isFormValid(level)) {
                JOptionPane.showMessageDialog(this, "Form must be between 1 and 5.");
                return;
            }

            if (classIds.size() > 3) {
                JOptionPane.showMessageDialog(this, "You can only enroll in up to 3 classes.");
                return;
            }

            receptionistService.registerStudent(name, password, phone, email, gender, ic, address, level, classIds);
            JOptionPane.showMessageDialog(this, "Student registered successfully.");

            nameField.setText("");
            passwordField.setText("");
            phoneField.setText("");
            emailField.setText("");
            genderDropdown.setSelectedIndex(0);
            icField.setText("");
            addressField.setText("");
            levelDropdown.setSelectedIndex(0);
            class1Dropdown.setSelectedIndex(-1);
            class2Dropdown.setSelectedIndex(-1);
            class3Dropdown.setSelectedIndex(-1);
        });

        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist));
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        jLabel.setForeground(new Color(70, 130, 180));
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        return button;
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(new Font("Arial", Font.PLAIN, 16));
        box.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 5, 5, 5)
        ));
    }

    private void loadAvailableClassesToDropdowns() {
        List<Classes> allClasses = receptionistService.getAllClasses();
        for (Classes c : allClasses) {
            Subject subject = receptionistService.getSubjectById(c.getSubjectId()).orElse(null);
            Tutor tutor = receptionistService.getTutorById(c.getTutorId()).orElse(null);
            if (subject != null && tutor != null) {
                String entry = c.getClassId() + " - " + " Form " + subject.getLevel() + " " + subject.getName() + " | " + tutor.getFullName()
                        + " | " + c.getDay() + " | " + c.getStartTime() + "-" + c.getEndTime();
                class1Dropdown.addItem(entry);
                class2Dropdown.addItem(entry);
                class3Dropdown.addItem(entry);
            }
        }
        class1Dropdown.setSelectedIndex(-1);
        class2Dropdown.setSelectedIndex(-1);
        class3Dropdown.setSelectedIndex(-1);
    }
}
