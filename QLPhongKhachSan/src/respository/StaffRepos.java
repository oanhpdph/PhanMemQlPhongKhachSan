/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.staff;
import utilities.jdbcUntil;

/**
 *
 * @author admin
 */
public class StaffRepos {

    public void insert(staff s) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "insert into Staff(code, name, dateOfBirth, sex, address, idPersonCard, phone, [user], pass, [rule])"
                + " values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setDate(3, (Date) s.getDateOfBirth());
            ps.setString(4, s.getSex());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getIdPersonCard());
            ps.setString(7, s.getPhone());
            ps.setString(8, s.getUser());
            ps.setString(9, s.getPassWord());
            ps.setString(10, s.getRule());
            ps.execute();
            System.out.println("Ket Noi Thanh Cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<staff> getAll() {
        ArrayList<staff> list = new ArrayList<>();
        Connection conn = jdbcUntil.getConnection();
        String sql = "select * from Staff";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                Date date = rs.getDate("dateOfBirth");
                String sex = rs.getString("sex");
                String address = rs.getString("address");
                String idPC = rs.getString("idPersonCard");
                String phone = rs.getString("phone");
                String user = rs.getString("user");
                String pass = rs.getString("pass");
                String rule = rs.getString("rule");

                staff s = new staff(id, code, name, date, sex, address, idPC, phone, user, pass, rule);
                list.add(s);
            }
            System.out.println("Ket Noi Thanh Cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
