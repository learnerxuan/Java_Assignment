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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

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

    private final JTable enrollmentTable = new JTable();
    private final DefaultTableModel enrollmentTableModel = new DefaultTableModel(
            new Object[]{"Class ID", "Subject", "Tutor", "Day", "Time", "Withdraw"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5; // Only Withdraw button column is editable
        }
    };

    public ManageEnrollmentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionist = receptionist;
        this.receptionistService = receptionistService;
        this.navigator = navigator;

        initUI();
    }

    private void initUI() {
        setBackground(new Color(240, 248, 255)); // Alice Blue
        setLayout(new GridBagLayout());

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 2, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        cardPanel.setPreferredSize(new Dimension(900, 600));
        cardPanel.setMaximumSize(new Dimension(1000, 700));
        cardPanel.setMinimumSize(new Dimension(800, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Title
        JLabel titleLabel = new JLabel("Manage Enrollment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weighty = 0.1;
        cardPanel.add(titleLabel, gbc);
        row++;

        // Search bar
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        studentIdLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        cardPanel.add(studentIdLabel, gbc);

        studentIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cardPanel.add(studentIdField, gbc);

        JButton searchBtn = new JButton("Search");
        styleButton(searchBtn);
        gbc.gridx = 2;
        gbc.weightx = 0;
        cardPanel.add(searchBtn, gbc);
        row++;

        // Enrollment Table
        enrollmentTable.setModel(enrollmentTableModel);
        enrollmentTable.setFont(new Font("Arial", Font.PLAIN, 15));
        enrollmentTable.setRowHeight(28);
        enrollmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        enrollmentTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(enrollmentTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Enrolled Classes"));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        cardPanel.add(tableScroll, gbc);
        row++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        // Set preferred column widths after table is created
        enrollmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        enrollmentTable.getColumnModel().getColumn(0).setPreferredWidth(90);  // Class ID
        enrollmentTable.getColumnModel().getColumn(1).setPreferredWidth(160); // Subject
        enrollmentTable.getColumnModel().getColumn(2).setPreferredWidth(140); // Tutor
        enrollmentTable.getColumnModel().getColumn(3).setPreferredWidth(90);  // Day
        enrollmentTable.getColumnModel().getColumn(4).setPreferredWidth(130); // Time
        enrollmentTable.getColumnModel().getColumn(5).setPreferredWidth(110); // Withdraw

        // Available classes
        JLabel availableLabel = new JLabel("Available Classes:");
        availableLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        availableLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        cardPanel.add(availableLabel, gbc);

        availableClassesDropdown.setFont(new Font("Arial", Font.PLAIN, 15));
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        cardPanel.add(availableClassesDropdown, gbc);

        JButton addBtn = new JButton("Add Subject");
        styleButton(addBtn);
        gbc.gridx = 2;
        cardPanel.add(addBtn, gbc);
        row++;

        // Back button
        JButton backBtn = new JButton("Back");
        styleButton(backBtn);
        gbc.gridx = 2;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        cardPanel.add(backBtn, gbc);

        add(cardPanel, new GridBagConstraints());

        // Actions
        searchBtn.addActionListener(e -> handleSearch());
        addBtn.addActionListener(e -> handleAdd());
        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist));
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

    private void handleSearch() {
        enrollmentTableModel.setRowCount(0);
        availableClassesDropdown.removeAllItems();

        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
            return;
        }

        List<Enrollment> enrollments = receptionistService.getEnrollmentsByStudentId(studentId);
        if (enrollments.isEmpty()) {
            enrollmentTableModel.addRow(new Object[]{"-", "No enrollments found for student " + studentId, "-", "-", "-", null});
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

                // Store "Withdraw" as a string, not a JButton
                enrollmentTableModel.addRow(new Object[]{cls.getClassId(), subjectName, tutorName, cls.getDay(), cls.getStartTime() + " - " + cls.getEndTime(), "Withdraw"});
            }
        }
        // Set renderer and editor for Withdraw column after updating the model
        enrollmentTable.getColumn("Withdraw").setCellRenderer(new ButtonRenderer());
        enrollmentTable.getColumn("Withdraw").setCellEditor(new ButtonEditor(new JCheckBox()));

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

    // ButtonRenderer and ButtonEditor for JTable buttons
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 14));
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            // Red style for Withdraw button
            setBackground(new Color(220, 53, 69)); // Bootstrap red
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 53, 69), 1, true),
                new EmptyBorder(8, 20, 8, 20)
            ));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String classId;
        private int editingRow;
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(new Color(220, 53, 69)); // Bootstrap red
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 53, 69), 1, true),
                new EmptyBorder(8, 20, 8, 20)
            ));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> {
                fireEditingStopped();
                // Withdraw logic here
                if (currentStudentId != null && classId != null) {
                    receptionistService.withdrawSubject(currentStudentId, classId);
                    handleSearch();
                }
            });
        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            editingRow = row;
            // Get classId from the model for this row
            Object classIdObj = table.getModel().getValueAt(row, 0);
            classId = (classIdObj != null) ? classIdObj.toString() : null;
            return button;
        }
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
