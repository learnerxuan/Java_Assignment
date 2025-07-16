package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import java.awt.*;

/**
 * @author henge
 */
public class DeleteStudentPanel extends JPanel{
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    public DeleteStudentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Delete Student 9Completed Studies)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JTextField studentIdField = new JTextField();

        centerPanel.add(new JLabel("Enter Student ID to delete:"));
        centerPanel.add(studentIdField);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Delete button action
        deleteBtn.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentId.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student ID " + studentId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION){
                receptionistService.deleteStudent(studentId);
                JOptionPane.showMessageDialog(this, "Deletion process completed.");
                studentIdField.setText(""); // Clear field
            }
        });

        // Back button action
        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist);
        });
    }
}
