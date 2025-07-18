package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.service.TutorService;

import javax.swing.*;
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

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome Label
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        manageClassesButton = new JButton("Manage My Classes");
        viewEnrolledStudentsButton = new JButton("View Enrolled Students");
        updateProfileButton = new JButton("Update My Profile");
        logoutButton = new JButton("Logout");

        buttonPanel.add(manageClassesButton);
        buttonPanel.add(viewEnrolledStudentsButton);
        buttonPanel.add(updateProfileButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);
        
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
