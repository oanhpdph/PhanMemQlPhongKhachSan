/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import model.room;
import java.sql.Connection;
import utilities.jdbcUntil;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;

public class RoomViewRepo {
     public void insert(room room){
         try{
             Connection conn=jdbcUntil.getConnection();
             String sql="Insert into Room (idStatus,idKindOfRoom,code,roomNumber,area,location,price,idPromotion) values(?,?,?,?,?,?,?,?)";
             PreparedStatement ps=conn.prepareStatement(sql);
             ps.setString(1, room.getIdStatus());
             ps.setString(2,room.getIdKindOfRoom());
             ps.setString(3, room.getCode());
             ps.setString(4, room.getRoomNumber());
             ps.setString(5, room.getArea());
             ps.setString(6, room.getLocation());
             ps.setString(7, room.getPrice());
             ps.setString(8,room.getIdPromotion());
             ps.execute();
         } catch (SQLException ex) {
         ex.printStackTrace();
         }
     }
     public void update(String id,room room){
        try{
             Connection conn=jdbcUntil.getConnection();
             String sql="Update Room set idStatus=?,idKindOfRoom=?,code=?,roomNumber=?,area=?,location=?,price=?,idPromotion=? where id=?";
             PreparedStatement ps=conn.prepareStatement(sql);
             ps.setString(1, room.getIdStatus());
             ps.setString(2,room.getIdKindOfRoom());
             ps.setString(3, room.getCode());
             ps.setString(4, room.getRoomNumber());
             ps.setString(5, room.getArea());
             ps.setString(6, room.getLocation());
             ps.setString(7, room.getPrice());
             ps.setString(8,room.getIdPromotion());
             ps.setString(9,id);
             ps.execute();
         } catch (SQLException ex) {
         ex.printStackTrace();
         } 
     }
     public void delete(String id){
         try{
             Connection conn=jdbcUntil.getConnection();
             String sql="Delete from Room where id=?";
             PreparedStatement ps=conn.prepareStatement(sql);
             ps.setString(1,id);
             ps.execute();
         } catch (SQLException ex) {
         ex.printStackTrace();
         } 
     }
     public  ArrayList<room> getAll(){
         ArrayList<room> listRoom= new ArrayList<>();
          try {
            Connection conn = jdbcUntil.getConnection();
            String sql = "Select room.id,status.name as 'Status',kindOfRoom.name as 'KingOfRoom',room.code,room.roomNumber,room.area,room.location,room.price,room.idPromotion from room inner join status on room.idStatus=status.id inner join kindOfRoom on room.IdKindOfRoom=kindOfRoom.Id inner join promotionR on room.idPromotion=promotionR.Id ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id=rs.getString("Id");
                String idStatus = rs.getString("Status");
                String idKor = rs.getString("KingOfRoom");
                String code =rs.getString("code");
                String rNb=rs.getString("roomNumber");
                String area=rs.getString("area");
                String location=rs.getString("location");
                String price=rs.getString("price");
                String idPro=rs.getString("idPromotion");
                room r= new room(id, idStatus, idKor, code, rNb, area, location, price,idPro);
                listRoom.add(r);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
         return listRoom;
}
}
