package com.waytogo;
import BEANS.user_signup;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Usignup
 */
@WebServlet("/Usignup")
public class Usignup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Usignup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		res.setContentType("text/html");
		
		PrintWriter pw = res.getWriter();
		
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String contact = req.getParameter("contact");
		String address = req.getParameter("address");
		user_signup ob=new user_signup();
		ob.setName(name);
		ob.setEmail(email);
		ob.setPassword(password);
		ob.setPhone(contact);
		ob.setAddress(address);
		boolean result=ob.InsertMethod();
		if(result==true)
		{
			res.sendRedirect(req.getContextPath() + "/U_signIn.html");
		}
		else
		{
			pw.println("Modiji");
		}
		}
	}
