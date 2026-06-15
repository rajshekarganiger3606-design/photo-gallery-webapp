package com.photogallery.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.photogallery.config.AppConfig;

import java.io.IOException;
import java.util.Map;

/**
 * Uploads images to Cloudinary and returns the public HTTPS URL.
 */
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", AppConfig.getCloudinaryCloudName(),
                "api_key", AppConfig.getCloudinaryApiKey(),
                "api_secret", AppConfig.getCloudinaryApiSecret(),
                "secure", true
        ));
    }

    public String uploadImage(byte[] imageData, String fileName) throws IOException {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Image data is empty");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> result = cloudinary.uploader().upload(
                imageData,
                ObjectUtils.asMap(
                        "folder", "photo-gallery",
                        "public_id", sanitizePublicId(fileName),
                        "overwrite", false,
                        "resource_type", "image"
                )
        );

        Object secureUrl = result.get("secure_url");
        if (secureUrl == null) {
            throw new IOException("Cloudinary did not return a secure_url");
        }

        return secureUrl.toString();
    }

    private String sanitizePublicId(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "photo-" + System.currentTimeMillis();
        }

        String baseName = fileName.contains(".")
                ? fileName.substring(0, fileName.lastIndexOf('.'))
                : fileName;

        return baseName.replaceAll("[^a-zA-Z0-9_-]", "_")
                + "-" + System.currentTimeMillis();
    }

    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || !imageUrl.contains("res.cloudinary.com")) {
            return;
        }

        String key = "/image/upload/";
        int index = imageUrl.indexOf(key);
        if (index == -1) {
            return;
        }

        // Extract path after /image/upload/
        String path = imageUrl.substring(index + key.length());

        // Strip the version segment (e.g., v123456789/) if present
        if (path.contains("/")) {
            String firstSegment = path.substring(0, path.indexOf('/'));
            if (firstSegment.matches("v\\d+")) {
                path = path.substring(path.indexOf('/') + 1);
            }
        }

        // Strip extension to get public_id
        int lastDot = path.lastIndexOf('.');
        String publicId = lastDot == -1 ? path : path.substring(0, lastDot);

        // Delete from Cloudinary
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
