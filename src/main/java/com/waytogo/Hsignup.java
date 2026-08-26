package com.waytogo;
import BEANS.h_signup;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class Hsignup
 */
@WebServlet("/Hsignup")
public class Hsignup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Hsignup() {
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
		
		String hotel_name = req.getParameter("hotel_name");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String contact = req.getParameter("contact");
		String state = req.getParameter("state");
		String city = req.getParameter("city");
		String description = req.getParameter("description");
		String image_link = req.getParameter("h_image_link");
		h_signup ob=new h_signup();
		ob.setHotel_name(hotel_name);
		ob.setEmail(email);
		ob.setPassword(password);
		ob.setPhone(contact);
		ob.setState(state);
		ob.setCity(city);
		ob.setDescription(description);
		ob.setH_image_link(image_link);
		boolean result=ob.InsertMethod();
		if(result==true) {
			
			res.sendRedirect(req.getContextPath() + "/H_signin.html");
		}
		else
		{
			pw.println("Unsuccess");
		}
		}

	}

