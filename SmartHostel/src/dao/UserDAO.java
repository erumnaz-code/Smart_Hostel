package dao;

import db.DatabaseConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection conn = DatabaseConnection.getConnection();

       public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("UserDAO.registerUser error: " + e.getMessage());
            return false;
        }
    }

       public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if at least one row found
        } catch (SQLException e) {
            System.out.println("UserDAO.usernameExists error: " + e.getMessage());
            return false;
        }
    }
}
