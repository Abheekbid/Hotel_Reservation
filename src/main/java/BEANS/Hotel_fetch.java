package BEANS;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Hotel_fetch {

    private ArrayList<HashMap<String, String>> hotelList = new ArrayList<>();

    public ArrayList<HashMap<String, String>> fetchAllHotels() {
        try {
            DB db = new DB();
            db.DBconnect();

            String q = "SELECT HOTEL_NAME, STATE, CITY, DESCRIPTION, H_IMAGE_LINK FROM hotels WHERE ROWNUM <= 10";

            ResultSet rs = db.stmt.executeQuery(q);

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("hotel_name",  rs.getString("HOTEL_NAME"));
                row.put("state",       rs.getString("STATE"));
                row.put("city",        rs.getString("CITY"));
                row.put("description", rs.getString("DESCRIPTION"));
                row.put("h_image_link",rs.getString("H_IMAGE_LINK"));
                hotelList.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotelList;
    }

    public ArrayList<HashMap<String, String>> fetchHotelsByLocation(String state, String city) {
        try {
            DB db = new DB();
            db.DBconnect();

            String q = "SELECT HOTEL_NAME, STATE, CITY, DESCRIPTION, H_IMAGE_LINK FROM hotels "
                     + "WHERE LOWER(STATE) LIKE LOWER('%" + state + "%') "
                     + "AND LOWER(CITY) LIKE LOWER('%" + city + "%')";

            ResultSet rs = db.stmt.executeQuery(q);

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                row.put("hotel_name",  rs.getString("HOTEL_NAME"));
                row.put("state",       rs.getString("STATE"));
                row.put("city",        rs.getString("CITY"));
                row.put("description", rs.getString("DESCRIPTION"));
                row.put("h_image_link",rs.getString("H_IMAGE_LINK"));
                hotelList.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotelList;
    }
}
