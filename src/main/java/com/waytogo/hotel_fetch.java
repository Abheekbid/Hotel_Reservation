package com.waytogo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import BEANS.Hotel_fetch;

/**
 * Servlet implementation class hotel_fetch
 */
@WebServlet("/hotel_fetch")
public class hotel_fetch extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public hotel_fetch() {
        super();
    }

	// Page load — returns top 10 hotels
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Hotel_fetch hf = new Hotel_fetch();
		ArrayList<HashMap<String, String>> hotelList = hf.fetchAllHotels();

		request.setAttribute("hotelList", hotelList);
		request.getRequestDispatcher("/Hotel_Fetch.jsp").forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String state = request.getParameter("state");
        String city  = request.getParameter("city");

        Hotel_fetch hf = new Hotel_fetch();
        ArrayList<HashMap<String, String>> hotelList;

        if (state != null && !state.trim().isEmpty() && city != null && !city.trim().isEmpty()) { //TO UNDERSTAND WEATHER THE SEARCH BOXES ARE EMPTY OF DETAILS ARE FILLED
            hotelList = hf.fetchHotelsByLocation(state, city);//SHOW THE FIRST TEN HOTELS FROM DB HOTELS
        } else {
            hotelList = hf.fetchAllHotels();//SHOW THE HOTELS WHOSE LOCATION IS SEARCHED
        }

        request.setAttribute("hotelList", hotelList);
        request.getRequestDispatcher("/Hotel_Fetch.jsp").forward(request, response);
    }
}
