package application;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import views.ViewDangNhap;
import views.ViewTrangChu;

public class Application {

    public static void main(String[] args) {
        try {

//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
         
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new ViewTrangChu().setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
