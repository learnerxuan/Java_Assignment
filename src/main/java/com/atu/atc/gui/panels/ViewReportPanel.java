/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AdminService;
import com.atu.atc.model.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 *
 * @author Xuan 
 */
public class ViewReportPanel extends JPanel {

    // UI Components
    private JTextField yearField;
    private JComboBox<String> monthComboBox;
    private JTextField subjectIdFilterField;
    private JTextField levelFilterField;
    private JButton generateReportButton;
    private JButton backButton;
    private JLabel resultLabel; 

    // Dependencies
    private final AdminService adminService;
    private final MainFrame.PanelNavigator navigator;

    // State
    private final Admin loggedInAdmin;

    public ViewReportPanel(AdminService adminService, MainFrame.PanelNavigator navigator, Admin loggedInAdmin) {
        this.adminService = adminService;
        this.navigator = navigator;
        this.loggedInAdmin = loggedInAdmin;

        // Panel Layout and Styling
        setBackground(new Color(240, 248, 255)); 
        setLayout(new BorderLayout(15, 15)); 
        setBorder(new EmptyBorder(20, 20, 20, 20)); 

        // Title
        JLabel titleLabel = new JLabel("View Monthly Income Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(25, 25, 112)); 
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE); // White background for the form card
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true), // Light blue border, rounded corners
                new EmptyBorder(25, 30, 25, 30) // Inner padding
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Year
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        yearLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        yearField = new JTextField(String.valueOf(LocalDate.now().getYear()), 10); // Default to current year
        yearField.setFont(new Font("Arial", Font.PLAIN, 16));
        yearField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(yearField, gbc);
        row++;

        // Month
        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        monthLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(monthLabel, gbc);
        gbc.gridx = 1;
        String[] months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        monthComboBox.setBackground(Color.WHITE);
        monthComboBox.setSelectedIndex(LocalDate.now().getMonthValue()); // Default to current month
        formPanel.add(monthComboBox, gbc);
        row++;

        // Subject ID Filter
        JLabel subjectIdLabel = new JLabel("Subject ID (Optional):");
        subjectIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subjectIdLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(subjectIdLabel, gbc);
        gbc.gridx = 1;
        subjectIdFilterField = new JTextField(15);
        subjectIdFilterField.setFont(new Font("Arial", Font.PLAIN, 16));
        subjectIdFilterField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(subjectIdFilterField, gbc);
        row++;

        // Level Filter
        JLabel levelLabel = new JLabel("Level (Optional, e.g., '1' to '5'):");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        levelLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(levelLabel, gbc);
        gbc.gridx = 1;
        levelFilterField = new JTextField(15);
        levelFilterField.setFont(new Font("Arial", Font.PLAIN, 16));
        levelFilterField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(levelFilterField, gbc);
        row++;

        // Add formPanel to the center, wrapped in a panel for centering
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setBackground(new Color(240, 248, 255)); // Match main background
        centerWrapperPanel.add(formPanel);
        add(centerWrapperPanel, BorderLayout.CENTER);


        // Result and Control Panel (SOUTH)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10)); // Add gap for result label
        bottomPanel.setBackground(new Color(240, 248, 255)); // Match main background

        resultLabel = new JLabel("Total Income: $0.00", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Slightly larger for result
        resultLabel.setForeground(new Color(0, 128, 0)); // Green for income
        bottomPanel.add(resultLabel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setBackground(new Color(240, 248, 255)); // Match main background
        generateReportButton = new JButton("Generate Report");
        backButton = new JButton("Back to Dashboard");

        // Apply button styling
        styleButton(generateReportButton);
        styleButton(backButton);

        controlPanel.add(generateReportButton);
        controlPanel.add(backButton);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Attach Action Listeners
        generateReportButton.addActionListener(e -> generateReport());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(65, 105, 225)); // Royal Blue
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true), // Matching border, rounded
                new EmptyBorder(8, 20, 8, 20) // Padding
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Add subtle hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 85, 200)); // Slightly darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225)); // Original color on exit
            }
        });
    }

    private void generateReport() {
        resultLabel.setText("Total Income: Calculating...");
        resultLabel.setForeground(Color.BLUE); // Indicate calculating state

        try {
            int year = Integer.parseInt(yearField.getText().trim());
            int month = monthComboBox.getSelectedIndex();

            // Validation
            if (month == 0) { // Index 0 is the empty string ""
                resultLabel.setText("Error: Please select a month.");
                resultLabel.setForeground(Color.RED);
                return;
            }

            String subjectId = subjectIdFilterField.getText().trim();
            String level = levelFilterField.getText().trim();

            // Delegate to AdminService for business logic
            double totalIncome = adminService.viewMonthlyIncomeReport(year, month,
                                                                      subjectId.isEmpty() ? null : subjectId,
                                                                      level.isEmpty() ? null : level);

            resultLabel.setText(String.format("Total Income: $%.2f", totalIncome));
            resultLabel.setForeground(new Color(0, 128, 0)); // Green for success

        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Invalid year. Please enter a valid number.");
            resultLabel.setForeground(Color.RED);
        } catch (Exception ex) {
            resultLabel.setText("Error generating report: " + ex.getMessage());
            resultLabel.setForeground(Color.RED);
            ex.printStackTrace(); // Print stack trace for debugging
        }
    }
}
