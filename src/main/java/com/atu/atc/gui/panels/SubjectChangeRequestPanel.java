package com.atu.atc.gui.panels;

import com.atu.atc.model.*;
import com.atu.atc.service.StudentService;
import com.atu.atc.gui.MainFrame.PanelNavigator;
import com.atu.atc.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectChangeRequestPanel extends JPanel {
    private JComboBox<String> currentSubjectCombo;
    private JComboBox<String> requestedSubjectCombo;
    private JButton submitButton;
    private JButton backButton;
    private Student currentStudent;
    private PanelNavigator navigator;
    private StudentService studentService;
    
    public SubjectChangeRequestPanel(StudentService studentService, PanelNavigator navigator, Student student) {
        this.studentService = studentService;
        this.currentStudent = student;
        this.navigator = navigator;
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Change Subject Request", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 8, 4));
        
        formPanel.add(new JLabel("Current Enrolled Course:"));
        currentSubjectCombo = new JComboBox<>();
        formPanel.add(currentSubjectCombo);
        
        formPanel.add(new JLabel("Requested New Course:"));
        requestedSubjectCombo = new JComboBox<>();
        formPanel.add(requestedSubjectCombo);
        
        submitButton = new JButton("Submit Request");
        backButton = new JButton("Back");
        
        formPanel.add(backButton);
        formPanel.add(submitButton);
        
        add(formPanel, BorderLayout.CENTER);
        
        loadSubjectChoices();
        
        submitButton.addActionListener((ActionEvent e) -> submitSubjectChangeRequest());
        
        backButton.addActionListener((ActionEvent e) -> {
            navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent);
        });
    }
    
    private void loadSubjectChoices() {
        List<Enrollment> myEnrollments = studentService.getEnrollmentRepository().getAll().stream()
                .filter(e -> e.getStudentId().equals(currentStudent.getId()))
                .collect(Collectors.toList());
        
        Set<String> mySubjectIds = myEnrollments.stream()
                .map(e -> studentService.getClassesRepository().getById(e.getClassId())
                        .map(Classes::getSubjectId)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        Map<String, Subject> allSubjects = studentService.getSubjectRepository().getAllSubjects().stream()
                .collect(Collectors.toMap(Subject::getSubjectId, s -> s));
        
        currentSubjectCombo.removeAllItems();
        requestedSubjectCombo.removeAllItems();
        
        for (String subjectId : mySubjectIds) {
            Subject subject = allSubjects.get(subjectId);
            if (subject != null) {
                currentSubjectCombo.addItem(subjectId + " - " + subject.getName());
            }
        }
        
        for (Map.Entry<String, Subject> entry : allSubjects.entrySet()) {
            if (!mySubjectIds.contains(entry.getKey())) {
                requestedSubjectCombo.addItem(entry.getKey() + " - " + entry.getValue().getName());
            }
        }
    }
    
    private void submitSubjectChangeRequest() {
        String current = (String) currentSubjectCombo.getSelectedItem();
        String requested = (String) requestedSubjectCombo.getSelectedItem();
        
        if (current == null || requested == null) {
            JOptionPane.showMessageDialog(this, "Please select both current and requested courses.", "Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String currentId = current.split(" - ")[0];
        String requestedId = requested.split(" - ")[0];
        
        studentService.submitSubjectChangeRequest(currentStudent.getId(), currentId, requestedId);
        
        JOptionPane.showMessageDialog(this, "Subject change request submitted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadSubjectChoices();
    }

}
