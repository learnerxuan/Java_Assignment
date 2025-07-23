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
import java.util.Optional;

/**
 * @author henge
 */
public class ManageEnrollmentPanel extends JPanel {
    private final Receptionist receptionist;
    private final ReceptionistService receptionistService;
    private final MainFrame.PanelNavigator navigator;

    private final JTextField studentIdField = new JTextField(10);
    private final JPanel enrollmentListPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private final JComboBox<String> availableClassesDropdown = new JComboBox<>();
    private String currentStudentId = null;

    public ManageEnrollmentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionist = receptionist;
        this.receptionistService = receptionistService;
        this.navigator = navigator;

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Top search bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Student ID:"));
        searchPanel.add(studentIdField);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchBtn);
        add(searchPanel, BorderLayout.NORTH);

        // Center panel showing enrollment list
        JPanel centerPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(enrollmentListPanel);
        centerPanel.add(new JLabel("Enrolled Classes:"), BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom: available classes + add + back
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JLabel("Available Classes:"));
        bottomPanel.add(availableClassesDropdown);
        JButton addBtn = new JButton("Add Subject");
        JButton backBtn = new JButton("Back");
        bottomPanel.add(addBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        searchBtn.addActionListener(e -> handleSearch());
        addBtn.addActionListener(e -> handleAdd());
        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist));
    }

    private void handleSearch() {
        enrollmentListPanel.removeAll();
        availableClassesDropdown.removeAllItems();

        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
            return;
        }

        List<Enrollment> enrollments = receptionistService.getEnrollmentsByStudentId(studentId);
        if (enrollments.isEmpty()) {
            enrollmentListPanel.add(new JLabel("No enrollments found for student " + studentId));
        } else {
            currentStudentId = studentId;
            for (Enrollment e : enrollments) {
                Optional<Classes> optClass = receptionistService.getClassById(e.getClassId());
                if (optClass.isEmpty()) continue;

                Classes cls = optClass.get();
                Optional<Subject> optSubj = receptionistService.getSubjectById(cls.getSubjectId());
                Optional<Tutor> optTutor = receptionistService.getTutorById(cls.getTutorId());

                String subjectName = optSubj.map(Subject::getName).orElse("Unknown Subject");
                String tutorName = optTutor.map(Tutor::getFullName).orElse("Unknown Tutor");

                JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                rowPanel.add(new JLabel(String.format("Class ID: %s | Subject: %s | Tutor: %s | Day: %s | Time: %s - %s",
                        cls.getClassId(), subjectName, tutorName, cls.getDay(), cls.getStartTime(), cls.getEndTime())));

                JButton withdrawBtn = new JButton("Withdraw");
                withdrawBtn.addActionListener(ev -> {
                    receptionistService.withdrawSubject(studentId, cls.getClassId());
                    handleSearch();
                });
                rowPanel.add(withdrawBtn);
                enrollmentListPanel.add(rowPanel);
            }
        }

        // Show available classes based on student level
        List<Classes> available = receptionistService.getAvailableClassesMatchingStudentLevel(studentId);
        for (Classes c : available) {
            Subject subj = receptionistService.getSubjectById(c.getSubjectId()).orElse(null);
            Tutor tut = receptionistService.getTutorById(c.getTutorId()).orElse(null);
            if (subj != null && tut != null) {
                availableClassesDropdown.addItem(
                        c.getClassId() + " - " + subj.getName() + " by " + tut.getFullName() + " on " + c.getDay() + " at " + c.getStartTime()
                );
            }
        }

        revalidate();
        repaint();
    }

    private void handleAdd() {
        if (currentStudentId == null) {
            JOptionPane.showMessageDialog(this, "Search a student first.");
            return;
        }

        List<Enrollment> currentEnrollments = receptionistService.getEnrollmentsByStudentId(currentStudentId);
        if (currentEnrollments.size() >= 3) {
            JOptionPane.showMessageDialog(this, "Student already enrolled in 3 subjects.");
            return;
        }

        String selected = (String) availableClassesDropdown.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a class to add.");
            return;
        }

        String classId = selected.split(" - ")[0];
        receptionistService.addSubjectEnrollment(currentStudentId, classId);
        JOptionPane.showMessageDialog(this, "Class added successfully.");
        handleSearch(); // Refresh
    }
}
