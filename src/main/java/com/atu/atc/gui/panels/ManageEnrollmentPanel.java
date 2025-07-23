package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Tutor;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * @author henge
 */
public class ManageEnrollmentPanel extends JPanel {
    private final Receptionist receptionist;
    private final ReceptionistService receptionistService;
    private final MainFrame.PanelNavigator navigator;

    private final JTextField studentIdField = new JTextField(10);
    private final JTextField classIdField = new JTextField(10);
    private final JTextArea enrollmentArea = new JTextArea(10, 40);

    public ManageEnrollmentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionist = receptionist;
        this.receptionistService = receptionistService;
        this.navigator = navigator;

        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Student Enrollments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(studentIdField);
        inputPanel.add(new JLabel("Class ID"));
        inputPanel.add(classIdField);

        JButton viewBtn = new JButton("View Enrollments");
        JButton addBtn = new JButton("Add Subject");
        JButton withdrawBtn = new JButton("Withdraw Subject");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(backBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        enrollmentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(enrollmentArea);
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        viewBtn.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a Student ID");
                return;
            }
            List<String> classes = receptionistService.getEnrolledClassIds(studentId);
            if (classes.isEmpty()){
                enrollmentArea.setText("No classes found for student " + studentId);
            } else{
                String content = classes.stream().collect(Collectors.joining("\n"));
                enrollmentArea.setText("Enrolled classes for " + studentId + ":\n" + content);
            }
        });

        addBtn.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            String classId = classIdField.getText().trim();
            if(studentId.isEmpty() || classId.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter both Student ID and Class ID.");
                return;
            }
            receptionistService.addSubjectEnrollment(studentId, classId);
        });

        withdrawBtn.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            String classId = classIdField.getText().trim();
            if(studentId.isEmpty() || classId.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter both Student ID and Class ID");
                return;
            }
            receptionistService.withdrawSubject(studentId, classId);
        });

        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist);
        });
    }
}
