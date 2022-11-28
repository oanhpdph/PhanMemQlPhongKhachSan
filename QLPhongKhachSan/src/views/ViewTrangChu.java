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
import utilities.Auth;
import utilities.DaysBetween2Dates;
import utilities.RandomCode;
import utilities.ReadWriteData;
import utilities.StringHandling;
import viewModel.ViewItem;
import static views.QrCode.client;

public class ViewTrangChu extends javax.swing.JFrame {

    private int temp = 0, tempDv = 0;
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
        loadPanel();
        // het init
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
        int temp = 0;
        for (Service service : serviceService.getAll()) {
            for (int i = 0; i < cbDichVu.getMaximumRowCount(); i++) {
                if (service.getName().equals(cbDichVu.getItemAt(i))) {
                    temp = 1;
                    break;
                }
            }
            if (temp == 1) {
                temp = 0;
                break;
            }
            defaultComboBoxModel.addElement(service.getName());
        }
    }

    public void loadPanel() {
        for (Room room : roomService.getAll()) {
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
                }

                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    tenPhong = jPanel.getName();
                    jpanelTemp = jPanel;
                }
            });

            if (room.getLocation().equals("tang 2")) {
                jPanel22.add(jPanel);
            }
            if (room.getLocation().equals("tang 1")) {
                jPanel15.add(jPanel);
            }
            if (room.getLocation().equals("tang 3")) {
                jPanel46.add(jPanel);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        jTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel15 = new javax.swing.JPanel();
        btnDx = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
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
        jTable2 = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
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
        jPanel38 = new javax.swing.JPanel();
        txtTenKS = new javax.swing.JLabel();
        lbThoiGian = new javax.swing.JLabel();

        jMenuThuePhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuThuePhong.setText("Thuê phòng");
        jMenuThuePhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuThuePhongActionPerformed(evt);
            }
        });
        popupPhong.add(jMenuThuePhong);

        jMenuCapNhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuCapNhat.setText("Cập nhật phòng");
        popupPhong.add(jMenuCapNhat);

        jMenu1.setText("Đổi trạng thái");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenuSanSang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuSanSang.setText("Sẵn sàng");
        jMenuSanSang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSanSangActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuSanSang);

        jMenuCoKhach.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuCoKhach.setText("Có khách");
        jMenuCoKhach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCoKhachActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuCoKhach);

        jMenuChuaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuChuaDon.setText("Chưa dọn");
        jMenuChuaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuChuaDonActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuChuaDon);

        jMenuDangDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuDangDon.setText("Đang dọn");
        jMenuDangDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuDangDonActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuDangDon);

        jMenuSuaChua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuSuaChua.setText("Sửa chữa");
        jMenuSuaChua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSuaChuaActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuSuaChua);

        popupPhong.add(jMenu1);

        menuDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        jTab.setBackground(new java.awt.Color(255, 204, 204));
        jTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTab.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Sẵn sàng:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("(0)");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Có khách:");

        jPanel36.setBackground(new java.awt.Color(204, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel36.setPreferredSize(new java.awt.Dimension(15, 15));
        jPanel36.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("(0)");

        jPanel37.setBackground(new java.awt.Color(204, 204, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel37.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Chưa dọn:");

        jPanel35.setBackground(new java.awt.Color(204, 255, 204));
        jPanel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel35.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("(0)");

        jPanel34.setBackground(new java.awt.Color(221, 216, 216));
        jPanel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel34.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Đang dọn:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("(0)");

        jPanel33.setBackground(new java.awt.Color(255, 153, 0));
        jPanel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel33.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Sửa chữa:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("(0)");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel31.setText("Tất cả:");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel32.setText("(0)");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(978, 500));

        jPanel9.setBackground(new java.awt.Color(255, 204, 204));

        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel46.setBackground(new java.awt.Color(255, 255, 204));
        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 3", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanel46.setAutoscrolls(true);
        jPanel46.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane9.setViewportView(jPanel46);

        jScrollPane10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel22.setBackground(new java.awt.Color(255, 255, 204));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 2", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanel22.setAutoscrolls(true);
        jPanel22.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane10.setViewportView(jPanel22);

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel15.setBackground(new java.awt.Color(255, 255, 204));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tầng 1", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Segoe UI", 0, 20))); // NOI18N
        jPanel15.setLayout(new java.awt.GridLayout(0, 5));
        jScrollPane8.setViewportView(jPanel15);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
                    .addComponent(jScrollPane10)
                    .addComponent(jScrollPane9))
                .addContainerGap(16, Short.MAX_VALUE))
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
        btnDx.setText("Đăng xuất");
        btnDx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDxActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel32)
                .addGap(32, 32, 32)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(53, 53, 53)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(40, 40, 40)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(52, 52, 52)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(48, 48, 48)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(34, 34, 34)
                .addComponent(btnDx)
                .addGap(67, 67, 67))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel12)
                                .addComponent(jLabel2)
                                .addComponent(jLabel11)
                                .addComponent(jLabel3)
                                .addComponent(jLabel8))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnDx)
                                    .addComponent(jButton1)
                                    .addComponent(jButton2))
                                .addComponent(jPanel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel31)
                        .addComponent(jLabel32))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel7)
                        .addComponent(jLabel4)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel11, jLabel12, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPanel33, jPanel34, jPanel35, jPanel36, jPanel37});

        jTab.addTab("Trang chủ", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(874, 400));

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
        txtTenKhachHang.setMinimumSize(new java.awt.Dimension(65, 22));

        txtCCCD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtCCCD.setMinimumSize(new java.awt.Dimension(65, 22));

        txtSDT.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSDT.setMinimumSize(new java.awt.Dimension(65, 22));

        rdNam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdNam.setText("Nam");

        rdNu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdNu.setText("Nữ");

        btnQuetMa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnQuetMa.setText("Quét mã");
        btnQuetMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetMaActionPerformed(evt);
            }
        });

        btnThuePhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThuePhong.setText("Thuê phòng");
        btnThuePhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThuePhongActionPerformed(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        txtDiaChi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane2.setViewportView(txtDiaChi);

        javax.swing.GroupLayout pnInforKhLayout = new javax.swing.GroupLayout(pnInforKh);
        pnInforKh.setLayout(pnInforKhLayout);
        pnInforKhLayout.setHorizontalGroup(
            pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnInforKhLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnInforKhLayout.createSequentialGroup()
                        .addComponent(btnQuetMa, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThuePhong, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnReset, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnInforKhLayout.createSequentialGroup()
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel26)
                            .addComponent(jLabel25)
                            .addComponent(jLabel24)
                            .addComponent(jLabel22)
                            .addComponent(jLabel35)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18)
                        .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnInforKhLayout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(rdNam)
                                .addGap(74, 74, 74)
                                .addComponent(rdNu))
                            .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(csNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCCCD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtSDT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2)
                                .addComponent(txtMaKH)))))
                .addGap(21, 21, 21))
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
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(csNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(rdNam)
                    .addComponent(rdNu))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnInforKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuetMa, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(btnThuePhong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnReset)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        txtMaPhong.setEnabled(false);
        txtMaPhong.setPreferredSize(new java.awt.Dimension(65, 22));

        txtSoPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoPhong.setEnabled(false);
        txtSoPhong.setPreferredSize(new java.awt.Dimension(65, 22));

        txtAreaRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtAreaRoom.setEnabled(false);
        txtAreaRoom.setMinimumSize(new java.awt.Dimension(65, 22));

        txtLocationRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtLocationRoom.setEnabled(false);
        txtLocationRoom.setPreferredSize(new java.awt.Dimension(65, 22));

        txtKindOfRoom.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtKindOfRoom.setEnabled(false);
        txtKindOfRoom.setMinimumSize(new java.awt.Dimension(65, 22));

        txtGiaGiam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaGiam.setText("0");
        txtGiaGiam.setEnabled(false);
        txtGiaGiam.setMinimumSize(new java.awt.Dimension(65, 22));

        btnDoiPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnDoiPhong.setText("Đổi phòng");
        btnDoiPhong.setPreferredSize(new java.awt.Dimension(72, 22));
        btnDoiPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiPhongActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setText("Giảm giá:");

        txtGiaPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaPhong.setEnabled(false);

        btnHuyPhong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHuyPhong.setText("Hủy");
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
                .addContainerGap(22, Short.MAX_VALUE))
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

        jTable2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Số phòng", "Tầng", "Loại phòng", "Trạng thái"
            }
        ));
        jScrollPane11.setViewportView(jTable2);

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("Danh sách các phòng có thể thuê");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(pnInforKh, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jLabel28))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                            .addComponent(pnInforKh, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel28)))
                .addContainerGap())
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
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE)
        );

        jTab.addTab("Thuê phòng", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1264, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
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
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        jScrollPane6.setViewportView(jPanel39);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1264, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
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
                .addContainerGap(581, Short.MAX_VALUE))
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
            .addGap(0, 1264, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jTab.addTab("Nhân viên", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1264, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jTab.addTab("Hóa đơn", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1264, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jTab.addTab("Thiết bị", jPanel7);

        jPanel38.setBackground(new java.awt.Color(255, 204, 204));
        jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtTenKS.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenKS.setText("Phần mềm quản lý khách sạn Tây Côn Lĩnh");

        lbThoiGian.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbThoiGian.setText("Thời gian");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTenKS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbThoiGian)
                .addGap(26, 26, 26))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKS)
                    .addComponent(lbThoiGian))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTab)
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTab, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        reset(new Client());
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnThuePhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThuePhongActionPerformed

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

            roomBill.setBillId(billService.searchHd(idClient).get(0).getId());// id hoa don
            roomBill.setRoomId(roomService.getRoomByNumber(txtSoPhong.getText().trim()).get(0).getId());
            roomBill.setPriceRoom(txtGiaPhong.getText());
            roomBill.setPromotionRoom(txtGiaGiam.getText());
            roomBill.setDateCheckIn(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

            roomBill.setDateCheckout(new SimpleDateFormat("yyyy-MM-dd 12:00:00").format(csTraPhong.getDate()));
            roomBillService.insert(roomBill);
            JOptionPane.showMessageDialog(this, "Thành Công!!");
            
            jPanel15.removeAll();
            jPanel22.removeAll();
            jPanel46.removeAll();
            loadPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Chưa chọn phòng");
            return;
        }
    }//GEN-LAST:event_btnThuePhongActionPerformed

    private void btnDoiPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiPhongActionPerformed
        if (temp == 0) {
            temp = 1;
            txtSoPhong.setEnabled(true);
            txtSoPhong.setBackground(Color.yellow);
            return;
        }
        if (temp == 1) {
            if (roomService.getRoomByNumber(txtSoPhong.getText().trim()) == null) {
                JOptionPane.showMessageDialog(this, "Xem lại số phòng");
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
            txtSoPhong.setBackground(Color.cyan);
        }
    }//GEN-LAST:event_btnDoiPhongActionPerformed

    private void btnQuetMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetMaActionPerformed
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
    }//GEN-LAST:event_btnQuetMaActionPerformed

    private void pnInforKhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnInforKhMouseEntered
        if (txtCCCD.getText().length() == 12) {
            if (clienService.checkTrung(txtCCCD.getText().trim()).isEmpty()) {
                return;
            } else {
                tempCheck = 1;
                Client client = clienService.checkTrung(txtCCCD.getText().trim()).get(0);
                fillClient(client);
                return;
            }
        }
    }//GEN-LAST:event_pnInforKhMouseEntered

    private void btnThemDvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDvActionPerformed
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

    }//GEN-LAST:event_btnThemDvActionPerformed

    private void cbDichVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDichVuItemStateChanged
        for (Service service : serviceService.getAll()) {
            if (cbDichVu.getSelectedItem().equals(service.getName())) {
                csNgaySd.setDate(new java.util.Date());
                txtMaDv.setText(service.getCode());
                txtGiaDv.setText(service.getPrice());
            }
        }
    }//GEN-LAST:event_cbDichVuItemStateChanged

    private void btnHuyPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyPhongActionPerformed
        Room room = new Room();
        fillRoom(room);
        if (txtSoPhong.isEnabled() == true) {
            txtSoPhong.setEnabled(false);
        }
    }//GEN-LAST:event_btnHuyPhongActionPerformed

    private void jMenuThuePhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuThuePhongActionPerformed
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
    }//GEN-LAST:event_jMenuThuePhongActionPerformed

    private void cbDichVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbDichVuMouseClicked
        loadCbDv();
    }//GEN-LAST:event_cbDichVuMouseClicked

    private void btnDxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDxActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Muốn đăng xuất khỏi chương trình?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            QrCode.client = null;
            System.exit(0);
        }
    }//GEN-LAST:event_btnDxActionPerformed

    private void jMenuSanSangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSanSangActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(204, 204, 255));

    }//GEN-LAST:event_jMenuSanSangActionPerformed

    private void jMenuDangDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuDangDonActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(221, 216, 216));
    }//GEN-LAST:event_jMenuDangDonActionPerformed

    private void jMenuSuaChuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSuaChuaActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(255, 153, 0));
    }//GEN-LAST:event_jMenuSuaChuaActionPerformed

    private void jMenuCoKhachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCoKhachActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(204, 255, 255));
    }//GEN-LAST:event_jMenuCoKhachActionPerformed

    private void jMenuChuaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuChuaDonActionPerformed
        jpanelTemp.setBackground(new java.awt.Color(204, 255, 204));
    }//GEN-LAST:event_jMenuChuaDonActionPerformed

    private void menuDichVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDichVuActionPerformed
        Room room = roomService.getRoomByNumber(tenPhong).get(0);
        if (room.getStatus().equals("2")) {
            jTab.setSelectedIndex(3);
            txtSoPhongDV.setText(tenPhong);
        } else {
            JOptionPane.showMessageDialog(this, "Phòng chưa được thuê.");
        }

    }//GEN-LAST:event_menuDichVuActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDoiPhong;
    private javax.swing.JButton btnDx;
    private javax.swing.JButton btnHuyDv;
    private javax.swing.JButton btnHuyPhong;
    private javax.swing.JButton btnQuetMa;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnThemDv;
    private javax.swing.JButton btnThuePhong;
    private javax.swing.JComboBox<String> cbDichVu;
    private com.toedter.calendar.JDateChooser csNgaySd;
    private com.toedter.calendar.JDateChooser csNgaySinh;
    private com.toedter.calendar.JDateChooser csTraPhong;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuCapNhat;
    private javax.swing.JMenuItem jMenuChuaDon;
    private javax.swing.JMenuItem jMenuCoKhach;
    private javax.swing.JMenuItem jMenuDangDon;
    private javax.swing.JMenuItem jMenuSanSang;
    private javax.swing.JMenuItem jMenuSuaChua;
    private javax.swing.JMenuItem jMenuThuePhong;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
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
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTab;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbThoiGian;
    private javax.swing.JMenuItem menuDichVu;
    private javax.swing.JMenuItem menuThemPhong;
    private javax.swing.JPanel pnInforKh;
    private javax.swing.JPopupMenu popupPhong;
    private javax.swing.JPopupMenu popupTang;
    private javax.swing.JRadioButton rdNam;
    private javax.swing.JRadioButton rdNu;
    private javax.swing.JTable tbNoiThat;
    private javax.swing.JTable tbTTDichVu;
    private javax.swing.JTable tblTTKhach;
    private javax.swing.JTextField txtAreaRoom;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtGiaDv;
    private javax.swing.JTextField txtGiaGiam;
    private javax.swing.JTextField txtGiaPhong;
    private javax.swing.JTextField txtGiamGiaDV;
    private javax.swing.JTextField txtKindOfRoom;
    private javax.swing.JTextField txtLocationRoom;
    private javax.swing.JTextField txtMaDv;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaPhong;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSoPhong;
    private javax.swing.JTextField txtSoPhongDV;
    private javax.swing.JLabel txtTenKS;
    private javax.swing.JTextField txtTenKhachHang;
    // End of variables declaration//GEN-END:variables

}
