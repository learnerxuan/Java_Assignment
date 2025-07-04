// src/main/java/com/atu/atc/gui/MainFrame.java

package com.atu.atc.gui;

import com.atu.atc.gui.panels.*; // Import all your dashboard and task panels
import com.atu.atc.service.*; // Import all your service classes
import com.atu.atc.model.User; // To pass logged-in user information

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MainFrame is the central JFrame window for the application.
 * It acts as the container for different JPanel screens (Login, Dashboards, Task-specific).
 * It manages the switching of panels using CardLayout and provides services to its child panels.
 * It also implements a navigation interface for panels to request screen changes.
 *
 * @author henge (and Gemini for enhancements)
 */
public class MainFrame2 extends JFrame {

    private final CardLayout cardLayout; // Manages switching between panels
    private final JPanel mainPanel;      // The container for all switchable panels

    // Services: Injected dependencies that provide business logic to GUI panels.
    // MainFrame holds them and passes them down to the specific panels that need them.
    private final AuthService authService;
    private final AdminService adminService;
    private final ReceptionistService receptionistService;
    private final TutorService tutorService;
    private final StudentService studentService;

    // Map to store already created dashboard panels to avoid re-creating them unnecessarily.
    // Task-specific panels (like RegisterTutorPanel) might be created on demand.
    private final Map<String, JPanel> loadedPanels = new HashMap<>();

    /**
     * Constructor for MainFrame.
     * All necessary service instances are injected here, following the Dependency Injection pattern.
     * This ensures MainFrame has access to all business logic without creating it itself.
     *
     * @param authService The authentication service.
     * @param adminService The admin service.
     * @param receptionistService The receptionist service.
     * @param tutorService The tutor service.
     * @param studentService The student service.
     */
    public MainFrame(AuthService authService, AdminService adminService,
                     ReceptionistService receptionistService, TutorService tutorService,
                     StudentService studentService) {
        // Assign injected services to instance variables.
        this.authService = authService;
        this.adminService = adminService;
        this.receptionistService = receptionistService;
        this.tutorService = tutorService;
        this.studentService = studentService;

        // --- JFrame Setup ---
        setTitle("Tuition Centre Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation for the window
        setSize(1000, 700); // Set initial window size
        setLocationRelativeTo(null); // Center the window on the screen

        // --- CardLayout Setup ---
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout); // mainPanel will use CardLayout to switch views
        add(mainPanel); // Add the mainPanel to the JFrame

        // --- Add Initial Panel: LoginPanel ---
        // The LoginPanel is the first screen the user sees.
        // It receives the AuthService it needs and the navigator (this MainFrame's navigator implementation).
        LoginPanel loginPanel = new LoginPanel(this.authService, this.navigator);
        mainPanel.add(loginPanel, "LoginPanel"); // Add the panel with a unique name
        loadedPanels.put("LoginPanel", loginPanel); // Keep track of it

        // Initially show the LoginPanel
        cardLayout.show(mainPanel, "LoginPanel");
    }

    /**
     * Interface for panels to request navigation from MainFrame.
     * This promotes loose coupling: panels don't need to know the internal
     * workings of MainFrame; they just use this interface to request a change.
     */
    public interface PanelNavigator {
        /**
         * Requests MainFrame to navigate to a specified panel.
         * @param panelName The unique name of the panel to navigate to (e.g., "AdminDashboard", "RegisterTutorPanel").
         * @param user The User object (Admin, Tutor, etc.) if applicable for the destination panel, or null.
         * This is crucial for dashboards to know who is logged in.
         */
        void navigateTo(String panelName, User user);

        /**
         * Requests MainFrame to log out the current user and return to the login screen.
         */
        void logout();
    }

    // --- Implementation of the PanelNavigator interface ---
    // This allows MainFrame to fulfill navigation requests from its child panels.
    private final PanelNavigator navigator = new PanelNavigator() {
        @Override
        public void navigateTo(String panelName, User user) {
            System.out.println("MainFrame Navigator: Requesting navigation to " + panelName + " for user: " + (user != null ? user.getId() : "N/A"));

            JPanel targetPanel = loadedPanels.get(panelName);

            // If the panel is a dashboard and not already loaded, create it.
            // This ensures dashboards are created once (or re-created if needed, e.g., on logout).
            if (targetPanel == null) {
                switch (panelName) {
                    case "AdminDashboard":
                        if (user instanceof com.atu.atc.model.Admin) { // Ensure correct user type
                            targetPanel = new AdminDashboardPanel(adminService, (com.atu.atc.model.Admin) user, this);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to AdminDashboard with non-Admin user or null.");
                            return; // Prevent navigation
                        }
                        break;
                    case "ReceptionistDashboard":
                        if (user instanceof com.atu.atc.model.Receptionist) {
                            targetPanel = new ReceptionistDashboardPanel(receptionistService, (com.atu.atc.model.Receptionist) user, this);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to ReceptionistDashboard with non-Receptionist user or null.");
                            return;
                        }
                        break;
                    case "TutorDashboard":
                        if (user instanceof com.atu.atc.model.Tutor) {
                            targetPanel = new TutorDashboardPanel(tutorService, (com.atu.atc.model.Tutor) user, this);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to TutorDashboard with non-Tutor user or null.");
                            return;
                        }
                        break;
                    case "StudentDashboard":
                        if (user instanceof com.atu.atc.model.Student) {
                            targetPanel = new StudentDashboardPanel(studentService, (com.atu.atc.model.Student) user, this);
                        } else {
                            System.err.println("MainFrame Navigator Error: Attempted to navigate to StudentDashboard with non-Student user or null.");
                            return;
                        }
                        break;
                    case "LoginPanel": // Allow re-creation of LoginPanel to clear state if needed
                        targetPanel = new LoginPanel(authService, this);
                        break;
                    // --- Add cases for other task-specific panels here if they need to be created on demand ---
                    // Example:
                    // case "RegisterTutorPanel":
                    //     targetPanel = new RegisterTutorPanel(adminService, this);
                    //     break;
                    default:
                        System.err.println("MainFrame Navigator Error: Unknown panel name requested: " + panelName);
                        return; // Do not navigate
                }
                // If a new panel was created, add it to the mainPanel and loadedPanels map
                if (targetPanel != null) {
                    mainPanel.add(targetPanel, panelName);
                    loadedPanels.put(panelName, targetPanel);
                }
            } else {
                // If the panel already exists, ensure it's updated with the new user context if applicable
                // For dashboards, you might have an `updateUserContext(User user)` method.
                if (panelName.endsWith("Dashboard") && targetPanel instanceof DashboardPanelInterface) {
                    ((DashboardPanelInterface) targetPanel).updateUserContext(user);
                }
            }

            // Show the requested panel
            if (targetPanel != null) {
                cardLayout.show(mainPanel, panelName);
                mainPanel.revalidate(); // Re-layout components
                mainPanel.repaint();    // Redraw components
            }
        }

        @Override
        public void logout() {
            System.out.println("MainFrame Navigator: Logging out. Returning to Login Panel.");
            // Clear all loaded panels except for the LoginPanel, or re-create LoginPanel
            // For simplicity, we'll just navigate back to a fresh LoginPanel.
            // If you want to keep dashboards in memory, you'd clear their user data here.
            loadedPanels.clear(); // Clear all panels
            LoginPanel newLoginPanel = new LoginPanel(authService, this);
            mainPanel.add(newLoginPanel, "LoginPanel"); // Re-add a fresh login panel
            loadedPanels.put("LoginPanel", newLoginPanel);
            cardLayout.show(mainPanel, "LoginPanel");
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    };

    /**
     * Provides the PanelNavigator instance to child panels.
     * Panels will use this to request navigation.
     * @return The PanelNavigator instance.
     */
    public PanelNavigator getNavigator() {
        return navigator;
    }

    // You might also want a generic method to get a service for a specific panel,
    // but typically panels receive what they need in their constructors.
    // For example: public AuthService getAuthService() { return authService; }
}
