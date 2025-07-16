package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.service.TutorService;
import com.atu.atc.data.SubjectRepository;
import com.atu.atc.data.ClassesRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageClassesPanel extends JPanel {

    private final TutorService tutorService;
    private final SubjectRepository subjectRepository;
    private final ClassesRepository classesRepository;
    private final MainFrame.PanelNavigator navigator;
    private Tutor loggedInTutor;

    private final JTextField classIdField = new JTextField(10);
    private final JTextField dayField = new JTextField(10);
    private final JTextField startTimeField = new JTextField(10);
    private final JTextField endTimeField = new JTextField(10);
    private final JComboBox<String> subjectComboBox = new JComboBox<>();
    private final JTextArea classArea = new JTextArea(10, 40);

    public ManageClassesPanel(TutorService tutorService, SubjectRepository subjectRepository, ClassesRepository classesRepository, MainFrame.PanelNavigator navigator) {
        this.tutorService = tutorService;
        this.subjectRepository = subjectRepository;
        this.classesRepository = classesRepository;
        this.navigator = navigator;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage My Classes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 10, 150));

        inputPanel.add(new JLabel("Class ID:"));
        classIdField.setEditable(false);
        inputPanel.add(classIdField);

        inputPanel.add(new JLabel("Subject:"));
        populateSubjectComboBox();
        inputPanel.add(subjectComboBox);

        inputPanel.add(new JLabel("Day:"));
        inputPanel.add(dayField);

        inputPanel.add(new JLabel("Start Time (HH:MM):"));
        inputPanel.add(startTimeField);

        inputPanel.add(new JLabel("End Time (HH:MM):"));
        inputPanel.add(endTimeField);

        JButton viewBtn = new JButton("View My Classes");
        JButton addBtn = new JButton("Add Class");
        JButton updateBtn = new JButton("Update Class");
        JButton deleteBtn = new JButton("Delete Class");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        classArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(classArea);
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            if (loggedInTutor == null) {
                classArea.setText("Tutor context not available.");
                return;
            }
            List<Classes> tutorClasses = classesRepository.getByTutorId(loggedInTutor.getId());
            if (tutorClasses.isEmpty()) {
                classArea.setText("You have no classes yet.");
            } else {
                String text = tutorClasses.stream().map(Classes::toString).collect(Collectors.joining("\n"));
                classArea.setText(text);
            }
        });

        addBtn.addActionListener(e -> {
            if (loggedInTutor == null) return;
            String subjectName = (String) subjectComboBox.getSelectedItem();
            String day = dayField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();

            if (day.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Day, Start Time and End Time cannot be empty.");
                return;
            }

            Optional<Subject> subjectOpt = subjectRepository.getAllSubjects().stream()
                    .filter(s -> s.getName().equals(subjectName))
                    .findFirst();

            if (subjectOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid subject selected.");
                return;
            }

            String schedule = day + " " + startTime + "-" + endTime;
            Classes newClass = new Classes("", subjectOpt.get().getSubjectId(), loggedInTutor.getId(), day, startTime, endTime);
            boolean added = tutorService.addClassInformation(newClass);
            JOptionPane.showMessageDialog(this, added ? "Class added." : "Failed to add class.");
        });

        updateBtn.addActionListener(e -> {
            if (loggedInTutor == null) return;
            String classId = classIdField.getText().trim();
            String subjectName = (String) subjectComboBox.getSelectedItem();
            String day = dayField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();

            if (classId.isEmpty() || day.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required for update.");
                return;
            }

            Optional<Subject> subjectOpt = subjectRepository.getAllSubjects().stream()
                    .filter(s -> s.getName().equals(subjectName))
                    .findFirst();

            if (subjectOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid subject selected.");
                return;
            }

            String schedule = day + " " + startTime + "-" + endTime;
            Classes updated = new Classes(classId, subjectOpt.get().getSubjectId(), loggedInTutor.getId(), day, startTime, endTime);
            boolean success = tutorService.updateClassInformation(updated);
            JOptionPane.showMessageDialog(this, success ? "Class updated." : "Update failed.");
        });

        deleteBtn.addActionListener(e -> {
            String classId = classIdField.getText().trim();
            if (classId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter Class ID to delete.");
                return;
            }
            boolean deleted = tutorService.deleteClassInformation(classId);
            JOptionPane.showMessageDialog(this, deleted ? "Class deleted." : "Failed to delete.");
        });

        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));
    }

    private void populateSubjectComboBox() {
        subjectComboBox.removeAllItems();
        subjectRepository.getAllSubjects().forEach(s -> subjectComboBox.addItem(s.getName()));
    }

    public void updateUserContext(User user) {
        if (user instanceof Tutor) {
            this.loggedInTutor = (Tutor) user;
        }
    }
}
