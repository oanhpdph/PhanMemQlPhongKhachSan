/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Item;
import utilities.JdbcUntil;

/**
 *
 * @author admin
 */
public class ItemRepos {
    
    public void insert(Item i) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "insert into Item(code, name) values(?,?)";
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
        Connection conn = JdbcUntil.getConnection();
        String sql = "delete from Item where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void update(Item i, String id) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "update Item set code=?, name=? where id=?";
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
    
    public List<Item> getAll() {
        List<Item> listService = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
        String sql = "select * from Item";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                Item i = new Item(id, code, name);
                listService.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }
    
    public ArrayList<Item> getSearch(String ma) {
        ArrayList<Item> listService = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
        String sql = "select * from Item where code=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                Item i = new Item(id, code, name);
                listService.add(i);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }
}
