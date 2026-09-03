package com.waytogo;

import BEANS.user_signin;
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
 * Servlet implementation class Usignin
 */
@WebServlet("/Usignin")
public class Usignin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Usignin() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html");

		String email    = req.getParameter("email");
		String password = req.getParameter("password");

		user_signin ob = new user_signin();
		ob.setEmail(email);
		ob.setPassword(password);

		HashMap<String, String> userData = ob.checkSignIn();

		PrintWriter pw = res.getWriter();

		if (userData != null) {
	
			HttpSession session = req.getSession();
			session.setAttribute("user_id", userData.get("user_id"));
			session.setAttribute("name",    userData.get("name"));
			session.setAttribute("email",   userData.get("email"));

			res.sendRedirect(req.getContextPath() + "/hotel_fetch");
		} else {
			pw.println("Invalid email or password. <a href='U_signIn.html'>Try again</a>");
		}
	}
}
