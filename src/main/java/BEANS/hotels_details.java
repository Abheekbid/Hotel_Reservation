package BEANS;

import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class hotels_details {

	private String hotel_name;
	private String hotel_id;

	public String getHotel_name()                  { return hotel_name; }
	public void   setHotel_name(String hotel_name) { this.hotel_name = hotel_name; }
	public String getHotel_id()                    { return hotel_id; }
	public void   setHotel_id(String hotel_id)     { this.hotel_id = hotel_id; }

	// ── Bookings ──────────────────────────────────────────────────────────────
	public HashMap<String, String[]> get_Bookings_Details() {
	    DB db = new DB();
	    try {
	        db.DBconnect();

	        PreparedStatement stmt = db.con.prepareStatement(
	            "SELECT BOOKING_ID, HOTEL_ID, ROOM_ID, CHECK_IN, CHECK_OUT, NUMBER_OF_DAYS " +
	            "FROM bookings WHERE HOTEL_ID = ? ORDER BY ROOM_ID"
	        );
	        stmt.setString(1, hotel_id);
	        ResultSet rs = stmt.executeQuery();

	        HashMap<String, String[]> hotels = new LinkedHashMap<>();
	        while (rs.next()) {
	            String booking_id   = rs.getString("BOOKING_ID");
	            String hotel_id_col = rs.getString("HOTEL_ID");
	            String room_id      = rs.getString("ROOM_ID");
	            Date   check_in     = rs.getDate("CHECK_IN");
	            Date   check_out    = rs.getDate("CHECK_OUT");
	            long   num_days     = rs.getLong("NUMBER_OF_DAYS");

	            hotels.put(booking_id, new String[] {
	                hotel_id_col,
	                room_id,
	                check_in.toString(),
	                check_out.toString(),
	                String.valueOf(num_days)
	            });
	        }
	        return hotels;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        db.close();
	    }
	}

	// ── Rooms ─────────────────────────────────────────────────────────────────
	public ArrayList<HashMap<String, String>> getRooms() {
	    ArrayList<HashMap<String, String>> list = new ArrayList<>();
	    DB db = new DB();
	    try {
	        db.DBconnect();

	        PreparedStatement ps = db.con.prepareStatement(
	            "SELECT ROOM_ID, ROOM_NUMBER, ROOM_TYPE, PRICE_PER_NIGHT " +
	            "FROM ROOMS WHERE HOTEL_ID = ? ORDER BY ROOM_ID ASC"
	        );
	        ps.setString(1, hotel_id);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            HashMap<String, String> row = new HashMap<>();
	            row.put("room_id",         rs.getString("ROOM_ID"));
	            row.put("room_number",     rs.getString("ROOM_NUMBER"));
	            row.put("room_type",       rs.getString("ROOM_TYPE"));
	            row.put("price_per_night", rs.getString("PRICE_PER_NIGHT"));
	            list.add(row);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        db.close();
	    }
	    return list;
	}

	public boolean addRoom(String roomNumber, String roomType, double pricePerNight) {
	    DB db = new DB();
	    try {
	        db.DBconnect();

	        PreparedStatement ps = db.con.prepareStatement(
	            "INSERT INTO ROOMS (ROOM_ID, HOTEL_ID, ROOM_NUMBER, ROOM_TYPE, PRICE_PER_NIGHT) " +
	            "VALUES (ROOMS_SEQ.NEXTVAL, ?, ?, ?, ?)"
	        );
	        ps.setString(1, hotel_id);
	        ps.setString(2, roomNumber);
	        ps.setString(3, roomType);
	        ps.setDouble(4, pricePerNight);

	        int rows = ps.executeUpdate();
	        return rows > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        db.close();
	    }
	}

	public boolean deleteRoom(String roomId) {
	    DB db = new DB();
	    try {
	        db.DBconnect();

	        PreparedStatement ps = db.con.prepareStatement(
	            "DELETE FROM ROOMS WHERE ROOM_ID = ? AND HOTEL_ID = ?"
	        );
	        ps.setString(1, roomId);
	        ps.setString(2, hotel_id);

	        int rows = ps.executeUpdate();
	        return rows > 0;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        db.close();
	    }
	}
}
