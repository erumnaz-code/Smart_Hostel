package dao;

import db.DatabaseConnection;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private Connection conn = DatabaseConnection.getConnection();


    public boolean addStudent(Student s) {
        String sql = "INSERT INTO students (cms, name, room_no, department, contact) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCms());
            ps.setString(2, s.getName());
            ps.setString(3, s.getRoomNo());
            ps.setString(4, s.getDepartment());
            ps.setString(5, s.getContact());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }
    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, room_no=?, department=?, contact=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getName());
            ps.setString(2, s.getRoomNo());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getContact());
            ps.setInt(5, s.getStudentId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

       public boolean deleteStudent(int studentId) {
        try {
            // STEP 1: Find the user_id linked to this student (0 if they never registered a login)
            int userId = 0;
            PreparedStatement ps0 = conn.prepareStatement("SELECT user_id FROM students WHERE id=?");
            ps0.setInt(1, studentId);
            ResultSet rs = ps0.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }

           
            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM students WHERE id=?");
            ps1.setInt(1, studentId);
            ps1.executeUpdate();

            if (userId != 0) {
                PreparedStatement ps2 = conn.prepareStatement("DELETE FROM users WHERE id=?");
                ps2.setInt(1, userId);
                ps2.executeUpdate();
            }

            // STEP 4: Re-sequence student IDs so they stay 1, 2, 3... after deletion
            Statement st = conn.createStatement();
            st.executeUpdate("SET @count = 0");
            st.executeUpdate("UPDATE students SET id = (@count := @count + 1) ORDER BY id");
            st.executeUpdate("ALTER TABLE students AUTO_INCREMENT = 1");

            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }


    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery(sql);
            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("cms"),
                    rs.getString("name"),
                    rs.getString("room_no"),
                    rs.getString("department"),
                    rs.getString("contact")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
        return list;
    }
    public Student getStudentByUserId(int userId) {
        String sql = "SELECT * FROM students WHERE user_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("cms"),
                    rs.getString("name"),
                    rs.getString("room_no"),
                    rs.getString("department"),
                    rs.getString("contact")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student by user ID: " + e.getMessage());
        }
        return null;
    }
}
