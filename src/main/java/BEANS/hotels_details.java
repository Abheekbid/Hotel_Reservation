package BEANS; 
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class hotels_details {
	private String hotel_name;
	private String hotel_id;
	public String getHotel_name() {
		return hotel_name;
	}
	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	
	public HashMap<String, String[]> get_Bookings_Details() {
	    try {
	        DB db = new DB();
	        db.DBconnect();

	        Statement stmt = db.stmt;

	        String sql = "SELECT BOOKING_ID, HOTEL_ID, ROOM_ID, CHECK_IN, CHECK_OUT, NUMBER_OF_DAYS "
	                + "FROM bookings "
	                + "ORDER BY ROOM_ID";

	        ResultSet rs = stmt.executeQuery(sql);

	        HashMap<String, String[]> Hotels = new LinkedHashMap<>();

	        while (rs.next()) {
	            String booking_id   = rs.getString("BOOKING_ID");
	            String hotel_id     = rs.getString("HOTEL_ID");
	            String room_id      = rs.getString("ROOM_ID");
	            Date check_in       = rs.getDate("CHECK_IN");
	            Date check_out      = rs.getDate("CHECK_OUT");
	            long number_of_days = rs.getLong("NUMBER_OF_DAYS");

	            Hotels.put(booking_id, new String[] {
	                hotel_id,
	                room_id,
	                check_in.toString(),
	                check_out.toString(),
	                String.valueOf(number_of_days)
	            });
	        }

	        return Hotels;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}