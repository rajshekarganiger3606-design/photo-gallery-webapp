package com.photogallery.servlet;

import com.photogallery.dao.AdminDAO;
import com.photogallery.filter.AdminAuthFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        if (isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/admin/upload");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String username = trim(req.getParameter("username"));
        String password = trim(req.getParameter("password"));

        if (username.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Username and password are required.");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
            return;
        }

        try {
            AdminDAO adminDAO = new AdminDAO();

            if (adminDAO.authenticate(username, password)) {
                HttpSession session = req.getSession(true);
                session.setAttribute(AdminAuthFilter.SESSION_ATTR, true);
                session.setAttribute(AdminAuthFilter.SESSION_USERNAME, username);
                resp.sendRedirect(req.getContextPath() + "/admin/upload");
                return;
            }

            req.setAttribute("error", "Invalid username or password.");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null
                && Boolean.TRUE.equals(session.getAttribute(AdminAuthFilter.SESSION_ATTR));
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
