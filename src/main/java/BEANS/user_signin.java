package BEANS;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class user_signin {

	private String password;
	private String email;

	public user_signin() {}

	public void setEmail(String email)       { this.email = email; }
	public void setPassword(String password) { this.password = password; }

	public HashMap<String, String> checkSignIn() {
		DB db = new DB();
		try {
			db.DBconnect();

			Statement stmt = db.stmt;
			String q1 = "SELECT USER_ID, NAME, EMAIL FROM users WHERE email='" + email + "' and password='" + password + "'";
			ResultSet rs = stmt.executeQuery(q1);

			if (rs.next()) {
				HashMap<String, String> userData = new HashMap<>();
				userData.put("user_id", rs.getString("USER_ID"));
				userData.put("name",    rs.getString("NAME"));
				userData.put("email",   rs.getString("EMAIL"));
				return userData;
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			db.close();
		}
	}
}
