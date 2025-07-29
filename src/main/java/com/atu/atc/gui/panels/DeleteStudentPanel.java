package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Student;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author henge
 */
public class DeleteStudentPanel extends JPanel {
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public DeleteStudentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
        loadStudents();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Delete Student (Completed Studies)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(new Color(240, 248, 255));

        // Left Panel for Deletion
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBackground(Color.WHITE);
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Student"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        deletePanel.add(new JLabel("Enter Student ID to delete:"), gbc);

        gbc.gridx = 1;
        JTextField studentIdField = new JTextField(20);
        deletePanel.add(studentIdField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JButton deleteBtn = new JButton("Delete");
        styleButton(deleteBtn);
        deletePanel.add(deleteBtn, gbc);

        contentPanel.add(deletePanel);

        // Right Panel for Student Table
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Existing Students"));

        String[] columnNames = {"ID", "Full Name", "Email", "Phone", "Gender", "Level"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tablePanel);

        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(240, 248, 255));
        JButton backBtn = new JButton("Back");
        styleButton(backBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Delete button action
        deleteBtn.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student ID " + studentId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                receptionistService.deleteStudent(studentId);
                JOptionPane.showMessageDialog(this, "Deletion process completed.");
                studentIdField.setText(""); // Clear field
                loadStudents(); // Refresh table
            }
        });

        // Back button action
        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist);
        });
    }

    private void loadStudents() {
        tableModel.setRowCount(0); // Clear existing table data

        List<Student> students = receptionistService.getAllStudents();

        if (students != null && !students.isEmpty()) {
            for (Student student : students) {
                tableModel.addRow(new Object[]{
                        student.getId(),
                        student.getFullName(),
                        student.getEmail(),
                        student.getPhoneNumber(),
                        student.getGender(),
                        student.getLevel()
                });
            }
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(65, 105, 225));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(65, 105, 225), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 85, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225));
            }
        });
    }
}