/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import model.Room;
import java.sql.Connection;
import utilities.JdbcUntil;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;

public class RoomViewrepo {
     public void insert(Room room){
         try{
             Connection conn=JdbcUntil.getConnection();
             String sql="Insert into Room (Status,KindOfRoom,idPromotion,code,roomNumber,area,location,price) values(?,?,?,?,?,?,?,?)";
             PreparedStatement ps=conn.prepareStatement(sql);
             ps.setInt(1,room.getStatus());
             ps.setInt(2,room.getKindOfRoom());
             ps.setString(3,room.getIdPromotion());
             ps.setString(4, room.getCode());
             ps.setString(5, room.getRoomNumber());
             ps.setString(6, room.getArea());
             ps.setString(7, room.getLocation());
             ps.setString(8, room.getPrice());
        
             ps.execute();
         } catch (SQLException ex) {
         ex.printStackTrace();
         }
     }
     public void update(String id,Room room){
        try{
             Connection conn=JdbcUntil.getConnection();
             String sql="Update Room set Status=?,KindOfRoom=?,idPromotion=?,code=?,roomNumber=?,area=?,location=?,price=? where id=?";
             PreparedStatement ps=conn.prepareStatement(sql);
             ps.setInt(1, room.getStatus());
             ps.setInt(2,room.getKindOfRoom());
              ps.setString(3,room.getIdPromotion());
             ps.setString(4, room.getCode());
             ps.setString(5, room.getRoomNumber());
             ps.setString(6, room.getArea());
             ps.setString(7, room.getLocation());
             ps.setString(8, room.getPrice());
             ps.setString(9,id);
             ps.execute();
         } catch (SQLException ex) {
         ex.printStackTrace();
         } 
     }
     public void delete(String id){
         try{
             Connection conn=JdbcUntil.getConnection();
             String sql="Delete from Room where id=?";
             PreparedStatement ps=conn.prepareStatement(sql);
             ps.setString(1,id);
             ps.execute();
         } catch (SQLException ex) {
         ex.printStackTrace();
         } 
     }
     public  ArrayList<Room> getAll(){
         ArrayList<Room> listRoom= new ArrayList<>();
          try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Select room.id,room.status,room.kindOfRoom,room.code,room.roomNumber,room.area,room.location,room.price,promotionR.Id as 'idPro' from room inner join promotionR on room.idPromotion=promotionR.Id ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id=rs.getString("Id");
                int Status = rs.getInt("Status");
                int kor = rs.getInt("KindOfRoom");
                String code =rs.getString("code");
                String rNb=rs.getString("roomNumber");
                String area=rs.getString("area");
                String location=rs.getString("location");
                String price=rs.getString("price");
                String idpro=rs.getString("idPro");
                Room r= new Room(id, Status, kor,idpro, code, rNb, area, location, price);
                listRoom.add(r);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
         return listRoom;
}
}
