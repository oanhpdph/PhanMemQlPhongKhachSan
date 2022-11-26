package respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bill;
import model.Room;
import utilities.JdbcUntil;

public class RoomRepo {

    Connection conn = JdbcUntil.getConnection();

    public List<Room> getRoomByNumber(String number) {
        String sql = "select * from Room where roomNUmber = ?";
        List<Room> list = new ArrayList<>();
        try {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, number);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getString("id"));
                room.setStatus(String.valueOf(rs.getInt("Status")));
                room.setKindOfRoom(String.valueOf(rs.getString("KindOfRoom")));
                room.setIdPromotion(rs.getString("idpromotion"));
                room.setCode(rs.getString("Code"));
                room.setArea(rs.getString("area"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setLocation(rs.getString("Location"));
                room.setPrice(rs.getString("Price"));
                list.add(room);
            }

        } catch (SQLException ex) {
            Logger.getLogger(RoomRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
