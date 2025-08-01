package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

        setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("Enrolled Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Student ID", "Full Name", "IC/Passport", "Email", "Phone", "Level", "Enrolled Month"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setEnabled(false);
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 240), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Info label
        infoLabel = new JLabel(" ", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(90, 90, 90));

        // Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 250, 255));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        bottomPanel.add(infoLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 250, 255));

        refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));
        backButton = createStyledButton("Back to Dashboard", new Color(231, 76, 60));

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event listeners
        refreshButton.addActionListener(e -> loadStudentData());
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));

        loadStudentData();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.darker(), 1, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        return button;
    }

    private void loadStudentData() {
        tableModel.setRowCount(0); // Clear table

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
                        parts[0].trim(), parts[1].trim(), parts[2].trim(),
                        parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim()
                    });
                } else {
                    System.err.println("Invalid student record: " + line);
                }
            }

            infoLabel.setText("Showing " + tableModel.getRowCount() + " enrolled student(s).");

        } catch (Exception ex) {
            infoLabel.setText("Error loading data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
