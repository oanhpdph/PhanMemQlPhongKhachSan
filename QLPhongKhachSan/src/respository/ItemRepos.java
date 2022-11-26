/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.*;
import java.util.ArrayList;
import model.item;
import utilities.jdbcUntil;

/**
 *
 * @author admin
 */
public class ItemRepos {
    
    public void insert(item i) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "insert into item(code, name) values(?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, i.getCode());
            ps.setString(2, i.getName());
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void delete(String id) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "delete from item where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void update(item i, String id) {
        Connection conn = jdbcUntil.getConnection();
        String sql = "update item set code=?, name=? where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, i.getCode());
            ps.setString(2, i.getName());
            ps.setString(3, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<item> getAll() {
        ArrayList<item> listService = new ArrayList<>();
        Connection conn = jdbcUntil.getConnection();
        String sql = "select * from item";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                item i = new item(id, code, name);
                listService.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }
    
    public ArrayList<item> getSearch(String ma) {
        ArrayList<item> listService = new ArrayList<>();
        Connection conn = jdbcUntil.getConnection();
        String sql = "select * from item where code=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                item i = new item(id, code, name);
                listService.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }
}
