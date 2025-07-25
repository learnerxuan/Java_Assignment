package com.atu.atc.data;

/**
 * @author henge
 */

import com.atu.atc.model.Payment;
import com.atu.atc.util.FileUtils;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private static final String FILE_PATH = "src/main/resources/data/payments.txt";
    private static final String HEADER = "payment_id,student_id,amount,date,payment_method,status,receptionist_id";
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
        lines.add(HEADER);
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
        return new ArrayList<>(payments);
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
