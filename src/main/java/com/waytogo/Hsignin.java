package com.waytogo;

import BEANS.h_signin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Servlet implementation class Hsignin
 */
@WebServlet("/Hsignin")
public class Hsignin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Hsignin() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");

		String email    = req.getParameter("email");
		String password = req.getParameter("password");

		h_signin ob = new h_signin();
		ob.setEmail(email);
		ob.setPassword(password);

		HashMap<String, String> hotelData = ob.sign_check();

		PrintWriter pw = res.getWriter();

		if (hotelData != null) {
			HttpSession session = req.getSession();
			session.setAttribute("hotel_id",   hotelData.get("hotel_id"));
			session.setAttribute("hotel_name", hotelData.get("hotel_name"));
			session.setAttribute("email",      hotelData.get("email"));
			session.setAttribute("description",      hotelData.get("description"));
			res.sendRedirect(req.getContextPath() + "/hotel_fetch");
		} else {
			pw.println("Invalid email or password. <a href='H_signin.html'>Try again</a>");
		}
	}
}
