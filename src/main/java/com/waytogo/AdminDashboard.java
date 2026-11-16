package com.waytogo;

import BEANS.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Central controller for all admin operations.
 *
 * URL mapping: /AdminDashboard
 *
 * Every request is first checked for a valid admin session.
 * No admin session → redirect to /A_signin.html.
 *
 * ── GET actions (via "action" query param) ────────────────────────────────
 *
 *   (none / default)     Load dashboard overview: all hotels, users, bookings,
 *                        and commission summary → forward to AdminDashboard.jsp
 *
 *   action=hotels        Fetch all hotels         → forward to AdminDashboard.jsp
 *   action=users         Fetch all users          → forward to AdminDashboard.jsp
 *   action=bookings      Fetch all bookings       → forward to AdminDashboard.jsp
 *   action=commissions   Fetch commission summary → forward to AdminDashboard.jsp
 *
 *   action=deleteHotel   &hotel_id=X   Delete hotel + its rooms → redirect back
 *   action=deleteUser    &user_id=X    Delete user              → redirect back
 *   action=deleteBooking &booking_id=X Delete booking           → redirect back
 *
 *   action=logout        Invalidate session → redirect to /A_signin.html
 *
 * All redirect-after-writes carry a "status" query param so the JSP can
 * show a success/error banner:
 *   status=deleted_hotel_ok | deleted_hotel_fail
 *   status=deleted_user_ok  | deleted_user_fail
 *   status=deleted_booking_ok | deleted_booking_fail
 */
@WebServlet("/AdminDashboard")
public class AdminDashboard extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ── Session guard ─────────────────────────────────────────────────────────
    private boolean isAdminLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("admin_email") != null;
    }

    // ── GET ───────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(req)) {
            res.sendRedirect(req.getContextPath() + "/A_signin.html");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "overview";

        Admin admin = new Admin();

        switch (action) {

            // ── DELETE HOTEL ──────────────────────────────────────────────────
            case "deleteHotel": {
                String hotelId = req.getParameter("hotel_id");
                if (hotelId == null || hotelId.trim().isEmpty()) {
                    res.sendRedirect(req.getContextPath() + "/AdminDashboard?action=hotels&status=deleted_hotel_fail");
                    return;
                }
                boolean ok = admin.deleteHotel(hotelId.trim());
                res.sendRedirect(req.getContextPath() + "/AdminDashboard?action=hotels&status="
                        + (ok ? "deleted_hotel_ok" : "deleted_hotel_fail"));
                return;
            }

            // ── DELETE USER ───────────────────────────────────────────────────
            case "deleteUser": {
                String userId = req.getParameter("user_id");
                if (userId == null || userId.trim().isEmpty()) {
                    res.sendRedirect(req.getContextPath() + "/AdminDashboard?action=users&status=deleted_user_fail");
                    return;
                }
                boolean ok = admin.deleteUser(userId.trim());
                res.sendRedirect(req.getContextPath() + "/AdminDashboard?action=users&status="
                        + (ok ? "deleted_user_ok" : "deleted_user_fail"));
                return;
            }

            // ── DELETE BOOKING ────────────────────────────────────────────────
            case "deleteBooking": {
                String bookingId = req.getParameter("booking_id");
                if (bookingId == null || bookingId.trim().isEmpty()) {
                    res.sendRedirect(req.getContextPath() + "/AdminDashboard?action=bookings&status=deleted_booking_fail");
                    return;
                }
                boolean ok = admin.deleteBooking(bookingId.trim());
                res.sendRedirect(req.getContextPath() + "/AdminDashboard?action=bookings&status="
                        + (ok ? "deleted_booking_ok" : "deleted_booking_fail"));
                return;
            }

            // ── LOGOUT ────────────────────────────────────────────────────────
            case "logout": {
                HttpSession session = req.getSession(false);
                if (session != null) session.invalidate();
                res.sendRedirect(req.getContextPath() + "/A_signin.html");
                return;
            }

            // ── VIEW: hotels only ─────────────────────────────────────────────
            case "hotels": {
                ArrayList<HashMap<String, String>> hotels = admin.getAllHotels();
                req.setAttribute("hotels",     hotels);
                req.setAttribute("activeTab",  "hotels");
                req.setAttribute("status",     req.getParameter("status"));
                req.getRequestDispatcher("/AdminDashboard.jsp").forward(req, res);
                return;
            }

            // ── VIEW: users only ──────────────────────────────────────────────
            case "users": {
                ArrayList<HashMap<String, String>> users = admin.getAllUsers();
                req.setAttribute("users",      users);
                req.setAttribute("activeTab",  "users");
                req.setAttribute("status",     req.getParameter("status"));
                req.getRequestDispatcher("/AdminDashboard.jsp").forward(req, res);
                return;
            }

            // ── VIEW: bookings only ───────────────────────────────────────────
            case "bookings": {
                ArrayList<HashMap<String, String>> bookings = admin.getAllBookings();
                req.setAttribute("bookings",   bookings);
                req.setAttribute("activeTab",  "bookings");
                req.setAttribute("status",     req.getParameter("status"));
                req.getRequestDispatcher("/AdminDashboard.jsp").forward(req, res);
                return;
            }

            // ── VIEW: commissions only ────────────────────────────────────────
            case "commissions": {
                ArrayList<HashMap<String, String>> commissions = admin.getCommissionSummary();
                req.setAttribute("commissions", commissions);
                req.setAttribute("activeTab",   "commissions");
                req.setAttribute("status",      req.getParameter("status"));
                req.getRequestDispatcher("/AdminDashboard.jsp").forward(req, res);
                return;
            }

            // ── OVERVIEW (default): load everything ───────────────────────────
            default: {
                req.setAttribute("hotels",      admin.getAllHotels());
                req.setAttribute("users",       admin.getAllUsers());
                req.setAttribute("bookings",    admin.getAllBookings());
                req.setAttribute("commissions", admin.getCommissionSummary());
                req.setAttribute("activeTab",   "overview");
                req.setAttribute("status",      req.getParameter("status"));
                req.getRequestDispatcher("/AdminDashboard.jsp").forward(req, res);
            }
        }
    }

    // ── POST (not used — all actions are GET-based links/redirects) ───────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doGet(req, res);
    }
}
