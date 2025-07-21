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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 *
 * @author Xuan
 */
public class AdminDashboardPanel extends JPanel implements DashboardPanelInterface {

    // Color Constants
    private static final Color PRIMARY_BLUE = new Color(41, 98, 255);
    private static final Color LIGHT_BLUE = new Color(230, 240, 255);
    private static final Color DARK_BLUE = new Color(25, 60, 150);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(220, 230, 245);

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
        setBackground(LIGHT_GRAY);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(DARK_BLUE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        manageTutorsButton = createStyledButton("Manage Tutors");
        manageReceptionistsButton = createStyledButton("Manage Receptionists");
        viewReportButton = createStyledButton("View Monthly Income Report");
        updateProfileButton = createStyledButton("Update My Profile");
        logoutButton = createLogoutButton("Logout");
    }

    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(400, 50));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(WHITE);
        button.setForeground(DARK_BLUE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setIconTextGap(10);
        button.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_BLUE);
                button.setBorder(new CompoundBorder(
                    new LineBorder(PRIMARY_BLUE, 2, true),
                    new EmptyBorder(9, 19, 9, 19)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
                button.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        return button;
    }

    private JButton createLogoutButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(400, 50));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(new Color(255, 245, 245));
        button.setForeground(new Color(180, 65, 65));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 200, 200), 1, true),
            new EmptyBorder(10, 20, 10, 20)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 235, 235));
                button.setBorder(new CompoundBorder(
                    new LineBorder(new Color(220, 100, 100), 2, true),
                    new EmptyBorder(9, 19, 9, 19)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 245, 245));
                button.setBorder(new CompoundBorder(
                    new LineBorder(new Color(255, 200, 200), 1, true),
                    new EmptyBorder(10, 20, 10, 20)
                ));
            }
        });

        return button;
    }

    private void setupLayout() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(LIGHT_GRAY);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Administrator Dashboard", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 120, 140));
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LIGHT_GRAY);
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel buttonContainer = new JPanel(new GridBagLayout());
        buttonContainer.setBackground(LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0; buttonContainer.add(manageTutorsButton, gbc);
        gbc.gridy = 1; buttonContainer.add(manageReceptionistsButton, gbc);
        gbc.gridy = 2; buttonContainer.add(viewReportButton, gbc);
        gbc.gridy = 3; buttonContainer.add(updateProfileButton, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        buttonContainer.add(logoutButton, gbc);

        contentPanel.add(buttonContainer);
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
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
