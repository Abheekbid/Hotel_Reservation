package BEANS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class Bookings {
	
	private int    bookingId;
	private String user_id;
	private String name;
	private String email;
	private String check_in;
	private String check_out;
	private String room_type;
	private String hotel_id;
	private String room_id;
	private String hotel_name;
	private double room_price;
	private long   numberOfDays;
	private double totalPrice;
	private double commissionRate;
	private double commissionAmount;

	public int    getBookingId()                           { return bookingId; }
	public String getHotel_name()                          { return hotel_name; }
	public void   setHotel_name(String hotel_name)         { this.hotel_name = hotel_name; }
	public double getCommissionRate()                      { return commissionRate; }
	public void   setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }
	public double getCommissionAmount()                    { return commissionAmount; }
	public void   setCommissionAmount(double v)            { this.commissionAmount = v; }
	public void   setTotalPrice(double totalPrice)         { this.totalPrice = totalPrice; }
	public void   setNumberOfDays(long numberOfDays)       { this.numberOfDays = numberOfDays; }
	public void   setRoom_price(double room_price)         { this.room_price = room_price; }
	public void   setroom_id(String room_id)               { this.room_id = room_id; }
	public void   setUser_id(String user_id)               { this.user_id = user_id; }
	public void   setName(String name)                     { this.name = name; }
	public void   setEmail(String email)                   { this.email = email; }
	public void   setCheck_in(String check_in)             { this.check_in = check_in; }
	public void   setCheck_out(String check_out)           { this.check_out = check_out; }
	public void   setRoom_type(String room_type)           { this.room_type = room_type; }
	public void   setHotel_id(String hotel_id)             { this.hotel_id = hotel_id; }

	// ── Availability check ────────────────────────────────────────────────────
	public HashMap<String, String> check_reserv() {
		DB db = new DB();
		try {
			db.DBconnect();

			Statement stmt = db.stmt;
			String s1 = "SELECT room_id, room_number FROM rooms "
			          + "WHERE room_type = '" + room_type + "' "
			          + "AND hotel_id = '" + hotel_id + "' "
			          + "AND NOT EXISTS ("
			          + "    SELECT 1 FROM bookings "
			          + "    WHERE bookings.room_id = rooms.room_id "
			          + "    AND bookings.check_out >= TO_DATE('" + check_in  + "', 'YYYY-MM-DD') "
			          + "    AND bookings.check_in  <= TO_DATE('" + check_out + "', 'YYYY-MM-DD')"
			          + ") ORDER BY room_id ASC";

			ResultSet rs = stmt.executeQuery(s1);
			HashMap<String, String> availableRoomIds = new HashMap<>();
			while (rs.next()) {
			    availableRoomIds.put(rs.getString("room_id"), rs.getString("room_number"));
			}
			return availableRoomIds;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			db.close();
		}
	}

	// ── Insert booking ────────────────────────────────────────────────────────
	public boolean InsertMethod(String room_id) {
		DB db = new DB();
		try {
			db.DBconnect();

			String q1 = "INSERT INTO bookings "
			          + "(BOOKING_ID, USER_ID, ROOM_ID, CHECK_IN, CHECK_OUT, NUMBER_OF_DAYS, "
			          + "ROOM_PRICE, TOTAL_PRICE, COMMISSION_RATE, COMMISSION_AMOUNT, "
			          + "BOOKING_STATUS, BOOKED_AT, NAME, EMAIL, HOTEL_ID, HOTEL_NAME) "
			          + "VALUES ("
			          + "bookings_seq.NEXTVAL, ?, ?, "
			          + "TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), "
			          + "?, ?, ?, ?, ?, 'PENDING', SYSDATE, ?, ?, ?, ?)";

			PreparedStatement ps = db.con.prepareStatement(q1);
			ps.setString(1,  user_id);
			ps.setString(2,  room_id);
			ps.setString(3,  check_in);
			ps.setString(4,  check_out);
			ps.setLong(5,    numberOfDays);
			ps.setDouble(6,  room_price);
			ps.setDouble(7,  totalPrice);
			ps.setDouble(8,  commissionRate);
			ps.setDouble(9,  commissionAmount);
			ps.setString(10, name);
			ps.setString(11, email);
			ps.setString(12, hotel_id);
			ps.setString(13, hotel_name);

			int x = ps.executeUpdate();
			if (x > 0) {
				Statement stmt = db.con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT BOOKINGS_SEQ.CURRVAL FROM dual");
				if (rs.next()) {
					bookingId = rs.getInt(1);
				}
			}
			return x > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}
	}

	// ── Get room price ────────────────────────────────────────────────────────
	public double getRoomPrice() {
		DB db = new DB();
		double price = 0.0;
		try {
			db.DBconnect();

			Statement stmt = db.stmt;
			String sql = "SELECT price_per_night FROM rooms WHERE room_id = '" + room_id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				price = rs.getDouble("price_per_night");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return price;
	}

	// ── Get hotel name ────────────────────────────────────────────────────────
	public String getHotelName(String hotel_id) {
		DB db = new DB();
		String hotelName = "";
		try {
			db.DBconnect();

			PreparedStatement ps = db.con.prepareStatement(
			    "SELECT HOTEL_NAME FROM HOTELS WHERE HOTEL_ID = ?");
			ps.setString(1, hotel_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				hotelName = rs.getString("HOTEL_NAME");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return hotelName;
	}
}
