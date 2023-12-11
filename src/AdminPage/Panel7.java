package AdminPage;

import UserPage.*;
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
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Mardana
 */
public class Panel7 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
       
    public Panel7() {
       initComponents();
       txt_hargaAwal.setEnabled(false);
       txt_namaBarang.setEnabled(false);
       txt_hargaDiskon.setEnabled(false);
       
        tabel_barang.setModel(table);
        table.addColumn("ID");
        table.addColumn("ID Barang");
        table.addColumn("Nama Barang");
        table.addColumn("Harga Awal");
        table.addColumn("Diskon");
        table.addColumn("Harga Dison");
        table.addColumn("Berlaku");
        table.addColumn("Sampai");
        
        TableColumnModel columnModel = tabel_barang.getColumnModel();

        // Mengatur lebar kolom berdasarkan indeks kolom
        columnModel.getColumn(0).setPreferredWidth(50); // ID Diskon
        columnModel.getColumn(1).setPreferredWidth(80); // ID Barang
        columnModel.getColumn(2).setPreferredWidth(150); // Nama Barang
        columnModel.getColumn(3).setPreferredWidth(100); // Harga Awal
        columnModel.getColumn(4).setPreferredWidth(80); // Diskon
        columnModel.getColumn(5).setPreferredWidth(100); // Harga Diskon
        columnModel.getColumn(6).setPreferredWidth(120); // Berlaku
        columnModel.getColumn(7).setPreferredWidth(120); // Sampai
          
        tampilData();
    }
    
    private  String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        // Parsing tanggal dengan format input
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

        // Menggunakan formatter untuk mengubah format tanggal
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(outputFormatter);
    }
    
    private void validasi(){
        boolean save = true;
    
        boolean kode = false, nama = false, hargaAwal = false, besarDiskon = false, 
                hargaDiskon = false, dateS = false, dateE = false;

    /* validateData(txt_kodebarang,"Kode Barang", 1);
    1. cek empty dan int
    2. cek empty textfield
    3. cek empty dan email
    */
    kode = validateData(txt_kodeBarang,"Kode Barang", 1);
    if(kode){
        nama = validateData(txt_namaBarang,"Nama Nama", 2);
    }else{
        save = false;
    }
    
    if(nama){
        hargaAwal = validateData(txt_hargaAwal,"Harga Awal", 1);
    }else{
        save = false;
    }
    
    if(hargaAwal){
        besarDiskon = validateData(txt_diskon,"Besaran Diskon", 1);
    }else{
        save = false;
    }
    
    if(besarDiskon){
        hargaDiskon = validateData(txt_hargaDiskon,"Harga Diskon", 1);
    }else{
        save = false;
    }
    
    if(hargaDiskon){
        dateS = validateData(txt_dateMulai,"Mulai Berlaku", 2);
    }else{
        save = false;
    }
    
    if(dateS){
        dateE = validateData(txt_dateAkhir,"Akhir Berlaku", 2);
    }else{
        save = false;
    }
    
    
    if(!dateE){
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
     
    private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = tabel_barang.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT tb_diskon.*, tb_barang.nama FROM tb_diskon INNER JOIN tb_barang ON tb_diskon.id_barang = tb_barang.id_barang";
        
        try{
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String idDiskon= rslt.getString("id_diskon");
                    String idBarang = rslt.getString("id_barang");
                    String nama = rslt.getString("nama");
                    String hargaAwal = rslt.getString("harga_awal");
                    String jmlDiskon = rslt.getString("jumlah_diskon");
                    String hargaDiskon = rslt.getString("harga_diskon");
                    String berlaku = rslt.getString("berlaku");
                           berlaku = convertDateFormat(berlaku, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    String sampai = rslt.getString("sampai");
                           sampai = convertDateFormat(sampai, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {idDiskon,idBarang, nama, hargaAwal, jmlDiskon, hargaDiskon, berlaku, sampai};
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
        txt_kodeBarang.setText("");
        txt_namaBarang.setText("");
        txt_hargaAwal.setText("");
        txt_diskon.setText("");
        txt_hargaDiskon.setText("");
        txt_dateMulai.setText("");
        txt_dateAkhir.setText(""); 
        
    } 
      
    private void tambahData(){
        int kode = Integer.parseInt(txt_kodeBarang.getText());
        int hargaAwal = Integer.parseInt(txt_hargaAwal.getText());
        int jmlDiskon = Integer.parseInt(txt_diskon.getText());
        int hargaDiskon = Integer.parseInt(txt_hargaDiskon.getText());
        String dateAwal = txt_dateMulai.getText();
               dateAwal = convertDateFormat(dateAwal, "dd-MMMM-yyyy", "yyyy/MM/dd");
        String dateAkhir = txt_dateAkhir.getText();
               dateAkhir = convertDateFormat(dateAkhir, "dd-MMMM-yyyy", "yyyy/MM/dd");
        
        //query untuk memasukan data
        String query = "INSERT INTO `tb_diskon` (`id_barang`, `harga_awal`, `jumlah_diskon`, `harga_diskon`, `berlaku`,`sampai`) "
                     + "VALUES ('"+kode+"', '"+hargaAwal+"', '"+jmlDiskon+"', '"+hargaDiskon+"', '"+dateAwal+"','"+dateAkhir+"')";
        
        try{
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            showMessage("Data Gagal Disimpan: " + e.getMessage(), "Error");
            
        }finally{
            tampilData();
            clear();
            
        }
    } 
    
    private void loadBarang(String productCode) {
    
        // Query SQL untuk mendapatkan informasi produk berdasarkan kode produk
        String query = "SELECT nama, harga FROM tb_barang WHERE id_barang = ?";
        
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Memuat data ke dalam JTextField sesuai dengan hasil query
                    txt_namaBarang.setText(resultSet.getString("nama"));
                    txt_hargaAwal.setText(resultSet.getString("harga"));
                    
                    
                } else {
                    // Kode produk tidak ditemukan
                    txt_namaBarang.setText("Produk Tidak Ditemukan");
                    txt_hargaAwal.setText("-");
                    
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle eksepsi SQL sesuai dengan kebutuhan aplikasi Anda
        }
    }
    
    private void cari(){
        //untuk mengahapus baris setelah input
        int row = tabel_barang.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        String cari = txt_cari.getText();
        String query = "SELECT tb_diskon.*, tb_barang.nama " +
               "FROM tb_diskon " +
               "INNER JOIN tb_barang ON tb_diskon.id_barang = tb_barang.id_barang " +
               "WHERE tb_diskon.id_barang LIKE '%" + cari + "%' OR tb_barang.nama LIKE '%" + cari + "%'";

        
        
        try{
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String idDiskon= rslt.getString("id_diskon");
                    String idBarang = rslt.getString("id_barang");
                    String nama = rslt.getString("nama");
                    String hargaAwal = rslt.getString("harga_awal");
                    String jmlDiskon = rslt.getString("jumlah_diskon");
                    String hargaDiskon = rslt.getString("harga_diskon");
                    String berlaku = rslt.getString("berlaku");
                           berlaku = convertDateFormat(berlaku, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    String sampai = rslt.getString("sampai");
                           sampai = convertDateFormat(sampai, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {idDiskon,idBarang, nama, hargaAwal, jmlDiskon, hargaDiskon, berlaku, sampai};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_barang.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
  
    private void hitungDiskon() {
    try {
        int hargaAwal = Integer.parseInt(txt_hargaAwal.getText());
        int diskon = Integer.parseInt(txt_diskon.getText());

        // Validasi input
        if (hargaAwal < 0 || diskon < 0 || diskon > 100) {
            throw new IllegalArgumentException("Input tidak valid. Pastikan harga dan diskon adalah angka positif.");
        }

        int hargaDiskon = hitungHargaDiskon(hargaAwal, diskon);
        txt_hargaDiskon.setText(String.valueOf(hargaDiskon));
        
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private int hitungHargaDiskon(int hargaAwal, int diskon) {
    return hargaAwal - (hargaAwal * diskon / 100);
}

   
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser2 = new date.DateChooser();
        dateChooser1 = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_kodeBarang = new JpanelKomponen.TextField();
        txt_hargaAwal = new JpanelKomponen.TextField();
        txt_dateMulai = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new JpanelKomponen.Button();
        txt_hargaDiskon = new JpanelKomponen.TextField();
        txt_dateAkhir = new JpanelKomponen.TextField();
        txt_diskon = new JpanelKomponen.TextField();
        btn_clear = new JpanelKomponen.Button();
        txt_namaBarang = new JpanelKomponen.TextField();
        btn_dateS = new JpanelKomponen.Button();
        btn_dateE = new JpanelKomponen.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_barang = new TabelKomponen.TableDark();
        txt_cari = new JpanelKomponen.TextField();
        btn_refresh = new JpanelKomponen.Button();
        btn_cari = new JpanelKomponen.Button();

        dateChooser2.setDateFormat("dd-MMMM-yyyy");
        dateChooser2.setTextRefernce(txt_dateAkhir);

        dateChooser1.setDateFormat("dd-MMMM-yyyy");
        dateChooser1.setTextRefernce(txt_dateMulai);

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_kodeBarang.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_kodeBarang.setHint("Kode Barang");
        txt_kodeBarang.setName(""); // NOI18N
        txt_kodeBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodeBarangActionPerformed(evt);
            }
        });

        txt_hargaAwal.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_hargaAwal.setHint("Harga Awal");
        txt_hargaAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hargaAwalActionPerformed(evt);
            }
        });

        txt_dateMulai.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_dateMulai.setHint("Mulai Berlaku");
        txt_dateMulai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateMulaiActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buat Diskon");

        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setText("Simpan");
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        txt_hargaDiskon.setHint("Harga Diskon");

        txt_dateAkhir.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_dateAkhir.setHint("Berlaku Sampai");
        txt_dateAkhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateAkhirActionPerformed(evt);
            }
        });

        txt_diskon.setHint("Besaran Diskon %");
        txt_diskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_diskonActionPerformed(evt);
            }
        });

        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setText("clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        txt_namaBarang.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_namaBarang.setHint("Nama Barang");
        txt_namaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaBarangActionPerformed(evt);
            }
        });

        btn_dateS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/calendar.png"))); // NOI18N
        btn_dateS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dateSActionPerformed(evt);
            }
        });

        btn_dateE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/calendar.png"))); // NOI18N
        btn_dateE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dateEActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txt_namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(jLabel1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(txt_kodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(txt_hargaAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btn_dateE, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_dateAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btn_dateS, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_dateMulai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(txt_hargaDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(3, 3, 3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txt_kodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_hargaAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_diskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_hargaDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_dateS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dateMulai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_dateE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_dateAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(30, 120, 360, 560);

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
        tabel_barang.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        tabel_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_barangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_barang);
        if (tabel_barang.getColumnModel().getColumnCount() > 0) {
            tabel_barang.getColumnModel().getColumn(0).setHeaderValue("Title 1");
            tabel_barang.getColumnModel().getColumn(1).setHeaderValue("Title 2");
            tabel_barang.getColumnModel().getColumn(2).setHeaderValue("Title 3");
            tabel_barang.getColumnModel().getColumn(3).setHeaderValue("Title 4");
        }

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(420, 130, 840, 470);

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

    private void txt_dateMulaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateMulaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateMulaiActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        validasi();
        tampilData();
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void tabel_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_barangMouseClicked
        int baris = tabel_barang.getSelectedRow();
                
        String kode = table.getValueAt(baris,1).toString();
        txt_kodeBarang.setText(kode);
        
        String nama = table.getValueAt(baris, 2).toString();
        txt_namaBarang.setText(nama);
        
        String hargaA = table.getValueAt(baris, 3).toString();
        txt_hargaAwal.setText(hargaA);
        
        String jmlDiskon = table.getValueAt(baris, 4).toString();
        txt_diskon.setText(jmlDiskon);
        
        String hargaD = table.getValueAt(baris, 5).toString();
        txt_hargaDiskon.setText(hargaD);
        
        String dateS = table.getValueAt(baris, 6).toString();
        txt_dateMulai.setText(dateS);
        
        String dateE = table.getValueAt(baris, 7).toString();
        txt_dateAkhir.setText(dateE);
        
        
    }//GEN-LAST:event_tabel_barangMouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
       tampilData();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_kodeBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodeBarangActionPerformed
        loadBarang(txt_kodeBarang.getText());
    }//GEN-LAST:event_txt_kodeBarangActionPerformed

    private void txt_hargaAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hargaAwalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hargaAwalActionPerformed

    private void txt_dateAkhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateAkhirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateAkhirActionPerformed

    private void txt_diskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_diskonActionPerformed
       hitungDiskon();
    }//GEN-LAST:event_txt_diskonActionPerformed

    private void txt_namaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namaBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namaBarangActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        cari();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void btn_dateSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dateSActionPerformed
        dateChooser1.showPopup();
    }//GEN-LAST:event_btn_dateSActionPerformed

    private void btn_dateEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dateEActionPerformed
        dateChooser2.showPopup();
    }//GEN-LAST:event_btn_dateEActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_cari;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_dateE;
    private JpanelKomponen.Button btn_dateS;
    private JpanelKomponen.Button btn_refresh;
    private JpanelKomponen.Button btn_simpan;
    private date.DateChooser dateChooser1;
    private date.DateChooser dateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private TabelKomponen.TableDark tabel_barang;
    private JpanelKomponen.TextField txt_cari;
    private JpanelKomponen.TextField txt_dateAkhir;
    private JpanelKomponen.TextField txt_dateMulai;
    private JpanelKomponen.TextField txt_diskon;
    private JpanelKomponen.TextField txt_hargaAwal;
    private JpanelKomponen.TextField txt_hargaDiskon;
    private JpanelKomponen.TextField txt_kodeBarang;
    private JpanelKomponen.TextField txt_namaBarang;
    // End of variables declaration//GEN-END:variables
}
