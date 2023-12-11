
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
public class Panel2 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
    
    public Panel2() {
       initComponents();
       txt_kodebarang.setEnabled(false);
       txt_date.setEnabled(false);
       
        tabel_barang.setModel(table);
        table.addColumn("ID Barang");
        table.addColumn("Nama Barang");
        table.addColumn("Harga");
        table.addColumn("Stok");
        table.addColumn("Satuan");
        table.addColumn("Di Update");
          
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
                        String dateIn = rslt.getString("diedit_pada");
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

    private void editData(){
        int i = tabel_barang.getSelectedRow();
        String id = table.getValueAt(i, 0).toString();
        String nama = txt_namabarang.getText();
        int harga = Integer.parseInt(txt_harga.getText());
        int stok = Integer.parseInt(txt_stok.getText());
        String satuan = (String) combo_satuan.getSelectedItem();
        String date = txt_date.getText();
        String dateEdit = convertDateFormat(date, "dd-MMMM-yyyy", "yyyy/MM/dd");
        
        
        String query = "UPDATE `tb_barang` SET `nama` = '"+nama+"', `harga` = '"+harga+"', `stok` = '"+stok+"', `satuan`  = '"+satuan+"', `diedit_pada`  = '"+dateEdit+"' " + "WHERE `tb_barang`.`id_barang` = '"+id+"';";

        try{
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null , "Data Update");
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Di Update. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

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
                   
                    String id = rslt.getString("id_barang");
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
        */
        kode = validateData(txt_kodebarang,"Kode Barang", 1);
        System.err.println("Kode Barang : "+kode);
        if(kode){
            nama = validateData(txt_namabarang,"Nama Barang", 2);
            System.err.println("Nama Barang : "+nama);
        }else{
            save = false;
        }
    
        if(nama){
            harga = validateData(txt_harga,"Harga", 1);
            System.err.println("Harga Barang : "+stok);
        }else{
            save = false;
        }
    
        if(harga){
            stok = validateData(txt_stok,"Stok", 1);
            System.err.println("Stok Barang : "+stok);
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
            System.err.println("Stok Barang : "+date);
        }else{
            save = false;
        }
    
        if(!date){
            save = false;
        }
    
        if(save){
            editData();
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
        btn_edit = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        combo_satuan = new javax.swing.JComboBox<>();
        txt_date = new JpanelKomponen.TextField();
        button3 = new JpanelKomponen.Button();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_barang = new TabelKomponen.TableDark();
        txt_cari = new JpanelKomponen.TextField();
        button2 = new JpanelKomponen.Button();
        btn_cari = new JpanelKomponen.Button();

        dateChooser.setDateFormat("dd-MMMM-yyyy");
        dateChooser.setTextRefernce(txt_date);

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_kodebarang.setHint("Kode Barang");
        txt_kodebarang.setName(""); // NOI18N

        txt_namabarang.setHint("Nama Barang");
        txt_namabarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namabarangActionPerformed(evt);
            }
        });

        txt_harga.setHint("Harga");

        txt_stok.setHint("Stok");
        txt_stok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stokActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Edit Produk");

        btn_edit.setForeground(new java.awt.Color(255, 255, 255));
        btn_edit.setText("Edit");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        combo_satuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pack", "Pcs", "Kg", "Ml", "Liter" }));
        combo_satuan.setSelectedIndex(-1);

        txt_date.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_date.setHint("Tanggal Masuk");
        txt_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateActionPerformed(evt);
            }
        });

        button3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/calendar.png"))); // NOI18N
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Satuan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(combo_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txt_namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_kodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(120, 120, 120))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(txt_kodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(txt_namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(60, 100, 460, 550);

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
        ));
        tabel_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_barangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_barang);

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(580, 150, 700, 470);

        txt_cari.setHint("Cari.......");
        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });
        background1.add(txt_cari);
        txt_cari.setBounds(1080, 110, 200, 34);

        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setText("Refresh");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });
        background1.add(button2);
        button2.setBounds(670, 630, 70, 32);

        btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });
        background1.add(btn_cari);
        btn_cari.setBounds(1030, 110, 40, 30);

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

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        validasi();
        tampilData();;
    }//GEN-LAST:event_btn_editActionPerformed

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

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
       tampilData();
    }//GEN-LAST:event_button2ActionPerformed

    private void txt_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        dateChooser.showPopup();
    }//GEN-LAST:event_button3ActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        cari();
    }//GEN-LAST:event_btn_cariActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_cari;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_edit;
    private JpanelKomponen.Button button2;
    private JpanelKomponen.Button button3;
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
