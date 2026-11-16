package com.waytogo;

import BEANS.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;

/**
 * Handles admin authentication.
 *
 * GET  /Adminsignin  — forwards to the admin sign-in page (A_signin.html)
 * POST /Adminsignin  — validates credentials; on success sets admin session
 *                      attributes and redirects to the dashboard.
 */
@WebServlet("/Adminsignin")
public class Adminsignin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ── GET: show the login page ──────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // If already logged in as admin, go straight to the dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("admin_email") != null) {
            res.sendRedirect(req.getContextPath() + "/AdminDashboard");
            return;
        }

        res.sendRedirect(req.getContextPath() + "/A_signin.html");
    }

    // ── POST: process credentials ─────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        // Basic null / empty guard
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/A_signin.html?error=missing_fields");
            return;
        }

        Admin admin = new Admin();
        admin.setEmail(email.trim());
        admin.setPassword(password);

        HashMap<String, String> result = admin.signIn();

        if (result != null) {
            // Valid credentials — create a fresh session to prevent fixation
            HttpSession session = req.getSession();
            session.setAttribute("admin_email", result.get("email"));
            session.setAttribute("admin_name",  result.get("name"));

            res.sendRedirect(req.getContextPath() + "/AdminDashboard");
        } else {
            res.sendRedirect(req.getContextPath() + "/A_signin.html?error=invalid_credentials");
        }
    }
}
