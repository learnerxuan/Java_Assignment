package com.atu.atc.data;

/**
 * @author henge
 */

import com.atu.atc.model.Payment;
import com.atu.atc.util.FileUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private static final String FILE_PATH = "data/payments.txt";
    private List<Payment> payments = new ArrayList<>();

    public void load(){
        payments.clear();
        List<String> lines = FileUtils.readDataLines(FILE_PATH);

        for (String line : lines){
            String[] parts = line.split(",", -1);
            if (parts.length == 7){
                String paymentId = parts[0].trim();
                String studentId = parts[1].trim();
                double amount = Double.parseDouble(parts[2].trim());
                LocalDate date = LocalDate.parse(parts[3].trim());
                String paymentMethod = parts[4].trim();
                String status = parts[5].trim();
                String receptionistId = parts[6].trim();

                Payment p = new Payment(paymentId, studentId, amount, date, paymentMethod, status, receptionistId);
                payments.add(p);
            }else {
                System.err.println("Invalid payment line: " + line);
            }
        }
    }

    public void save() {
        List<String> lines = new ArrayList<>();
        // Add header manually
        lines.add("payment_id,student_id,amount,date,payment_method,status,receptionist_id");
        for (Payment p : payments) {
            lines.add(p.toFileString());
        }
        FileUtils.writeLines(FILE_PATH, lines);
    }

    public void add(Payment payment) {
        payments.add(payment);
        save();
    }

    public List<Payment> getAll() {
        return payments;
    }

    public List<Payment> getByStudentId(String studentId) {
        List<Payment> result = new ArrayList<>();
        for (Payment p : payments) {
            if (p.getStudentId().equals(studentId)) {
                result.add(p);
            }
        }
        return result;
    }
}
