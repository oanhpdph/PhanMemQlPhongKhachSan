/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import DomainModel.item;
import ViewModel.Dung_ItemViewModel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import JDBC.jdbcUntil;
import ViewModel.Dung_Room_ItemViewModel;
import java.math.BigDecimal;

public class DungItemRepository {

    public ArrayList<Dung_ItemViewModel> getList() {
        ArrayList<Dung_ItemViewModel> list = new ArrayList<>();
        try {
            Connection conn = jdbcUntil.getConnection();
//            String sql="select item.code,item.name,status,amount,Room.code,roomNumber,area,location,price from item join "
//                    + "roomItem on item.id=roomitem.itemId join Room on roomItem.roomId=Room.id ";
            String sql = "select code,name from item ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Dung_ItemViewModel i = new Dung_ItemViewModel();
                String maTB = rs.getString(1);
                String tenTB = rs.getString(2);

                i.setCodeItem(maTB);
                i.setName(tenTB);

                list.add(i);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Dung_ItemViewModel i) {

        try {
            Connection conn = jdbcUntil.getConnection();
            String sqlInsert = "insert into item (code,name )values (?, ? )";
            PreparedStatement ps = conn.prepareStatement(sqlInsert);
            ps.setString(1, i.getCodeItem());
            ps.setString(2, i.getName());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean delete(String ma) {
        int check = 0;
        try {
            Connection conn = jdbcUntil.getConnection();
            String sqlDelete = "delete from item where code=? ";
            PreparedStatement ps = conn.prepareStatement(sqlDelete);
            ps.setString(1, ma);
            check = ps.executeUpdate();
            return check > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean update(String ma, Dung_ItemViewModel i) {
        int check = 0;
        try {
            Connection conn = jdbcUntil.getConnection();
            String sqlUpdate = "Update item set name=? where code=? ";
            PreparedStatement ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, i.getName());
            ps.setString(2, ma);
            check = ps.executeUpdate();
            return check > 0;
            //   System.out.println("truy van thanh cong");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Dung_ItemViewModel> search(String ma) {
        ArrayList<Dung_ItemViewModel> list = new ArrayList<>();
        try {
            Connection conn = jdbcUntil.getConnection();
            String sql = "select code,name from item where code=? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ma);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                String code = rs.getString("code");
                String ten = rs.getString("name");
               

                Dung_ItemViewModel item = new Dung_ItemViewModel(code,ten);
                list.add(item);
            }
            System.out.println("ket noi item Thanh cong");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
