package com.photogallery.servlet;

import com.photogallery.config.AppConfig;
import com.photogallery.dao.PhotoDAO;
import com.photogallery.model.Photo;
import com.photogallery.service.CloudinaryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String contextPath = req.getContextPath();
        boolean wantsJson = "true".equals(req.getParameter("ajax"));

        try {
            Part filePart = req.getPart("photo");

            if (filePart == null || filePart.getSize() == 0) {
                handleFailure(resp, contextPath, wantsJson, "No file selected.");
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            String mimeType = filePart.getContentType();

            if (mimeType == null || !mimeType.startsWith("image/")) {
                handleFailure(resp, contextPath, wantsJson, "Only image files are allowed.");
                return;
            }

            String imageUrl;
            if (AppConfig.isCloudinaryConfigured()) {
                byte[] imageData;
                try (InputStream is = filePart.getInputStream()) {
                    imageData = is.readAllBytes();
                }
                CloudinaryService cloudinaryService = new CloudinaryService();
                imageUrl = cloudinaryService.uploadImage(imageData, fileName);
            } else {
                String uploadPath = req.getServletContext().getRealPath("/uploads");
                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String baseName = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
                String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.')) : "";
                String savedFileName = baseName.replaceAll("[^a-zA-Z0-9_-]", "_") + "-" + System.currentTimeMillis() + extension;

                java.io.File fileToSave = new java.io.File(uploadDir, savedFileName);
                try (InputStream input = filePart.getInputStream();
                     java.io.OutputStream output = new java.io.FileOutputStream(fileToSave)) {
                    input.transferTo(output);
                }
                imageUrl = req.getContextPath() + "/uploads/" + savedFileName;
            }

            Photo photo = new Photo();
            photo.setFileName(fileName);
            photo.setMimeType(mimeType);
            photo.setImageUrl(imageUrl);

            PhotoDAO dao = new PhotoDAO();
            dao.savePhoto(photo);

            if (wantsJson) {
                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.write("{\"success\":true,"
                        + "\"message\":\"Photo uploaded successfully\","
                        + "\"imageUrl\":\"" + escapeJson(imageUrl) + "\"}");
                return;
            }

            resp.sendRedirect(contextPath + "/admin/upload?success=1");

        } catch (Exception e) {
            e.printStackTrace();
            handleFailure(resp, contextPath, wantsJson, e.getMessage());
        }
    }

    private void handleFailure(HttpServletResponse resp,
                               String contextPath,
                               boolean wantsJson,
                               String message)
            throws IOException {

        if (wantsJson) {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.write("{\"success\":false,"
                    + "\"message\":\"" + escapeJson(message) + "\"}");
            return;
        }

        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(contextPath + "/admin/upload?error=" + encodedMessage);
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
