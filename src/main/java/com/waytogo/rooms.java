package com.waytogo;

import BEANS.hotels_details;
import jakarta.servlet.RequestDispatcher;
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
 * Handles room management for hotel owners.
 *
 * GET  /rooms                        — load rooms list, forward to Hotels.jsp
 * GET  /rooms?action=delete&room_id=X — delete a room, redirect back with status
 * POST /rooms                        — add a new room, redirect back with status
 *
 * All requests are session-guarded — hotel owner must be logged in.
 */
@WebServlet("/rooms")
public class rooms extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ── Session guard ─────────────────────────────────────────────────────────
    private boolean isHotelLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("hotel_id") != null;
    }

    // ── GET ───────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isHotelLoggedIn(req)) {
            res.sendRedirect(req.getContextPath() + "/H_signin.html");
            return;
        }

        HttpSession session = req.getSession(false);
        String hotel_id   = (String) session.getAttribute("hotel_id");
        String hotel_name = (String) session.getAttribute("hotel_name");

        String action = req.getParameter("action");

        // ── DELETE ROOM ───────────────────────────────────────────────────────
        if ("delete".equals(action)) {
            String room_id = req.getParameter("room_id");

            if (room_id == null || room_id.trim().isEmpty()) {
                res.sendRedirect(req.getContextPath() + "/rooms?status=delete_fail");
                return;
            }

            hotels_details bean = new hotels_details();
            bean.setHotel_id(hotel_id);

            boolean ok = bean.deleteRoom(room_id.trim());
            res.sendRedirect(req.getContextPath() + "/rooms?status="
                    + (ok ? "delete_ok" : "delete_fail"));
            return;
        }

        // ── VIEW ROOMS ────────────────────────────────────────────────────────
        hotels_details bean = new hotels_details();
        bean.setHotel_id(hotel_id);

        ArrayList<HashMap<String, String>> roomList = bean.getRooms();

        req.setAttribute("hotel_id",   hotel_id);
        req.setAttribute("hotel_name", hotel_name);
        req.setAttribute("roomList",   roomList);
        req.setAttribute("status",     req.getParameter("status"));

        RequestDispatcher rd = req.getRequestDispatcher("/Hotels.jsp");
        rd.forward(req, res);
    }

    // ── POST: add a room ──────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (!isHotelLoggedIn(req)) {
            res.sendRedirect(req.getContextPath() + "/H_signin.html");
            return;
        }

        HttpSession session = req.getSession(false);
        String hotel_id = (String) session.getAttribute("hotel_id");

        String roomNumber = req.getParameter("room_number");
        String roomType   = req.getParameter("room_type");
        String priceStr   = req.getParameter("price_per_night");

        // Basic validation
        if (roomNumber == null || roomNumber.trim().isEmpty()
                || roomType == null || roomType.trim().isEmpty()
                || priceStr == null || priceStr.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/rooms?status=add_fail");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr.trim());
            if (price <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/rooms?status=add_fail");
            return;
        }

        hotels_details bean = new hotels_details();
        bean.setHotel_id(hotel_id);

        boolean ok = bean.addRoom(roomNumber.trim(), roomType.trim(), price);
        res.sendRedirect(req.getContextPath() + "/rooms?status="
                + (ok ? "add_ok" : "add_fail"));
    }
}
