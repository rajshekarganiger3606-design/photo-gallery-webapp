package com.photogallery.model;

import java.sql.Timestamp;

public class Photo {

    private int id;
    private String fileName;
    private String mimeType;
    private String imageUrl;
    private Timestamp uploadedAt;

    public Photo() {
    }

    public Photo(String fileName, String mimeType, String imageUrl) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
