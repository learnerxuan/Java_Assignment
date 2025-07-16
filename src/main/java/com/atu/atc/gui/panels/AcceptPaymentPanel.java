package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author henge
 */
public class AcceptPaymentPanel extends JPanel{
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;

    public AcceptPaymentPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Accept Payment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment((SwingConstants.CENTER));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JTextField studentIdField = new JTextField();
        JTextField amountField = new JTextField();

        String[] methods = {"Cash", "Card", "Online Transfer"};
        JComboBox<String> methodCombo = new JComboBox<>(methods);

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(studentIdField);

        formPanel.add(new JLabel("Amount (RM):"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Payment Method:"));
        formPanel.add(methodCombo);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton submitBtn = new JButton("Submit Payment");
        JButton backBtn = new JButton("Back");
        btnPanel.add(submitBtn);
        btnPanel.add(backBtn);

        add(btnPanel, BorderLayout.SOUTH);

        submitBtn.addActionListener((ActionEvent e) -> {
            String studentId = studentIdField.getText().trim();
            String amountStr = amountField.getText().trim();
            String method = (String) methodCombo.getSelectedItem();

            if(studentId.isEmpty() || amountStr.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }

            try{
                double amount = Double.parseDouble(amountStr);

                if(amount <= 0){
                    JOptionPane.showMessageDialog(this, "Amount must be positive.");
                    return;
                }

                // Call service
                String receipt = receptionistService.acceptPayment(studentId, amount, method, receptionist.getId());
                JOptionPane.showMessageDialog(this, "Payment Successful!\n" + receipt);

                // Clear inputs
                studentIdField.setText("");
                amountField.setText("");
                methodCombo.setSelectedIndex(0);

            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a number.");
            }
        });
    }
}
