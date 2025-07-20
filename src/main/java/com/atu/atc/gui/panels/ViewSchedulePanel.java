package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Tutor;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewSchedulePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    
    private final StudentService studentService;
    private final Student currentStudent;
    private final MainFrame.PanelNavigator navigator;
    
    public ViewSchedulePanel(StudentService studentService, MainFrame.PanelNavigator navigator, Student currentStudent) {
        this.studentService = studentService;
        this.navigator = navigator;
        this.currentStudent = currentStudent;
        
        setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel(new String[]{
                "Class ID", "Subject ID", "Subject Name", "Day", "Start Time", "End Time", "Tutor Name"
        }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        loadSchedule();
    }
    
    private void loadSchedule() {
        List<Classes> myClasses = studentService.getSchedule(currentStudent.getId());
        
        Map<String, Subject> subjectMap = studentService.getSubjectRepository().getAllSubjects().stream()
                .collect(Collectors.toMap(Subject::getSubjectId, s -> s));
        
        Map<String, Tutor> tutorMap = studentService.getTutorRepository().getAll().stream()
                .collect(Collectors.toMap(Tutor::getId, t -> t));
        
        tableModel.setRowCount(0);
        
        for (Classes cls : myClasses) {
            Subject subject = subjectMap.get(cls.getSubjectId());
            Tutor tutor = tutorMap.get(cls.getTutorId());
            
            String subjectName = (subject != null) ? subject.getName() : "Unknown";
            String tutorName = (tutor != null) ? tutor.getFullName() : "Unknown";
            
            tableModel.addRow(new Object[]{
                    cls.getClassId(),
                    cls.getSubjectId(),
                    subjectName,
                    cls.getDay(),
                    cls.getStartTime(),
                    cls.getEndTime(),
                    tutorName
            });
        }
    }
}
