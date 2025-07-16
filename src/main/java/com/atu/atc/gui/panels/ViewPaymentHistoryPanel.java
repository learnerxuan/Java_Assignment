package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Student;
import com.atu.atc.model.Payment;
import com.atu.atc.service.StudentService;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ViewPaymentHistoryPanel extends JPanel {
    private final StudentService studentService;
    private Student currentStudent;
    
    private final DefaultTableModel tableModel;
    private final JLabel titleLabel;
    
    // Constructor now accepts MainFrame and StudentService
    public ViewPaymentHistoryPanel(MainFrame navigatorFrame, StudentService studentService) {
        this.studentService = studentService;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        
        titleLabel = new JLabel("Payment History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        add(titleLabel, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel();
        JTable paymentsTable = new JTable(tableModel);
        paymentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentsTable.setRowHeight(25);
        paymentsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        paymentsTable.getTableHeader().setBackground(new Color(100, 149, 237));
        paymentsTable.getTableHeader().setForeground(Color.WHITE);
        paymentsTable.setFillsViewportHeight(true);
        paymentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(paymentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        JButton backBtn = createStyledButton();
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(new Color(240, 248, 255));
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);
        
        // Use the navigator interface to go back
        backBtn.addActionListener(e -> navigatorFrame.getNavigator().navigateTo(MainFrame.STUDENT_DASHBOARD, currentStudent));
    }
    
    public void loadPaymentHistory(Student student) {
        this.currentStudent = student;
        if (currentStudent != null) {
            titleLabel.setText("Payment History for " + currentStudent.getFullName());
            List<Payment> payments = studentService.getPaymentsByStudentId(currentStudent.getId());
            
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Payment ID", "Amount", "Date", "Method", "Status", "Receptionist ID"});
            
            if (!payments.isEmpty()) {
                for (Payment payment : payments) {
                    tableModel.addRow(new Object[]{
                            payment.getPaymentId(),
                            payment.getAmount(),
                            payment.getDate(),
                            payment.getPaymentMethod(),
                            payment.getStatus(),
                            payment.getReceptionistId()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "No payment history found for " + currentStudent.getFullName() + ".", "No Data", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            titleLabel.setText("Payment History (No Student Selected)");
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Payment ID", "Amount", "Date", "Method", "Status", "Receptionist ID"});
            JOptionPane.showMessageDialog(this, "No student is logged in to view payment history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JButton createStyledButton() {
        JButton button = new JButton("Back to Dashboard");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
        });
        return button;
    }
}