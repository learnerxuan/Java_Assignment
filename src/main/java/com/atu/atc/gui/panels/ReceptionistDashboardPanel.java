package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.User;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author henge
 */
public class ReceptionistDashboardPanel extends JPanel implements DashboardPanelInterface {
    private Receptionist receptionist;
    private final ReceptionistService receptionistService;
    private final MainFrame.PanelNavigator navigator;
    private JLabel welcomeLabel;

    public ReceptionistDashboardPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    @Override
    public void updateUserContext(User user) {
        if (user instanceof Receptionist) {
            this.receptionist = (Receptionist) user;
            welcomeLabel.setText("Welcome, " + receptionist.getFullName() + " (ID: " + receptionist.getId() + ")");
        }
    }

    private void initUI() {
        setBackground(new Color(240, 248, 255)); // Alice Blue
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 248, 255));

        // Panel for centered titles
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Receptionist Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomeLabel = new JLabel("Welcome, " + receptionist.getFullName() + " (ID: " + receptionist.getId() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Top padding

        titleContainer.add(titleLabel);
        titleContainer.add(welcomeLabel);

        headerPanel.add(titleContainer, BorderLayout.CENTER);

        // Panel for logout button
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(240, 248, 255));
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(220, 53, 69), new Color(200, 43, 59)); // Red color for logout
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                navigator.logout();
            }
        });
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content - Grid of cards
        JPanel mainContentPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        mainContentPanel.setBackground(new Color(240, 248, 255));
        mainContentPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Create and add styled buttons
        mainContentPanel.add(createDashboardButton("Register Student", "register_student.png", MainFrame.REGISTER_STUDENT_PANEL));
        mainContentPanel.add(createDashboardButton("Manage Enrollments", "manage_enrollment.png", MainFrame.MANAGE_ENROLLMENT_PANEL));
        mainContentPanel.add(createDashboardButton("Accept Payment", "accept_payment.png", MainFrame.ACCEPT_PAYMENT_PANEL));
        mainContentPanel.add(createDashboardButton("Delete Student", "delete_student.png", MainFrame.DELETE_STUDENT_PANEL));
        mainContentPanel.add(createDashboardButton("Update My Profile", "update_profile.png", MainFrame.UPDATE_RECEPTIONIST_PROFILE_PANEL));
        mainContentPanel.add(createDashboardButton("Handle Student Requests", "manage_requests.png", MainFrame.MANAGE_REQUESTS_PANEL));

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardButton(String text, String iconName, String panelName) {
        JButton button = new JButton("<html><center>" + text + "</center></html>");
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        styleButton(button, new Color(65, 105, 225), new Color(50, 85, 200));

        button.addActionListener(e -> navigator.navigateTo(panelName, receptionist));

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.add(button, BorderLayout.CENTER);
        return cardPanel;
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
}