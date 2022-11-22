
package respository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.jdbcUntil;

import viewModel.DSKhachhang;
public class DSKhachhangRepo {
    public void insert(DSKhachhang kh){
        String sql="Insert into client(code,name,dateofbirth,sex,address,idpersoncard,customphone)"
                + " values (?,?,?,?,?,?,?)";
        Connection conn = jdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kh.getCode());
            ps.setString(2, kh.getName());
            ps.setString(3, kh.getDateOfBirth());
            ps.setString(4, kh.getSex());
            ps.setString(5, kh.getAddress());
            ps.setString(6, kh.getIdPersonCard());
            ps.setString(7, kh.getCustomPhone());
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DSKhachhangRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void delete(String id){
        String sql = "delete client where code=?";
        Connection conn = jdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DSKhachhangRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void update(DSKhachhang kh, String id){
        String sql = "Update client set customphone=? where code=?";
        Connection conn = jdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kh.getCustomPhone());
            ps.setString(2, id);
           ;
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DSKhachhangRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<DSKhachhang> Getall(){
        ArrayList<DSKhachhang> list = new ArrayList<>();
        String sql = "Select * from client";
        Connection conn = jdbcUntil.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){
                String ma = rs.getString("code");
                String ten = rs.getString("name");
                String ngaysinh = rs.getString("dateOfBirth");
                String gt = rs.getString("sex");
                String diachi = rs.getString("address");
                String CCCD = rs.getString("idPersonCard");
                String sdt = rs.getString("customPhone");
                DSKhachhang kh = new DSKhachhang(ma, ten,ngaysinh, gt, diachi, CCCD, sdt);
                list.add(kh);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DSKhachhangRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }
}
