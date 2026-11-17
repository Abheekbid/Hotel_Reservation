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
 * Servlet implementation class Hotels
 */
@WebServlet("/Hotels")
public class Hotels extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Hotels() {
        super();
        // TODO Auto-generated constructor stub
    }
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);

		// Session guard — hotel owner must be logged in
		if (session == null || session.getAttribute("hotel_id") == null) {
			res.sendRedirect(req.getContextPath() + "/H_signin.html");
			return;
		}

		String hotel_name = (String) session.getAttribute("hotel_name");
		String hotel_id   = (String) session.getAttribute("hotel_id");

		hotels_details hotelBean = new hotels_details();
		hotelBean.setHotel_id(hotel_id);

		HashMap<String, String[]> bookings = hotelBean.get_Bookings_Details();
		ArrayList<HashMap<String, String>> roomList = hotelBean.getRooms();

		// Pass everything to the JSP
		req.setAttribute("hotel_name", hotel_name);
		req.setAttribute("hotel_id",   hotel_id);
		req.setAttribute("bookings",   bookings);
		req.setAttribute("roomList",   roomList);

		RequestDispatcher rd = req.getRequestDispatcher("Hotels.jsp");
		rd.forward(req, res);
	}
 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
 
}