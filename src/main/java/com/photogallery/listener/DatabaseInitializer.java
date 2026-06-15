package com.photogallery.listener;

import com.photogallery.config.AppConfig;
import com.photogallery.config.DatabaseConfig;
import com.photogallery.dao.AdminDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            initializeDatabase();
            event.getServletContext().setAttribute("dbConnected", true);
            System.out.println("[PhotoGallery] Database connected and schema ready.");
        } catch (Exception e) {
            event.getServletContext().setAttribute("dbConnected", false);
            event.getServletContext().setAttribute("dbError", e.getMessage());
            System.err.println("[PhotoGallery] Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeDatabase() throws SQLException {
        createDatabaseIfNeeded();
        createTables();
        migrateOldPhotosTableIfNeeded();
        seedDefaultAdmin();
        verifyConnection();
    }

    private void createDatabaseIfNeeded() {
        String baseUrl = AppConfig.getBaseDbUrl();
        try (Connection conn = DatabaseConfig.getConnection(baseUrl);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS photo_gallery");
            System.out.println("[PhotoGallery] Created database photo_gallery if it did not exist.");
        } catch (SQLException e) {
            System.out.println("[PhotoGallery] Skip database creation (permission restricted or database already exists): " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS photos ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "file_name VARCHAR(255) NOT NULL, "
                            + "mime_type VARCHAR(100) NOT NULL, "
                            + "image_url VARCHAR(500) NOT NULL, "
                            + "uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                            + ")"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS admin_users ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "username VARCHAR(50) NOT NULL UNIQUE, "
                            + "password VARCHAR(255) NOT NULL"
                            + ")"
            );
        }
    }

    private void migrateOldPhotosTableIfNeeded() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            if (!tableExists(conn, "photos")) {
                return;
            }

            boolean hasImageData = columnExists(conn, "photos", "image_data");
            boolean hasImageUrl = columnExists(conn, "photos", "image_url");

            try (Statement stmt = conn.createStatement()) {
                if (hasImageData && !hasImageUrl) {
                    stmt.executeUpdate("DELETE FROM photos");
                    stmt.executeUpdate(
                            "ALTER TABLE photos "
                                    + "ADD COLUMN image_url VARCHAR(500) NOT NULL DEFAULT '' "
                                    + "AFTER mime_type"
                    );
                    stmt.executeUpdate("ALTER TABLE photos DROP COLUMN image_data");
                    stmt.executeUpdate(
                            "ALTER TABLE photos MODIFY image_url VARCHAR(500) NOT NULL"
                    );
                } else if (!hasImageUrl) {
                    stmt.executeUpdate(
                            "ALTER TABLE photos "
                                    + "ADD COLUMN image_url VARCHAR(500) NOT NULL DEFAULT '' "
                                    + "AFTER mime_type"
                    );
                    stmt.executeUpdate(
                            "ALTER TABLE photos MODIFY image_url VARCHAR(500) NOT NULL"
                    );
                }
            }
        }
    }

    private void seedDefaultAdmin() throws SQLException {
        AdminDAO adminDAO = new AdminDAO();
        adminDAO.createAdminIfMissing(
                AppConfig.getAdminUsername(),
                AppConfig.getAdminPassword()
        );
    }

    private void verifyConnection() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            if (!rs.next()) {
                throw new SQLException("Database verification query failed");
            }
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private boolean columnExists(Connection conn, String tableName, String columnName)
            throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, tableName, columnName)) {
            return rs.next();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // No cleanup required.
    }
}
