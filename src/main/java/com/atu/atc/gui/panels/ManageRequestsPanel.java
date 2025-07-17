package com.atu.atc.gui.panels;

import com.atu.atc.gui.MainFrame;
import com.atu.atc.model.Receptionist;
import com.atu.atc.model.Request;
import com.atu.atc.service.ReceptionistService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author henge
 */
public class ManageRequestsPanel extends JPanel{
    private final ReceptionistService receptionistService;
    private final Receptionist receptionist;
    private final MainFrame.PanelNavigator navigator;
    private JTable requestTable;
    private DefaultTableModel tableModel;

    public ManageRequestsPanel(ReceptionistService receptionistService, Receptionist receptionist, MainFrame.PanelNavigator navigator){
        this.receptionistService = receptionistService;
        this.receptionist = receptionist;
        this.navigator = navigator;

        initUI();
        loadRequests();
    }

    private void initUI(){
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Subject Change Requests", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Request ID", "Student ID", "Current Subject", "Status", "Date"}, 0);
        requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");

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

    private void loadRequests(){
        tableModel.setRowCount(0);
        List<Request> requests = receptionistService.getPendingRequests();
        for (Request r : requests){
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

    private void handleApprove(){
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a request to approve.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        String result = receptionistService.approveRequest(requestId);
        JOptionPane.showMessageDialog(this, result);
        loadRequests();
    }

    private void handleReject(){
        int selectedRow = requestTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a request to reject.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);
        String result = receptionistService.rejectRequest(requestId);
        JOptionPane.showMessageDialog(this, result);
        loadRequests();
    }

    private void handleDelete(){
        int selectedRow = requestTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a request to delete.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this request?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION){
            String result = receptionistService.deleteRequest(requestId);
            JOptionPane.showMessageDialog(this, result);
            loadRequests();
        }
    }
}
