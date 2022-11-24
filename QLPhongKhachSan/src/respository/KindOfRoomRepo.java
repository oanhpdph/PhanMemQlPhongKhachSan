/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.kindOfRoom;
import utilities.jdbcUntil;

/**
 *
 * @author TGDD
 */
public class KindOfRoomRepo {
     public  ArrayList<kindOfRoom> getAll(){
         ArrayList<kindOfRoom> listKOR= new ArrayList<>();
          try {
            Connection conn = jdbcUntil.getConnection();
            String sql = "Select * from KindOfRoom ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id=rs.getString("Id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                int amount=rs.getInt("amount");
               
                kindOfRoom kor= new kindOfRoom(id, code, name,amount);
                listKOR.add(kor);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
         return listKOR;
}
}
