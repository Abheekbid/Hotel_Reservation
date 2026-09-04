package BEANS;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class Bookings {
	
	private String user_id;
	private String name;
	private String email;
	private String check_in;
	private String check_out;
	private String room_type;
	private String hotel_id;
	private String room_id;
	private double room_price;
	private long numberOfDays;
	private double totalPrice;
	private double commissionRate;
	private double commissionAmount;
	
	public double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(double commissionRate) {
		this.commissionRate = commissionRate;
	}

	public double getCommissionAmount() {
		return commissionAmount;
	}

	public void setCommissionAmount(double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setNumberOfDays(long numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	
	public void setRoom_price(double room_price) {
		this.room_price = room_price;
	}

	public void setroom_id(String room_id) {
		this.room_id = room_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCheck_in(String check_in) {
		this.check_in = check_in;
	}

	public void setCheck_out(String check_out) {
		this.check_out = check_out;
	}

	public void setRoom_type(String room_type) {
		this.room_type = room_type;
	}

	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}

	// Returns all available rooms as HashMap<room_id, room_number>
	public HashMap<String, String> check_reserv() {
		try {
			Date checkIn  = Date.valueOf(check_in);
			Date checkOut = Date.valueOf(check_out);
			DB db = new DB();
			db.DBconnect();

			Statement stmt = db.stmt;

			String s1 = "SELECT room_id, room_number FROM rooms "
			          + "WHERE room_type = '" + room_type + "' "
			          + "AND hotel_id = '" + hotel_id + "' "
			          + "AND NOT EXISTS ("
			          + "    SELECT 1 FROM bookings "
			          + "    WHERE bookings.room_id = rooms.room_id "
			          + "    AND bookings.check_out >= '" + checkIn + "' "
			          + "    AND bookings.check_in  <= '" + checkOut + "'"
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
		}
	}

	public boolean InsertMethod(String room_id) {
		try {
			Date checkIn  = Date.valueOf(check_in);
			Date checkOut = Date.valueOf(check_out);
			DB db = new DB();
			db.DBconnect();

			Statement stmt = db.stmt;

			String q1 = "INSERT INTO bookings "
			          + "(BOOKING_ID, USER_ID, ROOM_ID, CHECK_IN, CHECK_OUT, NUMBER_OF_DAYS, "
			          + "ROOM_PRICE, TOTAL_PRICE, COMMISSION_RATE, COMMISSION_AMOUNT, "
			          + "BOOKING_STATUS, BOOKED_AT, NAME, EMAIL, HOTEL_ID) "
			          + "VALUES ("
			          + "bookings_seq.NEXTVAL, "
			          + "'" + user_id          + "', "
			          + "'" + room_id          + "', "
			          + "'" + checkIn          + "', "
			          + "'" + checkOut         + "', "
			          + numberOfDays           + ", "
			          + "'" + room_price       + "', "
			          + "'" + totalPrice       + "', "
			          + "'" + commissionRate   + "', "
			          + "'" + commissionAmount + "', "
			          + "'pending', "
			          + "SYSDATE, "
			          + "'" + name             + "', "
			          + "'" + email            + "', "
			          + "'" + hotel_id         + "')";

			int x = stmt.executeUpdate(q1);
			return x > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public double getRoomPrice() {
		double price = 0.0;
		try {
			DB db = new DB();
			db.DBconnect();

			Statement stmt = db.stmt;
			String sql = "SELECT price_per_night FROM rooms WHERE room_id = '" + room_id + "'";

			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				price = rs.getDouble("price_per_night");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}
}
