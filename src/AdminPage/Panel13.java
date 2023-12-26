
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
public class Panel13 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
     
    public Panel13() {
       initComponents();
       txt_kodesuppliyer.setEnabled(false);
       txt_date.setEnabled(false);
       
        tabel_pemasok.setModel(table);
        table.addColumn("ID pemasok");
        table.addColumn("Nama");
        table.addColumn("Alamat");
        table.addColumn("Masuk Pada");
          
        tampilData();
    }
    
    
    private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = tabel_pemasok.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT * FROM `tb_pemasok` ";
        
        try{ 
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                String id= rslt.getString("id_pemasok");
                String nama = rslt.getString("nama");
                String alamat = rslt.getString("alamat");
                String dateIn = rslt.getString("tanggal_masuk");
                       dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {id,nama,alamat,dateIn};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
            
            //mengeset nilai yang ditampung agar muncul di table
            tabel_pemasok.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
          
    private void clear(){
        txt_kodesuppliyer.setText("");
        txt_namasuppliyer.setText("");
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

private void hapusData() {
    // Ambil data no pendaftaran
    int i = tabel_pemasok.getSelectedRow();
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus.");
            return;
        }

    String kode = table.getValueAt(i, 0).toString();
    int konfirmasi = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM `tb_pemasok` WHERE `tb_pemasok`.`id_pemasok` = ?";

            try {
                PreparedStatement ps = connect.prepareStatement(query);
                ps.setString(1, kode);
                ps.execute();
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            } catch (SQLException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "Data Gagal Dihapus. Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

            } finally {
                tampilData();
                clear();
            }
        }
}

    private void cari(){
        int row = tabel_pemasok.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String cari = txt_cari.getText();
        
        String query = "SELECT * FROM `tb_pemasok` WHERE `id_pemasok`  LIKE '%"+cari+"%' OR `nama` LIKE '%"+cari+"%' ";
                
            try{
                Statement sttmnt = connect.createStatement();//membuat statement
                ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
           
                while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_pemasok");
                String nama = rslt.getString("nama");
                String alamat = rslt.getString("alamat");
                String dateIn = rslt.getString("tanggal_masuk");
                       dateIn = convertDateFormat(dateIn, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    
                //masukan semua data kedalam array
                String[] data = {id,nama,alamat,dateIn};
                    //menambahakan baris sesuai dengan data yang tersimpan diarray
                    table.addRow(data);
                }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_pemasok.setModel(table);
           
            }catch(Exception e){
                System.out.println(e);
            }
    } 

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooser = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_kodesuppliyer = new JpanelKomponen.TextField();
        txt_namasuppliyer = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        btn_hapus = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        txt_date = new JpanelKomponen.TextField();
        button2 = new JpanelKomponen.Button();
        txt_alamat = new JpanelKomponen.TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_pemasok = new TabelKomponen.TableDark();
        btn_cari = new JpanelKomponen.Button();
        txt_cari = new JpanelKomponen.TextField();
        btn_refresh = new JpanelKomponen.Button();

        dateChooser.setDateFormat("dd-MMMM-yyyy");
        dateChooser.setTextRefernce(txt_date);

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_kodesuppliyer.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_kodesuppliyer.setHint("Kode Pemasok");
        txt_kodesuppliyer.setName(""); // NOI18N

        txt_namasuppliyer.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_namasuppliyer.setHint("Nama Pemasok");
        txt_namasuppliyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namasuppliyerActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Hapus Pemasok");

        btn_hapus.setForeground(new java.awt.Color(255, 255, 255));
        btn_hapus.setText("Hapus");
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
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

        txt_alamat.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        txt_alamat.setHint("Alamat");
        txt_alamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_alamatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_clear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_namasuppliyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1))
                    .addComponent(txt_alamat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_kodesuppliyer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(52, 52, 52))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(txt_kodesuppliyer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_namasuppliyer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(btn_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(50, 70, 460, 500);

        tabel_pemasok.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_pemasok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_pemasokMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_pemasok);
        if (tabel_pemasok.getColumnModel().getColumnCount() > 0) {
            tabel_pemasok.getColumnModel().getColumn(0).setResizable(false);
            tabel_pemasok.getColumnModel().getColumn(1).setResizable(false);
            tabel_pemasok.getColumnModel().getColumn(3).setResizable(false);
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

    private void txt_namasuppliyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namasuppliyerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namasuppliyerActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        hapusData();
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void tabel_pemasokMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_pemasokMouseClicked
        int baris = tabel_pemasok.getSelectedRow();
        
        String kode = table.getValueAt(baris,0).toString();
        txt_kodesuppliyer.setText(kode);
        
        String nama = table.getValueAt(baris, 1).toString();
        txt_namasuppliyer.setText(nama);
        
        String alamat = table.getValueAt(baris, 2).toString();
        txt_alamat.setText(alamat);
        
        
        String date = table.getValueAt(baris, 3).toString();
        txt_date.setText(date);
        
        
    }//GEN-LAST:event_tabel_pemasokMouseClicked

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

    private void txt_alamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_alamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_alamatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_cari;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_hapus;
    private JpanelKomponen.Button btn_refresh;
    private JpanelKomponen.Button button2;
    private date.DateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private TabelKomponen.TableDark tabel_pemasok;
    private JpanelKomponen.TextField txt_alamat;
    private JpanelKomponen.TextField txt_cari;
    private JpanelKomponen.TextField txt_date;
    private JpanelKomponen.TextField txt_kodesuppliyer;
    private JpanelKomponen.TextField txt_namasuppliyer;
    // End of variables declaration//GEN-END:variables
}
