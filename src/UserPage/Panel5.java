
package UserPage;


import Koneksi.DatabaseKoneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Mardana
 */
public class Panel5 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
            
    public Panel5() {
       initComponents();
       
        tampilData(0,"none");
        cekKeuntungan();
    }
    
    private  String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        // Parsing tanggal dengan format input
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

        // Menggunakan formatter untuk mengubah format tanggal
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(outputFormatter);
    }
        
    private void tampilData(int x, String b){
        
        table.setColumnCount(0);
            
        tabel_dataTransaksi.setModel(table);
        table.addColumn("ID ");
        table.addColumn("Cart ID");
        table.addColumn("Member ID");
        table.addColumn("Subtotal");
        table.addColumn("Modal");
        table.addColumn("Jumlah Uang");
        table.addColumn("Jumlah Kembalian");
        table.addColumn("Tanggal Transaksi");
        
        // Mendapatkan model kolom dari tabel
        TableColumnModel columnModel = tabel_dataTransaksi.getColumnModel();

        // Mengatur lebar kolom sesuai kebutuhan
        columnModel.getColumn(0).setPreferredWidth(50); // Ganti 50 dengan lebar yang diinginkan
        columnModel.getColumn(1).setPreferredWidth(100); // Ganti 100 dengan lebar yang diinginkan
        columnModel.getColumn(2).setPreferredWidth(80); // Ganti 80 dengan lebar yang diinginkan
        columnModel.getColumn(3).setPreferredWidth(120);
        columnModel.getColumn(4).setPreferredWidth(120);// Ganti 120 dengan lebar yang diinginkan
        columnModel.getColumn(5).setPreferredWidth(100); // Ganti 100 dengan lebar yang diinginkan
        columnModel.getColumn(6).setPreferredWidth(120); // Ganti 120 dengan lebar yang diinginkan
        columnModel.getColumn(7).setPreferredWidth(150); 
        
        //untuk mengahapus baris setelah input
        int row = tabel_dataTransaksi.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        String query = "";
        if(x != 1){
            query = "SELECT * FROM `tb_transaksi` ";
        }else{
             query = b;
        }
        try{Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String idTransaksi = rslt.getString("id_transaksi");
                    String idCart = rslt.getString("id_cart");
                    String idMember = rslt.getString("id_member");
                    String total = rslt.getString("total_harga");
                    String modal = rslt.getString("total_modal");
                    String bayar = rslt.getString("total_bayar");
                    String kembali = rslt.getString("jumlah_kembalian");
                    String date = rslt.getString("tanggal_transaksi");
                           date = convertDateFormat(date, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                   
           
                //masukan semua data kedalam array
                String[] data = {idTransaksi,idCart,idMember,total,modal,bayar,kembali,date};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_dataTransaksi.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
  
    private void clear(){
        txt_tanggal.setText("");
        combo_bulan.setSelectedIndex(-1);
        txt_tahun.setText("");
    }
    
        
private void cariData() {
    // Ambil nilai dari komponen UI
    String tanggal = txt_tanggal.getText().trim();
    
    String bulan = "";
    String angkaBulan = "";
    int index = combo_bulan.getSelectedIndex();
        if(index > -1){
            bulan = (String) combo_bulan.getSelectedItem();
            angkaBulan = convertBulanToAngka(bulan);
        }else{
            angkaBulan = "00";
        }
  
  
    if (!angkaBulan.equals("00")) {
        bulan = convertBulanToAngka( bulan);
    }
    
    String tahun = txt_tahun.getText().trim();

    // Validasi agar tidak ada input yang kosong
    if (tanggal.isEmpty() && index < 0 && tahun.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Harap isi setidaknya Bulan atau Tahun", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    if (!tanggal.isEmpty() && index < 0 && tahun.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Pencarian data tidak bisa hanya berdasarkan tanggal", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Buat string untuk menyimpan kondisi pencarian
    String kondisiPencarian = "";

    // Tentukan kondisi pencarian berdasarkan input yang ada
    if (!tanggal.isEmpty()) {
        kondisiPencarian += "DAY(tanggal_transaksi) = " + tanggal;
    }

    if (!bulan.isEmpty()) {
        if (!kondisiPencarian.isEmpty()) {
            kondisiPencarian += " AND ";
        }
        kondisiPencarian += "MONTH(tanggal_transaksi) = " + bulan;
    }

    if (!tahun.isEmpty()) {
        if (!kondisiPencarian.isEmpty()) {
            kondisiPencarian += " AND ";
        }
        kondisiPencarian += "YEAR(tanggal_transaksi) = " + tahun;
    }

    // Buat query SQL sesuai dengan kondisi pencarian yang sudah ditentukan
    String query = "SELECT * FROM tb_transaksi";

    if (!kondisiPencarian.isEmpty()) {
        query += " WHERE " + kondisiPencarian;
        tampilData(1, query);
    }

    
}


    private void cekKeuntungan(){
        
        int jumlahTransaksi = 0, hargaPokok = 0, hasilPenjualan = 0, keuntungan = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
                jumlahTransaksi++;
                hargaPokok += Integer.valueOf(table.getValueAt(i, 4).toString());
                hasilPenjualan += Integer.valueOf(table.getValueAt(i, 3).toString()); 
            }
        keuntungan = hasilPenjualan - hargaPokok;
        lb_jumlahTransaksi.setText(String.valueOf(jumlahTransaksi));
        lb_hargaPokok.setText(String.valueOf(hargaPokok));
        lb_hasilPenjualan.setText(String.valueOf(hasilPenjualan));
        lb_keuntungan.setText(String.valueOf(keuntungan));
    }
    
private String convertBulanToAngka(String namaBulan) {
    switch (namaBulan.toLowerCase()) {
            case "januari":
                return "01";
            case "februari":
                return "02";
            case "maret":
                return "03";
            case "april":
                return "04";
            case "mei":
                return "05";
            case "juni":
                return "06";
            case "juli":
                return "07";
            case "agustus":
                return "08";
            case "september":
                return "09";
            case "oktober":
                return "10";
            case "november":
                return "11";
            case "desember":
                return "12";
            default:
                return "00";
    }
}

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jk = new javax.swing.ButtonGroup();
        dateChooser1 = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_tanggal = new JpanelKomponen.TextField();
        btn_cari = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        txt_tahun = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        combo_bulan = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_dataTransaksi = new TabelKomponen.TableDark();
        btn_refresh = new JpanelKomponen.Button();
        panelTransparent2 = new Komponen.PanelTransparent();
        jLabel4 = new javax.swing.JLabel();
        lb_jumlahTransaksi = new javax.swing.JLabel();
        lb_hasilPenjualan1 = new javax.swing.JLabel();
        panelTransparent4 = new Komponen.PanelTransparent();
        jLabel8 = new javax.swing.JLabel();
        lb_keuntungan = new javax.swing.JLabel();
        lb_hasilPenjualan4 = new javax.swing.JLabel();
        panelTransparent5 = new Komponen.PanelTransparent();
        jLabel10 = new javax.swing.JLabel();
        lb_hasilPenjualan = new javax.swing.JLabel();
        lb_hasilPenjualan2 = new javax.swing.JLabel();
        panelTransparent6 = new Komponen.PanelTransparent();
        jLabel12 = new javax.swing.JLabel();
        lb_hargaPokok = new javax.swing.JLabel();
        lb_hasilPenjualan3 = new javax.swing.JLabel();

        dateChooser1.setDateFormat("dd-MMMM-yyyy");

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_tanggal.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_tanggal.setHint("Tanggal Transaksi");
        txt_tanggal.setName(""); // NOI18N
        txt_tanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tanggalActionPerformed(evt);
            }
        });

        btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        btn_cari.setText("Cari");
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setText("clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        txt_tahun.setHint("Tahun Transaksi");

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cari Transaksi");

        combo_bulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 42, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_tahun, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(combo_bulan, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_tahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(60, 60, 330, 470);

        tabel_dataTransaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_dataTransaksi.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        tabel_dataTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_dataTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_dataTransaksi);

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(430, 50, 820, 250);

        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        background1.add(btn_refresh);
        btn_refresh.setBounds(430, 310, 80, 32);

        panelTransparent2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel4.setFont(new java.awt.Font("Prompt", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Jumlah Transaksi");

        lb_jumlahTransaksi.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_jumlahTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        lb_jumlahTransaksi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_jumlahTransaksi.setText("0");

        lb_hasilPenjualan1.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_hasilPenjualan1.setForeground(new java.awt.Color(255, 255, 255));
        lb_hasilPenjualan1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_hasilPenjualan1.setText("Rp.");

        javax.swing.GroupLayout panelTransparent2Layout = new javax.swing.GroupLayout(panelTransparent2);
        panelTransparent2.setLayout(panelTransparent2Layout);
        panelTransparent2Layout.setHorizontalGroup(
            panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
            .addGroup(panelTransparent2Layout.createSequentialGroup()
                .addComponent(lb_hasilPenjualan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lb_jumlahTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTransparent2Layout.setVerticalGroup(
            panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_jumlahTransaksi)
                    .addComponent(lb_hasilPenjualan1))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        background1.add(panelTransparent2);
        panelTransparent2.setBounds(430, 360, 400, 120);

        panelTransparent4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel8.setFont(new java.awt.Font("Prompt", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Keuntungan");

        lb_keuntungan.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_keuntungan.setForeground(new java.awt.Color(255, 255, 255));
        lb_keuntungan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_keuntungan.setText("0");

        lb_hasilPenjualan4.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_hasilPenjualan4.setForeground(new java.awt.Color(255, 255, 255));
        lb_hasilPenjualan4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_hasilPenjualan4.setText("Rp.");

        javax.swing.GroupLayout panelTransparent4Layout = new javax.swing.GroupLayout(panelTransparent4);
        panelTransparent4.setLayout(panelTransparent4Layout);
        panelTransparent4Layout.setHorizontalGroup(
            panelTransparent4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
            .addGroup(panelTransparent4Layout.createSequentialGroup()
                .addComponent(lb_hasilPenjualan4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lb_keuntungan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTransparent4Layout.setVerticalGroup(
            panelTransparent4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransparent4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_keuntungan)
                    .addComponent(lb_hasilPenjualan4))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        background1.add(panelTransparent4);
        panelTransparent4.setBounds(840, 500, 400, 120);

        panelTransparent5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel10.setFont(new java.awt.Font("Prompt", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Hasil Penjualan");

        lb_hasilPenjualan.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_hasilPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        lb_hasilPenjualan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_hasilPenjualan.setText("0");

        lb_hasilPenjualan2.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_hasilPenjualan2.setForeground(new java.awt.Color(255, 255, 255));
        lb_hasilPenjualan2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_hasilPenjualan2.setText("Rp.");

        javax.swing.GroupLayout panelTransparent5Layout = new javax.swing.GroupLayout(panelTransparent5);
        panelTransparent5.setLayout(panelTransparent5Layout);
        panelTransparent5Layout.setHorizontalGroup(
            panelTransparent5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
            .addGroup(panelTransparent5Layout.createSequentialGroup()
                .addComponent(lb_hasilPenjualan2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lb_hasilPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTransparent5Layout.setVerticalGroup(
            panelTransparent5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransparent5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_hasilPenjualan)
                    .addComponent(lb_hasilPenjualan2))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        background1.add(panelTransparent5);
        panelTransparent5.setBounds(430, 500, 400, 120);

        panelTransparent6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel12.setFont(new java.awt.Font("Prompt", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Harga Pokok");

        lb_hargaPokok.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_hargaPokok.setForeground(new java.awt.Color(255, 255, 255));
        lb_hargaPokok.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lb_hargaPokok.setText("0");

        lb_hasilPenjualan3.setFont(new java.awt.Font("Prompt", 1, 24)); // NOI18N
        lb_hasilPenjualan3.setForeground(new java.awt.Color(255, 255, 255));
        lb_hasilPenjualan3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_hasilPenjualan3.setText("Rp.");

        javax.swing.GroupLayout panelTransparent6Layout = new javax.swing.GroupLayout(panelTransparent6);
        panelTransparent6.setLayout(panelTransparent6Layout);
        panelTransparent6Layout.setHorizontalGroup(
            panelTransparent6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTransparent6Layout.createSequentialGroup()
                .addComponent(lb_hasilPenjualan3)
                .addGap(0, 0, 0)
                .addComponent(lb_hargaPokok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTransparent6Layout.setVerticalGroup(
            panelTransparent6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addGroup(panelTransparent6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_hargaPokok)
                    .addComponent(lb_hasilPenjualan3))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        background1.add(panelTransparent6);
        panelTransparent6.setBounds(840, 360, 400, 120);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 1376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        cariData();
        cekKeuntungan();
    }//GEN-LAST:event_btn_cariActionPerformed

    private void tabel_dataTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_dataTransaksiMouseClicked
                
    }//GEN-LAST:event_tabel_dataTransaksiMouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
       tampilData(0,"none");
       cekKeuntungan();
       
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_tanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tanggalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_cari;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_refresh;
    private javax.swing.JComboBox<String> combo_bulan;
    private date.DateChooser dateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jk;
    private javax.swing.JLabel lb_hargaPokok;
    private javax.swing.JLabel lb_hasilPenjualan;
    private javax.swing.JLabel lb_hasilPenjualan1;
    private javax.swing.JLabel lb_hasilPenjualan2;
    private javax.swing.JLabel lb_hasilPenjualan3;
    private javax.swing.JLabel lb_hasilPenjualan4;
    private javax.swing.JLabel lb_jumlahTransaksi;
    private javax.swing.JLabel lb_keuntungan;
    private Komponen.PanelTransparent panelTransparent2;
    private Komponen.PanelTransparent panelTransparent4;
    private Komponen.PanelTransparent panelTransparent5;
    private Komponen.PanelTransparent panelTransparent6;
    private TabelKomponen.TableDark tabel_dataTransaksi;
    private JpanelKomponen.TextField txt_tahun;
    private JpanelKomponen.TextField txt_tanggal;
    // End of variables declaration//GEN-END:variables
}
