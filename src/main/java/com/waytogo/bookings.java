package com.waytogo;
import java.util.ArrayList;
import BEANS.Bookings;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

/**
 * Servlet implementation class bookings
 */
@WebServlet("/bookings")
public class bookings extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public bookings() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String room_id = req.getParameter("room_id");

		if (room_id != null) {
			// User selected a room — store all booking details in session and go to payment
			HttpSession session = req.getSession();

			// room_id selected
			session.setAttribute("room_id", room_id);

			// Get room price from bean
			Bookings book = new Bookings();
			book.setroom_id(room_id);
			double roomPrice = book.getRoomPrice();

			// Retrieve what was stored in session during POST
			long numberOfDays = Long.parseLong(session.getAttribute("number_of_days").toString());
			String room_type  = (String) session.getAttribute("room_type");

			double commissionRate;
			switch (room_type) {
				case "Single Room": commissionRate = 10.0; break;
				case "Double Room": commissionRate = 12.0; break;
				case "Deluxe":      commissionRate = 15.0; break;
				default:            commissionRate = 0.0;
			}

			double totalPrice       = roomPrice * numberOfDays;
			double commissionAmount = totalPrice * (commissionRate / 100);

			session.setAttribute("room_price",        roomPrice);
			session.setAttribute("total_price",       totalPrice);
			session.setAttribute("commission_rate",   commissionRate);
			session.setAttribute("commission_amount", commissionAmount);

			res.sendRedirect(req.getContextPath() + "/payment");
			return;
		}

		// No room_id — initial page load, show booking form
		String hotel_id = req.getParameter("hotel_id");
		HttpSession session = req.getSession();
		session.setAttribute("hotel_id", hotel_id);
		
		req.setAttribute("hotel_id", hotel_id);
		String hotel_name = "";
		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, String>> hotelList =
		    (ArrayList<HashMap<String, String>>) session.getAttribute("hotelList");

		if (hotelList != null && hotel_id != null) {
		    for (HashMap<String, String> hotel : hotelList) {
		        if (hotel_id.equals(hotel.get("hotel_id"))) {
		            hotel_name = hotel.get("hotel_name");
		            break;
		        }
		    }
		}
		session.setAttribute("hotel_name", hotel_name);
		RequestDispatcher rd = req.getRequestDispatcher("bookings.jsp");
		rd.forward(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String hotel_id = (session != null) ? (String) session.getAttribute("hotel_id") : null;
		if (hotel_id == null) {
			hotel_id = req.getParameter("hotel_id");
		}

		String name      = req.getParameter("name");
		String email     = req.getParameter("email");
		String check_in  = req.getParameter("check_in");
		String check_out = req.getParameter("check_out");
		String room_type = req.getParameter("room_type");

		LocalDate checkInDate  = LocalDate.parse(check_in);
		LocalDate checkOutDate = LocalDate.parse(check_out);
		long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

		if (numberOfDays <= 0) {
			res.sendRedirect(req.getContextPath() + "/bookings?error=invalid_dates");
			return;
		}

		Bookings book = new Bookings();
		book.setName(name);
		book.setEmail(email);
		book.setCheck_in(check_in);
		book.setCheck_out(check_out);
		book.setRoom_type(room_type);
		book.setHotel_id(hotel_id);
		book.setNumberOfDays(numberOfDays);

		// Store form details in session for use across pages
		session = req.getSession();
		session.setAttribute("name",          name);
		session.setAttribute("email",         email);
		session.setAttribute("check_in",      check_in);
		session.setAttribute("check_out",     check_out);
		session.setAttribute("room_type",     room_type);
		session.setAttribute("hotel_id",      hotel_id);
		session.setAttribute("number_of_days", numberOfDays);

		// Get available rooms
		HashMap<String, String> map = book.check_reserv();
		req.setAttribute("roomIds",  map);
		req.setAttribute("hotel_id", hotel_id);

		RequestDispatcher rd = req.getRequestDispatcher("bookings.jsp");
		rd.forward(req, res);
	}
}
