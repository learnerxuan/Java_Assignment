package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Tutor;
import com.atu.atc.model.User;
import com.atu.atc.service.TutorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class ManageClassesPanel extends JPanel {

    private final TutorService tutorService;
    private final MainFrame.PanelNavigator navigator;
    private Tutor loggedInTutor;

    private final JTextField classIdField = new JTextField(10);
    private final JTextField dayField = new JTextField(10);
    private final JTextField startTimeField = new JTextField(10);
    private final JTextField endTimeField = new JTextField(10);
    private final JComboBox<String> subjectComboBox = new JComboBox<>();
    private final JTextArea classArea = new JTextArea(10, 40);

    public ManageClassesPanel(TutorService tutorService, MainFrame.PanelNavigator navigator, Tutor tutorUser) {
        this.tutorService = tutorService;
        this.navigator = navigator;
        this.loggedInTutor = tutorUser;
        initUI();
    }

    private void initUI() {
        setBackground(new Color(245, 250, 255));
        setLayout(new BorderLayout(25, 25));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Manage My Classes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 20, 20));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        formPanel.add(new JLabel("Class ID:"));
        formPanel.add(classIdField);

        formPanel.add(new JLabel("Subject:"));
        populateSubjectComboBox();
        formPanel.add(subjectComboBox);

        formPanel.add(new JLabel("Day:"));
        formPanel.add(dayField);

        formPanel.add(new JLabel("Start Time (HH:MM):"));
        formPanel.add(startTimeField);

        formPanel.add(new JLabel("End Time (HH:MM):"));
        formPanel.add(endTimeField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewBtn = createStyledButton("View Classes");
        JButton addBtn = createStyledButton("Add Class");
        JButton updateBtn = createStyledButton("Update");
        JButton deleteBtn = createStyledButton("Delete");
        JButton backBtn = createBackButton("Back");

        buttonPanel.add(viewBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);

        JPanel centerCard = new JPanel();
        centerCard.setLayout(new BoxLayout(centerCard, BoxLayout.Y_AXIS));
        centerCard.setBackground(Color.WHITE);
        centerCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 220, 240), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        centerCard.add(formPanel);
        centerCard.add(Box.createVerticalStrut(20));
        centerCard.add(buttonPanel);
        centerCard.add(Box.createVerticalStrut(20));
        
        JScrollPane scrollPane = new JScrollPane(classArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Classes"));
        classArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        classArea.setEditable(false);

        centerCard.add(scrollPane);
        add(centerCard, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> viewClasses());
        addBtn.addActionListener(e -> addClass());
        updateBtn.addActionListener(e -> updateClass());
        deleteBtn.addActionListener(e -> deleteClass());
        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.TUTOR_DASHBOARD, loggedInTutor));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 130, 180), 1, true),
            new EmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }

    private JButton createBackButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 230, 230));
        button.setForeground(new Color(150, 50, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 150, 150), 1, true),
            new EmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }

    private void viewClasses() {
        if (loggedInTutor == null) {
            classArea.setText("Tutor context not available.");
            return;
        }
        List<String[]> classes = tutorService.getClassesByTutorId(loggedInTutor.getId());
        if (classes.isEmpty()) {
            classArea.setText("You have no classes yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String[] cls : classes) {
                sb.append("Class ID: ").append(cls[0])
                  .append(" | Subject: ").append(cls[1])
                  .append(" | Day: ").append(cls[2])
                  .append(" | Time: ").append(cls[3]).append("-").append(cls[4])
                  .append("\n");
            }
            classArea.setText(sb.toString());
        }
    }

    private void addClass() {
        String subjectId = loggedInTutor.getSubject();
        String day = dayField.getText().trim();
        String start = startTimeField.getText().trim();
        String end = endTimeField.getText().trim();
        if (day.isEmpty() || start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        String schedule = day + " " + start + "-" + end;
        boolean added = tutorService.addClassInformation(subjectId, loggedInTutor.getLevel(), 0.0, schedule, loggedInTutor.getId());
        JOptionPane.showMessageDialog(this, added ? "Class added." : "Failed to add class.");
    }

    private void updateClass() {
        String classId = classIdField.getText().trim();
        String subjectId = loggedInTutor.getSubject();
        String day = dayField.getText().trim();
        String start = startTimeField.getText().trim();
        String end = endTimeField.getText().trim();
        if (classId.isEmpty() || day.isEmpty() || start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        String schedule = day + " " + start + "-" + end;
        boolean updated = tutorService.updateClassInformation(classId, subjectId, loggedInTutor.getLevel(), 0.0, schedule, loggedInTutor.getId());
        JOptionPane.showMessageDialog(this, updated ? "Class updated." : "Failed to update class.");
    }

    private void deleteClass() {
        String classId = classIdField.getText().trim();
        if (classId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Class ID to delete.");
            return;
        }
        boolean deleted = tutorService.deleteClassInformation(classId);
        JOptionPane.showMessageDialog(this, deleted ? "Class deleted." : "Failed to delete class.");
    }

    private void populateSubjectComboBox() {
        subjectComboBox.removeAllItems();
        if (loggedInTutor != null) {
            String tutorSubject = loggedInTutor.getSubject();
            subjectComboBox.addItem(tutorSubject);
            subjectComboBox.setSelectedItem(tutorSubject);
            subjectComboBox.setEnabled(false);
        }
    }

    public void updateUserContext(User user) {
        if (user instanceof Tutor) {
            this.loggedInTutor = (Tutor) user;
        }
    }
}
