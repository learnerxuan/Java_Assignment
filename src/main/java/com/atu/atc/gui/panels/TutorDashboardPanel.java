package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TutorDashboardPanel extends JPanel implements DashboardPanelInterface {

    private JLabel welcomeLabel;
    private JButton manageClassesButton;
    private JButton viewEnrolledStudentsButton;
    private JButton updateProfileButton;
    private JButton logoutButton;

    private final TutorService tutorService;
    private final MainFrame.PanelNavigator navigator;

    private Tutor loggedInTutor;

    public TutorDashboardPanel(TutorService tutorService, Tutor loggedInTutor, MainFrame.PanelNavigator navigator) {
        this.tutorService = tutorService;
        this.loggedInTutor = loggedInTutor;
        this.navigator = navigator;

        setBackground(new Color(245, 250, 255)); // Light background
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Top Title
        JLabel titleLabel = new JLabel("Tutor Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        // Welcome Message
        welcomeLabel = new JLabel("", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(70, 70, 70));

        // Styled Buttons
        manageClassesButton = createStyledButton("📚  Manage My Classes");
        viewEnrolledStudentsButton = createStyledButton("👥  View Enrolled Students");
        updateProfileButton = createStyledButton("✏️  Update My Profile");
        logoutButton = createLogoutButton("🚪  Logout");

        // Central Panel with Buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 240), 2, true),
            new EmptyBorder(30, 60, 30, 60)
        ));

        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(manageClassesButton);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(viewEnrolledStudentsButton);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(updateProfileButton);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(logoutButton);

        add(centerPanel, BorderLayout.CENTER);

        // Button Actions
        manageClassesButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.MANAGE_CLASSES_PANEL, loggedInTutor);
            System.out.println("Tutor: Navigating to Manage Classes Panel.");
        });

        viewEnrolledStudentsButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.VIEW_ENROLLED_STUDENTS_PANEL, loggedInTutor);
            System.out.println("Tutor: Navigating to View Enrolled Students Panel.");
        });

        updateProfileButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.UPDATE_TUTOR_PROFILE_PANEL, loggedInTutor);
            System.out.println("Tutor: Navigating to Update My Profile Panel.");
        });

        logoutButton.addActionListener(e -> {
            navigator.logout();
        });

        updateUserContext(loggedInTutor);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 130, 180), 1, true),
            new EmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }

    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(255, 230, 230)); // Light red
        button.setForeground(new Color(180, 60, 60));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 150, 150), 1, true),
            new EmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }

    @Override
    public void updateUserContext(User user) {
        if (user instanceof Tutor) {
            this.loggedInTutor = (Tutor) user;
            welcomeLabel.setText("Welcome, Tutor " + loggedInTutor.getFullName() + "!");
        } else {
            welcomeLabel.setText("Welcome, Tutor!");
            System.err.println("TutorDashboardPanel: Received non-Tutor user context.");
        }
    }
}
