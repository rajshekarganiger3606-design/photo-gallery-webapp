package com.photogallery.dao;

import com.photogallery.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT password FROM admin_users WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return password.equals(rs.getString("password"));
                }
            }
        }

        return false;
    }

    public void createAdminIfMissing(String username, String password) throws SQLException {
        String checkSql = "SELECT id FROM admin_users WHERE username = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return;
                }
            }

            String insertSql = "INSERT INTO admin_users(username, password) VALUES (?, ?)";

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
            }
        }
    }
}
