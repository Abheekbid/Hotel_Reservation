package BEANS;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

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

	// Returns hotel data as a HashMap on success, null on failure
	public HashMap<String, String> sign_check() {
		try {
			DB db = new DB();
			db.DBconnect();

			Statement stmt = db.stmt;

			String s1 = "SELECT HOTEL_ID, HOTEL_NAME, EMAIL FROM hotels WHERE email='" + email + "' and password='" + password + "'";

			ResultSet rs = stmt.executeQuery(s1);

			if (rs.next()) {
				HashMap<String, String> hotelData = new HashMap<>();
				hotelData.put("hotel_id",   rs.getString("HOTEL_ID"));
				hotelData.put("hotel_name", rs.getString("HOTEL_NAME"));
				hotelData.put("email",      rs.getString("EMAIL"));
				return hotelData;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
