package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ViewEnrolledSubjectsPanel extends JPanel {
    private final StudentService studentService;
    private Student currentStudent;
    
    private final DefaultTableModel tableModel;
    private final JLabel titleLabel;
    
    // Constructor now accepts MainFrame and StudentService
    public ViewEnrolledSubjectsPanel(MainFrame navigatorFrame, StudentService studentService) {
        this.studentService = studentService;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        
        titleLabel = new JLabel("Enrolled Subjects", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        add(titleLabel, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel();
        JTable subjectsTable = new JTable(tableModel);
        subjectsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        subjectsTable.setRowHeight(25);
        subjectsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        subjectsTable.getTableHeader().setBackground(new Color(100, 149, 237));
        subjectsTable.getTableHeader().setForeground(Color.WHITE);
        subjectsTable.setFillsViewportHeight(true);
        subjectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(subjectsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        JButton backBtn = createStyledButton();
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(240, 248, 255));
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);
        
        // Use the navigator interface to go back
        backBtn.addActionListener(e -> navigatorFrame.getNavigator().navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent));
    }
    
    public void loadEnrolledSubjects(Student student) {
        this.currentStudent = student;
        if (currentStudent != null) {
            titleLabel.setText("Enrolled Subjects for " + currentStudent.getFullName());
            List<String[]> scheduleData = studentService.getStudentClassSchedule(currentStudent.getId());
            
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            
            if (!scheduleData.isEmpty()) {
                tableModel.setColumnIdentifiers(scheduleData.get(0));
                for (int i = 1; i < scheduleData.size(); i++) {
                    tableModel.addRow(scheduleData.get(i));
                }
            } else {
                tableModel.setColumnIdentifiers(new String[]{"Class ID", "Subject Name", "Tutor ID", "Day", "Start Time", "End Time"});
                JOptionPane.showMessageDialog(this, "No enrolled subjects found for " + currentStudent.getFullName() + ".", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            titleLabel.setText("Enrolled Subjects (No Student Selected)");
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Class ID", "Subject Name", "Tutor ID", "Day", "Start Time", "End Time"});
            JOptionPane.showMessageDialog(this, "No student is logged in to view enrolled subjects.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JButton createStyledButton() {
        JButton button = new JButton("Back to Dashboard");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
        });
        return button;
    }
}