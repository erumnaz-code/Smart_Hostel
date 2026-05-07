package service;

import dao.AttendanceDAO;
import dao.BillingDAO;
import model.Billing;

import java.util.List;

public class BillingService {

    private BillingDAO    billingDAO    = new BillingDAO();
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

        public double calculateBill(int studentId, String yearMonth) {
        return attendanceDAO.sumPresentAmount(studentId, yearMonth);
    }

        public double calculateBill(int studentId, String yearMonth, double extraCharges) {
        return calculateBill(studentId, yearMonth) + extraCharges;
    }

        public double calculateBill(int studentId, String yearMonth, double extraCharges, double discount) {
        return calculateBill(studentId, yearMonth, extraCharges) - discount;
    }

       public boolean generateBill(int studentId, String month, String yearMonth) {
        double amount = calculateBill(studentId, yearMonth);
        Billing bill  = new Billing(0, studentId, month, amount, "UNPAID");
        return billingDAO.saveBill(bill);
    }

    public boolean markBillPaid(int studentId, String month) {
        return billingDAO.markAsPaid(studentId, month);
    }

    public List<Billing> getStudentBills(int studentId) {
        return billingDAO.getBillsByStudent(studentId);
    }

    public List<Billing> getAllBills() {
        return billingDAO.getAllBills();
    }
}
