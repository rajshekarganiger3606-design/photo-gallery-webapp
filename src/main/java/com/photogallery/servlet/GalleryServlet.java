package com.photogallery.servlet;

import com.photogallery.dao.PhotoDAO;
import com.photogallery.model.Photo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class GalleryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            PhotoDAO dao = new PhotoDAO();
            List<Photo> photos = dao.getAllPhotos();

            req.setAttribute("photos", photos);
            req.getRequestDispatcher("/WEB-INF/gallery.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/error.jsp").forward(req, resp);
        }
    }
}
