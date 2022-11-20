package respository;

import java.sql.Connection;
import java.sql.Date;
import model.Bill;
import utilities.JdbcUntil;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillRepo {

    Connection conn = JdbcUntil.getConnection();

    public void insert(Bill bill) {
        String sql = "insert into bill(idClient,idStaff,code,Price,[status],[date]) values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bill.getIdClient());
            ps.setString(2, bill.getIdStaff());
            ps.setString(3, bill.getCode());
            ps.setString(4, bill.getPrice());
            ps.setString(5, bill.getStatus());
            ps.setDate(6, Date.valueOf(bill.getDate()));
            ps.execute();
        } catch (SQLException ex) {
            System.out.println("sai insert");
        }
    }
}
