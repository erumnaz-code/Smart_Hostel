package dao;

import db.DatabaseConnection;
import model.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    private Connection conn = DatabaseConnection.getConnection();

    /** Save (insert or update) a daily menu */
    public boolean saveMenu(Menu menu) {
        String sql = "INSERT INTO menu (date, breakfast, lunch, dinner, breakfast_price, lunch_price, dinner_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "breakfast=VALUES(breakfast), lunch=VALUES(lunch), dinner=VALUES(dinner), " +
                     "breakfast_price=VALUES(breakfast_price), lunch_price=VALUES(lunch_price), " +
                     "dinner_price=VALUES(dinner_price)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, menu.getDate());
            ps.setString(2, menu.getBreakfast());
            ps.setString(3, menu.getLunch());
            ps.setString(4, menu.getDinner());
            ps.setDouble(5, menu.getBreakfastPrice());
            ps.setDouble(6, menu.getLunchPrice());
            ps.setDouble(7, menu.getDinnerPrice());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving menu: " + e.getMessage());
            return false;
        }
    }

    public Menu getMenuByDate(String date) {
        String sql = "SELECT * FROM menu WHERE date=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Error fetching menu: " + e.getMessage());
        }
        return null;
    }

    /** Get all menus */
    public List<Menu> getAllMenus() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY date DESC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery(sql);
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching menus: " + e.getMessage());
        }
        return list;
    }

    private Menu mapRow(ResultSet rs) throws SQLException {
        return new Menu(
            rs.getInt("id"),
            rs.getString("date"),
            rs.getString("breakfast"),
            rs.getString("lunch"),
            rs.getString("dinner"),
            rs.getDouble("breakfast_price"),
            rs.getDouble("lunch_price"),
            rs.getDouble("dinner_price")
        );
    }
}
