package com.photogallery.servlet;

import com.photogallery.config.AppConfig;
import com.photogallery.filter.AdminAuthFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("cloudinaryConfigured", AppConfig.isCloudinaryConfigured());
        req.setAttribute(
                "adminUsername",
                req.getSession().getAttribute(AdminAuthFilter.SESSION_USERNAME)
        );

        if ("1".equals(req.getParameter("success"))) {
            req.setAttribute("message", "Photo uploaded successfully.");
            req.setAttribute("messageType", "success");
        } else if (req.getParameter("error") != null) {
            req.setAttribute("message", req.getParameter("error"));
            req.setAttribute("messageType", "error");
        }

        req.getRequestDispatcher("/WEB-INF/upload.jsp").forward(req, resp);
    }
}
