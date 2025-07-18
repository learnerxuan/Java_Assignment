package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Classes;
import com.atu.atc.model.Enrollment;
import com.atu.atc.model.Student;
import com.atu.atc.model.Subject;
import com.atu.atc.model.Tutor;
import com.atu.atc.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViewSchedulePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    
    private final StudentService studentService;
    private final Student currentStudent;
    private final MainFrame mainFrame;
    
    public ViewSchedulePanel(StudentService studentService, Student currentStudent, MainFrame mainFrame) {
        this.studentService = studentService;
        this.currentStudent = currentStudent;
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel(new String[]{
                "Class ID", "Subject ID", "Subject Name", "Day", "Start Time", "End Time", "Tutor Name"
        }, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        loadSchedule();
    }
    
    private void loadSchedule() {
        List<Enrollment> allEnrollments = studentService.getEnrollmentRepository().getAll();
        List<Enrollment> myEnrollments = allEnrollments.stream()
                .filter(e -> e.getStudentId().equals(currentStudent.getId()))
                .collect(Collectors.toList());
        
        Map<String, Subject> subjects = studentService.getSubjectRepository().getAllSubjects().stream()
                .collect(Collectors.toMap(Subject::getSubjectId, s -> s));
        
        Map<String, Tutor> tutors = studentService.getTutorRepository().getAll().stream()
                .collect(Collectors.toMap(Tutor::getId, t -> t));
        
        tableModel.setRowCount(0);
        
        for (Enrollment enrollment : myEnrollments) {
            Optional<Classes> clsOptional = studentService.getClassesRepository().getById(enrollment.getClassId());
            clsOptional.ifPresent(cls -> {
                Subject subject = subjects.get(cls.getSubjectId());
                Tutor tutor = tutors.get(cls.getTutorId());
                
                String tutorName = (tutor != null) ? tutor.getFullName() : "Unknown";
                String subjectName = (subject != null) ? subject.getName() : "Unknown";
                
                tableModel.addRow(new Object[]{
                        cls.getClassId(),
                        cls.getSubjectId(),
                        subjectName,
                        cls.getDay(),
                        cls.getStartTime(),
                        cls.getEndTime(),
                        tutorName
                });
            });
        }
    }
}
