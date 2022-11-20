package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Client;
import respository.ClientRepo;
import utilities.ReadWriteData;
import views.ViewTrangChu;

public class ClientService implements IService<Client, String> {

    private ClientRepo repo;
    private ReadWriteData readWriteData;

    public ClientService() {
        readWriteData = new ReadWriteData();
        repo = new ClientRepo();
    }

    private String checkData(Client entity) {
        if (entity.getName().equals("")
                || entity.getCode().equals("")
                || entity.getDateOfBirth().equals("")
                || entity.getAddress().equals("")
                || entity.getSex() == null
                || entity.getIdPersonCard().equals("")
                || entity.getCustomPhone().equals("")
                || entity.getCode().equals("")) {
            return "trong";
        } else {
            String reSdt = "0[0-9]{9}";
            String reCC = "0[0-9]{11}";
            if (entity.getCustomPhone().matches(reSdt) == false) {
                return "sdt";
            }
            if (entity.getIdPersonCard().matches(reCC) == false) {
                return "canCuoc";
            }
        }
        return "";
    }

    public List<Client> checkTrung(String cccd) {
        return repo.checkTrung(cccd);
    }

    @Override
    public String insert(Client entity) {
        String check = checkData(entity);
        if (check.equals("trong")) {
            return "Thông tin khách hàng cần nhập đầy đủ";
        } else if (check.equals("canCuoc")) {
            return "Số căn cước phải đủ 12 số";
        } else if (check.equals("sdt")) {
            return "Số điện thoại cần nhập 10 số và bắt đầu bằng 0";
        } else {
            try {
                repo.insert(entity);
                readWriteData.ghidl(entity.getCode(), "maKh.txt");
            } catch (ParseException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "Thêm thành công khách hàng";
    }

    @Override
    public void update(Client entity, String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Client> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
