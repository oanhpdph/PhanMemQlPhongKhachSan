
package respository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.jdbcUntil;
import viewModel.PhieuDV;
public class PhieuDVRepo {
    public void delete(String id){
        String sql = "delete roombillservice where idbill=?";
        Connection conn = jdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PhieuDVRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        
    }
    public void update(PhieuDV dv, String id){
        String sql = "Update roombillservice set priceService=?,promotionService=?,dateofHire=? where idbill=?";
        Connection conn = jdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dv.getGia());
            ps.setString(2, dv.getGiamgia());
            ps.setString(3, dv.getNgaythue());
            ps.setString(4, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(PhieuDVRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public ArrayList<PhieuDV> getAll(){
        ArrayList<PhieuDV> list = new ArrayList<>();
        String sql="select idbill,service.code,service.name,service.price,promotionService,dateOfHire,bill.code as 'billcode',roomNumber\n" +
"	from RoomBillService inner join service on RoomBillService.IdService= service.id \n" +
"	inner join RoomBill on RoomBill.BillId=RoomBillService.IdBill \n" +
"	inner join room on room.id= RoomBill.RoomId \n" +
"	inner join bill on bill.id=RoomBill.BillId";
        Connection conn = jdbcUntil.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while(rs.next()){
            String id = rs.getString("idbill");
            String code = rs.getString("code");
            String name = rs.getString("name");
            String gia = rs.getString("price");
            String giamgia = rs.getString("promotionService");
            String ngaythue = rs.getString("dateOfHire");
            String mahoadon= rs.getString("billcode");
            String sophong = rs.getString("roomnumber");
            PhieuDV dv = new PhieuDV(id,code, name, gia, giamgia, ngaythue, sophong, mahoadon);
            list.add(dv);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhieuDVRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
