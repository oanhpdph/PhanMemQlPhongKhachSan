package views;

import Service.ViewItemService;
import com.google.zxing.qrcode.encoder.QRCode;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Bill;
import model.BillRoom;
import model.Client;
import model.PromotionR;
import model.Room;
import model.Service;

import respository.RoomBillRepo;
import service.BillService;
import service.ClientService;
import service.PromotionRService;
import service.RoomBillService;
import service.RoomBillServiceService;
import service.RoomService;
import service.ServiceService;
import service.roomServiceManh;
import utilities.Auth;
import utilities.DaysBetween2Dates;
import utilities.RandomCode;
import utilities.ReadWriteData;
import utilities.StringHandling;
import viewModel.ViewItem;
import static views.QrCode.client;

public class ViewTrangChu extends javax.swing.JFrame {

    private int temp = 0;
    private int thread = 0;
    public int tempCheck = 0;
    private String tenPhong = "";
    public ButtonGroup gr = new ButtonGroup();
    private RandomCode rand = new RandomCode();
    private ReadWriteData readWriteData = new ReadWriteData();
    private DaysBetween2Dates between2Dates = new DaysBetween2Dates();
    private Auth auth;

    private JPanel jpanelTemp;
    private ClientService clienService;
    private BillService billService;
    private RoomService roomService;
    private ViewItemService viewItemService;
    private RoomBillService roomBillService;
    private ServiceService serviceService;
    private RoomBillServiceService roomBillServiceService;
    
    private roomServiceManh roomServiceManh;
    private PromotionRService promotionRService;
    Calendar calendar = Calendar.getInstance();

    public ViewTrangChu() {
        initComponents();

        this.setSize(GetMaxWidth(), GetMaxHeight());

        clienService = new ClientService();
        billService = new BillService();
        roomService = new RoomService();
        roomBillService = new RoomBillService();
        viewItemService = new ViewItemService();
        serviceService = new ServiceService();
        roomBillServiceService = new RoomBillServiceService();
        auth = new Auth();

        gr.add(rdNu);
        gr.add(rdNam);

        // chon ngay
        csTraPhong.setDate(new java.util.Date());
        csTraPhong.setMinSelectableDate(new java.util.Date());
        maxNs();
        new Thread() {
            public void run() {
                while (true) {
                    Calendar calendar = new GregorianCalendar();
                    String hour = (calendar.get(calendar.HOUR_OF_DAY) < 9) ? "0" + calendar.get(calendar.HOUR_OF_DAY) : "" + calendar.get(calendar.HOUR_OF_DAY) + "";
                    String minute = (calendar.get(calendar.MINUTE) < 9) ? "0" + calendar.get(calendar.MINUTE) : "" + calendar.get(calendar.MINUTE) + "";
                    int am_pm = calendar.get(calendar.AM_PM);
                    String day_night;
                    if (am_pm == calendar.AM) {
                        day_night = "AM";
                    } else {
                        day_night = "PM";
                    }
                    lbThoiGian.setText(hour + " : " + minute + " " + day_night);
                }
            }
        }.start();

        loadCbDv();
        loadPanel("Tầng 1");
        loadPanel("Tầng 2");
        loadPanel("Tầng 3");
        loadSl();

        // het init
    }
    public void loadDataRoom() {
        DefaultTableModel dlm = (DefaultTableModel) tb_qlp.getModel();
        dlm.setRowCount(0);
        int stt = 1;
        for (Room r : this.roomServiceManh.getAll()) {
            Object[] rowData = {
                stt, r.getStatus(), r.getKindOfRoom(), getNamePro(r.getIdPromotion()), r.getCode(), r.getRoomNumber(), r.getArea(), r.getLocation(), r.getPrice()
            };
            dlm.addRow(rowData);
            stt++;
        }
    }

    public String getNamePro(String id) {
        String code = "";
        for (PromotionR pr : this.promotionRService.getAll()) {
            if (id.equals(pr.getId())) {
                code = pr.getCode();
                break;
            }
        }
        return code;
    }
     public Room getFromData() {
        String status = String.valueOf(cb_stt.getSelectedIndex());
        String kor = String.valueOf(cb_kor.getSelectedIndex());
        String code = txt_code.getText();
        String rmb = txt_rn.getText();
        String area = txt_area.getText();
        String lct = txt_loc.getText();
        String pri = txt_pri.getText();
        String codepro = cb_pro.getSelectedItem().toString();
        String idPro = "";
        for (PromotionR pr : this.promotionRService.getAll()) {
            if (codepro.equals(pr.getCode())) {
                idPro = pr.getId();
                break;
            }
        }

        Room r = new Room("", status, kor, idPro, code, rmb, area, lct, pri);
        return r;
    }

    public String getIdFromData() {
        Room r1 = this.getFromData();
        String id = "";
        for (Room r : this.roomServiceManh.getAll()) {
            if (r1.getCode().equals(r.getCode())) {
                id = r.getId();
                break;
            }
        }
        return id;
    }
    public int GetMaxWidth() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    }

    public int GetMaxHeight() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    }

    public void loadCbDv() {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) cbDichVu.getModel();
        List<String> list = new ArrayList<>();
        int tempCB = 0;
        for (Service service : serviceService.getAll()) {
            for (int i = 0; i < cbDichVu.getMaximumRowCount(); i++) {
                if (service.getName().equals(cbDichVu.getItemAt(i))) {
                    tempCB = 1;
                    break;
                }
            }
            if (tempCB == 1) {
                tempCB = 0;
                break;
            }
            defaultComboBoxModel.addElement(service.getName());
        }
    }

    public void loadSl() {

        int ss = 0, ck = 0, cd = 0, dd = 0, sc = 0;
        for (Room room : roomService.getAll()) {
            if (room.getStatus().equals("1")) {
                ss = ss + 1;
            }
            if (room.getStatus().equals("2")) {
                ck = ck + 1;
            }
            if (room.getStatus().equals("3")) {
                cd = cd + 1;
            }
            if (room.getStatus().equals("4")) {
                dd = dd + 1;
            }
            if (room.getStatus().equals("5")) {
                sc = sc + 1;
            }
        }
        jLabelAll.setText("(" + roomService.getAll().size() + ")");
        jLabelSS.setText("(" + ss + ")");
        jLabelCK.setText("(" + ck + ")");
        jLabelCD.setText("(" + cd + ")");
        jLabelDD.setText("(" + dd + ")");
        jLabelSC.setText("(" + sc + ")");
    }

    public void loadPanel(String floor) {
        List<Room> list = new ArrayList<>();
        for (Room room : roomService.getAll()) {
            if (room.getLocation().equals(floor)) {
                list.add(room);
            }
            if (floor.equals("all")) {
                list.add(room);
            }
        }

        Collections.reverse(list);
        for (Room room : list) {

            JPanel jPanel = new JPanel();
            JLabel jLabel = new JLabel();
            JLabel jLabel1 = new JLabel();
            JLabel jLabel2 = new JLabel();
            JLabel jLabel3 = new JLabel();
            JLabel jLabel4 = new JLabel();
            JLabel jLabel5 = new JLabel();
            JLabel jLabel6 = new JLabel();
            jPanel.setName(room.getRoomNumber());

            if (room.getStatus().equals("1")) {
                jPanel.setBackground(new java.awt.Color(204, 204, 255));
            }
            if (room.getStatus().equals("2")) {
                jPanel.setBackground(new java.awt.Color(204, 255, 255));
            }
            if (room.getStatus().equals("3")) {
                jPanel.setBackground(new java.awt.Color(204, 255, 204));
            }
            if (room.getStatus().equals("4")) {
                jPanel.setBackground(new java.awt.Color(221, 216, 216));
            }
            if (room.getStatus().equals("5")) {
                jPanel.setBackground(new java.awt.Color(255, 153, 0));
            }
            jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, room.getRoomNumber(), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N
            jPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel1.setText("Loại:");

            jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel2.setText("Diện tích:");

            jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel3.setText("Giá:");

            jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel4.setText(Integer.parseInt(room.getKindOfRoom()) == 1 ? "Phong don" : Integer.parseInt(room.getKindOfRoom()) == 2 ? "phong doi" : "phong vip");

            jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel5.setText(room.getArea() + " " + "m2");

            jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
            jLabel6.setText(room.getPrice() + " " + "VNĐ");

            javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel);
            jPanel.setLayout(jPanel10Layout);
            jPanel10Layout.setHorizontalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel3))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                    .addContainerGap())
            );
            jPanel10Layout.setVerticalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                    .addGap(16, 16, 16)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel4))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel5))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel6))
                                    .addContainerGap())
            );

            jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    popupPhong.show(jPanel, evt.getX(), evt.getY());
                    tenPhong = jPanel.getName();
                    jpanelTemp = jPanel;
                }
            });

            if (room.getLocation().equals("Tầng 2")) {
                jPanelTang2.add(jPanel);
                jPanelTang2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 2" + " " + "(" + list.size() + ")", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
            }
            if (room.getLocation().equals("Tầng 1")) {
                jPanelTang1.add(jPanel);
                jPanelTang1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 1" + " " + "(" + list.size() + ")", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
            }
            if (room.getLocation().equals("Tầng 3")) {
                jPanelTang3.add(jPanel);
                jPanelTang3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 3" + " " + "(" + list.size() + ")", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N

            }
        }
    }

    void maxNs() {

        int year = calendar.get(Calendar.YEAR) - 14;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        String maxNs = day + "/" + month + "/" + year;
        try {
            csNgaySinh.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(maxNs));
            csNgaySinh.setMaxSelectableDate(new SimpleDateFormat("dd/MM/yyyy").parse(maxNs));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void reset(Client client) {
        txtCCCD.setText(client.getIdPersonCard());
        txtMaKH.setText(client.getCode());
        txtSDT.setText(client.getCustomPhone());
        txtDiaChi.setText(client.getAddress());
        txtTenKhachHang.setText(client.getName());
        gr.clearSelection();
        maxNs();
        tempCheck = 0;
    }

    public void fillRoom(Room room) {
        txtMaPhong.setText(room.getCode());
        txtSoPhong.setText(room.getRoomNumber());
        txtAreaRoom.setText(room.getArea());
        String loaiPhong = "";
        if (room.getKindOfRoom() != null) {
            loaiPhong = Integer.parseInt(room.getKindOfRoom()) == 1 ? "Phòng đơn" : Integer.parseInt(room.getKindOfRoom()) == 2 ? "Phòng đôi" : "Phòng vip";
        }
        txtKindOfRoom.setText(loaiPhong);
        txtLocationRoom.setText(room.getLocation());
        txtGiaPhong.setText(room.getPrice());
    }

    void fillClient(Client client) {
        txtTenKhachHang.setText(client.getName());
        try {
            System.out.println(client.getDateOfBirth());
            if (client.getDateOfBirth().indexOf("-") != -1) {
                client.setDateOfBirth(client.getDateOfBirth().substring(8) + "/" + client.getDateOfBirth().substring(5, 7) + "/" + client.getDateOfBirth().substring(0, 4));
            }
            csNgaySinh.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(client.getDateOfBirth()));
        } catch (ParseException ex) {
            Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (client.getSex().equals("Nam")) {
            rdNam.setSelected(true);
        } else {
            rdNu.setSelected(true);
        }
        txtCCCD.setText(client.getIdPersonCard());
        txtDiaChi.setText(client.getAddress());
        txtSDT.setText(client.getCustomPhone());
        txtMaKH.setText(client.getCode());
    }

    public class threadChuY extends Thread {

        public void run() {
            while (true) {
                try {
                    threadChuY.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (txtCCCD.getText().length() == 12) {
                    jLabel6.setText("");
                    if (!clienService.checkTrung(txtCCCD.getText().trim()).isEmpty()) {
                        tempCheck = 1;
                        Client client = clienService.checkTrung(txtCCCD.getText().trim()).get(0);
                        fillClient(client);
                    }
                } else {
                    jLabel6.setText("*");
                    jLabel6.setForeground(Color.red);
                    jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (txtTenKhachHang.getText().length() != 0) {
                    jLabel8.setText("");
                } else {
                    jLabel8.setText("*");
                    jLabel8.setForeground(Color.red);
                    jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (gr.isSelected(rdNam.getModel()) || gr.isSelected(rdNu.getModel())) {
                    jLabel41.setText("");
                } else {
                    jLabel41.setText("*");
                    jLabel41.setForeground(Color.red);
                    jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (txtSDT.getText().length() == 10) {
                    jLabel7.setText("");
                } else {
                    jLabel7.setText("*");
                    jLabel7.setForeground(Color.red);
                    jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }

                if (txtDiaChi.getText().length() != 0) {
                    jLabel42.setText("");
                } else {
                    jLabel42.setText("*");
                    jLabel42.setForeground(Color.red);
                    jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 16));
                }
                if (jTab.getSelectedIndex() != 1) {
                    return;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        popupPhong = new javax.swing.JPopupMenu();
        jMenuThuePhong = new javax.swing.JMenuItem();
        jMenuCapNhat = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuSanSang = new javax.swing.JMenuItem();
        jMenuCoKhach = new javax.swing.JMenuItem();
        jMenuChuaDon = new javax.swing.JMenuItem();
        jMenuDangDon = new javax.swing.JMenuItem();
        jMenuSuaChua = new javax.swing.JMenuItem();
        menuDichVu = new javax.swing.JMenuItem();
        popupTang = new javax.swing.JPopupMenu();
        menuThemPhong = new javax.swing.JMenuItem();
        jPanel38 = new javax.swing.JPanel();
        txtTenKS = new javax.swing.JLabel();
        lbThoiGian = new javax.swing.JLabel();
        jTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelSS = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabelCK = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabelCD = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabelDD = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabelSC = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabelAll = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanelTang3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPanelTang2 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanelTang1 = new javax.swing.JPanel();
        btnDx = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel40 = new javax.swing.JPanel();
        pnInforKh = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        txtCCCD = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        rdNam = new javax.swing.JRadioButton();
        rdNu = new javax.swing.JRadioButton();
        btnQuetMa = new javax.swing.JButton();
        btnThuePhong = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        txtMaKH = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        csNgaySinh = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbNoiThat = new javax.swing.JTable();
        txtMaPhong = new javax.swing.JTextField();
        txtSoPhong = new javax.swing.JTextField();
        txtAreaRoom = new javax.swing.JTextField();
        txtLocationRoom = new javax.swing.JTextField();
        txtKindOfRoom = new javax.swing.JTextField();
        txtGiaGiam = new javax.swing.JTextField();
        btnDoiPhong = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtGiaPhong = new javax.swing.JTextField();
        btnHuyPhong = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        csTraPhong = new com.toedter.calendar.JDateChooser();
        jScrollPane11 = new javax.swing.JScrollPane();
        tbDsPhong = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        pnInforKh1 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        txtTenKhachHang1 = new javax.swing.JTextField();
        txtCCCD1 = new javax.swing.JTextField();
        txtSDT1 = new javax.swing.JTextField();
        btnThuePhong1 = new javax.swing.JButton();
        btnReset1 = new javax.swing.JButton();
        txtMaKH1 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tbDsTraPhong = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tbNoiThat1 = new javax.swing.JTable();
        txtMaPhong1 = new javax.swing.JTextField();
        txtSoPhong1 = new javax.swing.JTextField();
        txtAreaRoom1 = new javax.swing.JTextField();
        txtLocationRoom1 = new javax.swing.JTextField();
        txtKindOfRoom1 = new javax.swing.JTextField();
        txtGiaGiam1 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtGiaPhong1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        csTraPhong1 = new com.toedter.calendar.JDateChooser();
        jPanel13 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel39 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtSoPhongDV = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtMaDv = new javax.swing.JTextField();
        cbDichVu = new javax.swing.JComboBox<>();
        txtGiamGiaDV = new javax.swing.JTextField();
        txtGiaDv = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        btnThemDv = new javax.swing.JButton();
        btnHuyDv = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        csNgaySd = new com.toedter.calendar.JDateChooser();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbTTDichVu = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblTTKhach = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
      

        jLabel45 = new javax.swing.JLabel();
        txt_rn = new javax.swing.JTextField();
        txt_pri = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        tb_qlp = new javax.swing.JTable();
        btn_ht = new javax.swing.JButton();
        cb_stt = new javax.swing.JComboBox<>();
        cb_kor = new javax.swing.JComboBox<>();
        jLabel57 = new javax.swing.JLabel();
        cb_pro = new javax.swing.JComboBox<>();
        btn_search = new javax.swing.JButton();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txt_area = new javax.swing.JTextField();
        txt_sear = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txt_loc = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        txt_code = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();

        popupPhong.setBorderPainted(false);
        popupPhong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jMenuThuePhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuThuePhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_rent_16px.png"))); // NOI18N
        jMenuThuePhong.setText("Thuê phòng");
        jMenuThuePhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuThuePhongActionPerformed(evt);
            }
        });
        popupPhong.add(jMenuThuePhong);

        jMenuCapNhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        jMenuCapNhat.setText("Cập nhật phòng");
        popupPhong.add(jMenuCapNhat);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_view_more_16px.png"))); // NOI18N
        jMenu1.setText("Đổi trạng thái");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenuSanSang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuSanSang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ok_16px.png"))); // NOI18N
        jMenuSanSang.setText("Sẵn sàng");
        jMenuSanSang.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuSanSang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSanSangActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuSanSang);

        jMenuCoKhach.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuCoKhach.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_customer_16px.png"))); // NOI18N
        jMenuCoKhach.setText("Có khách");
        jMenuCoKhach.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuCoKhach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCoKhachActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuCoKhach);

        jMenuChuaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuChuaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_broom_16px.png"))); // NOI18N
        jMenuChuaDon.setText("Chưa dọn");
        jMenuChuaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuChuaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuChuaDonActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuChuaDon);

        jMenuDangDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuDangDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_vacuuming_16px.png"))); // NOI18N
        jMenuDangDon.setText("Đang dọn");
        jMenuDangDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuDangDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDangDonActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuDangDon);

        jMenuSuaChua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuSuaChua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Tools_16px.png"))); // NOI18N
        jMenuSuaChua.setText("Sửa chữa");
        jMenuSuaChua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenuSuaChua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSuaChuaActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuSuaChua);

        popupPhong.add(jMenu1);

        menuDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        menuDichVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_service_16px.png"))); // NOI18N
        menuDichVu.setText("Thêm dịch vụ");
        menuDichVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDichVuActionPerformed(evt);
            }
        });
        popupPhong.add(menuDichVu);

        menuThemPhong.setText("Thêm phòng");
        popupTang.add(menuThemPhong);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 204, 204));

        jPanel38.setBackground(new java.awt.Color(255, 204, 204));
        jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtTenKS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenKS.setText("Phần mềm quản lý khách sạn Tây Côn Lĩnh");

        lbThoiGian.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbThoiGian.setText("Thời gian");

        jTab.setBackground(new java.awt.Color(255, 204, 204));
        jTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTab.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Sẵn sàng:");

        jLabelSS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelSS.setText("(0)");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Có khách:");

        jPanel36.setBackground(new java.awt.Color(204, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel36.setPreferredSize(new java.awt.Dimension(15, 15));
        jPanel36.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabelCK.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelCK.setText("(0)");

        jPanel37.setBackground(new java.awt.Color(204, 204, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel37.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Chưa dọn:");

        jPanel35.setBackground(new java.awt.Color(204, 255, 204));
        jPanel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel35.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabelCD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelCD.setText("(0)");

        jPanel34.setBackground(new java.awt.Color(221, 216, 216));
        jPanel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel34.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Đang dọn:");

        jLabelDD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelDD.setText("(0)");

        jPanel33.setBackground(new java.awt.Color(255, 153, 0));
        jPanel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel33.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Sửa chữa:");

        jLabelSC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelSC.setText("(0)");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setText("Tất cả:");

        jLabelAll.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelAll.setText("(0)");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(978, 500));

        jPanel9.setBackground(new java.awt.Color(255, 204, 204));

        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelTang3.setBackground(new java.awt.Color(255, 255, 204));
        jPanelTang3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 3", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanelTang3.setAutoscrolls(true);
        jPanelTang3.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane9.setViewportView(jPanelTang3);

        jScrollPane10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelTang2.setBackground(new java.awt.Color(255, 255, 204));
        jPanelTang2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 2", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanelTang2.setAutoscrolls(true);
        jPanelTang2.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane10.setViewportView(jPanelTang2);

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelTang1.setBackground(new java.awt.Color(255, 255, 204));
        jPanelTang1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 1", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanelTang1.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane8.setViewportView(jPanelTang1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1206, Short.MAX_VALUE)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel9);

        btnDx.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDx.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Logout_16px.png"))); // NOI18N
        btnDx.setText("Đăng xuất");
        btnDx.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelAll)
                .addGap(40, 40, 40)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSS)
                .addGap(40, 40, 40)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCK)
                .addGap(40, 40, 40)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCD)
                .addGap(40, 40, 40)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDD)
                .addGap(40, 40, 40)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelSC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDx)
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel33, jPanel34, jPanel35, jPanel36, jPanel37});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabelCK)
                                .addComponent(jLabel3)
                                .addComponent(jLabelCD))
                            .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel31)
                                .addComponent(jLabelAll))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabelDD)
                                .addComponent(jLabel4)
                                .addComponent(jLabelSC))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabelSS))
                            .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnDx)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabelCD, jLabelCK, jLabelDD, jLabelSC, jLabelSS});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel33, jPanel34, jPanel35, jPanel36, jPanel37});

        jTab.addTab("Trang chủ", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(874, 400));

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane7.setPreferredSize(new java.awt.Dimension(1200, 612));

        jPanel40.setBackground(new java.awt.Color(255, 204, 204));

        pnInforKh.setBackground(new java.awt.Color(255, 255, 204));
        pnInforKh.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        pnInforKh.setPreferredSize(new java.awt.Dimension(380, 390));
        pnInforKh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnInforKhMouseEntered(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Họ và Tên:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Giới tính:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("Ngày Sinh:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Số cccd:");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("Số điện thoại:");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("Địa chỉ:");

        txtTenKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenKhachHang.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtTenKhachHang.setMinimumSize(new java.awt.Dimension(65, 22));

        txtCCCD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCCCD.setMinimumSize(new java.awt.Dimension(65, 22));

        txtSDT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSDT.setMinimumSize(new java.awt.Dimension(65, 22));

        rdNam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdNam.setText("Nam");
        rdNam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        rdNu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdNu.setText("Nữ");
        rdNu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnQuetMa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnQuetMa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Qr_Code_16px.png"))); // NOI18N
        btnQuetMa.setText("Quét mã");
        btnQuetMa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuetMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetMaActionPerformed(evt);
            }
        });

        btnThuePhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThuePhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_handshake_16px.png"))); // NOI18N
        btnThuePhong.setText("Thuê phòng");
        btnThuePhong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThuePhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThuePhongActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reset_16px.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaKH.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtMaKH.setEnabled(false);
        txtMaKH.setName(""); // NOI18N

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setText("Mã:");

        csNgaySinh.setDateFormatString("dd/MM/yyyy");
        csNgaySinh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        csNgaySinh.setMaxSelectableDate(new java.util.Date(1136052108000L));
        csNgaySinh.setMinSelectableDate(new java.util.Date(-631173535000L));

        txtDiaChi.setColumns(20);
        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDiaChi.setRows(5);
        txtDiaChi.setToolTipText("");
        txtDiaChi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane2.setViewportView(txtDiaChi);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 0));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 0, 0));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(204, 0, 0));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout pnInforKhLayout = new javax.swing.GroupLayout(pnInforKh);
        pnInforKh.setLayout(pnInforKhLayout);
        pnInforKhLayout.setHorizontalGroup(
            pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnInforKhLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addComponent(btnReset)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnInforKhLayout.createSequentialGroup()
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnInforKhLayout.createSequentialGroup()
                                .addComponent(btnQuetMa, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnThuePhong, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnInforKhLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel35)
                                    .addComponent(jLabel27))
                                .addGap(18, 18, 18)
                                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnInforKhLayout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(rdNam)
                                        .addGap(74, 74, 74)
                                        .addComponent(rdNu))
                                    .addComponent(txtSDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCCCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(csNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMaKH)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17))))
        );

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnQuetMa, btnReset});

        pnInforKhLayout.setVerticalGroup(
            pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnInforKhLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(csNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(rdNam)
                    .addComponent(rdNu)
                    .addComponent(jLabel41))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnQuetMa, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(btnThuePhong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnQuetMa, btnReset});

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csNgaySinh, txtMaKH, txtTenKhachHang});

        pnInforKhLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtCCCD, txtSDT});

        jPanel11.setBackground(new java.awt.Color(255, 255, 204));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin phòng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(380, 490));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Mã Phòng:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Số phòng:");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Diện tích:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Vị trí:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Giá:");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Loại phòng:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Nội thất trong phòng:");

        tbNoiThat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbNoiThat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên", "Tình trạng", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbNoiThat.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbNoiThat);
        if (tbNoiThat.getColumnModel().getColumnCount() > 0) {
            tbNoiThat.getColumnModel().getColumn(0).setResizable(false);
            tbNoiThat.getColumnModel().getColumn(1).setResizable(false);
            tbNoiThat.getColumnModel().getColumn(2).setResizable(false);
        }

        txtMaPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaPhong.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtMaPhong.setEnabled(false);
        txtMaPhong.setPreferredSize(new java.awt.Dimension(65, 22));

        txtSoPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoPhong.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtSoPhong.setEnabled(false);
        txtSoPhong.setPreferredSize(new java.awt.Dimension(65, 22));

        txtAreaRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAreaRoom.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtAreaRoom.setEnabled(false);
        txtAreaRoom.setMinimumSize(new java.awt.Dimension(65, 22));

        txtLocationRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtLocationRoom.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtLocationRoom.setEnabled(false);
        txtLocationRoom.setPreferredSize(new java.awt.Dimension(65, 22));

        txtKindOfRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtKindOfRoom.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtKindOfRoom.setEnabled(false);
        txtKindOfRoom.setMinimumSize(new java.awt.Dimension(65, 22));

        txtGiaGiam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaGiam.setText("0");
        txtGiaGiam.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtGiaGiam.setEnabled(false);
        txtGiaGiam.setMinimumSize(new java.awt.Dimension(65, 22));

        btnDoiPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDoiPhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_sync_16px.png"))); // NOI18N
        btnDoiPhong.setText("Đổi phòng");
        btnDoiPhong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDoiPhong.setPreferredSize(new java.awt.Dimension(72, 22));
        btnDoiPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiPhongActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setText("Giảm giá:");

        txtGiaPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaPhong.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtGiaPhong.setEnabled(false);

        btnHuyPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHuyPhong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_clear_shopping_cart_16px.png"))); // NOI18N
        btnHuyPhong.setText("Hủy");
        btnHuyPhong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHuyPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyPhongActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("VNĐ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("VNĐ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("m2");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Ngày trả phòng:");

        csTraPhong.setDateFormatCalendar(null);
        csTraPhong.setDateFormatString("dd/MM/yyyy");
        csTraPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        csTraPhong.setMaxSelectableDate(new java.util.Date(253370743302000L));
        csTraPhong.setMinSelectableDate(new java.util.Date(-62135791098000L));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel18))
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGiaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGiaGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel9)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtAreaRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13))
                            .addComponent(txtLocationRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(csTraPhong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                                .addComponent(txtKindOfRoom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(btnDoiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHuyPhong))))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDoiPhong, btnHuyPhong});

        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAreaRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel16)))
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(txtLocationRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKindOfRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)))
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(csTraPhong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDoiPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuyPhong))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel11Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDoiPhong, btnHuyPhong});

        jPanel11Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {csTraPhong, txtAreaRoom, txtGiaGiam, txtKindOfRoom, txtLocationRoom, txtMaPhong, txtSoPhong});

        tbDsPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbDsPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Số phòng", "Tầng", "Loại phòng", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDsPhong.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane11.setViewportView(tbDsPhong);
        if (tbDsPhong.getColumnModel().getColumnCount() > 0) {
            tbDsPhong.getColumnModel().getColumn(0).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(1).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(2).setResizable(false);
            tbDsPhong.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("Danh sách các phòng có thể thuê");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(pnInforKh, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel28))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(815, 815, 815))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel40Layout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel28))
                        .addGroup(jPanel40Layout.createSequentialGroup()
                            .addGap(31, 31, 31)
                            .addComponent(pnInforKh, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jScrollPane7.setViewportView(jPanel40);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
        );

        jTab.addTab("Thuê phòng", jPanel2);

        jPanel10.setBackground(new java.awt.Color(255, 204, 204));

        pnInforKh1.setBackground(new java.awt.Color(255, 255, 204));
        pnInforKh1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        pnInforKh1.setPreferredSize(new java.awt.Dimension(380, 390));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setText("Họ và Tên:");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel43.setText("Số cccd:");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel44.setText("Số điện thoại:");

        txtTenKhachHang1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenKhachHang1.setMinimumSize(new java.awt.Dimension(65, 22));

        txtCCCD1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCCCD1.setMinimumSize(new java.awt.Dimension(65, 22));

        txtSDT1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSDT1.setMinimumSize(new java.awt.Dimension(65, 22));

        btnThuePhong1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThuePhong1.setText("Trả Phòng");
        btnThuePhong1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnReset1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnReset1.setText("Hủy");
        btnReset1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtMaKH1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaKH1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtMaKH1.setEnabled(false);
        txtMaKH1.setName(""); // NOI18N

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel46.setText("Mã:");

        tbDsTraPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbDsTraPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Số phòng", "Mã Phòng", "Giá"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDsTraPhong.getTableHeader().setReorderingAllowed(false);
        jScrollPane15.setViewportView(tbDsTraPhong);
        if (tbDsTraPhong.getColumnModel().getColumnCount() > 0) {
            tbDsTraPhong.getColumnModel().getColumn(0).setResizable(false);
            tbDsTraPhong.getColumnModel().getColumn(1).setResizable(false);
            tbDsTraPhong.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout pnInforKh1Layout = new javax.swing.GroupLayout(pnInforKh1);
        pnInforKh1.setLayout(pnInforKh1Layout);
        pnInforKh1Layout.setHorizontalGroup(
            pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnInforKh1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnInforKh1Layout.createSequentialGroup()
                        .addComponent(btnThuePhong1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnReset1))
                    .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(pnInforKh1Layout.createSequentialGroup()
                            .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel44)
                                .addComponent(jLabel43)
                                .addComponent(jLabel32)
                                .addComponent(jLabel46))
                            .addGap(18, 18, 18)
                            .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtCCCD1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                .addComponent(txtTenKhachHang1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtMaKH1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSDT1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        pnInforKh1Layout.setVerticalGroup(
            pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnInforKh1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addGap(18, 18, 18)
                .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(txtTenKhachHang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCCCD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addGap(18, 18, 18)
                .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(txtSDT1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 208, Short.MAX_VALUE)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(pnInforKh1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset1)
                    .addComponent(btnThuePhong1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 204));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin phòng", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanel12.setPreferredSize(new java.awt.Dimension(380, 490));

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel47.setText("Mã Phòng:");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel48.setText("Số phòng:");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel49.setText("Diện tích:");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel50.setText("Vị trí:");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel51.setText("Giá:");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel52.setText("Loại phòng:");

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel53.setText("Nội thất trong phòng:");

        tbNoiThat1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbNoiThat1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên", "Tình trạng", "Số lượng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbNoiThat1.getTableHeader().setReorderingAllowed(false);
        jScrollPane14.setViewportView(tbNoiThat1);
        if (tbNoiThat1.getColumnModel().getColumnCount() > 0) {
            tbNoiThat1.getColumnModel().getColumn(0).setResizable(false);
            tbNoiThat1.getColumnModel().getColumn(1).setResizable(false);
            tbNoiThat1.getColumnModel().getColumn(2).setResizable(false);
        }

        txtMaPhong1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaPhong1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtMaPhong1.setEnabled(false);
        txtMaPhong1.setPreferredSize(new java.awt.Dimension(65, 22));

        txtSoPhong1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoPhong1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtSoPhong1.setEnabled(false);
        txtSoPhong1.setPreferredSize(new java.awt.Dimension(65, 22));

        txtAreaRoom1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAreaRoom1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtAreaRoom1.setEnabled(false);
        txtAreaRoom1.setMinimumSize(new java.awt.Dimension(65, 22));

        txtLocationRoom1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtLocationRoom1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtLocationRoom1.setEnabled(false);
        txtLocationRoom1.setPreferredSize(new java.awt.Dimension(65, 22));

        txtKindOfRoom1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtKindOfRoom1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtKindOfRoom1.setEnabled(false);
        txtKindOfRoom1.setMinimumSize(new java.awt.Dimension(65, 22));

        txtGiaGiam1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaGiam1.setText("0");
        txtGiaGiam1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtGiaGiam1.setEnabled(false);
        txtGiaGiam1.setMinimumSize(new java.awt.Dimension(65, 22));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel54.setText("Giảm giá:");

        txtGiaPhong1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaPhong1.setDisabledTextColor(new java.awt.Color(0, 0, 204));
        txtGiaPhong1.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("VNĐ");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("VNĐ");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel55.setText("m2");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel56.setText("Ngày trả phòng:");

        csTraPhong1.setDateFormatCalendar(null);
        csTraPhong1.setDateFormatString("dd/MM/yyyy");
        csTraPhong1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        csTraPhong1.setMaxSelectableDate(new java.util.Date(253370743302000L));
        csTraPhong1.setMinSelectableDate(new java.util.Date(-62135791098000L));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56)
                            .addComponent(jLabel51)
                            .addComponent(jLabel49)
                            .addComponent(jLabel50)
                            .addComponent(jLabel52)
                            .addComponent(jLabel54))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGiaGiam1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(txtGiaPhong1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addGap(56, 56, 56))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(csTraPhong1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(txtKindOfRoom1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtLocationRoom1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtAreaRoom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel55)
                                .addGap(66, 66, 66))))
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addComponent(jLabel48))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaPhong1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(txtSoPhong1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaPhong1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(txtSoPhong1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAreaRoom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(txtLocationRoom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKindOfRoom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel52)))
                    .addComponent(jLabel50))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(csTraPhong1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaGiam1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaPhong1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jLabel53)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hóa đơn", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 327, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(pnInforKh1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .addComponent(pnInforKh1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );

        jScrollPane12.setViewportView(jPanel10);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12)
        );

        jTab.addTab("Trả phòng", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel39.setBackground(new java.awt.Color(255, 204, 204));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dịch vụ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setText("Số phòng:");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel33.setText("Dịch vụ:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("Mã dịch vụ:");

        txtMaDv.setEnabled(false);

        cbDichVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dịch vụ" }));
        cbDichVu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbDichVuItemStateChanged(evt);
            }
        });
        cbDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbDichVuMouseClicked(evt);
            }
        });

        txtGiamGiaDV.setText("0");
        txtGiamGiaDV.setEnabled(false);

        txtGiaDv.setEnabled(false);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel36.setText("Giảm giá:");

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setText("Giá:");

        btnThemDv.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThemDv.setText("Thêm");
        btnThemDv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDvActionPerformed(evt);
            }
        });

        btnHuyDv.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHuyDv.setText("Hủy");

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel39.setText("VNĐ");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel38.setText("VNĐ");

        csNgaySd.setDateFormatString("dd/MM/yyyy");
        csNgaySd.setEnabled(false);

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel40.setText("Ngày sử dụng:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(btnThemDv))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel37))
                                .addGap(19, 19, 19)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(csNgaySd, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel14Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnHuyDv))
                                            .addComponent(txtGiamGiaDV, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtGiaDv, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel39)
                                            .addComponent(jLabel38))))))
                        .addGap(52, 52, 52))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMaDv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel29))
                                .addGap(36, 36, 36)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSoPhongDV, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(23, 23, 23))))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoPhongDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtMaDv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(csNgaySd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiamGiaDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaDv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThemDv)
                    .addComponent(btnHuyDv))
                .addContainerGap())
        );

        tbTTDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbTTDichVu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Số phòng", "Dịch vụ", "Mã dịch vụ", "Ngày sử dụng", "Số lần", "Giảm giá", "Giá", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbTTDichVu.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tbTTDichVu);
        if (tbTTDichVu.getColumnModel().getColumnCount() > 0) {
            tbTTDichVu.getColumnModel().getColumn(0).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(1).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(2).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(3).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(4).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(5).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(6).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(7).setResizable(false);
            tbTTDichVu.getColumnModel().getColumn(8).setResizable(false);
        }

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jScrollPane6.setViewportView(jPanel39);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTab.addTab("Dịch vụ", jPanel4);

        jPanel8.setBackground(new java.awt.Color(255, 204, 204));

        tblTTKhach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Kh", "Tên", "Tuổi", "Giới tính", "Số căn cước", "Số điện thoại", "Địa chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTTKhach.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblTTKhach);
        if (tblTTKhach.getColumnModel().getColumnCount() > 0) {
            tblTTKhach.getColumnModel().getColumn(0).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(1).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(2).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(3).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(4).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(5).setResizable(false);
            tblTTKhach.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(608, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTab.addTab("Khách hàng", jPanel8);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1291, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );

        jTab.addTab("Nhân viên", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1291, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );

        jTab.addTab("Hóa đơn", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1291, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 668, Short.MAX_VALUE)
        );

        jTab.addTab("Thiết bị", jPanel7);


        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTenKS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbThoiGian)
                .addGap(35, 35, 35))
            .addComponent(jTab)
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKS)
                    .addComponent(lbThoiGian))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {                                         
        reset(new Client());
    }                                        

    private void btnThuePhongActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if (JOptionPane.showConfirmDialog(this, "Xác nhận cho thuê phòng.", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (!txtMaPhong.getText().equals("")) {
                // them khach hang
                if (tempCheck == 0) {
                    txtMaKH.setText(rand.createCode("kh", "maKh.txt"));
                    for (Client client : clienService.getAll()) {
                        if (client.getCode().equals(txtMaKH.getText())) {
                            try {
                                readWriteData.ghidl(Integer.parseInt(txtMaKH.getText().substring(2)), "maKh.txt");
                            } catch (IOException ex) {
                                Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            btnThuePhongActionPerformed(evt);
                        }
                    }

                    Client client = new Client();
                    client.setName(txtTenKhachHang.getText().trim());
                    client.setAddress(txtDiaChi.getText().trim());
                    client.setCustomPhone(txtSDT.getText().trim());
                    client.setDateOfBirth(String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(csNgaySinh.getDate())));

                    if (rdNu.isSelected()) {
                        client.setSex("Nữ");
                    } else {
                        client.setSex("Nam");
                    }
                    client.setIdPersonCard(txtCCCD.getText().trim());
                    client.setCode(txtMaKH.getText().trim());
                    String string = clienService.insert(client);
                    if (string != null) {
                        JOptionPane.showMessageDialog(this, string);
                    }
                }
                // them hoa don
                List<Client> listKh = clienService.checkTrung(txtCCCD.getText().trim());
                if (listKh == null || listKh.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "khong tim thay khach hang");
                    return;
                }
                String idClient = listKh.get(0).getId();

                long noDay = between2Dates.daysBetween2Dates(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()), new SimpleDateFormat("yyyy-MM-dd").format(csTraPhong.getDate()));
                if (noDay == 0) {
                    JOptionPane.showMessageDialog(this, "Chọn ngày trả phòng");
                    return;
                }
                if (billService.searchHd(idClient) == null) {
                    Bill bill = new Bill();
                    if (clienService.checkTrung(txtCCCD.getText().trim()) == null) {
                        return;
                    }
                    bill.setIdClient(idClient);
                    bill.setIdStaff(auth.id);

                    String maHd = rand.createCode("hd", "maHd.txt");
                    System.out.println(maHd);

                    bill.setCode(maHd);
                    bill.setPrice(String.valueOf(noDay * (Float.parseFloat(txtGiaPhong.getText().trim()) - Float.parseFloat(txtGiaGiam.getText().trim()))));
                    bill.setStatus("0");// 0 chua thanh toan
                    bill.setDate(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())));
                    billService.insert(bill);
                }
                // thêm phòng vào hóa đơn chi tiết
                BillRoom roomBill = new BillRoom();
                Room room = roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0);
                roomBill.setBillId(billService.searchHd(idClient).get(0).getId());// id hoa don
                roomBill.setRoomId(room.getId());
                roomBill.setPriceRoom(txtGiaPhong.getText());
                roomBill.setPromotionRoom(txtGiaGiam.getText());
                roomBill.setDateCheckIn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

                roomBill.setDateCheckout(new SimpleDateFormat("yyyy-MM-dd 12:00:00").format(csTraPhong.getDate()));
                roomBillService.insert(roomBill);
                JOptionPane.showMessageDialog(this, "Thành Công!!");
                room.setStatus("2");
                roomService.update(room, room.getRoomNumber());
                if (room.getLocation().equals("Tầng 1")) {
                    jPanelTang1.removeAll();
                }
                if (room.getLocation().equals("Tầng 2")) {
                    jPanelTang2.removeAll();
                }
                if (room.getLocation().equals("Tầng 3")) {
                    jPanelTang3.removeAll();
                }
                loadPanel(room.getLocation());
                btnResetActionPerformed(evt);
                btnHuyPhongActionPerformed(evt);
            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn phòng");
                return;
            }
        }
    }                                            

    private void btnDoiPhongActionPerformed(java.awt.event.ActionEvent evt) {                                            
        DefaultTableModel defaultTableModelds = (DefaultTableModel) tbDsPhong.getModel();
        defaultTableModelds.setRowCount(0);
        for (Room room : roomService.getAll()) {
            if (room.getStatus().equals("1")) {
                defaultTableModelds.addRow(new Object[]{room.getRoomNumber(), room.getLocation(), room.getKindOfRoom(), room.getPrice()});
            }
        }
        if (temp == 0) {
            temp = 1;
            txtSoPhong.setEnabled(true);
            return;
        }
        if (temp == 1) {
            if (roomService.getRoomByNumber(txtSoPhong.getText().trim()) == null || !roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0).getStatus().equals("1")) {
                JOptionPane.showMessageDialog(this, "Nhập sai số phòng hoặc phòng chưa sẵn sàng.");
                return;
            }
            PromotionRService promotionRService = new PromotionRService();

            Room room = roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0);
            String ngay = String.valueOf(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE));
            PromotionR pr = promotionRService.searchPromotionR(room.getIdPromotion(), ngay);
            if (pr != null) {
                txtGiaGiam.setText(pr.getValue());
            }
            if (room.getStatus().equals("1")) {
                fillRoom(room);
            }

            if (viewItemService.getAll(room.getId()) != null) {
                DefaultTableModel defaultTableModel = (DefaultTableModel) tbNoiThat.getModel();
                defaultTableModel.setRowCount(0);
                for (ViewItem item : viewItemService.getAll(room.getId())) {
                    defaultTableModel.addRow(new Object[]{item.getName(), item.getStatus(), item.getAmount()});
                }
            }
            txtSoPhong.setEnabled(false);
            temp = 0;

        }
    }                                           

    private void btnQuetMaActionPerformed(java.awt.event.ActionEvent evt) {                                          
        QrCode qrCode = new QrCode();
        if (qrCode.isVisible() == true) {
            qrCode.show();
        } else {
            qrCode.setVisible(true);
        }
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ViewTrangChu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (qrCode.temp == 1) {
                        fillClient(qrCode.client);
                        return;
                    }
                }
            }
        }.start();
        qrCode.temp = 0;
    }                                         

    private void pnInforKhMouseEntered(java.awt.event.MouseEvent evt) {                                       
        threadChuY t1 = new threadChuY();
        t1.start();
    }                                      

    private void btnThemDvActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (roomService.getRoomByNumber(txtSoPhongDV.getText().trim()) == null) {
            JOptionPane.showMessageDialog(this, "Xem lại số phòng");
            return;
        }
        String idRoom = roomService.getRoomByNumber(txtSoPhongDV.getText().trim()).get(0).getId();
        String idService = serviceService.getByCode(txtMaDv.getText().trim()).get(0).getId();
        String idBill = billService.getId(txtSoPhongDV.getText().trim(), new java.util.Date());
        model.RoomBillService roomBillService = new model.RoomBillService();
        roomBillService.setIdBill(idBill);
        roomBillService.setIdRoom(idRoom);
        roomBillService.setIdService(idService);
        roomBillService.setPriceService(txtGiaDv.getText().trim());
        roomBillService.setPromotionService(txtGiamGiaDV.getText().trim());
        roomBillService.setDateofHire(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        JOptionPane.showMessageDialog(this, roomBillServiceService.insert(roomBillService));

    }                                         

    private void cbDichVuItemStateChanged(java.awt.event.ItemEvent evt) {                                          
        for (Service service : serviceService.getAll()) {
            if (cbDichVu.getSelectedItem().equals(service.getName())) {
                csNgaySd.setDate(new java.util.Date());
                txtMaDv.setText(service.getCode());
                txtGiaDv.setText(service.getPrice());
            }
        }
    }                                         

    private void btnHuyPhongActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Room room = new Room();
        fillRoom(room);
        if (txtSoPhong.isEnabled() == true) {
            temp = 0;
            txtSoPhong.setEnabled(false);

        }
        txtGiaGiam.setText("0");
    }                                           

    private void jMenuThuePhongActionPerformed(java.awt.event.ActionEvent evt) {                                               
        PromotionRService promotionRService = new PromotionRService();
        Room room = roomService.getRoomByNumber(tenPhong).get(0);
        if (room.getStatus().equals("1")) {
            String ngay = String.valueOf(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DATE));
            PromotionR pr = promotionRService.searchPromotionR(room.getIdPromotion(), ngay);
            if (pr != null) {
                txtGiaGiam.setText(pr.getValue());
            }
            jTab.setSelectedIndex(1);
            fillRoom(room);
        } else {
            JOptionPane.showMessageDialog(this, "Phòng chưa sẵn sàng cho thuê");
            txtSoPhong.setText("");
        }
    }                                              

    private void cbDichVuMouseClicked(java.awt.event.MouseEvent evt) {                                      
        loadCbDv();
    }                                     

    private void btnDxActionPerformed(java.awt.event.ActionEvent evt) {                                      
        if (JOptionPane.showConfirmDialog(this, "Muốn đăng xuất khỏi chương trình?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            QrCode.client = null;
            System.exit(0);
        }
    }                                     

    private void jMenuSanSangActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jpanelTemp.setBackground(new java.awt.Color(204, 204, 255));
        Room room = new Room();
        room.setStatus("1");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();
    }                                            

    private void jMenuDangDonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jpanelTemp.setBackground(new java.awt.Color(221, 216, 216));
        Room room = new Room();
        room.setStatus("4");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();

    }                                            

    private void jMenuSuaChuaActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jpanelTemp.setBackground(new java.awt.Color(255, 153, 0));
        Room room = new Room();
        room.setStatus("5");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();

    }                                            

    private void jMenuCoKhachActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jpanelTemp.setBackground(new java.awt.Color(204, 255, 255));
        Room room = new Room();
        room.setStatus("2");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();

    }                                            

    private void jMenuChuaDonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jpanelTemp.setBackground(new java.awt.Color(204, 255, 204));
        Room room = new Room();
        room.setStatus("3");
        room.setRoomNumber(tenPhong);
        roomService.update(room, room.getRoomNumber());
        loadSl();

    }                                            

    private void menuDichVuActionPerformed(java.awt.event.ActionEvent evt) {                                           
        Room room = roomService.getRoomByNumber(tenPhong).get(0);
        if (room.getStatus().equals("2")) {
            jTab.setSelectedIndex(3);
            txtSoPhongDV.setText(tenPhong);
        } else {
            JOptionPane.showMessageDialog(this, "Phòng chưa được thuê.");
        }

    }                                          
   
    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        Room r = this.getFromData();
        this.roomServiceManh.insert(r);
        JOptionPane.showMessageDialog(this, "Thêm thành công!");
        loadDataRoom();
    }                                        

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
        int row = tb_qlp.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần sửa!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa không?", "message", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                Room r = this.getFromData();
                this.roomServiceManh.update(getIdFromData(), r);
                loadDataRoom();
            }
        }
    }                                       

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
        int row = tb_qlp.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần xóa!");
        } else {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?", "message", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                Room r = this.getFromData();
                this.roomServiceManh.delete(getIdFromData());
                loadDataRoom();
            }
        }
    }                                       

    private void tb_qlpMouseClicked(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        int row = tb_qlp.getSelectedRow();
        cb_stt.setSelectedItem(this.tb_qlp.getValueAt(row, 1).toString());
        cb_kor.setSelectedItem(tb_qlp.getValueAt(row, 2).toString());
        txt_code.setText(this.tb_qlp.getValueAt(row, 4).toString());
        txt_rn.setText(this.tb_qlp.getValueAt(row, 5).toString());
        txt_loc.setText(this.tb_qlp.getValueAt(row, 6).toString());
        txt_area.setText(this.tb_qlp.getValueAt(row, 7).toString());
        txt_pri.setText(this.tb_qlp.getValueAt(row, 8).toString());
        cb_pro.setSelectedItem(this.tb_qlp.getValueAt(row, 3).toString());
    }                                   

    private void btn_htActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
        //        lbl_id.setText("");
        cb_stt.setSelectedIndex(0);
        cb_kor.setSelectedIndex(0);
        txt_code.setText("");
        txt_area.setText("");
        txt_loc.setText("");
        txt_pri.setText("");
        txt_rn.setText("");
        cb_pro.setSelectedIndex(0);
    }                                      

    private void cb_proActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
    }                                      
     public void loadTableSearch(){
       DefaultTableModel dlm=(DefaultTableModel) tb_qlp.getModel();
       dlm.setRowCount(0);
       int stt=1;
      Room r=this.roomServiceManh.getSearchRoom(txt_sear.getText().toString());
       Object[] rowData={
               stt,r.getStatus(),r.getKindOfRoom(),getNamePro(r.getIdPromotion()),r.getCode(),r.getRoomNumber(),r.getArea(),r.getLocation(),r.getPrice()
            };
             dlm.addRow(rowData);            
   }
    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        int check=0;
        if(txt_sear.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Mời nhập đối tượng cần tìm");
        }else{
            for(Room r: this.roomServiceManh.getAll()){
                if(txt_sear.getText().equals(r.getCode())){
                    this.loadTableSearch();
                    JOptionPane.showMessageDialog(this, "Tìm thành công");
                    check=1;
                    break;
                }
            }
            if(check==0){
                JOptionPane.showMessageDialog(this, "Không có đối tượng cần tìm");
            }
        }
    }                                          

    private void txt_locActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewTrangChu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new ViewTrangChu().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnDoiPhong;
    private javax.swing.JButton btnDx;
    private javax.swing.JButton btnHuyDv;
    private javax.swing.JButton btnHuyPhong;
    private javax.swing.JButton btnQuetMa;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnThemDv;
    private javax.swing.JButton btnThuePhong;
    private javax.swing.JButton btnThuePhong1;
    private javax.swing.JButton btn_ht;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cbDichVu;
    private javax.swing.JComboBox<String> cb_kor;
    private javax.swing.JComboBox<String> cb_pro;
    private javax.swing.JComboBox<String> cb_stt;
    private com.toedter.calendar.JDateChooser csNgaySd;
    private com.toedter.calendar.JDateChooser csNgaySinh;
    private com.toedter.calendar.JDateChooser csTraPhong;
    private com.toedter.calendar.JDateChooser csTraPhong1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAll;
    private javax.swing.JLabel jLabelCD;
    private javax.swing.JLabel jLabelCK;
    private javax.swing.JLabel jLabelDD;
    private javax.swing.JLabel jLabelSC;
    private javax.swing.JLabel jLabelSS;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuCapNhat;
    private javax.swing.JMenuItem jMenuChuaDon;
    private javax.swing.JMenuItem jMenuCoKhach;
    private javax.swing.JMenuItem jMenuDangDon;
    private javax.swing.JMenuItem jMenuSanSang;
    private javax.swing.JMenuItem jMenuSuaChua;
    private javax.swing.JMenuItem jMenuThuePhong;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelTang1;
    private javax.swing.JPanel jPanelTang2;
    private javax.swing.JPanel jPanelTang3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTab;
    private javax.swing.JLabel lbThoiGian;
    private javax.swing.JMenuItem menuDichVu;
    private javax.swing.JMenuItem menuThemPhong;
    private javax.swing.JPanel pnInforKh;
    private javax.swing.JPanel pnInforKh1;
    private javax.swing.JPopupMenu popupPhong;
    private javax.swing.JPopupMenu popupTang;
    private javax.swing.JRadioButton rdNam;
    private javax.swing.JRadioButton rdNu;
    private javax.swing.JTable tbDsPhong;
    private javax.swing.JTable tbDsTraPhong;
    private javax.swing.JTable tbNoiThat;
    private javax.swing.JTable tbNoiThat1;
    private javax.swing.JTable tbTTDichVu;
    private javax.swing.JTable tb_qlp;
    private javax.swing.JTable tblTTKhach;
    private javax.swing.JTextField txtAreaRoom;
    private javax.swing.JTextField txtAreaRoom1;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtCCCD1;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtGiaDv;
    private javax.swing.JTextField txtGiaGiam;
    private javax.swing.JTextField txtGiaGiam1;
    private javax.swing.JTextField txtGiaPhong;
    private javax.swing.JTextField txtGiaPhong1;
    private javax.swing.JTextField txtGiamGiaDV;
    private javax.swing.JTextField txtKindOfRoom;
    private javax.swing.JTextField txtKindOfRoom1;
    private javax.swing.JTextField txtLocationRoom;
    private javax.swing.JTextField txtLocationRoom1;
    private javax.swing.JTextField txtMaDv;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKH1;
    private javax.swing.JTextField txtMaPhong;
    private javax.swing.JTextField txtMaPhong1;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDT1;
    private javax.swing.JTextField txtSoPhong;
    private javax.swing.JTextField txtSoPhong1;
    private javax.swing.JTextField txtSoPhongDV;
    private javax.swing.JLabel txtTenKS;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtTenKhachHang1;
    private javax.swing.JTextField txt_area;
    private javax.swing.JTextField txt_code;
    private javax.swing.JTextField txt_loc;
    private javax.swing.JTextField txt_pri;
    private javax.swing.JTextField txt_rn;
    private javax.swing.JTextField txt_sear;
    // End of variables declaration                   

}

