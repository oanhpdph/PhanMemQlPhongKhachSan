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
import model.status;
import utilities.jdbcUntil;

/**
 *
 * @author TGDD
 */
public class StatusRepo {
    
      public  ArrayList<status> getAll(){
         ArrayList<status> listSTT= new ArrayList<>();
          try {
            Connection conn = jdbcUntil.getConnection();
            String sql = "Select * from Status ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id=rs.getString("Id");
                String code = rs.getString("code");
                String name = rs.getString("name");
               
                status stt= new status(id, code, name);
                listSTT.add(stt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
         return listSTT;
}
}
