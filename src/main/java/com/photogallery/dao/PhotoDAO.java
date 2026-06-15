package com.photogallery.dao;

import com.photogallery.config.DatabaseConfig;
import com.photogallery.model.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAO {

    public void savePhoto(Photo photo) throws SQLException {
        String sql = "INSERT INTO photos(file_name, mime_type, image_url) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, photo.getFileName());
            pstmt.setString(2, photo.getMimeType());
            pstmt.setString(3, photo.getImageUrl());

            pstmt.executeUpdate();
        }
    }

    public List<Photo> getAllPhotos() throws SQLException {
        List<Photo> photos = new ArrayList<>();

        String sql = "SELECT id, file_name, mime_type, image_url, uploaded_at "
                + "FROM photos ORDER BY uploaded_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                photos.add(mapPhoto(rs));
            }
        }

        return photos;
    }

    public Photo getPhotoById(int id) throws SQLException {
        String sql = "SELECT id, file_name, mime_type, image_url, uploaded_at "
                + "FROM photos WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapPhoto(rs);
                }
            }
        }

        return null;
    }

    private Photo mapPhoto(ResultSet rs) throws SQLException {
        Photo photo = new Photo();

        photo.setId(rs.getInt("id"));
        photo.setFileName(rs.getString("file_name"));
        photo.setMimeType(rs.getString("mime_type"));
        photo.setImageUrl(rs.getString("image_url"));
        photo.setUploadedAt(rs.getTimestamp("uploaded_at"));

        return photo;
    }
}
