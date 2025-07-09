package com.atu.atc.gui;

import com.atu.atc.service.*;
import com.atu.atc.gui.panels.*;
import com.atu.atc.model.User;
import com.atu.atc.model.Admin;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    // --- Panel Name Constants ---
    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String ADMIN_DASHBOARD = "AdminDashboard";
    public static final String RECEPTIONIST_DASHBOARD = "ReceptionistDashboard";
    public static final String TUTOR_DASHBOARD = "TutorDashboard";
    public static final String STUDENT_DASHBOARD = "StudentDashboard";
    public static final String REGISTER_TUTOR_PANEL = "RegisterTutorPanel";
    public static final String VIEW_REPORT_PANEL = "ViewReportPanel";
    public static final String MANAGE_RECEPTIONISTS_PANEL = "ManageReceptionistsPanel";

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    private final AuthService authService;
    private final AdminService adminService;
    private final ReceptionistService receptionistService;
    private final TutorService tutorService;
    private final StudentService studentService;

    private final Map<String, JPanel> loadedPanels = new HashMap<>();

    public MainFrame(AuthService authService, AdminService adminService,
                      ReceptionistService receptionistService, TutorService tutorService,
                      StudentService studentService) {
        this.authService = authService;
        this.adminService = adminService;
        this.receptionistService = receptionistService;
        this.tutorService = tutorService;
        this.studentService = studentService;

        setTitle("Tuition Centre Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        LoginPanel loginPanel = new LoginPanel(this.authService, this.navigator);
        mainPanel.add(loginPanel, LOGIN_PANEL);
        loadedPanels.put(LOGIN_PANEL, loginPanel);

        cardLayout.show(mainPanel, LOGIN_PANEL);
    }

    public interface PanelNavigator {
        void navigateTo(String panelName, User user);
        void logout();
    }

    private final PanelNavigator navigator = new PanelNavigator() {
        @Override
        public void navigateTo(String panelName, User user) {
            System.out.println("Navigating to: " + panelName);
            JPanel targetPanel = loadedPanels.get(panelName);

            if (targetPanel == null) {
                switch (panelName) {
                    case ADMIN_DASHBOARD:
                        if (user instanceof com.atu.atc.model.Admin adminUser) {
                            targetPanel = new AdminDashboardPanel(adminService, adminUser, this);
                        } else {
                            System.err.println("Invalid user type for Admin Dashboard.");
                            return;
                        }
                        break;
                    case RECEPTIONIST_DASHBOARD:
                        if (user instanceof com.atu.atc.model.Receptionist rcpUser) {
                            targetPanel = new ReceptionistDashboardPanel(receptionistService, rcpUser, this);
                        } else {
                            System.err.println("Invalid user type for Receptionist Dashboard.");
                            return;
                        }
                        break;
                    case TUTOR_DASHBOARD:
                        if (user instanceof com.atu.atc.model.Tutor tutorUser) {
                            targetPanel = new TutorDashboardPanel(tutorService, tutorUser, this);
                        } else {
                            System.err.println("Invalid user type for Tutor Dashboard.");
                            return;
                        }
                        break;
                    case STUDENT_DASHBOARD:
                        if (user instanceof com.atu.atc.model.Student studentUser) {
                            targetPanel = new StudentDashboardPanel(studentService, studentUser, this);
                        } else {
                            System.err.println("Invalid user type for Student Dashboard.");
                            return;
                        }
                        break;
                    case REGISTER_TUTOR_PANEL:
                        if (user instanceof Admin adminUser) { 
                            targetPanel = new RegisterTutorPanel(adminService, this, adminUser);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to RegisterTutorPanel with non-Admin user or null.");
                            return; 
                        }
                        break;
                    case VIEW_REPORT_PANEL:
                        if (user instanceof Admin adminUser) {
                            targetPanel = new ViewReportPanel(adminService, this, adminUser);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to ViewReportPanel with non-Admin user or null.");
                            return; 
                        }
                        break;
                    case MainFrame.MANAGE_RECEPTIONISTS_PANEL:
                        if (user instanceof com.atu.atc.model.Admin adminUser) {
                            targetPanel = new ManageReceptionistsPanel(adminService, this, adminUser);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to ManageReceptionistsPanel with non-Admin user or null.");
                            return; // Prevent navigation
                        }
                        break;
                    case LOGIN_PANEL:
                        targetPanel = new LoginPanel(authService, this);
                        break;
                    default:
                        System.err.println("Unknown panel requested: " + panelName);
                        return;
                }
                mainPanel.add(targetPanel, panelName);
                loadedPanels.put(panelName, targetPanel);
            } else {
                if (panelName.endsWith("Dashboard") && targetPanel instanceof DashboardPanelInterface dpi) {
                    dpi.updateUserContext(user);
                }
            }

            cardLayout.show(mainPanel, panelName);
            mainPanel.revalidate();
            mainPanel.repaint();
        }

        @Override
        public void logout() {
            System.out.println("Logging out...");
            loadedPanels.clear();
            LoginPanel newLoginPanel = new LoginPanel(authService, this);
            mainPanel.add(newLoginPanel, LOGIN_PANEL);
            loadedPanels.put(LOGIN_PANEL, newLoginPanel);
            cardLayout.show(mainPanel, LOGIN_PANEL);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    };

    public PanelNavigator getNavigator() {
        return navigator;
    }
}
