package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.gui.MainFrame.PanelNavigator;
import com.atu.atc.model.Request;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ViewRequestStatusPanel extends JPanel {
    private final StudentService studentService;
    private final PanelNavigator navigator;
    private final Student currentStudent;
    
    private JTable requestTable;
    private DefaultTableModel tableModel;
    
    public ViewRequestStatusPanel(StudentService studentService, PanelNavigator navigator, Student currentStudent) {
        this.studentService = studentService;
        this.navigator = navigator;
        this.currentStudent = currentStudent;
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Request Status", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(new Object[]{"Request ID", "Current Subject", "Requested Subject", "Status"}, 0);
        requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent));
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        loadRequestData();
    }
    
    private void loadRequestData() {
        tableModel.setRowCount(0);
        List<Request> requests = studentService.getRequestsByStudentId(currentStudent.getId());
        
        for (Request req : requests) {
            Optional<Subject> currentSubject = studentService.getSubjectRepository().getSubjectById(req.getCurrentSubjectId());
            Optional<Subject> requestedSubject = studentService.getSubjectRepository().getSubjectById(req.getRequestedSubjectId());
            
            String currentName = currentSubject.map(Subject::getName).orElse("Unknown");
            String requestedName = requestedSubject.map(Subject::getName).orElse("Unknown");
            
            tableModel.addRow(new Object[]{
                    req.getRequestId(),
                    req.getCurrentSubjectId() + " - " + currentName,
                    req.getRequestedSubjectId() + " - " + requestedName,
                    req.getStatus()
            });
        }
    }
}
