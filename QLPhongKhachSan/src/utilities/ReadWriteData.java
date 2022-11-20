/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FPTSHOP
 */
public class ReadWriteData {

    public List<String> doc(String nameFile) throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File(nameFile);

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

    public void ghidl(String code, String nameFile) throws FileNotFoundException, IOException {
        File file = new File(nameFile);
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
}
