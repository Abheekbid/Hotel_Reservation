package BEANS;

import java.sql.ResultSet;
import java.sql.Statement;

public class user_signin {
	
	private String password;
	private String email;
	
	public user_signin() {
		
	}
	
	 public void setEmail(String email) {
		 this.email=email;
	 }
	 public void setPassword(String password) {
		 this.password=password;
	 }
	 
	
	 public boolean checkSignIn()
		{
			try {
				DB db = new DB();
				
				db.DBconnect();
				
				Statement stmt =db.stmt;
				
				String q1 = "SELECT * FROM users WHERE email='"+email+"' and password='"+password+"'";
				
				ResultSet rs = stmt.executeQuery(q1);
				
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
