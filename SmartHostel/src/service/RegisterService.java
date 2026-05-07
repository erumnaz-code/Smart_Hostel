package service;

import db.DatabaseConnection;
import java.sql.*;

public class RegisterService {

    public static final int SUCCESS         = 0;
    public static final int CMS_NOT_FOUND   = 1;  
    public static final int ALREADY_LINKED  = 2;  
    public static final int USERNAME_TAKEN  = 3;  
    public static final int DB_ERROR        = 4;  
    private Connection conn = DatabaseConnection.getConnection();

        public int register(String cmsId, String username, String password) {
        try {
            
            String checkCms = "SELECT id, user_id FROM students WHERE cms = ?";
            PreparedStatement ps1 = conn.prepareStatement(checkCms);
            ps1.setString(1, cmsId);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                return CMS_NOT_FOUND;          
            }

            int studentId   = rs1.getInt("id");
            int existUserId = rs1.getInt("user_id"); // 0 when DB value is NULL

            // STEP 2 — Already registered?
            if (existUserId != 0) {
                return ALREADY_LINKED;
            }

            // STEP 3 — Username taken?
            String checkUsername = "SELECT id FROM users WHERE username = ?";
            PreparedStatement ps2 = conn.prepareStatement(checkUsername);
            ps2.setString(1, username);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                return USERNAME_TAKEN;
            }

            String insertUser = "INSERT INTO users (username, password, role) VALUES (?, ?, 'STUDENT')";
            PreparedStatement ps3 = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            ps3.setString(1, username);
            ps3.setString(2, password);
            ps3.executeUpdate();

            ResultSet keys    = ps3.getGeneratedKeys();
            int       newUserId = 0;
            if (keys.next()) newUserId = keys.getInt(1);

            
            String linkStudent = "UPDATE students SET user_id = ? WHERE id = ?";
            PreparedStatement ps4 = conn.prepareStatement(linkStudent);
            ps4.setInt(1, newUserId);
            ps4.setInt(2, studentId);
            ps4.executeUpdate();

            return SUCCESS;

        } catch (SQLException e) {
            System.out.println("RegisterService error: " + e.getMessage());
            return DB_ERROR;
        }
    }
}
