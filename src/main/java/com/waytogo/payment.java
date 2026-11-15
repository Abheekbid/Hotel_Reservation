package com.waytogo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class payment
 */
@WebServlet("/payment")
public class payment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public payment() {
		super();
	}

	// GET — forwards to payment confirmation page
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		// Guard — redirect if not logged in
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("user_id") == null) {
			res.sendRedirect(req.getContextPath() + "/U_signIn.html");
			return;
		}

		req.getRequestDispatcher("/payment.jsp").forward(req, res);
	}

	// POST — processes payment, calls InsertMethod, redirects to result
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("user_id") == null) {
			res.sendRedirect(req.getContextPath() + "/U_signIn.html");
			return;
		}

		// Read everything from session
		String user_id    = (String) session.getAttribute("user_id");
		String hotel_id   = (String) session.getAttribute("hotel_id");
		String room_id    = (String) session.getAttribute("room_id");
		String room_type  = (String) session.getAttribute("room_type");
		String check_in   = (String) session.getAttribute("check_in");
		String check_out  = (String) session.getAttribute("check_out");
		String name       = (String) session.getAttribute("name");
		String email      = (String) session.getAttribute("email");

		double room_price       = Double.parseDouble(session.getAttribute("room_price").toString());
		long   numberOfDays     = Long.parseLong(session.getAttribute("number_of_days").toString());
		double totalPrice       = Double.parseDouble(session.getAttribute("total_price").toString());
		double commissionRate   = Double.parseDouble(session.getAttribute("commission_rate").toString());
		double commissionAmount = Double.parseDouble(session.getAttribute("commission_amount").toString());

		BEANS.Bookings book = new BEANS.Bookings();
		book.setUser_id(user_id);
		book.setHotel_id(hotel_id);
		book.setroom_id(room_id);
		book.setRoom_type(room_type);
		book.setCheck_in(check_in);
		book.setCheck_out(check_out);
		book.setName(name);
		book.setEmail(email);
		book.setRoom_price(room_price);
		book.setNumberOfDays(numberOfDays);
		book.setTotalPrice(totalPrice);
		book.setCommissionRate(commissionRate);
		book.setCommissionAmount(commissionAmount);

		boolean success = book.InsertMethod(room_id);

		if (success) {
			int bookingId=book.getBookingId();
			session.setAttribute("bookingId", bookingId);
			res.sendRedirect(req.getContextPath() + "/Payment_Success.jsp");
		} else {
			res.sendRedirect(req.getContextPath() + "/payment?error=payment_failed");
		}
	}
}
