/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.service.AdminService;
import com.atu.atc.model.User;
import com.atu.atc.model.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 *
 * @author Xuan
 */
public class AdminDashboardPanel extends JPanel implements DashboardPanelInterface {

    // UI Components
    private JLabel welcomeLabel;
    private JButton manageTutorsButton;
    private JButton manageReceptionistsButton;
    private JButton viewReportButton;
    private JButton updateProfileButton;
    private JButton logoutButton;

    // Dependencies
    private final AdminService adminService;
    private final MainFrame.PanelNavigator navigator;

    // State
    private Admin loggedInAdmin;

    public AdminDashboardPanel(AdminService adminService, Admin loggedInAdmin, MainFrame.PanelNavigator navigator) {
        this.adminService = adminService;
        this.loggedInAdmin = loggedInAdmin;
        this.navigator = navigator;

        initializeComponents();
        setupLayout();
        setupEventListeners();
        updateUserContext(loggedInAdmin);
    }

    private void initializeComponents() {
        setBackground(new Color(240, 248, 255)); // Alice Blue - light background
        setLayout(new BorderLayout(20, 20)); // Add some padding around the edges

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Larger, bolder font
        welcomeLabel.setForeground(new Color(25, 25, 112)); // Midnight Blue
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(new EmptyBorder(20, 0, 10, 0)); // Padding below title

        manageTutorsButton = createStyledButton("Manage Tutors");
        manageReceptionistsButton = createStyledButton("Manage Receptionists");
        viewReportButton = createStyledButton("View Monthly Income Report");
        updateProfileButton = createStyledButton("Update My Profile");
        logoutButton = createLogoutButton("Logout");
    }

    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(65, 105, 225)); // Royal Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true), // Matching border, rounded
                new EmptyBorder(10, 25, 10, 25) // Padding
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 85, 200)); // Slightly darker blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225)); // Original color on exit
            }
        });

        return button;
    }

    private JButton createLogoutButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(255, 245, 245)); // Very light red/pink
        button.setForeground(new Color(180, 65, 65)); // Darker red for text
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 200, 200), 1, true), // Light red border
                new EmptyBorder(10, 25, 10, 25) // Padding
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 235)); // Slightly darker red on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 245, 245)); // Original color on exit
            }
        });

        return button;
    }

    private void setupLayout() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255)); // Match main background
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Administrator Dashboard", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 120, 140)); // Muted blue-gray
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0)); // Padding below subtitle
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Button Panel (Central Card)
        JPanel buttonCardPanel = new JPanel(new GridLayout(5, 1, 15, 15)); // 5 rows, 1 column, with gaps
        buttonCardPanel.setBackground(Color.WHITE); // White background for the card
        buttonCardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true), // Light blue border, rounded corners
                new EmptyBorder(30, 80, 30, 80) // Inner padding for buttons
        ));

        buttonCardPanel.add(manageTutorsButton);
        buttonCardPanel.add(manageReceptionistsButton);
        buttonCardPanel.add(viewReportButton);
        buttonCardPanel.add(updateProfileButton);
        buttonCardPanel.add(logoutButton);

        // Add the button card panel to the center, wrapped in another panel for centering
        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setBackground(new Color(240, 248, 255)); // Match main background
        centerWrapperPanel.add(buttonCardPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(centerWrapperPanel, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        manageTutorsButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.MANAGE_TUTORS_PANEL, loggedInAdmin);
            System.out.println("Admin: Navigating to Manage Tutors Panel.");
        });

        manageReceptionistsButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.MANAGE_RECEPTIONISTS_PANEL, loggedInAdmin);
            System.out.println("Navigating to Manage Receptionists Panel.");
        });

        viewReportButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.VIEW_REPORT_PANEL, loggedInAdmin);
            System.out.println("Navigating to View Monthly Income Report Panel.");
        });

        updateProfileButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.UPDATE_ADMIN_PROFILE_PANEL, loggedInAdmin);
            System.out.println("Navigating to Update My Profile Panel.");
        });

        logoutButton.addActionListener(e -> {
            navigator.logout();
        });
    }

    @Override
    public void updateUserContext(User user) {
        if (user instanceof Admin) {
            this.loggedInAdmin = (Admin) user;
            welcomeLabel.setText("Welcome back, " + loggedInAdmin.getFullName() + "!");
        } else {
            welcomeLabel.setText("Welcome, Administrator!");
        }
    }
}
