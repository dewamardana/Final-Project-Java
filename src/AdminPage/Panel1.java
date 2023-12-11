
package AdminPage;

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
import javax.swing.JComboBox;
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
       txt_date.setEnabled(false);
       
        tabel_barang.setModel(table);
        table.addColumn("ID Barang");
        table.addColumn("Nama Barang");
        table.addColumn("Harga");
        table.addColumn("Stok");
        table.addColumn("Satuan");
        table.addColumn("Masuk Pada");
          
        tampilData();
    }
    
    
    private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = tabel_barang.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT * FROM `tb_barang` ";
        
        try{ 
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                String id= rslt.getString("id_barang");
                String nama = rslt.getString("nama");
                String harga = rslt.getString("harga");
                String stok = rslt.getString("stok");
                String satuan = rslt.getString("satuan");
                String dateIn = rslt.getString("tanggal_masuk");
                       dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {id,nama,harga,stok,satuan,dateIn};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
            
            //mengeset nilai yang ditampung agar muncul di table
            tabel_barang.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
          
    private void clear(){
        txt_kodebarang.setText("");
        txt_namabarang.setText("");
        txt_stok.setText("");
        txt_harga.setText("");
        txt_date.setText("");
        txt_cari.setText("");
        combo_satuan.setSelectedIndex(-1);
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
        int kode = Integer.parseInt(txt_kodebarang.getText());
        String nama = txt_namabarang.getText();
        int harga = Integer.parseInt(txt_harga.getText());
        int stok = Integer.parseInt(txt_stok.getText());
        String satuan = (String) combo_satuan.getSelectedItem();
        String date = txt_date.getText();
        String dateIn = convertDateFormat(date, "dd-MMMM-yyyy", "yyyy/MM/dd");        
        
        //panggil koneksi
        Connection connect = DatabaseKoneksi.createConnection();
        //query untuk memasukan data
        String query = "INSERT INTO `tb_barang` (id_barang, `nama`, `harga`, `stok`, `satuan`, `tanggal_masuk`,  `diedit_pada`) "
                     + "VALUES ('"+kode+"', '"+nama+"', '"+harga+"', '"+stok+"', '"+satuan+"', '"+dateIn+"', '"+dateIn+"')";
        
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
        int row = tabel_barang.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String cari = txt_cari.getText();
        
        String query = "SELECT * FROM `tb_barang` WHERE `id_barang`  LIKE '%"+cari+"%' OR `nama` LIKE '%"+cari+"%' ";
                
            try{
                Statement sttmnt = connect.createStatement();//membuat statement
                ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
           
                while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_barang");
                    String nama = rslt.getString("nama");
                    String harga = rslt.getString("harga");
                    String stok = rslt.getString("stok");
                    String satuan = rslt.getString("satuan");
                    String dateIn = rslt.getString("tanggal_masuk");
                           dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                    //masukan semua data kedalam array
                    String[] data = {id,nama,harga,stok,satuan,dateIn};
                    //menambahakan baris sesuai dengan data yang tersimpan diarray
                    table.addRow(data);
                }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_barang.setModel(table);
           
            }catch(Exception e){
                System.out.println(e);
            }
    } 

    public  boolean validateComboBox(JComboBox<?> comboBox, String fieldName) {
        boolean fik = true;
            if (comboBox.getSelectedIndex() >= 0) {
                System.out.println(fieldName + ": " + comboBox.getSelectedItem());
            } else {
                showMessage(fieldName + " harus dipilih.", "Error");
                comboBox.requestFocus();
                fik = false;
            }
        return fik;
    }

    private void validasi(){
        boolean save = true;
    
        boolean kode, nama = false, stok = false, harga = false, date = false, satuan = false;

        /* validateData(txt_kodebarang,"Kode Barang", 1);
        1. cek empty dan int
        2. cek empty textfield
        3. cek empty dan email
        */
        kode = validateData(txt_kodebarang,"Kode Barang", 1);
            if(kode){
                nama = validateData(txt_namabarang,"Nama Barang", 2);
            }else{
                save = false;
            }      
    
            if(nama){
                harga = validateData(txt_harga,"Harga", 1);
            }else{  
                save = false;
            }
    
            if(harga){
                stok = validateData(txt_stok,"Stok", 1);
            }else{
                save = false;
            }
    
            if(stok){
                satuan = validateComboBox(combo_satuan, "Satuan");
            }else{
                save = false;
            }
    
            if(satuan){
                date = validateData(txt_date,"Tanggal Masuk", 2);
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

        dateChooser = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_kodebarang = new JpanelKomponen.TextField();
        txt_namabarang = new JpanelKomponen.TextField();
        txt_harga = new JpanelKomponen.TextField();
        txt_stok = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        combo_satuan = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txt_date = new JpanelKomponen.TextField();
        button2 = new JpanelKomponen.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_barang = new TabelKomponen.TableDark();
        btn_cari = new JpanelKomponen.Button();
        txt_cari = new JpanelKomponen.TextField();
        btn_refresh = new JpanelKomponen.Button();

        dateChooser.setDateFormat("dd-MMMM-yyyy");
        dateChooser.setTextRefernce(txt_date);

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_kodebarang.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_kodebarang.setHint("Kode Barang");
        txt_kodebarang.setName(""); // NOI18N

        txt_namabarang.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_namabarang.setHint("Nama Barang");
        txt_namabarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namabarangActionPerformed(evt);
            }
        });

        txt_harga.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_harga.setHint("Harga");

        txt_stok.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_stok.setHint("Stok");
        txt_stok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stokActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tambah Produk");

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

        combo_satuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pack", "Pcs", "Kg", "Ml", "Liter" }));
        combo_satuan.setSelectedIndex(-1);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Satuan");

        txt_date.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_date.setHint("Tanggal Masuk");
        txt_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateActionPerformed(evt);
            }
        });

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
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_harga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_namabarang, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                                    .addComponent(txt_kodebarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel1))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(combo_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(txt_kodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(combo_satuan))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(50, 70, 460, 550);

        tabel_barang.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_barangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_barang);
        if (tabel_barang.getColumnModel().getColumnCount() > 0) {
            tabel_barang.getColumnModel().getColumn(0).setResizable(false);
            tabel_barang.getColumnModel().getColumn(1).setResizable(false);
            tabel_barang.getColumnModel().getColumn(3).setResizable(false);
        }

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(550, 120, 750, 470);

        btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });
        background1.add(btn_cari);
        btn_cari.setBounds(1022, 80, 40, 30);

        txt_cari.setHint("Cari.......");
        background1.add(txt_cari);
        txt_cari.setBounds(1070, 80, 240, 34);

        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        background1.add(btn_refresh);
        btn_refresh.setBounds(660, 600, 80, 32);

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

    private void txt_namabarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namabarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namabarangActionPerformed

    private void txt_stokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_stokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_stokActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        validasi();
        //tampilData();
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void tabel_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_barangMouseClicked
        int baris = tabel_barang.getSelectedRow();
        
        String kode = table.getValueAt(baris,0).toString();
        txt_kodebarang.setText(kode);
        
        String nama = table.getValueAt(baris, 1).toString();
        txt_namabarang.setText(nama);
        
        String harga = table.getValueAt(baris, 2).toString();
        txt_harga.setText(harga);
        
        String stok = table.getValueAt(baris, 3).toString();
        txt_stok.setText(stok);
        
        String satuan = table.getValueAt(baris, 4).toString();
        combo_satuan.setSelectedItem(satuan);
        
        String date = table.getValueAt(baris, 5).toString();
        txt_date.setText(date);
        
        
    }//GEN-LAST:event_tabel_barangMouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        cari();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
       tampilData();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
       dateChooser.showPopup();
    }//GEN-LAST:event_button2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_cari;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_refresh;
    private JpanelKomponen.Button btn_simpan;
    private JpanelKomponen.Button button2;
    private javax.swing.JComboBox<String> combo_satuan;
    private date.DateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private TabelKomponen.TableDark tabel_barang;
    private JpanelKomponen.TextField txt_cari;
    private JpanelKomponen.TextField txt_date;
    private JpanelKomponen.TextField txt_harga;
    private JpanelKomponen.TextField txt_kodebarang;
    private JpanelKomponen.TextField txt_namabarang;
    private JpanelKomponen.TextField txt_stok;
    // End of variables declaration//GEN-END:variables
}
