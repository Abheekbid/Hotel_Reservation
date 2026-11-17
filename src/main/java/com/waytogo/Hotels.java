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
		HttpSession session = req.getSession();

		String hotel_name=(String)session.getAttribute("hotel_name");
		String hotel_id=(String)session.getAttribute("hotel_id");
		String description=(String)session.getAttribute("description");
		
		hotels_details hotel = new hotels_details();
		
		HashMap<String, String[]> Hotels = new HashMap<>();
		
		Hotels=hotel.get_Bookings_Details();
 
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