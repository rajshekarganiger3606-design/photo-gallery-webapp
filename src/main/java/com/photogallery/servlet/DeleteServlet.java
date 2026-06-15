package com.photogallery.servlet;

import com.photogallery.config.AppConfig;
import com.photogallery.dao.PhotoDAO;
import com.photogallery.model.Photo;
import com.photogallery.service.CloudinaryService;
import com.photogallery.filter.AdminAuthFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class DeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null
                && Boolean.TRUE.equals(session.getAttribute(AdminAuthFilter.SESSION_ATTR));
        if (!loggedIn) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":false,\"message\":\"Access denied. Admin login required.\"}");
            return;
        }

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":false,\"message\":\"Photo ID is required.\"}");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":false,\"message\":\"Invalid photo ID format.\"}");
            return;
        }

        try {
            PhotoDAO dao = new PhotoDAO();
            Photo photo = dao.getPhotoById(id);

            if (photo == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\":false,\"message\":\"Photo not found.\"}");
                return;
            }

            // Delete from Cloudinary if configured
            if (AppConfig.isCloudinaryConfigured()) {
                try {
                    CloudinaryService cloudinaryService = new CloudinaryService();
                    cloudinaryService.deleteImage(photo.getImageUrl());
                } catch (Exception e) {
                    System.err.println("[PhotoGallery] Failed to delete image from Cloudinary: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                // Delete local file
                try {
                    String imageUrl = photo.getImageUrl();
                    String contextPath = req.getContextPath();
                    if (imageUrl.startsWith(contextPath)) {
                        String relativePath = imageUrl.substring(contextPath.length());
                        String realPath = req.getServletContext().getRealPath(relativePath);
                        if (realPath != null) {
                            java.io.File file = new java.io.File(realPath);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[PhotoGallery] Failed to delete local image file: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Delete from Database
            dao.deletePhoto(id);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":true,\"message\":\"Photo deleted successfully.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":false,\"message\":\"Failed to delete photo: " + e.getMessage() + "\"}");
        }
    }
}
