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
import model.PromotionR;
import utilities.jdbcUntil;

/**
 *
 * @author TGDD
 */
public class PromotionRepo {
       public  ArrayList<PromotionR> getAll(){
         ArrayList<PromotionR> listSTT= new ArrayList<>();
          try {
            Connection conn = jdbcUntil.getConnection();
            String sql = "Select * from promotionR ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id=rs.getString("Id");
                String vl = rs.getString("value");
                String ds = rs.getString("dateStart");
                String de=rs.getString("dateEnd");
               
               PromotionR pro= new PromotionR(id, vl, ds, de);
                listSTT.add(pro);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
         return listSTT;
}
}
