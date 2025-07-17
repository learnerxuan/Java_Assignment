package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewEnrolledStudentsPanel extends JPanel {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JButton backButton;
    private JLabel infoLabel;

    private final TutorService tutorService;
    private final MainFrame.PanelNavigator navigator;
    private final Tutor loggedInTutor;

    public ViewEnrolledStudentsPanel(TutorService tutorService, MainFrame.PanelNavigator navigator, Tutor loggedInTutor) {
        this.tutorService = tutorService;
        this.navigator = navigator;
        this.loggedInTutor = loggedInTutor;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("View Enrolled Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Full Name", "IC/Passport", "Email", "Phone", "Level", "Enrolled Month"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setEnabled(false);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        infoLabel = new JLabel("Showing students enrolled in your subjects.", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        add(infoLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        refreshButton = new JButton("Refresh");
        backButton = new JButton("Back to Dashboard");

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadStudentData());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));

        loadStudentData();
    }

    private void loadStudentData() {
        tableModel.setRowCount(0); // Clear existing rows

        try {
            List<String> studentLines = tutorService.viewStudentsEnrolledInMySubjects(loggedInTutor.getId());

            if (studentLines.isEmpty()) {
                infoLabel.setText("No students currently enrolled in your subjects.");
                return;
            }

            for (String line : studentLines) {
                String[] parts = line.split(",", -1); // allow empty fields
                if (parts.length >= 7) {
                    tableModel.addRow(new Object[]{
                        parts[0].trim(), // Student ID
                        parts[1].trim(), // Full Name
                        parts[2].trim(), // IC/Passport
                        parts[3].trim(), // Email
                        parts[4].trim(), // Phone
                        parts[5].trim(), // Level
                        parts[6].trim()  // Enrolled Month
                    });
                } else {
                    System.err.println("Invalid student record: " + line);
                }
            }

            infoLabel.setText("Showing " + studentLines.size() + " enrolled student(s).");

        } catch (Exception ex) {
            infoLabel.setText("Error loading data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
