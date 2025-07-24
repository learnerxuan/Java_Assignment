package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Request;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author henge
 */
public class ManageRequestsPanel extends JPanel {
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public ManageRequestsPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator) {
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
        loadRequests();
    }

    private void initUI() {
        setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Manage Subject Change Requests", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 25, 112));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Request ID", "Student ID", "Current Subject", "Requested Subject", "Status", "Date"}, 0);
        requestTable = new JTable(tableModel);
        requestTable.setFont(new Font("Arial", Font.PLAIN, 14));
        requestTable.setRowHeight(25);
        requestTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(requestTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton approveBtn = new JButton("Approve");
        styleButton(approveBtn, new Color(40, 167, 69), new Color(33, 136, 56)); // Green

        JButton rejectBtn = new JButton("Reject");
        styleButton(rejectBtn, new Color(220, 53, 69), new Color(200, 43, 59)); // Red

        JButton deleteBtn = new JButton("Delete");
        styleButton(deleteBtn, new Color(108, 117, 125), new Color(88, 97, 105)); // Gray

        JButton backBtn = new JButton("Back");
        styleButton(backBtn, new Color(65, 105, 225), new Color(50, 85, 200)); // Blue

        approveBtn.addActionListener(e -> handleApprove());
        rejectBtn.addActionListener(e -> handleReject());
        deleteBtn.addActionListener(e -> handleDelete());
        backBtn.addActionListener(e -> navigator.navigateTo(MainFrame.RECEPTIONIST_DASHBOARD, receptionist));

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadRequests() {
        tableModel.setRowCount(0);
        List<Request> requests = receptionistService.getPendingRequests();
        for (Request r : requests) {
            tableModel.addRow(new Object[]{
                    r.getRequestId(),
                    r.getStudentId(),
                    r.getCurrentSubjectId(),
                    r.getRequestedSubjectId(),
                    r.getStatus(),
                    r.getRequestDate()
            });
        }
    }

    private void handleApprove() {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to approve.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        String result = receptionistService.approveRequest(requestId);
        JOptionPane.showMessageDialog(this, result);
        loadRequests();
    }

    private void handleReject() {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to reject.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        String result = receptionistService.rejectRequest(requestId);
        JOptionPane.showMessageDialog(this, result);
        loadRequests();
    }

    private void handleDelete() {
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to delete.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this request?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String result = receptionistService.deleteRequest(requestId);
            JOptionPane.showMessageDialog(this, result);
            loadRequests();
        }
    }

    private void styleButton(JButton button, Color bgColor, Color hoverColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
}