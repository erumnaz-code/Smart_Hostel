package dao;

import db.DatabaseConnection;
import model.Attendance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    private Connection conn = DatabaseConnection.getConnection();

    
     //Mark attendance for a student with the meal price at that time.
         public boolean markAttendance(int studentId, String date, String mealType, String status, double price) {
        String checkSql = "SELECT id FROM attendance WHERE student_id=? AND date=? AND meal_type=?";
        try {
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, studentId);
            check.setString(2, date);
            check.setString(3, mealType);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Update
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE attendance SET status=?, price=? WHERE student_id=? AND date=? AND meal_type=?"
                );
                ps.setString(1, status);
                ps.setDouble(2, price);
                ps.setInt(3, studentId);
                ps.setString(4, date);
                ps.setString(5, mealType);
                ps.executeUpdate();
            } else {
                // Insert
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO attendance (student_id, date, meal_type, status, price) VALUES (?, ?, ?, ?, ?)"
                );
                ps.setInt(1, studentId);
                ps.setString(2, date);
                ps.setString(3, mealType);
                ps.setString(4, status);
                ps.setDouble(5, price);
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error marking attendance: " + e.getMessage());
            return false;
        }
    }

        public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.cms, s.name " +
                     "FROM attendance a JOIN students s ON a.student_id = s.id " +
                     "WHERE a.student_id=? ORDER BY a.date DESC, a.meal_type";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Attendance a = new Attendance(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("date"),
                    rs.getString("meal_type"),
                    rs.getString("status"),
                    rs.getDouble("price")
                );
                a.setCms(rs.getString("cms"));
                a.setStudentName(rs.getString("name"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching attendance: " + e.getMessage());
        }
        return list;
    }

        public List<Attendance> getAllAttendanceByDate(String date, String mealType) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.*, s.cms, s.name " +
                     "FROM attendance a JOIN students s ON a.student_id = s.id " +
                     "WHERE a.date=? AND a.meal_type=? ORDER BY s.cms";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ps.setString(2, mealType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Attendance a = new Attendance(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("date"),
                    rs.getString("meal_type"),
                    rs.getString("status"),
                    rs.getDouble("price")
                );
                a.setCms(rs.getString("cms"));
                a.setStudentName(rs.getString("name"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all attendance: " + e.getMessage());
        }
        return list;
    }

        public double sumPresentAmount(int studentId, String yearMonth) {
        // yearMonth format: "2025-04"
        String sql = "SELECT SUM(price) FROM attendance " +
                     "WHERE student_id=? AND DATE_FORMAT(date,'%Y-%m')=? AND status='PRESENT'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setString(2, yearMonth);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error summing attendance amount: " + e.getMessage());
        }
        return 0.0;
    }

        public int countPresent(int studentId, String yearMonth) {
        String sql = "SELECT COUNT(*) FROM attendance " +
                     "WHERE student_id=? AND DATE_FORMAT(date,'%Y-%m')=? AND status='PRESENT'";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setString(2, yearMonth);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error counting attendance: " + e.getMessage());
        }
        return 0;
    }
}
