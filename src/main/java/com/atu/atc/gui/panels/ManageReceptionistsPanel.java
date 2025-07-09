/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame; 
import com.atu.atc.service.AdminService; 
import com.atu.atc.model.Admin; 
import com.atu.atc.model.Receptionist; 
import com.atu.atc.model.User; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import java.awt.*;
import java.util.List; 
import java.util.Optional; 
/**
 *
 * @author Xuan
 */
public class ManageReceptionistsPanel extends JPanel{
    
    // UI Components for Registration Form
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox;
    private JButton registerButton;
    private JLabel registerMessageLabel;
    
    // UI Components for Table View 
    private JTable receptionistsTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton deleteButton;
    private JLabel tableMessageLabel;

    private JButton backButton;

    // Dependencies 
    private final AdminService adminService;
    private final MainFrame.PanelNavigator navigator;

    // State 
    private final Admin loggedInAdmin;
    
    public ManageReceptionistsPanel(AdminService adminService, MainFrame.PanelNavigator navigator, Admin loggedInAdmin) {
        this.adminService = adminService;
        this.navigator = navigator;
        this.loggedInAdmin = loggedInAdmin;

        setLayout(new BorderLayout(15, 15)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Manage Receptionists", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        // Main Content Panel which  hold the registration form and the table view side-by-side
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0)); 

        // Registration Form Panel (Left Side)
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createTitledBorder("Register New Receptionist"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6); 
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
        String[] genders = {"", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        registerPanel.add(genderComboBox, gbc);
        row++;

        // Register Button
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2; // Span across two columns
        registerButton = new JButton("Register Receptionist");
        registerPanel.add(registerButton, gbc);
        row++;

        // Register Message Label
        gbc.gridx = 0; gbc.gridy = row;
        registerMessageLabel = new JLabel("", SwingConstants.CENTER);
        registerMessageLabel.setForeground(Color.RED);
        registerPanel.add(registerMessageLabel, gbc);

        contentPanel.add(registerPanel); 

        // Receptionists Table Panel (Right Side)
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Existing Receptionists"));

        // Table Setup
        String[] columnNames = {"ID", "Full Name", "Email", "Phone", "Gender"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        receptionistsTable = new JTable(tableModel);
        receptionistsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        JScrollPane scrollPane = new JScrollPane(receptionistsTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Table Control Buttons
        JPanel tableControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        refreshButton = new JButton("Refresh List");
        deleteButton = new JButton("Delete Selected");
        tableMessageLabel = new JLabel("", SwingConstants.CENTER);
        tableMessageLabel.setForeground(Color.RED);

        tableControlPanel.add(refreshButton);
        tableControlPanel.add(deleteButton);
        tablePanel.add(tableControlPanel, BorderLayout.SOUTH);
        tablePanel.add(tableMessageLabel, BorderLayout.NORTH); 

        contentPanel.add(tablePanel); 

        // Add main content panel to the center
        add(contentPanel, BorderLayout.CENTER); 

        // Back Button (SOUTH)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButton = new JButton("Back to Admin Dashboard");
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Attach Action Listeners 
        registerButton.addActionListener(e -> registerReceptionist());
        refreshButton.addActionListener(e -> loadReceptionists());
        deleteButton.addActionListener(e -> deleteSelectedReceptionist());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));

        // Initial load of receptionists when panel is displayed
        loadReceptionists();
    }

    private void registerReceptionist() {
        registerMessageLabel.setText(""); 
        registerMessageLabel.setForeground(Color.RED);

        String fullName = fullNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();

        // Basic client-side validation
        if (fullName.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty() ||
            gender == null || gender.isEmpty()) {
            registerMessageLabel.setText("Please fill in all fields.");
            return;
        }

        // Delegate to AdminService
        boolean success = adminService.registerReceptionist(fullName, password, phone, email, gender);

        if (success) {
            registerMessageLabel.setText("Receptionist '" + fullName + "' registered successfully!");
            registerMessageLabel.setForeground(new Color(0, 100, 0)); // Dark green
            clearRegistrationFields();
            loadReceptionists(); // Refresh table after successful registration
        } else {
            registerMessageLabel.setText("Registration failed. Check inputs or console for details.");
        }
    }

    // Loads all receptionists from the AdminService and populates the JTable.
    private void loadReceptionists() {
        tableMessageLabel.setText("Loading receptionists...");
        tableMessageLabel.setForeground(Color.BLUE);
        tableModel.setRowCount(0); 

        List<Receptionist> receptionists = adminService.getAllReceptionists();

        if (receptionists != null && !receptionists.isEmpty()) {
            for (Receptionist rcp : receptionists) {
                tableModel.addRow(new Object[]{
                    rcp.getId(),
                    rcp.getFullName(),
                    rcp.getEmail(),
                    rcp.getPhoneNumber(),
                    rcp.getGender()
                });
            }
            tableMessageLabel.setText("Loaded " + receptionists.size() + " receptionists.");
            tableMessageLabel.setForeground(Color.BLACK); // Reset color
        } else {
            tableMessageLabel.setText("No receptionists found.");
            tableMessageLabel.setForeground(Color.ORANGE);
        }
    }

    private void deleteSelectedReceptionist() {
        tableMessageLabel.setText(""); 
        tableMessageLabel.setForeground(Color.RED);

        int selectedRow = receptionistsTable.getSelectedRow();
        if (selectedRow == -1) {
            tableMessageLabel.setText("Please select a receptionist to delete.");
            return;
        }

        String receptionistId = (String) tableModel.getValueAt(selectedRow, 0); 
        String receptionistName = (String) tableModel.getValueAt(selectedRow, 1); 

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete receptionist '" + receptionistName + "' (ID: " + receptionistId + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Delegate to AdminService
            boolean success = adminService.deleteReceptionist(receptionistId);

            if (success) {
                tableMessageLabel.setText("Receptionist '" + receptionistName + "' deleted successfully.");
                tableMessageLabel.setForeground(new Color(0, 100, 0)); // Dark green
                loadReceptionists(); // Refresh table after deletion
            } else {
                tableMessageLabel.setText("Deletion failed for '" + receptionistName + "'. Check console for details.");
            }
        }
    }

    private void clearRegistrationFields() {
        fullNameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
        emailField.setText("");
        genderComboBox.setSelectedIndex(0); 
    }
}
