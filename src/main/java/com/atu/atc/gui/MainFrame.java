package com.atu.atc.gui;

import com.atu.atc.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author henge
 */
public class MainFrame extends JFrame{
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final Set<String> addedPanels = new HashSet<>();

    public MainFrame(){
        setTitle("Tuition Centre Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initially load only LoginPanel
        mainPanel.add(new LoginPanel(this), "login");
        addedPanels.add("login");

        add(mainPanel);
        setVisible(true);
    }

    // Show login panel
    public void showLoginPanel(){
        cardLayout.show(mainPanel, "login");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Show admin dashboard panel
    public void showAdminDashboard(){
        if (!addedPanels.contains("admin")){
            mainPanel.add(new AdminDashboardPanel(this), "admin");
            addedPanels.add("admin");
        }
        cardLayout.show(mainPanel, "admin");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Show receptionist dashboard panel
    public void showReceptionistDashboard(){
        if (!addedPanels.contains("receptionist")) {
            mainPanel.add(new ReceptionistDashboardPanel(this), "receptionist");
            addedPanels.add("receptionist");
        }
        cardLayout.show(mainPanel, "receptionist");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Show student dashboard panel
    public void showStudentDashboard(){
        if (!addedPanels.contains("student")) {
            mainPanel.add(new StudentDashboardPanel(this), "student");
            addedPanels.add("student");
        }
        cardLayout.show(mainPanel, "student");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Show tutor dashboard panel
    public void showTutorDashboard(){
        if (!addedPanels.contains("tutor")) {
            mainPanel.add(new TutorDashboardPanel(this), "tutor");
            addedPanels.add("tutor");
        }
        cardLayout.show(mainPanel, "tutor");
        mainPanel.revalidate();
        mainPanel.repaint();
}
