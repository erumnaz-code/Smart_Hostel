package dao;

import db.DatabaseConnection;
import model.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    private Connection conn = DatabaseConnection.getConnection();


    public boolean addComplaint(int studentId, String text) {
        String sql = "INSERT INTO complaints (student_id, text, status) VALUES (?, ?, 'PENDING')";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setString(2, text);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding complaint: " + e.getMessage());
            return false;
        }
    }


    public boolean updateStatus(int complaintId, String status) {
        String sql = "UPDATE complaints SET status=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, complaintId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating complaint: " + e.getMessage());
            return false;
        }
    }

    
    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.id, s.cms, s.name, c.text, c.status, c.created_at " +
                     "FROM complaints c " +
                     "JOIN students s ON c.student_id = s.id " +
                     "ORDER BY c.created_at DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery(sql);
            while (rs.next()) {
                Complaint c = new Complaint(
                    rs.getInt("id"),
                    0,
                    rs.getString("text"),
                    rs.getString("status"),
                    rs.getString("created_at")
                );
                c.setCms(rs.getString("cms"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching complaints: " + e.getMessage());
        }
        return list;
    }

    public List<Complaint> getComplaintsByStudent(int studentId) {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE student_id=? ORDER BY created_at ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Complaint(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("text"),
                    rs.getString("status"),
                    rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student complaints: " + e.getMessage());
        }
        return list;
    }
}
