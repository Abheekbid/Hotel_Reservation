package com.waytogo;

import BEANS.bookings;
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
 * Servlet implementation class Booking
 */
@WebServlet("/bookings")
public class Booking extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Booking() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String room_id = req.getParameter("room_id");

		if (room_id != null) {
			HttpSession session = req.getSession();
			session.setAttribute("room_id", room_id);

			bookings book = new bookings();
			book.setroom_id(room_id);
			double roomPrice = book.getRoomPrice();

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

		bookings book = new bookings();
		String hotel_name = book.getHotelName(hotel_id);
		session.setAttribute("hotel_name", hotel_name);

		req.setAttribute("hotel_id", hotel_id);
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

		bookings book = new bookings();
		book.setName(name);
		book.setEmail(email);
		book.setCheck_in(check_in);
		book.setCheck_out(check_out);
		book.setRoom_type(room_type);
		book.setHotel_id(hotel_id);
		book.setNumberOfDays(numberOfDays);

		session = req.getSession();
		session.setAttribute("name",           name);
		session.setAttribute("email",          email);
		session.setAttribute("check_in",       check_in);
		session.setAttribute("check_out",      check_out);
		session.setAttribute("room_type",      room_type);
		session.setAttribute("hotel_id",       hotel_id);
		session.setAttribute("number_of_days", numberOfDays);

		HashMap<String, String> map = book.check_reserv();
		req.setAttribute("roomIds",  map);
		req.setAttribute("hotel_id", hotel_id);

		RequestDispatcher rd = req.getRequestDispatcher("bookings.jsp");
		rd.forward(req, res);
	}
}
