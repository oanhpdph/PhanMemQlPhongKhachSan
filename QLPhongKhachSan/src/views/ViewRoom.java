/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.PromotionR;
import model.Room;
import service.IPromotionR;
import service.PromotionRService;
import service.IRoom;
import service.RoomService;


/**
 *
 * @author TGDD
 */
public class ViewRoom extends javax.swing.JFrame {
    IRoom rsv;
    IPromotionR prl;
    /**
     * Creates new form viewPhong
     */
    public ViewRoom() {
        initComponents();
        this.rsv = new RoomService();
        this.prl = new PromotionRService();
        loadData();
        setLocation();
        loadDataPro();
    }
   public void setLocation(){
       this.setLocationRelativeTo(null);
   }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txt_rn = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_area = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_loc = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txt_code = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_pri = new javax.swing.JTextField();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_xoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_qlp = new javax.swing.JTable();
        btn_ht = new javax.swing.JButton();
        cb_stt = new javax.swing.JComboBox<>();
        cb_kor = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cb_pro = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 204, 204));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ PHÒNG");

        jLabel3.setText("Status");

        jLabel4.setText("KindofRoom");

        txt_loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_locActionPerformed(evt);
            }
        });

        jLabel8.setText("Code");

        jLabel9.setText("RoomNumber");

        jLabel10.setText("Area");

        jLabel11.setText("Location");

        jLabel12.setText("Price");

        btn_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Add.png"))); // NOI18N
        btn_them.setText("Thêm phòng");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        btn_sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Edit.png"))); // NOI18N
        btn_sua.setText("Sửa phòng");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        btn_xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Delete.png"))); // NOI18N
        btn_xoa.setText("Xóa phòng");
        btn_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xoaActionPerformed(evt);
            }
        });

        tb_qlp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Status", "KindofRoom", "Promotion", "Code", "roomNumber", "Area", "Location", "Price"
            }
        ));
        tb_qlp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_qlpMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_qlp);

        btn_ht.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Refresh.png"))); // NOI18N
        btn_ht.setText("Reset");
        btn_ht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_htActionPerformed(evt);
            }
        });

        cb_stt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sẵn Sàng", "Có Khách", "Chưa Dọn", "Đang Dọn", "Đang Sửa" }));

        cb_kor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Phòng Đơn", "Phòng Đôi", "Phòng Vip" }));

        jLabel5.setText("Code_Promotion");

        cb_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_proActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(btn_them)
                .addGap(122, 122, 122)
                .addComponent(btn_sua)
                .addGap(120, 120, 120)
                .addComponent(btn_xoa)
                .addGap(129, 129, 129)
                .addComponent(btn_ht)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(433, 433, 433)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cb_kor, javax.swing.GroupLayout.Alignment.LEADING, 0, 199, Short.MAX_VALUE)
                            .addComponent(cb_stt, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_code))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_area, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_rn, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                                    .addComponent(cb_pro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(86, 86, 86)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_pri, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_loc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(114, 114, 114))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_code, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txt_loc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel5)
                    .addComponent(cb_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cb_stt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txt_rn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(txt_pri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cb_kor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txt_area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ht)
                    .addComponent(btn_xoa)
                    .addComponent(btn_sua)
                    .addComponent(btn_them))
                .addGap(56, 56, 56)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void loadData(){
        DefaultTableModel dlm =(DefaultTableModel) tb_qlp.getModel();
        dlm.setRowCount(0);
        int stt=1;
        for(Room r : this.rsv.getAll()){
            Object[] rowData={
               stt,getStatus(r.getStatus()),getKindOfRoom(r.getKindOfRoom()),getNamePro(r.getIdPromotion()),r.getCode(),r.getRoomNumber(),r.getArea(),r.getLocation(),r.getPrice()
            };
             dlm.addRow(rowData);
             stt++;
        }
    }   
    public String getNamePro(String id){
        String code="";
        for(PromotionR pr: this.prl.getAll()){
            if(id.equals(pr.getId())){
                code=pr.getCode();
                break;
            }
        }
        return code;
    }

    public String getStatus(int stt){
        String name="";
        if(stt==0){
            name="Sẵn Sàng";
        }else if(stt==1){
            name="Có Khách";
        }else if(stt==2){
            name="Chưa dọn";
        }else if(stt==3){
            name="Đang dọn";
        }else if(stt==4){
            name="Đang sửa";
        }
        return name;
    }
      public String getKindOfRoom(int kor){
        String name="";
        if(kor==0){
            name="Phòng Đơn";
        }else if(kor==1){
            name="Phòng Đôi";
        }else if(kor==2){
            name="Phòng Vip";
        }
        return name;
    }
      public ArrayList<String> getcodePro(){
        ArrayList<String> listName= new ArrayList<>();
        String code="";
         for(PromotionR stt : this.prl.getAll()){
           code=stt.getCode();
           listName.add(code);
        }
         return listName;
    }
     public void loadDataPro(){
         DefaultComboBoxModel cbm =(DefaultComboBoxModel) cb_pro.getModel();
        for(String stt : this.getcodePro()){
           cbm.addElement(stt);
        }
    } 
      
     
    public Room getFromData(){
        int status=cb_stt.getSelectedIndex();
        int kor=cb_kor.getSelectedIndex();
        String code=txt_code.getText();
        String rmb=txt_rn.getText();
        String area=txt_area.getText();
        String lct=txt_loc.getText();
        String pri=txt_pri.getText();
        String codepro=cb_pro.getSelectedItem().toString();
        String idPro="";
        for(PromotionR pr: this.prl.getAll()){
            if(codepro.equals(pr.getCode())){
                idPro=pr.getId();
                break;
            }
        }
        
        Room r = new Room(status, kor,idPro, code, rmb, area, lct, pri);
        return r;
    }
    public String getIdFromData(){
          Room r1 =this.getFromData();
        String id="";
        for(Room r : this.rsv.getAll()){        
            if( r1.getCode().equals(r.getCode())
                       ){
                 id=r.getId();
                 break;
            }       
        }
        return id;
    }
    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
        // TODO add your handling code here:
        Room r=this.getFromData();
        this.rsv.insert(r);
        JOptionPane.showMessageDialog(this,"Thêm thành công!");
        loadData();
    }//GEN-LAST:event_btn_themActionPerformed

    private void tb_qlpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_qlpMouseClicked
        // TODO add your handling code here:
        int row= tb_qlp.getSelectedRow();
        cb_stt.setSelectedItem(this.tb_qlp.getValueAt(row, 1).toString());
        cb_kor.setSelectedItem(tb_qlp.getValueAt(row, 2).toString());
        txt_code.setText(this.tb_qlp.getValueAt(row, 4).toString());
        txt_rn.setText(this.tb_qlp.getValueAt(row, 5).toString());
        txt_loc.setText(this.tb_qlp.getValueAt(row, 6).toString());
        txt_area.setText(this.tb_qlp.getValueAt(row, 7).toString());
        txt_pri.setText(this.tb_qlp.getValueAt(row, 8).toString());
        cb_pro.setSelectedItem(this.tb_qlp.getValueAt(row, 3).toString());
    }//GEN-LAST:event_tb_qlpMouseClicked

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
        // TODO add your handling code here:
         int row=tb_qlp.getSelectedRow();
        if(row==-1){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần sửa!");
        }else{
        int choice= JOptionPane.showConfirmDialog(this, "Bạn có muốn sửa không?","message", JOptionPane.YES_NO_OPTION);
        if(choice==JOptionPane.YES_OPTION){
        Room r=this.getFromData();
        this.rsv.update(getIdFromData(), r);
        JOptionPane.showMessageDialog(this, "Sửa thành công!");
        loadData();
        }
        }
    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xoaActionPerformed
        // TODO add your handling code here:
        int row=tb_qlp.getSelectedRow();
        if(row==-1){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đối tượng cần xóa!");
        }else{
         int choice= JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa không?","message", JOptionPane.YES_NO_OPTION);
         if(choice==JOptionPane.YES_OPTION){
        Room r=this.getFromData();
        this.rsv.delete(getIdFromData());
        JOptionPane.showMessageDialog(this, "Xóa thành công!");
        loadData();
           }
        }
    }//GEN-LAST:event_btn_xoaActionPerformed

    private void btn_htActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_htActionPerformed
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
    }//GEN-LAST:event_btn_htActionPerformed

    private void txt_locActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_locActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_locActionPerformed

    private void cb_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_proActionPerformed

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
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewRoom().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_ht;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xoa;
    private javax.swing.JComboBox<String> cb_kor;
    private javax.swing.JComboBox<String> cb_pro;
    private javax.swing.JComboBox<String> cb_stt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_qlp;
    private javax.swing.JTextField txt_area;
    private javax.swing.JTextField txt_code;
    private javax.swing.JTextField txt_loc;
    private javax.swing.JTextField txt_pri;
    private javax.swing.JTextField txt_rn;
    // End of variables declaration//GEN-END:variables
}