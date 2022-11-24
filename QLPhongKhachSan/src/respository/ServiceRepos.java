/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.service;
import utilities.jdbcUntil;

/**
 *
 * @author admin
 */
public class ServiceRepos {

    public void insert(service s) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "insert into [service](code,name,price,notes) values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPrice());
            ps.setString(4, s.getNotes());
            ps.execute();
            System.out.println("them thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(String id) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "delete from service where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(service s, String id) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "update service set code=?, name=?, price=?, notes=? where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPrice());
            ps.setString(4, s.getNotes());
            ps.setString(5, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<service> getAll() {
        ArrayList<service> listService = new ArrayList<>();
        Connection conn = jdbcUntil.getConnection();
        String sql = "select * from service";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String price = rs.getString("price");
                String notes = rs.getString("notes");
                String idpro = rs.getString("idpromotion");
                service s = new service(id, code, name, price, notes, idpro);
                listService.add(s);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }

    public ArrayList<service> getSearch(String ma) {
        ArrayList<service> listService = new ArrayList<>();
        Connection conn = jdbcUntil.getConnection();
        String sql = "select * from service where code = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String price = rs.getString("price");
                String notes = rs.getString("notes");
                String idpro = rs.getString("idpromotion");
                service s = new service(id, code, name, price, notes, idpro);
                listService.add(s);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }

}
