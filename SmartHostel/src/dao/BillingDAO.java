package dao;

import db.DatabaseConnection;
import model.Billing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    private Connection conn = DatabaseConnection.getConnection();

   
    public boolean saveBill(Billing bill) {
        String checkSql = "SELECT id FROM billing WHERE student_id=? AND month=?";
        try {
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, bill.getStudentId());
            check.setString(2, bill.getMonth());
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE billing SET total_amount=?, status=? WHERE student_id=? AND month=?"
                );
                ps.setDouble(1, bill.getTotalAmount());
                ps.setString(2, bill.getStatus());
                ps.setInt(3, bill.getStudentId());
                ps.setString(4, bill.getMonth());
                ps.executeUpdate();
            } else {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO billing (student_id, month, total_amount, status) VALUES (?, ?, ?, ?)"
                );
                ps.setInt(1, bill.getStudentId());
                ps.setString(2, bill.getMonth());
                ps.setDouble(3, bill.getTotalAmount());
                ps.setString(4, bill.getStatus());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving bill: " + e.getMessage());
            return false;
        }
    }

    
    public boolean markAsPaid(int studentId, String month) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE billing SET status='PAID' WHERE student_id=? AND month=?"
            );
            ps.setInt(1, studentId);
            ps.setString(2, month);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating bill status: " + e.getMessage());
            return false;
        }
    }

    
    public List<Billing> getBillsByStudent(int studentId) {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT b.*, s.cms, s.name FROM billing b " +
                     "JOIN students s ON b.student_id = s.id " +
                     "WHERE b.student_id=? ORDER BY b.id ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Billing b = new Billing(
                    rs.getInt("id"), rs.getInt("student_id"),
                    rs.getString("month"), rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                b.setCms(rs.getString("cms"));
                b.setStudentName(rs.getString("name"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bills: " + e.getMessage());
        }
        return list;
    }

    public List<Billing> getAllBills() {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT b.*, s.cms, s.name FROM billing b " +
                     "JOIN students s ON b.student_id = s.id " +
                     "ORDER BY b.id ASC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery(sql);
            while (rs.next()) {
                Billing b = new Billing(
                    rs.getInt("id"), rs.getInt("student_id"),
                    rs.getString("month"), rs.getDouble("total_amount"),
                    rs.getString("status")
                );
                b.setCms(rs.getString("cms"));
                b.setStudentName(rs.getString("name"));
                list.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all bills: " + e.getMessage());
        }
        return list;
    }
}
