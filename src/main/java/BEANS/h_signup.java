package BEANS;

import java.sql.Statement;

public class h_signup {
	private String password;
	private String hotel_name;
	private String state;
	private String city;
	private String phone;
	private String email;
	private String description;
	private String h_image_link;

	public String getHotel_name()                  { return hotel_name; }
	public void   setHotel_name(String hotel_name) { this.hotel_name = hotel_name; }
	public String getPassword()                    { return password; }
	public void   setPassword(String password)     { this.password = password; }
	public String getState()                       { return state; }
	public void   setState(String state)           { this.state = state; }
	public String getCity()                        { return city; }
	public void   setCity(String city)             { this.city = city; }
	public String getPhone()                       { return phone; }
	public void   setPhone(String phone)           { this.phone = phone; }
	public String getEmail()                       { return email; }
	public void   setEmail(String email)           { this.email = email; }
	public String getDescription()                 { return description; }
	public void   setDescription(String description) { this.description = description; }
	public String getH_image_link()                { return h_image_link; }
	public void   setH_image_link(String h_image_link) { this.h_image_link = h_image_link; }

    public boolean InsertMethod() {
		DB db = new DB();
		try {
			db.DBconnect();

			String q1 = "insert into hotels "
			          + "(HOTEL_ID, HOTEL_NAME, PASSWORD, STATE, CITY, PHONE, EMAIL, DESCRIPTION, H_IMAGE_LINK) "
			          + "values "
			          + "(hotels_seq.NEXTVAL, '" + hotel_name + "','" + password + "','" + state + "','"
			          + city + "','" + phone + "','" + email + "','" + description + "','" + h_image_link + "')";

			Statement stmt = db.stmt;
			int x = stmt.executeUpdate(q1);
			return x > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
	}
}
