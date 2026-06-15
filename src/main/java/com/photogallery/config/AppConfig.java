package com.photogallery.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                PROPS.load(input);
            } else {
                System.err.println("[PhotoGallery] application.properties not found on classpath.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    private AppConfig() {
    }

    public static String get(String propertyKey, String envKey) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue.trim();
        }
        return PROPS.getProperty(propertyKey, "").trim();
    }

    public static String getDbUrl() {
        // 1. Check custom DB_URL env var or property
        String dbUrl = get("db.url", "DB_URL");
        if (!dbUrl.isEmpty()) {
            return dbUrl;
        }

        // 2. Check Railway's MYSQL_URL and convert it to JDBC format
        String mysqlUrl = System.getenv("MYSQL_URL");
        if (mysqlUrl != null && !mysqlUrl.isBlank()) {
            if (mysqlUrl.startsWith("mysql://")) {
                return "jdbc:mysql://" + mysqlUrl.substring(8);
            }
            return mysqlUrl.trim();
        }

        // 3. Construct JDBC URL from individual Railway variables
        String host = System.getenv("MYSQLHOST");
        String port = System.getenv("MYSQLPORT");
        String database = System.getenv("MYSQLDATABASE");
        if (host != null && !host.isBlank() && port != null && !port.isBlank() && database != null && !database.isBlank()) {
            return "jdbc:mysql://" + host.trim() + ":" + port.trim() + "/" + database.trim()
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        }

        return "";
    }

    public static String getBaseDbUrl() {
        String dbUrl = getDbUrl();
        int slashIndex = dbUrl.lastIndexOf('/');
        if (slashIndex > 0) {
            return dbUrl.substring(0, slashIndex + 1);
        }
        return dbUrl;
    }

    public static String getDbUser() {
        String dbUser = get("db.user", "DB_USER");
        if (!dbUser.isEmpty()) {
            return dbUser;
        }
        String mysqlUser = System.getenv("MYSQLUSER");
        if (mysqlUser != null && !mysqlUser.isBlank()) {
            return mysqlUser.trim();
        }
        return "";
    }

    public static String getDbPassword() {
        String dbPassword = get("db.password", "DB_PASSWORD");
        if (!dbPassword.isEmpty()) {
            return dbPassword;
        }
        String mysqlPassword = System.getenv("MYSQLPASSWORD");
        if (mysqlPassword != null && !mysqlPassword.isBlank()) {
            return mysqlPassword.trim();
        }
        return "";
    }

    public static String getCloudinaryCloudName() {
        return get("cloudinary.cloud_name", "CLOUDINARY_CLOUD_NAME");
    }

    public static String getCloudinaryApiKey() {
        return get("cloudinary.api_key", "CLOUDINARY_API_KEY");
    }

    public static String getCloudinaryApiSecret() {
        return get("cloudinary.api_secret", "CLOUDINARY_API_SECRET");
    }

    public static String getAdminUsername() {
        String username = get("admin.username", "ADMIN_USERNAME");
        return username.isEmpty() ? "admin" : username;
    }

    public static String getAdminPassword() {
        String password = get("admin.password", "ADMIN_PASSWORD");
        return password.isEmpty() ? "admin123" : password;
    }

    public static boolean isCloudinaryConfigured() {
        return !getCloudinaryCloudName().isEmpty()
                && !getCloudinaryApiKey().isEmpty()
                && !getCloudinaryApiSecret().isEmpty()
                && !getCloudinaryCloudName().equals("your_cloud_name");
    }
}
