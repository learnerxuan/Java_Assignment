package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Payment;
import com.atu.atc.service.ReceptionistService;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * @author henge
 */
public class AcceptPaymentPanel extends JPanel {
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    public AcceptPaymentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
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
        JLabel titleLabel = new JLabel("Accept Payment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        cardPanel.add(titleLabel, gbc);
        row++;

        // Form
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        studentIdLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        cardPanel.add(studentIdLabel, gbc);

        JTextField studentIdField = new JTextField();
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cardPanel.add(studentIdField, gbc);
        row++;

        JLabel amountLabel = new JLabel("Amount (RM):");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        amountLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        cardPanel.add(amountLabel, gbc);

        JTextField amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 16));
        amountField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(173, 216, 230), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cardPanel.add(amountField, gbc);
        row++;

        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        methodLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        cardPanel.add(methodLabel, gbc);

        String[] methods = {"Cash", "Card", "Online Transfer"};
        JComboBox<String> methodCombo = new JComboBox<>(methods);
        methodCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cardPanel.add(methodCombo, gbc);
        row++;

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton submitBtn = new JButton("Submit Payment");
        styleButton(submitBtn);
        btnPanel.add(submitBtn);

        JButton showTransactionsBtn = new JButton("Show Past Transactions");
        styleButton(showTransactionsBtn);
        btnPanel.add(showTransactionsBtn);

        JButton backBtn = new JButton("Back");
        styleButton(backBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        cardPanel.add(btnPanel, gbc);

        add(cardPanel, new GridBagConstraints());

        showTransactionsBtn.addActionListener(e -> {
            List<Payment> payments = receptionistService.getAllPayments();

            if (payments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No transactions found.");
                return;
            }

            // Create table
            String[] columns = {"Payment ID", "Student ID", "Amount", "Date", "Method", "Status", "Receptionist ID"};
            Object[][] data = new Object[payments.size()][columns.length];

            for (int i = 0; i < payments.size(); i++) {
                Payment p = payments.get(i);
                data[i][0] = p.getPaymentId();
                data[i][1] = p.getStudentId();
                data[i][2] = "RM " + String.format("%.2f", p.getAmount());
                data[i][3] = p.getDate().toString();
                data[i][4] = p.getPaymentMethod();
                data[i][5] = p.getStatus();
                data[i][6] = p.getReceptionistId();
            }

            JTable table = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(table);

            JOptionPane.showMessageDialog(this, scrollPane, "Past Transactions", JOptionPane.PLAIN_MESSAGE);
        });

        submitBtn.addActionListener((ActionEvent e) -> {
            String studentId = studentIdField.getText().trim();
            String amountStr = amountField.getText().trim();
            String method = (String) methodCombo.getSelectedItem();

            if (studentId.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.");
                    return;
                }

                // Call service
                String receipt = receptionistService.acceptPayment(studentId, amount, method, receptionist.getId());
                
                JTextArea receiptArea = new JTextArea(receipt);
                receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Larger font
                receiptArea.setEditable(false);
                receiptArea.setOpaque(false); // Make it transparent
                
                JScrollPane scrollPane = new JScrollPane(receiptArea);
                scrollPane.setPreferredSize(new Dimension(400, 300)); // Set preferred size for the scroll pane
                
                JOptionPane.showMessageDialog(this, scrollPane, "Payment Successful!", JOptionPane.PLAIN_MESSAGE);

                // Clear inputs
                studentIdField.setText("");
                amountField.setText("");
                methodCombo.setSelectedIndex(0);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.");
            }
        });

        // Back action
        backBtn.addActionListener(e -> {
            navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist);
        });
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
}