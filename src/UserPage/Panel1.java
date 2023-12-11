package UserPage;

import Koneksi.DatabaseKoneksi;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mardana
 */
public class Panel1 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
            
    public Panel1() {
       initComponents();
       
       tabel_member.setModel(table);
        table.addColumn("ID Member");
        table.addColumn("Nama");
        table.addColumn("Alamat");
        table.addColumn("Jumlah Poin");
        table.addColumn("Tanggal Daftar");
          
        tampilData();
    }
    
        private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = tabel_member.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT * FROM `tb_member` ";
        
        try{
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_member");
                    String nama = rslt.getString("nama");
                    String alamat = rslt.getString("alamat");
                    String poin = rslt.getString("poin");
                    String dateIn = rslt.getString("tanggal_gabung");
                           dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
           
                //masukan semua data kedalam array
                String[] data = {id,nama,alamat,poin,dateIn};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_member.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
    
   
//    private void cek(){
//        rd_pria.setActionCommand("Pria");
//        rd_wanita.setActionCommand("Wanita");
//        ButtonModel selectedModel = jk.getSelection();
//        String gender = null;
//        
//            if (selectedModel != null) {
//                gender = selectedModel.getActionCommand();
//            }
//
//            if (gender != null) {
//                JOptionPane.showMessageDialog(null, "Jenis Kelamin: " + gender, "Info", JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null, "Pilih jenis kelamin terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
//            }
//    }
        
        
    private void clear(){
        txt_idMember.setText("");
        txt_nama.setText("");
        txt_alamat.setText("");
        txt_date.setText(""); 
        txt_cari.setText("");
    } 
    
    private  String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        // Parsing tanggal dengan format input
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

        // Menggunakan formatter untuk mengubah format tanggal
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(outputFormatter);
    }
    
    
  
    private void tambahData(){
        String kode = txt_idMember.getText();
        String nama = txt_nama.getText();
        String alamat = txt_alamat.getText();
        int poin = 0;
        String date = txt_date.getText();
        String dateIn = convertDateFormat(date, "dd-MMMM-yyyy", "yyyy/MM/dd");
        
         //query untuk memasukan data
        String query = "INSERT INTO `tb_member` (`id_member`, `nama`, `alamat`, `poin`,`tanggal_gabung`) "
                     + "VALUES ('"+kode+"', '"+nama+"', '"+alamat+"', '"+poin+"','"+dateIn+"')";
        
        try{
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }finally{
            tampilData();
            clear();
            
        }
    }
   
    private void cari(){
        int row = tabel_member.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String cari = txt_cari.getText();
        
        String query = "SELECT * FROM `tb_member` WHERE `id_member`  LIKE '%"+cari+"%' OR `nama` LIKE '%"+cari+"%' ";
                
       try{
           Statement sttmnt = connect.createStatement();//membuat statement
           ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
           
           while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_member");
                    String nama = rslt.getString("nama");
                    String alamat = rslt.getString("alamat");
                    String poin = rslt.getString("poin");
                    String dateIn = rslt.getString("tanggal_gabung");
                           dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
           
                //masukan semua data kedalam array
                String[] data = {id,nama,alamat,poin,dateIn};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_member.setModel(table);
           
        
    }catch(Exception e){
           System.out.println(e);
    }
    }
   
    private void validasi(){
    boolean save = true;
    
    boolean kode, nama = false, alamat = false, harga = false, date = false, satuan = false;

    /* validateData(txt_kodebarang,"Kode Barang", 1);
    1. cek empty dan int
    2. cek empty textfield
    3. cek empty dan email
    */
    kode = validateData(txt_idMember,"Id Member", 1);
    if(kode){
        nama = validateData(txt_nama,"Nama Member", 2);
    }else{
        save = false;
    }
    
    if(nama){
        alamat = validateData(txt_alamat,"Alamat Member", 2);
    }else{
        save = false;
    }
    
    if(alamat){
        date = validateData(txt_date,"Tanggal Bergabung", 2);
    }else{
        save = false;
    }
    
    if(!date){
        save = false;
    }
    
    if(save){
        tambahData();
    }
}

    private boolean validateData(JTextField textField, String fieldName , int pilihan) {
    int p = pilihan;
    String fieldValue = textField.getText();
    boolean fik = true;
     
    switch (p) {
        case 1 -> {
            if (!fieldValue.isEmpty()) {
                try {
                    int intValue = Integer.parseInt(fieldValue);
                    // Jika berhasil diubah ke tipe data integer
                    System.out.println(fieldName + ": " + intValue);
                } catch (NumberFormatException e) {
                        // Jika gagal diubah ke tipe data integer
                        showMessage(fieldName + " harus berupa angka.", "Error");
                        textField.requestFocus();
                        textField.selectAll();
                        fik = false;
                }
            } else {
                    // Jika teks field kosong
                    showMessage(fieldName + " tidak boleh kosong.", "Error");
                    textField.requestFocus();
                    fik = false;
                }
            break;
            }
        case 2 -> {
            if (!fieldValue.isEmpty()) {
                System.out.println(fieldName + ": " + fieldValue);
            } else {
                showMessage(fieldName + " tidak boleh kosong.", "Error");
                textField.requestFocus();
                fik = false;
                }
            break;
        }
        case 3 -> {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = EMAIL_REGEX.matcher(fieldValue);

            if (fieldValue.isEmpty()) {
                    showMessage(fieldName + " tidak boleh kosong.", "Error");
                    textField.requestFocus();
                    fik = false;
            } else if (!matcher.matches()) {
                    showMessage("Format " + fieldName + " tidak valid.", "Error");
                    textField.requestFocus();
                    textField.selectAll();
                    fik = false;
             }
            break;
        }
        default -> {
            System.out.println("Masukan Parameter yang valid");
            break;
        }
        
        
    }
       

    return fik;
}

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jk = new javax.swing.ButtonGroup();
        dateChooser1 = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_idMember = new JpanelKomponen.TextField();
        txt_nama = new JpanelKomponen.TextField();
        txt_date = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        txt_alamat = new JpanelKomponen.TextField();
        button2 = new JpanelKomponen.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_member = new TabelKomponen.TableDark();
        txt_cari = new JpanelKomponen.TextField();
        btn_refresh = new JpanelKomponen.Button();
        btn_cari = new JpanelKomponen.Button();

        dateChooser1.setDateFormat("dd-MMMM-yyyy");
        dateChooser1.setTextRefernce(txt_date);

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_idMember.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_idMember.setHint("Id Member");
        txt_idMember.setName(""); // NOI18N
        txt_idMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idMemberActionPerformed(evt);
            }
        });

        txt_nama.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_nama.setHint("Nama");
        txt_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaActionPerformed(evt);
            }
        });

        txt_date.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_date.setHint("Atur Tanggal");
        txt_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tambah Member");

        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setText("Simpan");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setText("clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        txt_alamat.setHint("Alamat");

        button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/calendar.png"))); // NOI18N
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(33, 33, 33))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_idMember, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(txt_idMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(40, 80, 400, 470);

        tabel_member.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabel_member.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        tabel_member.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_memberMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_member);

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(490, 130, 670, 470);

        txt_cari.setHint("Cari.......");
        background1.add(txt_cari);
        txt_cari.setBounds(930, 90, 240, 34);

        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        background1.add(btn_refresh);
        btn_refresh.setBounds(490, 610, 80, 32);

        btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });
        background1.add(btn_cari);
        btn_cari.setBounds(880, 90, 40, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 1376, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        validasi();
        tampilData();
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void tabel_memberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_memberMouseClicked
        int baris = tabel_member.getSelectedRow();
        
        String kode = table.getValueAt(baris,0).toString();
        txt_idMember.setText(kode);
        
        String nama = table.getValueAt(baris, 1).toString();
        txt_nama.setText(nama);
        
        String alamat = table.getValueAt(baris, 2).toString();
        txt_alamat.setText(alamat);
        
        String date = table.getValueAt(baris, 4).toString();
        txt_date.setText(date);
        
        
    }//GEN-LAST:event_tabel_memberMouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
       tampilData();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_idMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idMemberActionPerformed

    private void txt_namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namaActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        dateChooser1.showPopup();
    }//GEN-LAST:event_button2ActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        cari();
    }//GEN-LAST:event_btn_cariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_cari;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_refresh;
    private JpanelKomponen.Button btn_simpan;
    private JpanelKomponen.Button button2;
    private date.DateChooser dateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jk;
    private TabelKomponen.TableDark tabel_member;
    private JpanelKomponen.TextField txt_alamat;
    private JpanelKomponen.TextField txt_cari;
    private JpanelKomponen.TextField txt_date;
    private JpanelKomponen.TextField txt_idMember;
    private JpanelKomponen.TextField txt_nama;
    // End of variables declaration//GEN-END:variables
}
