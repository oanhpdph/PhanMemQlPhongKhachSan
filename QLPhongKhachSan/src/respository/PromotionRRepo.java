package respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.PromotionR;
import utilities.JdbcUntil;

public class PromotionRRepo {

    Connection conn = JdbcUntil.getConnection();

    public PromotionR searchPromotionR(String id, String dateEnd) {
        String sql = "select * from promotionR where id=? and dateEnd>=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, dateEnd);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                PromotionR pr = new PromotionR();
                pr.setId(rs.getString("id"));
                pr.setCode(rs.getString("code"));
                pr.setValue(rs.getString("value"));
                pr.setDateEnd(String.valueOf(rs.getDate("dateend")));
                pr.setDateStart(String.valueOf(rs.getString("dateStart")));
                return pr;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PromotionRRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<PromotionR> getAll() {
        ArrayList<PromotionR> listSTT = new ArrayList<>();
        try {
            Connection conn = JdbcUntil.getConnection();
            String sql = "Select * from promotionR ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("Id");
                String code = rs.getString("code");
                String vl = rs.getString("value");
                String ds = rs.getString("dateStart");
                String de = rs.getString("dateEnd");

                PromotionR pro = new PromotionR(id, code, vl, ds, de);
                listSTT.add(pro);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listSTT;
    }
}
