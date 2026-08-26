package BEANS;

import java.sql.ResultSet;
import java.sql.Statement;

public class h_signin {
	
	private String password;
	private String email;
	
	public h_signin() {
		
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean sign_check() {
		try {
			
			
			DB db = new DB();
			
			db.DBconnect();
			
			Statement stmt = db.stmt;
			
			String s1 = "SELECT * FROM hotels WHERE email='"+email+"' and password='"+password+"'";
			
			ResultSet rs = stmt.executeQuery(s1);
			
			if(rs.next()) {
				return true;
			}
			
			else
				return false;
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
}
