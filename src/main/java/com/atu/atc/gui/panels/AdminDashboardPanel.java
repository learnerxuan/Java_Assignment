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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Xuan
 */
public class AdminDashboardPanel extends JPanel implements DashboardPanelInterface{
    
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
    
    public AdminDashboardPanel(AdminService adminService, Admin loggedInAdmin, MainFrame.PanelNavigator navigator){
        this.adminService = adminService;
        this.loggedInAdmin = loggedInAdmin;
        this.navigator = navigator;
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        // Welcome Message Section
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,100,20,100));
        
        manageTutorsButton = new JButton("Manage Tutors");
        manageReceptionistsButton = new JButton("Manage Receptionists");
        viewReportButton = new JButton("View Monthly Income Report");
        updateProfileButton = new JButton("Update My Profile");
        logoutButton = new JButton("Logout");
        
        buttonPanel.add(manageTutorsButton);
        buttonPanel.add(manageReceptionistsButton);
        buttonPanel.add(viewReportButton);
        buttonPanel.add(updateProfileButton);
        buttonPanel.add(logoutButton);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        manageTutorsButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.REGISTER_TUTOR_PANEL, loggedInAdmin);
            System.out.println("Admin: Navigating to Register Tutor Panel.");
        });
        
        manageReceptionistsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Receptionists functionality coming soon!");
        });
        
        viewReportButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.VIEW_REPORT_PANEL, loggedInAdmin);
            System.out.println("Admin: Navigating to View Monthly Income Report Panel.");
        });
        
        updateProfileButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Update Profile functionality coming soon!");
        });
        
        logoutButton.addActionListener(e -> {
            navigator.logout();
        });
        
        updateUserContext(loggedInAdmin);
    }
    
    @Override
    public void updateUserContext(User user){
        if (user instanceof Admin){
            this.loggedInAdmin = (Admin) user;
            welcomeLabel.setText("Welcome, Admin "+ loggedInAdmin.getFullName() + "!");
        } else {
            welcomeLabel.setText("Welcome, Administrator!");
        }
    }
}
