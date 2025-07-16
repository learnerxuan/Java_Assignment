package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Classes;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class SubmitSubjectChangeRequestPanel extends JPanel {
    private final MainFrame navigatorFrame;
    private final StudentService studentService;
    private Student currentStudent;
    
    private JComboBox<String> currentSubjectComboBox;
    private JComboBox<String> requestedSubjectComboBox;
    private JLabel lblStatus;
    
    // Maps to hold Subject ID to Subject Name for convenience
    private final Map<String, String> availableSubjectsMap = new HashMap<>();
    private final Map<String, String> enrolledSubjectsMap = new HashMap<>();
    
    public SubmitSubjectChangeRequestPanel(MainFrame navigatorFrame, StudentService studentService) {
        this.navigatorFrame = navigatorFrame;
        this.studentService = studentService;
        setLayout(new BorderLayout());
        initComponents();
    }
    
    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Submit Subject Change Request", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        JLabel lblCurrentSubject = new JLabel("Current Subject:");
        currentSubjectComboBox = new JComboBox<>();
        JLabel lblRequestedSubject = new JLabel("Request New Subject:");
        requestedSubjectComboBox = new JComboBox<>();
        
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblCurrentSubject, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(currentSubjectComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblRequestedSubject, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(requestedSubjectComboBox, gbc);
        
        lblStatus = new JLabel("");
        lblStatus.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; formPanel.add(lblStatus, gbc);
        
        JButton btnSubmit = new JButton("Submit Request");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitRequest();
            }
        });
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> navigatorFrame.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnBack);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void setStudent(Student student) {
        this.currentStudent = student;
        populateComboBoxes();
    }
    
    private void populateComboBoxes() {
        if (currentStudent == null) {
            lblStatus.setText("Error: No student selected.");
            return;
        }
        currentSubjectComboBox.removeAllItems();
        requestedSubjectComboBox.removeAllItems();
        availableSubjectsMap.clear();
        enrolledSubjectsMap.clear();

        List<Subject> allSubjects = studentService.getSubjectRepository().getAllSubjects();
        for (Subject subject : allSubjects) {
            String display = subject.getSubjectId() + " - " + subject.getName();
            requestedSubjectComboBox.addItem(display);
            availableSubjectsMap.put(subject.getSubjectId(), subject.getName());
        }
        
        // Populate Current Subject ComboBox (subjects the student is currently enrolled in)
        List<Enrollment> enrollments = studentService.getEnrollmentsByStudentId(currentStudent.getId());
        if (enrollments.isEmpty()) {
            currentSubjectComboBox.addItem("No current enrollments");
            currentSubjectComboBox.setEnabled(false);
        } else {
            currentSubjectComboBox.setEnabled(true);
            for (Enrollment enrollment : enrollments) {
                // Get Class details from Enrollment
                Optional<Classes> classOpt = studentService.getClassesRepository().getById(enrollment.getClassId());
                if (classOpt.isPresent()) {
                    Classes enrolledClass = classOpt.get();
                    // Get Subject details from Class's subjectId
                    Optional<Subject> subjectOpt = studentService.getSubjectDetails(enrolledClass.getSubjectId());
                    if (subjectOpt.isPresent()) {
                        Subject subject = subjectOpt.get();
                        String display = subject.getSubjectId() + " - " + subject.getName() + " (Class: " + enrolledClass.getClassId() + ")";
                        currentSubjectComboBox.addItem(display);
                        enrolledSubjectsMap.put(subject.getSubjectId(), subject.getName()); // Store just subject ID for request
                    }
                }
            }
        }
        lblStatus.setText("");
    }
    
    private void submitRequest() {
        String currentSubjectDisplay = (String) currentSubjectComboBox.getSelectedItem();
        String requestedSubjectDisplay = (String) requestedSubjectComboBox.getSelectedItem();
        
        if (currentSubjectDisplay == null || requestedSubjectDisplay == null ||
                currentSubjectDisplay.equals("No current enrollments")) {
            lblStatus.setText("Please select both current and requested subjects.");
            return;
        }
        
        // Extract subject IDs from display strings
        String currentSubjectId = currentSubjectDisplay.split(" - ")[0];
        String requestedSubjectId = requestedSubjectDisplay.split(" - ")[0];
        
        if (currentSubjectId.equals(requestedSubjectId)) {
            lblStatus.setText("Current and requested subjects cannot be the same.");
            return;
        }
        
        // Assuming initial status for new requests is "Pending"
        boolean success = studentService.submitSubjectChangeRequest(currentStudent.getId(),
                currentSubjectId,
                requestedSubjectId,
                "Pending");
        if (success) {
            lblStatus.setText("Subject change request submitted successfully!");
            lblStatus.setForeground(Color.BLUE);
            // Optionally, refresh combo boxes or navigate away
            // populateComboBoxes();
        } else {
            lblStatus.setText("Failed to submit request.");
            lblStatus.setForeground(Color.RED);
        }
    }
}