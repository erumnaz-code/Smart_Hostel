package service;

import db.DatabaseConnection;
import model.User;

import java.sql.*;

public class LoginService {

    private Connection conn = DatabaseConnection.getConnection();

        public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null; 
    }
}
