/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package respository;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Service;
import utilities.JdbcUntil;
import viewModel.PromotionSVM;

/**
 *
 * @author admin
 */
public class ServiceRepos {
    
    public void insert(Service s) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "insert into [service](code,name,price,notes,idpromotion) values(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPrice());
            ps.setString(4, s.getNotes());
            ps.setString(5, s.getIdPromotion());
            ps.execute();
            System.out.println("them thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void delete(String id) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "delete from service where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void update(Service s, String id) {
        Connection conn = JdbcUntil.getConnection();
        String sql = "update service set code=?, name=?, price=?, notes=?, idpromotion=? where id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getCode());
            ps.setString(2, s.getName());
            ps.setString(3, s.getPrice());
            ps.setString(4, s.getNotes());
            ps.setString(5, s.getIdPromotion() );
            ps.setString(6, id);
            ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Service> getAll() {
        ArrayList<Service> listService = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
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
                Service s = new Service(id, code, name, price, notes, idpro);
                listService.add(s);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }
    
    public ArrayList<Service> getSearch(String ma) {
        ArrayList<Service> listService = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
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
                Service s = new Service(id, code, name, price, notes, idpro);
                listService.add(s);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listService;
    }
    
    public ArrayList<PromotionSVM> getAllPS() {
        ArrayList<PromotionSVM> listPS = new ArrayList<>();
        Connection conn = JdbcUntil.getConnection();
        String sql = "select Id, value, dateStart, dateEnd from promotionS";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String id = rs.getString("Id");
                int value = rs.getInt("value");
                Date dS = rs.getDate("dateStart");
                Date dE = rs.getDate("dateEnd");
                
                PromotionSVM m = new PromotionSVM(id, value, dS, dE);
                listPS.add(m);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listPS;
    }
    
}
