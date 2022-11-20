package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bill;
import respository.BillRepo;

public class BillService implements IService<Bill, String> {

    private BillRepo repo;

   

    public BillService() {
        repo = new BillRepo();
    }

    @Override
    public String insert(Bill entity) {
        repo.insert(entity);
        try {
            ghidl(entity.getCode());
        } catch (IOException ex) {
            Logger.getLogger(BillService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "thêm thành công";
    }

    public void ghidl(String code) throws FileNotFoundException, IOException {
        File file = new File("maHd.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
            }
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(code);
        fos.close();
        oos.close();
    }

    

    public List<String> doc() throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File("maHd.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
            }
        }
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        String code;
        List<String> listtemp = new ArrayList<>();
        while (fis.available() > 0) {
            code = (String) ois.readObject();
            listtemp.add(code);
        }
        fis.close();
        ois.close();
        return listtemp;
    }

    @Override
    public void update(Bill entity, String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Bill> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
