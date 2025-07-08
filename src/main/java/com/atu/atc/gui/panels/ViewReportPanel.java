/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame; 
import com.atu.atc.service.AdminService; 
import com.atu.atc.model.Admin; 

import javax.swing.*;
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

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("View Monthly Income Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Year
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        yearField = new JTextField(String.valueOf(LocalDate.now().getYear()), 10); // Default to current year
        formPanel.add(yearField, gbc);
        row++;

        // Month
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Month:"), gbc);
        gbc.gridx = 1;
        String[] months = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(LocalDate.now().getMonthValue()); 
        formPanel.add(monthComboBox, gbc);
        row++;

        // Subject ID Filter 
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Subject ID (Optional):"), gbc);
        gbc.gridx = 1;
        subjectIdFilterField = new JTextField(15);
        formPanel.add(subjectIdFilterField, gbc);
        row++;

        // Level Filter
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Level (Optional, e.g., '1' to '5'):"), gbc);
        gbc.gridx = 1;
        levelFilterField = new JTextField(15);
        formPanel.add(levelFilterField, gbc);
        row++;

        // Add formPanel to the center
        add(formPanel, BorderLayout.CENTER);

        // Result and Control Panel (SOUTH) 
        JPanel bottomPanel = new JPanel(new BorderLayout());

        resultLabel = new JLabel("Total Income: $0.00", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setForeground(new Color(0, 100, 0)); 
        bottomPanel.add(resultLabel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        generateReportButton = new JButton("Generate Report");
        backButton = new JButton("Back to Dashboard");

        controlPanel.add(generateReportButton);
        controlPanel.add(backButton);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Attach Action Listeners
        generateReportButton.addActionListener(e -> generateReport());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.ADMIN_DASHBOARD, loggedInAdmin));
    }


    private void generateReport() {
        resultLabel.setText("Total Income: Calculating...");
        resultLabel.setForeground(Color.BLUE);

        try {
            int year = Integer.parseInt(yearField.getText().trim());
            int month = monthComboBox.getSelectedIndex(); 

            // Validation 
            if (month == 0) {
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
            resultLabel.setForeground(new Color(0, 100, 0)); 

        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Invalid year. Please enter a valid number.");
            resultLabel.setForeground(Color.RED);
        } catch (Exception ex) {
            resultLabel.setText("Error generating report: " + ex.getMessage());
            resultLabel.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }
}