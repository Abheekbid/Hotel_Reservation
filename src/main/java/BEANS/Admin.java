package BEANS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Admin {

    // ── Fields (mirrors ADMIN table) ─────────────────────────────────────────
    private String email;
    private String password;
    private String name;

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public String getEmail()              { return email; }
    public void   setEmail(String email)  { this.email = email; }

    public String getPassword()                 { return password; }
    public void   setPassword(String password)  { this.password = password; }

    public String getName()             { return name; }
    public void   setName(String name)  { this.name = name; }

    // =========================================================================
    // AUTH
    // =========================================================================

    /**
     * Verifies admin credentials against the ADMIN table.
     *
     * @return HashMap with {email, name} on success, null on failure.
     */
    public HashMap<String, String> signIn() {
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "SELECT EMAIL, NAME FROM ADMIN WHERE EMAIL = ? AND PASSWORD = ?"
            );
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HashMap<String, String> result = new HashMap<>();
                result.put("email", rs.getString("EMAIL"));
                result.put("name",  rs.getString("NAME"));
                return result;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================================
    // HOTELS
    // =========================================================================

    /**
     * Returns all hotels from the HOTELS table.
     * Each row: hotel_id, hotel_name, email, phone, state, city, description, h_image_link.
     */
    public ArrayList<HashMap<String, String>> getAllHotels() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "SELECT HOTEL_ID, HOTEL_NAME, EMAIL, PHONE, STATE, CITY, " +
                "DESCRIPTION, H_IMAGE_LINK FROM HOTELS ORDER BY HOTEL_ID ASC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("hotel_id",    rs.getString("HOTEL_ID"));
                row.put("hotel_name",  rs.getString("HOTEL_NAME"));
                row.put("email",       rs.getString("EMAIL"));
                row.put("phone",       rs.getString("PHONE"));
                row.put("state",       rs.getString("STATE"));
                row.put("city",        rs.getString("CITY"));
                row.put("description", rs.getString("DESCRIPTION"));
                row.put("h_image_link",rs.getString("H_IMAGE_LINK"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Deletes a hotel by hotel_id.
     * Also deletes associated rooms (cascade not assumed at DB level).
     *
     * @return true if at least one hotel row was removed.
     */
    public boolean deleteHotel(String hotelId) {
        try {
            DB db = new DB();
            db.DBconnect();

            // Remove rooms that belong to this hotel first
            PreparedStatement psRooms = db.con.prepareStatement(
                "DELETE FROM ROOMS WHERE HOTEL_ID = ?"
            );
            psRooms.setString(1, hotelId);
            psRooms.executeUpdate();

            // Remove the hotel
            PreparedStatement psHotel = db.con.prepareStatement(
                "DELETE FROM HOTELS WHERE HOTEL_ID = ?"
            );
            psHotel.setString(1, hotelId);
            int rows = psHotel.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // USERS
    // =========================================================================

    /**
     * Returns all guest users from the USERS table.
     * Each row: user_id, name, email, phone, address, user_type, created_at.
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "SELECT USER_ID, NAME, EMAIL, PHONE, ADDRESS, USER_TYPE, " +
                "TO_CHAR(CREATED_AT, 'YYYY-MM-DD') AS CREATED_AT " +
                "FROM USERS ORDER BY USER_ID ASC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("user_id",    rs.getString("USER_ID"));
                row.put("name",       rs.getString("NAME"));
                row.put("email",      rs.getString("EMAIL"));
                row.put("phone",      rs.getString("PHONE"));
                row.put("address",    rs.getString("ADDRESS"));
                row.put("user_type",  rs.getString("USER_TYPE"));
                row.put("created_at", rs.getString("CREATED_AT"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Deletes a user by user_id.
     *
     * @return true if at least one user row was removed.
     */
    public boolean deleteUser(String userId) {
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "DELETE FROM USERS WHERE USER_ID = ?"
            );
            ps.setString(1, userId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // BOOKINGS
    // =========================================================================

    /**
     * Returns all bookings from the BOOKINGS table, most recent first.
     * Each row includes commission fields so the admin can see earnings.
     */
    public ArrayList<HashMap<String, String>> getAllBookings() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "SELECT BOOKING_ID, USER_ID, ROOM_ID, HOTEL_ID, NAME, EMAIL, " +
                "TO_CHAR(CHECK_IN,  'YYYY-MM-DD') AS CHECK_IN,  " +
                "TO_CHAR(CHECK_OUT, 'YYYY-MM-DD') AS CHECK_OUT, " +
                "NUMBER_OF_DAYS, ROOM_PRICE, TOTAL_PRICE, " +
                "COMMISSION_RATE, COMMISSION_AMOUNT, BOOKING_STATUS, " +
                "TO_CHAR(BOOKED_AT, 'YYYY-MM-DD HH24:MI') AS BOOKED_AT " +
                "FROM BOOKINGS ORDER BY BOOKING_ID DESC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("booking_id",        rs.getString("BOOKING_ID"));
                row.put("user_id",           rs.getString("USER_ID"));
                row.put("room_id",           rs.getString("ROOM_ID"));
                row.put("hotel_id",          rs.getString("HOTEL_ID"));
                row.put("hotel_name",        rs.getString("HOTEL_NAME"));
                row.put("name",              rs.getString("NAME"));
                row.put("email",             rs.getString("EMAIL"));
                row.put("check_in",          rs.getString("CHECK_IN"));
                row.put("check_out",         rs.getString("CHECK_OUT"));
                row.put("number_of_days",    rs.getString("NUMBER_OF_DAYS"));
                row.put("room_price",        rs.getString("ROOM_PRICE"));
                row.put("total_price",       rs.getString("TOTAL_PRICE"));
                row.put("commission_rate",   rs.getString("COMMISSION_RATE"));
                row.put("commission_amount", rs.getString("COMMISSION_AMOUNT"));
                row.put("booking_status",    rs.getString("BOOKING_STATUS"));
                row.put("booked_at",         rs.getString("BOOKED_AT"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Deletes a booking by booking_id.
     *
     * @return true if at least one booking row was removed.
     */
    public boolean deleteBooking(String bookingId) {
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "DELETE FROM BOOKINGS WHERE BOOKING_ID = ?"
            );
            ps.setString(1, bookingId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // COMMISSIONS (summary for the admin earnings view)
    // =========================================================================

    /**
     * Returns total commission earned, grouped by hotel.
     * Each row: hotel_id, hotel_name, total_commission, booking_count.
     */
    public ArrayList<HashMap<String, String>> getCommissionSummary() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            DB db = new DB();
            db.DBconnect();

            PreparedStatement ps = db.con.prepareStatement(
                "SELECT HOTEL_ID, HOTEL_NAME, " +
                "SUM(COMMISSION_AMOUNT) AS TOTAL_COMMISSION, " +
                "COUNT(*) AS BOOKING_COUNT " +
                "FROM BOOKINGS " +
                "GROUP BY HOTEL_ID, HOTEL_NAME " +
                "ORDER BY TOTAL_COMMISSION DESC"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("hotel_id",         rs.getString("HOTEL_ID"));
                row.put("hotel_name",       rs.getString("HOTEL_NAME"));
                row.put("total_commission", rs.getString("TOTAL_COMMISSION"));
                row.put("booking_count",    rs.getString("BOOKING_COUNT"));
                list.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
