package BEANS;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class bookings {

	private String user_id;
	private String name;
    private String email;
    private Date check_in;
    private Date check_out;
    private String room_type;
    private String hotel_id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCheck_in() {
		return check_in;
	}
	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}
	public Date getCheck_out() {
		return check_out;
	}
	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}
	public String getRoom_type() {
		return room_type;
	}
	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}
	public ArrayList<String> GetMethod()
	{
		try {
			DB db = new DB();
			
			db.DBconnect();
			
			Statement stmt = db.stmt;
			
		
	}
	 public boolean InsertMethod()
		{
			try {
				DB db = new DB();
				
				db.DBconnect();
				
				Statement stmt=db.stmt;

				String q1
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
    
}
