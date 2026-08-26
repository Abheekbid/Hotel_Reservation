package com.waytogo;

import BEANS.h_signin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Hsignin
 */
@WebServlet("/Hsignin")
public class Hsignin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Hsignin() {
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
		
		String password=req.getParameter("password");
		String email=req.getParameter("email");
		
		h_signin ob = new h_signin();
		
		ob.setEmail(email);
		ob.setPassword(password);
		
		boolean result=ob.sign_check();
		
		PrintWriter pw = res.getWriter();
		
		
		if(result) {
			pw.println("Succ");
		}
		else
			pw.println("Modi ji");
		
		
		
	}

}
