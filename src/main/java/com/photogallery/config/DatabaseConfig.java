package com.photogallery.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return getConnection(AppConfig.getDbUrl());
    }

    public static Connection getConnection(String jdbcUrl) throws SQLException {
        return DriverManager.getConnection(
                jdbcUrl,
                AppConfig.getDbUser(),
                AppConfig.getDbPassword()
        );
    }
}
