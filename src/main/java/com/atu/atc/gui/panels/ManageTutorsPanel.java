/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AdminService;
import com.atu.atc.model.Admin;
import com.atu.atc.model.Tutor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Xuan
 */
public class ManageTutorsPanel extends JPanel {

    // UI Components for Registration Form
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> subjectComboBox;
    private JComboBox<String> levelComboBox;
    private JButton registerButton;
    private JLabel registerMessageLabel;

    // UI Components for Table View
    private JTable tutorsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton deleteButton;
    private JLabel tableMessageLabel;

    // Common UI Components
    private JButton backButton;

    // Dependencies
    private final AdminService adminService;
    private final MainFrame.PanelNavigator navigator;

    // State
    private final Admin loggedInAdmin;

    public ManageTutorsPanel(AdminService adminService, MainFrame.PanelNavigator navigator, Admin loggedInAdmin) {
        this.adminService = adminService;
        this.navigator = navigator;
        this.loggedInAdmin = loggedInAdmin;

        // Panel Layout and Styling
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20)); 
        setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("Manage Tutors", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(25, 25, 112)); 
        add(titleLabel, BorderLayout.NORTH);

        // Main Content Panel (CENTER) 
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(new Color(240, 248, 255));

        // 1. Registration Form Panel (Left Side)
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(BorderFactory.createTitledBorder("Register New Tutor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6); // Padding for components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Full Name
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        registerPanel.add(fullNameField, gbc);
        row++;

        // Password
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        registerPanel.add(passwordField, gbc);
        row++;

        // Phone Number
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        registerPanel.add(phoneField, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        registerPanel.add(emailField, gbc);
        row++;

        // Gender
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        String[] genders = {"", "Male", "Female"};
        genderComboBox = new JComboBox<>(genders);
        registerPanel.add(genderComboBox, gbc);
        row++;

        // Subject
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        String[] subjects = {"", "Mathematics", "Science", "English", "History", "Biology", "Chemistry", "Geography", "Computer Science", "Additional Mathematics"};
        subjectComboBox = new JComboBox<>(subjects);
        registerPanel.add(subjectComboBox, gbc);
        row++;

        // Level
        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(new JLabel("Level (1-5):"), gbc);
        gbc.gridx = 1;
        String[] levels = {"", "1", "2", "3", "4", "5"};
        levelComboBox = new JComboBox<>(levels);
        registerPanel.add(levelComboBox, gbc);
        row++;

        // Register Button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2; // Span across two columns
        registerButton = new JButton("Register Tutor");
        // Apply button styling directly
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(65, 105, 225)); // Royal Blue
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(50, 85, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(65, 105, 225));
            }
        });
        registerPanel.add(registerButton, gbc);
        row++;

        // Register Message Label
        gbc.gridx = 0; gbc.gridy = row;
        registerMessageLabel = new JLabel("", SwingConstants.CENTER);
        registerMessageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        registerMessageLabel.setForeground(Color.RED);
        registerPanel.add(registerMessageLabel, gbc);

        contentPanel.add(registerPanel);

        // 2. Tutors Table Panel (Right Side)
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Existing Tutors"));

        // Table Setup
        String[] columnNames = {"ID", "Full Name", "Email", "Phone", "Gender", "Subject", "Level"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        tutorsTable = new JTable(tableModel);
        tutorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row can be selected
        JScrollPane scrollPane = new JScrollPane(tutorsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Table Control Buttons
        JPanel tableControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        tableControlPanel.setBackground(Color.WHITE); // Match table panel background
        refreshButton = new JButton("Refresh List");
        deleteButton = new JButton("Delete Selected");

        // Apply button styling directly
        refreshButton.setFont(new Font("Arial", Font.BOLD, 16));
        refreshButton.setBackground(new Color(65, 105, 225));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(50, 85, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(65, 105, 225));
            }
        });

        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setBackground(new Color(65, 105, 225));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(50, 85, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(65, 105, 225));
            }
        });

        tableMessageLabel = new JLabel("", SwingConstants.CENTER);
        tableMessageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        tableMessageLabel.setForeground(Color.RED);

        tableControlPanel.add(refreshButton);
        tableControlPanel.add(deleteButton);
        tablePanel.add(tableControlPanel, BorderLayout.SOUTH);
        tablePanel.add(tableMessageLabel, BorderLayout.NORTH);

        contentPanel.add(tablePanel);

        add(contentPanel, BorderLayout.CENTER);

        // Back Button (SOUTH)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255)); // Match main background
        backButton = new JButton("Back to Admin Dashboard");
        // Apply button styling directly
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(65, 105, 225));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(50, 85, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(65, 105, 225));
            }
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Attach Action Listeners
        registerButton.addActionListener(e -> registerTutor());
        refreshButton.addActionListener(e -> loadTutors());
        deleteButton.addActionListener(e -> deleteSelectedTutor());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));

        // Initial load of tutors when panel is displayed
        loadTutors();
    }

    private void registerTutor() {
        registerMessageLabel.setText("");
        registerMessageLabel.setForeground(Color.RED);

        String fullName = fullNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String subject = (String) subjectComboBox.getSelectedItem();
        String level = (String) levelComboBox.getSelectedItem();

        // Basic client-side validation
        if (fullName.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() ||
            gender == null || gender.isEmpty() ||
            subject == null || subject.isEmpty() ||
            level == null || level.isEmpty()) {
            registerMessageLabel.setText("Please fill in all fields.");
            return;
        }

        // Delegate to AdminService
        Optional<Tutor> registeredTutorOptional = adminService.registerTutor(fullName, password, phone, email, gender, subject, level);

        // Handle Service Result
        if (registeredTutorOptional.isPresent()) {
            Tutor newTutor = registeredTutorOptional.get();
            registerMessageLabel.setText("Tutor '" + newTutor.getFullName() + "' registered successfully! ID: " + newTutor.getId());
            registerMessageLabel.setForeground(new Color(0, 128, 0)); // Green for success
            clearRegistrationFields();
            loadTutors(); // Refresh table after successful registration
        } else {
            registerMessageLabel.setText("Tutor registration failed. Check inputs or console for details.");
        }
    }

    private void loadTutors() {
        tableMessageLabel.setText("Loading tutors...");
        tableMessageLabel.setForeground(Color.BLUE);
        tableModel.setRowCount(0); // Clear existing table data

        List<Tutor> tutors = adminService.getAllTutors();

        if (tutors != null && !tutors.isEmpty()) {
            for (Tutor tutor : tutors) {
                tableModel.addRow(new Object[]{
                    tutor.getId(),
                    tutor.getFullName(),
                    tutor.getEmail(),
                    tutor.getPhoneNumber(),
                    tutor.getGender(),
                    tutor.getSubject(),
                    tutor.getLevel()
                });
            }
            tableMessageLabel.setText("Loaded " + tutors.size() + " tutors.");
            tableMessageLabel.setForeground(Color.BLACK);
        } else {
            tableMessageLabel.setText("No tutors found.");
            tableMessageLabel.setForeground(Color.ORANGE);
        }
    }

    private void deleteSelectedTutor() {
        tableMessageLabel.setText("");
        tableMessageLabel.setForeground(Color.RED);

        int selectedRow = tutorsTable.getSelectedRow();
        if (selectedRow == -1) {
            tableMessageLabel.setText("Please select a tutor to delete.");
            return;
        }

        String tutorId = (String) tableModel.getValueAt(selectedRow, 0);
        String tutorName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete tutor '" + tutorName + "' (ID: " + tutorId + ")?\n" +
                "Note: Tutor cannot be deleted if assigned to active classes.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminService.deleteTutor(tutorId);

            if (success) {
                loadTutors(); // Refresh table after deletion
                tableMessageLabel.setText("Tutor '" + tutorName + "' deleted successfully.");
                tableMessageLabel.setForeground(new Color(0, 128, 0)); // Green for success
            } else {
                tableMessageLabel.setText("Deletion failed for " + tutorName);
            }
        }
    }

    private void clearRegistrationFields() {
        fullNameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        emailField.setText("");
        genderComboBox.setSelectedIndex(0);
        subjectComboBox.setSelectedIndex(0);
        levelComboBox.setSelectedIndex(0);
    }
}
