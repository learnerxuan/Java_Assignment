package com.atu.atc.gui.panels;

import com.atu.atc.model.Payment;
import com.atu.atc.model.Student;
import com.atu.atc.service.StudentService;
import com.atu.atc.gui.MainFrame.PanelNavigator;
import com.atu.atc.gui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewPaymentStatusPanel extends JPanel {
    private final StudentService studentService;
    private final PanelNavigator navigator;
    private final Student currentStudent;
    
    private JTable paymentTable;
    private JLabel totalBalanceLabel;
    private JButton backButton;
    
    public ViewPaymentStatusPanel(StudentService studentService, PanelNavigator navigator, Student student) {
        this.studentService = studentService;
        this.navigator = navigator;
        this.currentStudent = student;
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("View Payment Status", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);
        
        paymentTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(paymentTable);
        add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        totalBalanceLabel = new JLabel("Total Unpaid Balance: RM0.00");
        totalBalanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalBalanceLabel, BorderLayout.WEST);
        
        backButton = new JButton("Back");
        bottomPanel.add(backButton, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        loadPayments();
        
        backButton.addActionListener(e -> {
            navigator.navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent);
        });
    }
    
    private void loadPayments() {
        List<Payment> payments = studentService.getPayments(currentStudent.getId());
        
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Payment ID", "Amount", "Status", "Date"}, 0);
        for (Payment p : payments) {
            model.addRow(new Object[]{
                    p.getPaymentId(),
                    String.format("RM%.2f", p.getAmount()),
                    p.getStatus(),
                    p.getDate()
            });
        }
        
        paymentTable.setModel(model);
        double balance = studentService.getTotalBalance(currentStudent.getId());
        totalBalanceLabel.setText(String.format("Total Unpaid Balance: RM%.2f", balance));
    }
}
