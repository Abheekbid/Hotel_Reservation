package com.waytogo;

import BEANS.user_signin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Usignin
 */
@WebServlet("/Usignin")
public class Usignin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Usignin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		res.setContentType("text/html");
		
		PrintWriter pw = res.getWriter();
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		user_signin ob = new user_signin();
		
		ob.setEmail(email);
		ob.setPassword(password);
		
		boolean result = ob.checkSignIn();
		
		if(result) {
			pw.println("Success babaji");
		}
		else
			pw.println("Modi ji");
	}

}
