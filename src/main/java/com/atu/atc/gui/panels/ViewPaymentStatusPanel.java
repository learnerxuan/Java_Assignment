package com.atu.atc.gui.panels;

import com.atu.atc.model.User;
import com.atu.atc.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class ViewPaymentStatusPanel extends JPanel {
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private Student loggedInStudent;
    
    public ViewPaymentStatusPanel(User user) {
        if (user instanceof Student) {
            this.loggedInStudent = (Student) user;
        } else {
            JOptionPane.showMessageDialog(this, "Invalid user. Only students can view payments.");
            return;
        }
        
        setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Your Payment History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        tableModel = new DefaultTableModel(new String[]{
                "Payment ID", "Amount", "Method", "Receptionist", "Status"
        }, 0);
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        add(scrollPane, BorderLayout.CENTER);
        
        loadPaymentsForStudent();
    }
    
    private void loadPaymentsForStudent() {
        Map<String, String> receptionistMap = loadReceptionistNames("data/receptionists.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader("data/payments.txt"))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",", -1);
                if (parts.length != 7) continue;
                
                String paymentId = parts[0].trim();
                String studentId = parts[1].trim();
                String amount = parts[2].trim();
                String method = parts[4].trim();
                String status = parts[5].trim();
                String receptionistId = parts[6].trim();
                
                if (studentId.equals(loggedInStudent.getId())) {
                    String receptionistName = receptionistMap.getOrDefault(receptionistId, "Unknown");
                    tableModel.addRow(new Object[]{
                            paymentId, amount, method, receptionistName, status
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading payments file: " + e.getMessage());
        }
    }
    
    private Map<String, String> loadReceptionistNames(String filePath) {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirst = true;
            while ((line = reader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    map.put(parts[0].trim(), parts[1].trim()); // receptionist_id -> name
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load receptionist data: " + e.getMessage());
        }
        return map;
    }
}