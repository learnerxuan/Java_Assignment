package com.atu.atc.gui.panels;

import com.atu.atc.model.*;
import com.atu.atc.data.*;
import com.atu.atc.service.StudentService;
import com.atu.atc.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectChangeRequestPanel extends JPanel {
    private JComboBox<String> currentSubjectCombo;
    private JComboBox<String> requestedSubjectCombo;
    private JButton submitButton;
    private JButton backButton;
    private Student currentStudent;
    private MainFrame mainFrame;
    private StudentService studentService;
    
    private EnrollmentRepository enrollmentRepository = new EnrollmentRepository();
    private ClassesRepository classesRepository = new ClassesRepository();
    private SubjectRepository subjectRepository = new SubjectRepository();
    private RequestRepository requestRepository = new RequestRepository();
    
    public SubjectChangeRequestPanel(StudentService studentService, Student student, MainFrame mainFrame) {
        this.studentService = studentService;
        this.currentStudent = student;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Request Subject Change", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
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
            mainFrame.getNavigator().navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent);
        });
    }
    
    private void loadSubjectChoices() {
        List<Enrollment> myEnrollments = enrollmentRepository.getAll().stream()
                .filter(e -> e.getStudentId().equals(currentStudent.getId()))
                .collect(Collectors.toList());
        
        Set<String> mySubjectIds = myEnrollments.stream()
                .map(e -> classesRepository.getById(e.getClassId())
                        .map(Classes::getSubjectId)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        Map<String, Subject> allSubjects = subjectRepository.getAllSubjects().stream()
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
        if (currentSubjectCombo.getSelectedItem() == null || requestedSubjectCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select both current and requested courses.", "Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String current = ((String) currentSubjectCombo.getSelectedItem()).split(" - ")[0];
        String requested = ((String) requestedSubjectCombo.getSelectedItem()).split(" - ")[0];
        
        String requestId = "RQ" + String.format("%03d", requestRepository.getAll().size() + 1);
        Request request = new Request(requestId, currentStudent.getId(), current, requested, "Pending", LocalDate.now());
        requestRepository.add(request);
        requestRepository.save();
        
        JOptionPane.showMessageDialog(this, "Subject change request submitted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        loadSubjectChoices();
    }
}
