/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DomainModel.item;
import Repository.DungItemRepository;
import ViewModel.Dung_ItemViewModel;
import ViewModel.Dung_Room_ItemViewModel;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import View.Dung_ViewItem;
public class ServiceItem_Dung implements Interface_Dung_Item {
    

    private DungItemRepository item = new DungItemRepository();

    @Override
    public ArrayList<Dung_ItemViewModel> list() {
        return item.getList();
    }



    @Override
    public String delete(String ma) {
        boolean delete = this.item.delete(ma);
        if (delete) {
            return "Xoá thành công";
//            return JOptionPane.showMessageDialog(, "Xoa thanh cong");
        }
        return "Xoá thất bại";
    }



    @Override
    public String insert(Dung_ItemViewModel i) {
        boolean add = this.item.insert(i);
        if (add) {
            return "Them thành công";
        }
        return "Them thất bại";
    }

    @Override
    public String update(String ma, Dung_ItemViewModel i) {
       boolean update = this.item.update(ma,i);
        if (update) {
            return "Sua thành công";
        }
        return "Sua thất bại";
    }

    @Override
    public ArrayList<Dung_ItemViewModel> search(String ma) {
       return item.search(ma);
    }





 
    
    
}
