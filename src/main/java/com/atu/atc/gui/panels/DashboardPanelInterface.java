package com.atu.atc.gui.panels;

import com.atu.atc.model.User;

/**
 * @author henge
 */

/**
 * Common interface for all dashboard panels (Admin, Receptionist, Tutor, Student).
 * Allows MainFrame to pass the logged-in user object to the panel after navigation.
 */

public interface DashboardPanelInterface {
    /**
     * Updates the user context for the dashboard panel.
     * This method is called by MainFrame after a successful login
     * to allow the dashboard panel to update its UI based on the logged-in user.
     *
     * @param user The logged-in user (Admin, Receptionist, Tutor, or Student)
     */
    void updateUserContext(User user);
}
