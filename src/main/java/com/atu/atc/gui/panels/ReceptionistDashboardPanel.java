package com.atu.atc.gui.panels;

/**
 * @author henge
 */

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.User;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceptionistDashboardPanel extends JPanel implements DashboardPanelInterface {
    private Receptionist receptionist;
    private final ReceptionistService receptionistService;
    private final MainFrame.PanelNavigator navigator;

    public ReceptionistDashboardPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    // Called when MainFrame switches to this dashboard and passes updated user context
    @Override
    public void updateUserContext(User user){
        if(user instanceof Receptionist){
            this.receptionist = (Receptionist) user;
            // Optionally update welcome label
        }
    }

    private void initUI(){
        setLayout(new BorderLayout());

        // Top welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + receptionist.getFullName() + " (ID: " + receptionist.getId() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // Center panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 200));

        JButton registerBtn = new JButton("Register Student");
        JButton manageEnrollBtn = new JButton("Manage Enrollments");
        JButton paymentBtn = new JButton("Accept Payment");
        JButton profileBtn = new JButton("Update My Profile");
        JButton requestBtn = new JButton("Handle Student Requests");
        JButton logoutBtn = new JButton("Logout");

        buttonPanel.add(registerBtn);
        buttonPanel.add(manageEnrollBtn);
        buttonPanel.add(paymentBtn);
        buttonPanel.add(profileBtn);
        buttonPanel.add(requestBtn);
        buttonPanel.add(logoutBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // Button Actions
        registerBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.REGISTER_STUDENT_PANEL, receptionist);
        });

        manageEnrollBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Manage Enrollments Panel (Coming Soon)");
        });

        paymentBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Accept Payment Panel (Coming Soon)");
        });

        profileBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Update Profile Panel (Coming Soon)");
        });

        requestBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Student Request Panel (Coming Soon)");
        });

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION){
                navigator.logout(); // Goes back to login panel
            }
        });
    }
}
