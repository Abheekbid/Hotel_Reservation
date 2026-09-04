package com.waytogo;

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
		    String hotel_id = req.getParameter("hotel_id");

		    HttpSession session = req.getSession();
	        session.setAttribute("hotel_id", hotel_id);

		    req.setAttribute("hotel_id", hotel_id);

		    RequestDispatcher rd = req.getRequestDispatcher("bookings.jsp");
		    rd.forward(req, res);
		    
		    String room_id = req.getParameter("room_id");
		    if (room_id != null) {
		        req.setAttribute("room_id", room_id);
		        RequestDispatcher rd1 = req.getRequestDispatcher("payments.jsp");
		        rd1.forward(req, res);
		        return;
		    }
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
		LocalDate checkInDate  = LocalDate.parse(check_in);
	    LocalDate checkOutDate = LocalDate.parse(check_out);

	    long numberOfDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

	    if (numberOfDays <= 0) {
	        res.sendRedirect(req.getContextPath() + "/bookings?error=invalid_dates");
	        return;
	    }
		String room_type = req.getParameter("room_type");

		Bookings book = new Bookings();
		book.setName(name);
		book.setEmail(email);
		book.setCheck_in(check_in);
		book.setCheck_out(check_out);
		book.setRoom_type(room_type);
		book.setHotel_id(hotel_id);
		book.setNumberOfDays(numberOfDays);

		HashMap<String, String> map = book.check_reserv();
		req.setAttribute("roomIds", map);
		req.setAttribute("hotel_id", hotel_id);

		RequestDispatcher rd = req.getRequestDispatcher("bookings.jsp");
		rd.forward(req, res);
		
		double roomPrice = book.getRoomPrice();
		book.setRoom_price(roomPrice);

		double totalPrice = roomPrice * numberOfDays;

		double commissionRate;
		switch (room_type) {
		    case "Single Room": commissionRate = 10.0; break;
		    case "Double Room": commissionRate = 12.0; break;
		    case "Deluxe":      commissionRate = 15.0; break;
		    default:            commissionRate = 0.0;
		}

		double commissionAmount = totalPrice * (commissionRate / 100);
		book.setCommissionRate(commissionRate);
		book.setCommissionAmount(commissionAmount);
	}
}
