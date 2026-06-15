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
        return get("db.url", "DB_URL");
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
        return get("db.user", "DB_USER");
    }

    public static String getDbPassword() {
        return get("db.password", "DB_PASSWORD");
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
