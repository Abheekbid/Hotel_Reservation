package BEANS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	Connection con;
	Statement stmt;

	public DB() {}

	public boolean DBconnect() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		    con = DriverManager.getConnection(
		        "jdbc:oracle:thin:@localhost:1521:XE",
		        "system",
		        "manager"
		    );

		    stmt = con.createStatement();
		    return true;

		} catch (Exception e) {
		    e.printStackTrace();
			return false;
		}
	}

	public void close() {
		try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
		try { if (con  != null) con.close();  } catch (SQLException ignored) {}
	}
}
