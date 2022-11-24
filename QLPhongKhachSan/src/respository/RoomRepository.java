///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package Repository;
//
//import JDBC.jdbcUntil;
//import ViewModel.Item_RoomViewModel;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//
///**
// *
// * @author Admin
// */
//public class RoomRepository {
//        public ArrayList<Item_RoomViewModel> getList() {
//        ArrayList<Item_RoomViewModel> list1 = new ArrayList<>();
//        try {
//            Connection conn = jdbcUntil.getConnection();
//
//            String sql = "select Room.code,roomNumber,item.name,roomItem.status,roomItem.amount from item join roomItem on item.id=roomitem.itemId join Room on roomItem.roomId=Room.id ";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.execute();
//
//            ResultSet rs = ps.getResultSet();
//            while (rs.next()) {
//                Item_RoomViewModel i = new Item_RoomViewModel();
//                String maP = rs.getString(1);
//                int soP = rs.getInt(2);
//                String tenTB = rs.getString(3);
//                String tt = rs.getString(4);
//                int sl = rs.getInt(5);
//
//                i.setCodeRoom(maP);
//                i.setRoomNumber(soP);
//                i.setName(tenTB);
//                i.setStatus(tt);
//                i.setAmount(sl);
//
//                list1.add(i);
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list1;
//    }
//}
