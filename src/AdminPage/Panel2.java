
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
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Mardana
 */
public class Panel2 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
    int hargaRataRataAdd = 0;
    boolean entered = false;
    int hargaBelidb = 0, hargaRataRatadb = 0, stokdb = 0, margindb = 0, hargaJualdb = 0;
    
     
    public Panel2() {
       initComponents();
       txt_date.setEnabled(false);
       txt_hargaJual.setEnabled(false);
       txt_namaPemasok.setEnabled(false);
       txt_kodebarang.setEnabled(false);
       
        tabel_barang.setModel(table);
        table.addColumn("ID Barang");
        table.addColumn("Nama Barang");
        table.addColumn("ID Pemasok");
        table.addColumn("Harga Beli");
        table.addColumn("Margin");
        table.addColumn("Harga Jual");
        table.addColumn("Stok");
        table.addColumn("Satuan");
        table.addColumn("Masuk Pada");
        
        TableColumnModel columnModel = tabel_barang.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(50); // Ganti 50 dengan lebar yang diinginkan
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(50); // Ganti 50 dengan lebar yang diinginkan
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(40);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(40);
        columnModel.getColumn(7).setPreferredWidth(50);
        columnModel.getColumn(8).setPreferredWidth(80);
          
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
                   
                String idbarang = rslt.getString("id_barang");
                String nama = rslt.getString("nama");
                String idPemasok = rslt.getString("id_pemasok");
                String hargaBeli = rslt.getString("harga_beli");
                String margin = rslt.getString("margin");
                String hargaJual = rslt.getString("harga_jual");
                String stok = rslt.getString("stok");
                String satuan = rslt.getString("satuan");
                String dateIn = rslt.getString("tanggal_masuk");
                       dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {idbarang, nama, idPemasok, hargaBeli, margin, hargaJual, stok, satuan, dateIn};
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
        txt_kodePemasok.setText("");
        txt_namaPemasok.setText("");
        txt_hargaBeli.setText("");
        txt_margin.setText("");
        txt_hargaJual.setText("");
        txt_stok.setText("");
        txt_date.setText("");
        txt_cari.setText("");
        combo_satuan.setSelectedIndex(-1);
    } 
    
    

    private String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        // Parsing tanggal dengan format input
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

        // Menggunakan formatter untuk mengubah format tanggal
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(outputFormatter);
    }
    
    private void CekData(String idProduk) {
        // Query SQL untuk mendapatkan informasi produk berdasarkan kode produk
        
        boolean cekHargabeli = true, cekStok = true, cekMargin = true;
        int Harga = Integer.parseInt(txt_hargaBeli.getText());
        int stok = Integer.parseInt( txt_stok.getText());
        int margin = Integer.parseInt( txt_margin.getText());
        String query = "SELECT margin, harga_beli, harga_rataRata, harga_jual, stok FROM tb_barang WHERE id_barang = ?";
        
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, idProduk);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Memuat data ke dalam JTextField sesuai dengan hasil query
                   margindb = resultSet.getInt("margin");
                   hargaBelidb = resultSet.getInt("harga_beli");
                   hargaRataRatadb = resultSet.getInt("harga_rataRata");
                   stokdb = resultSet.getInt("stok");
                   hargaJualdb = resultSet.getInt("harga_jual");
                    
                } else {
                    // Kode produk tidak ditemukan
                    showMessage("Produk Tidak Ditemukan", "Error");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle eksepsi SQL sesuai dengan kebutuhan aplikasi Anda
        }
        
        if(hargaBelidb != Harga){
            cekHargabeli = false;
        }
        
        if(margin != margindb){
            cekMargin = false;
        }
        
        if(stok != stokdb){
            cekStok = false;
        }
        
        if (!cekHargabeli) {
            CekHarga(1, hargaRataRatadb);
            System.out.println("1");
        }else{
            if (!cekMargin){
                CekHarga(2, hargaRataRatadb);
                System.out.println("2");
            }else if (!cekStok) {
                CekHarga(3, hargaRataRatadb);
                System.out.println("3");
            }else{
                CekHarga(4, hargaRataRatadb);
                System.out.println("Tidak ada Perubahan Data");
                
            }
            
        }
        
    }
    
    private void CekHarga(int x, int hargaRataRata) {
        
        String idProduk = txt_kodebarang.getText();
        int Harga = Integer.parseInt(txt_hargaBeli.getText());
        int stok = Integer.parseInt( txt_stok.getText());
        int margin = Integer.parseInt( txt_margin.getText());
        
        int hargaRata = 0;
        int hargaJual = 0;
        int hargaBeli = 0;
        entered = true;
        
        hargaRataRataAdd = hargaRataRata;
        
        if ( x == 1){
           hargaRata = Harga / stok;
           hargaRataRataAdd = hargaRata;
           hargaJual = hargaRata + (hargaRata * margin/100);
           
           txt_stok.setText(String.valueOf(stok));
           txt_hargaBeli.setText(String.valueOf(Harga));
           txt_hargaJual.setText(String.valueOf(hargaJual));   
        }else if( x == 2){
            hargaJual = hargaRataRata + (hargaRataRata * margin/100);
            txt_hargaJual.setText(String.valueOf(hargaJual));
            
        }else if ( x == 3){
            hargaBeli = hargaRataRata * stok;
            txt_hargaBeli.setText(String.valueOf(hargaBeli));
            System.out.println("set harga beli");
            
            
        }else{
            System.out.println("Tidak ada Perubahan Data");
            txt_stok.setText(String.valueOf(stokdb));
            txt_hargaBeli.setText(String.valueOf(hargaBelidb));
            txt_hargaJual.setText(String.valueOf(hargaJualdb));
        }
             
    }
         
    private void editData(){
        int kodeBarang = Integer.parseInt(txt_kodebarang.getText());
        String namaBarang = txt_namabarang.getText();
        int kodePemasok = Integer.parseInt(txt_kodePemasok.getText());
        int hargaBeli = Integer.parseInt(txt_hargaBeli.getText());
        int margin = Integer.parseInt(txt_margin.getText());
        int hargaJual = Integer.parseInt(txt_hargaJual.getText());
        int stok = Integer.parseInt(txt_stok.getText());
        String satuan = (String) combo_satuan.getSelectedItem();
        String date = txt_date.getText();
        String dateIn = convertDateFormat(date, "dd-MMMM-yyyy", "yyyy/MM/dd");
        
        if (hargaRataRataAdd != 0) {
        
        String query = "UPDATE `tb_barang` SET `id_pemasok` = '"+kodePemasok+"', `nama` = '"+namaBarang+"', `harga_beli` = '"+hargaBeli+"', `harga_jual` = '"+hargaJual+"', `harga_rataRata` = '"+hargaRataRataAdd+"', `margin` = '"+margin+"', `stok` = '"+stok+"', `satuan`  = '"+satuan+"', `diedit_pada`  = '"+dateIn+"' " + "WHERE `tb_barang`.`id_barang` = '"+kodeBarang+"';";

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
        }else{
            showMessage(" Harga Beli Rata Rata Mencetak 0 ", "Error");
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
                   
                String idbarang = rslt.getString("id_barang");
                String nama = rslt.getString("nama");
                String idPemasok = rslt.getString("id_pemasok");
                String hargaBeli = rslt.getString("harga_beli");
                String margin = rslt.getString("margin");
                String hargaJual = rslt.getString("harga_jual");
                String stok = rslt.getString("stok");
                String satuan = rslt.getString("satuan");
                String dateIn = rslt.getString("tanggal_masuk");
                       dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {idbarang, nama, idPemasok, hargaBeli, margin, hargaJual, stok, satuan, dateIn};
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
    
        boolean kodeBarang = false, namaBarang = false, kodePemasok = false, namaPemasok = false, 
                stok = false, hargaBeli = false, hargaJual = false, 
                margin = false, date = false, satuan = false;

        /* validateData(txt_kodebarang,"Kode Barang", 1);
        1. cek empty dan int
        2. cek empty textfield
        3. cek empty dan email
        */
        kodeBarang = validateData(txt_kodebarang,"Kode Barang", 1);
            if(kodeBarang){
                namaBarang = validateData(txt_namabarang,"Nama Barang", 2);
            }else{
                save = false;
            }      
    
            if(namaBarang){
                kodePemasok = validateData(txt_kodePemasok,"Kode Pemasok", 1);
            }else{  
                save = false;
            }
    
            if(kodePemasok){
               namaPemasok = validateData(txt_namaPemasok,"Nama Pemasok", 2);
            }else{
                save = false;
            }
    
            if(namaPemasok){
                hargaBeli = validateData(txt_hargaBeli,"Harga Beli", 1);
            }else{
                save = false;
            }
    
            if(hargaBeli){
                stok = validateData(txt_stok,"Stok Barang", 1);
            }else{
                save = false;
            }
            
            if(stok){
                satuan = validateComboBox(combo_satuan, "Satuan");
            }else{
                save = false;
            } 
            
            if(satuan){
                stok = validateData(txt_margin,"Margin", 1);
            }else{
                save = false;
            }
            
            if(stok){
                hargaJual = validateData(txt_hargaJual,"Harga Jual", 1);
            }else{
                save = false;
            }
            
            if(hargaJual){
                date = validateData(txt_date,"Tanggal Masuk", 2);
                System.err.println("Stok Barang : "+date);
            }else{
                save = false;
            }
            
             
    
            if(!date){
                save = false;
            }
    
            if(save){
                if (!entered){
                    String idProduk = txt_kodebarang.getText();
                    CekData(idProduk);
                    editData(); 
                }else{
                    editData(); 
                }
                  
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
    
    private void loadBarang(String idProduk) {
        // Query SQL untuk mendapatkan informasi produk berdasarkan kode produk
        String query = "SELECT nama FROM tb_barang WHERE id_barang = ?";
        
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, idProduk);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Memuat data ke dalam JTextField sesuai dengan hasil query
                    txt_namabarang.setText(resultSet.getString("nama"));
                } else {
                    // Kode produk tidak ditemukan
                    txt_namabarang.setText("Produk Tidak Ditemukan");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle eksepsi SQL sesuai dengan kebutuhan aplikasi Anda
        }
    }
    
    
    private void loadPemasok(String idPemasok) {
        // Query SQL untuk mendapatkan informasi produk berdasarkan kode produk
        String query = "SELECT nama FROM tb_pemasok WHERE id_pemasok = ?";
        
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, idPemasok);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Memuat data ke dalam JTextField sesuai dengan hasil query
                    txt_namaPemasok.setText(resultSet.getString("nama"));
                } else {
                    // Kode produk tidak ditemukan
                    txt_namaPemasok.setText("Produk Tidak Ditemukan");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle eksepsi SQL sesuai dengan kebutuhan aplikasi Anda
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_kodebarang = new JpanelKomponen.TextField();
        txt_namabarang = new JpanelKomponen.TextField();
        txt_hargaJual = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        txt_date = new JpanelKomponen.TextField();
        button2 = new JpanelKomponen.Button();
        txt_kodePemasok = new JpanelKomponen.TextField();
        txt_hargaBeli = new JpanelKomponen.TextField();
        txt_namaPemasok = new JpanelKomponen.TextField();
        txt_margin = new JpanelKomponen.TextField();
        txt_stok = new JpanelKomponen.TextField();
        combo_satuan = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
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
        txt_kodebarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodebarangActionPerformed(evt);
            }
        });

        txt_namabarang.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_namabarang.setHint("Nama Barang");
        txt_namabarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namabarangActionPerformed(evt);
            }
        });

        txt_hargaJual.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_hargaJual.setHint("Harga Jual");
        txt_hargaJual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hargaJualActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Edit Produk");

        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setText("Edit");
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

        txt_kodePemasok.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_kodePemasok.setHint("Kode Pemasok");
        txt_kodePemasok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodePemasokActionPerformed(evt);
            }
        });

        txt_hargaBeli.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_hargaBeli.setHint("Harga Beli");

        txt_namaPemasok.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_namaPemasok.setHint("Nama Pemasok");

        txt_margin.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_margin.setHint("Margin");
        txt_margin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_marginActionPerformed(evt);
            }
        });

        txt_stok.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_stok.setHint("Stok");
        txt_stok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_stokActionPerformed(evt);
            }
        });

        combo_satuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pack", "Pcs", "Kg", "Ml", "Liter", "Dus" }));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Satuan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_namabarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_kodePemasok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_namaPemasok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_hargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(combo_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txt_kodebarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txt_hargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                                .addComponent(txt_margin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel1)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel1)
                .addGap(44, 44, 44)
                .addComponent(txt_kodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_namabarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_kodePemasok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_namaPemasok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_hargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(combo_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(txt_margin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_hargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(50, 70, 420, 700);

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
        jScrollPane1.setBounds(500, 120, 800, 470);

        btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });
        background1.add(btn_cari);
        btn_cari.setBounds(1010, 80, 40, 30);

        txt_cari.setHint("Cari.......");
        background1.add(txt_cari);
        txt_cari.setBounds(1060, 80, 240, 34);

        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        background1.add(btn_refresh);
        btn_refresh.setBounds(500, 600, 80, 32);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 1376, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
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
        
        String kodePemasok = table.getValueAt(baris, 2).toString();
        txt_kodePemasok.setText(kodePemasok);
        
        String namaPemasok = table.getValueAt(baris, 2).toString();
        loadPemasok(namaPemasok);
        
        String hargabeli = table.getValueAt(baris, 3).toString();
        txt_hargaBeli.setText(hargabeli);
        
        String margin = table.getValueAt(baris, 4).toString();
        txt_margin.setText(margin);
        
        String hargajual = table.getValueAt(baris, 5).toString();
        txt_hargaJual.setText(hargajual);
        
        String stok = table.getValueAt(baris, 6).toString();
        txt_stok.setText(stok);
        
        String satuan = table.getValueAt(baris, 7).toString();
        combo_satuan.setSelectedItem(satuan);
        
        String date = table.getValueAt(baris, 8).toString();
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

    private void txt_marginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_marginActionPerformed
    String idProduk = txt_kodebarang.getText();
        CekData(idProduk);
    }//GEN-LAST:event_txt_marginActionPerformed

    private void txt_kodebarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodebarangActionPerformed
        loadBarang(txt_kodebarang.getText());
    }//GEN-LAST:event_txt_kodebarangActionPerformed

    private void txt_kodePemasokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodePemasokActionPerformed
        loadPemasok(txt_kodePemasok.getText());
    }//GEN-LAST:event_txt_kodePemasokActionPerformed

    private void txt_hargaJualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hargaJualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hargaJualActionPerformed


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
    private JpanelKomponen.TextField txt_hargaBeli;
    private JpanelKomponen.TextField txt_hargaJual;
    private JpanelKomponen.TextField txt_kodePemasok;
    private JpanelKomponen.TextField txt_kodebarang;
    private JpanelKomponen.TextField txt_margin;
    private JpanelKomponen.TextField txt_namaPemasok;
    private JpanelKomponen.TextField txt_namabarang;
    private JpanelKomponen.TextField txt_stok;
    // End of variables declaration//GEN-END:variables
}
