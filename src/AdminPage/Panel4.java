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
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mardana
 */

public class Panel4 extends javax.swing.JPanel {

    DefaultTableModel table = new DefaultTableModel();
    Connection connect = DatabaseKoneksi.createConnection();//memanggil koneksi
    
    public Panel4() {
       initComponents();
       
       tabel_user.setModel(table);
        table.addColumn("ID User");
        table.addColumn("Username");
        table.addColumn("Password");
        table.addColumn("Email");
        table.addColumn("Nama");
        table.addColumn("Alamat");
        table.addColumn("Tanggal Lahir");
        table.addColumn("Jenis Kelamin");
        
          
        tampilData();
    }
    
private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = tabel_user.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT * FROM `tb_user` WHERE `admin` = 0 ";
        
        try{
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                String id= rslt.getString("id_user");
                String username = rslt.getString("username");
                String password = rslt.getString("password");
                String email = rslt.getString("email");
                String nama = rslt.getString("nama");
                String alamat = rslt.getString("alamat");
                String tglLahir = rslt.getString("tanggal_lahir");
                       tglLahir = convertDateFormat(tglLahir, "yyyy-MM-dd", "dd-MMMM-yyyy");
                String gender = rslt.getString("gender");
                    
                //masukan semua data kedalam array
                String[] data = {id,username,password,email,nama,alamat,tglLahir,gender};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_user.setModel(table);
            
       }catch(Exception e){
            System.out.println(e);
        }
       
}
    
private void clear(){
        txt_idUser.setText("");
        txt_username.setText("");
        txt_password.setText("");
        txt_email.setText("");
        txt_nama.setText("");
        txt_alamat.setText("");
        txt_date.setText("");
        jk.clearSelection();     
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
    int kode = Integer.parseInt(txt_idUser.getText());
    String user = txt_username.getText();
    String pass = txt_password.getText();
    String email = txt_email.getText();
    String nama = txt_nama.getText();
    String alamat = txt_alamat.getText();
    String date = txt_date.getText();
    String dateIn = convertDateFormat(date, "dd-MMMM-yyyy", "yyyy-MM-dd");
    int admin = 0;
        
    rd_pria.setActionCommand("Pria");
    rd_wanita.setActionCommand("Wanita");
    ButtonModel selectedModel = jk.getSelection();
    String gender = selectedModel.getActionCommand(); // Mengambil nilai dari radio button yang terpilih
        
    //query untuk memasukan data
    String query = "INSERT INTO `tb_user` (`id_user`, `username`, `password`, `email`,`nama`, `alamat`, `tanggal_lahir`,`admin`,`gender`) "
                     + "VALUES ('"+kode+"', '"+user+"', '"+pass+"', '"+email+"','"+nama+"','"+alamat+"','"+dateIn+"','"+admin+"','"+gender+"')";
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
        int row = tabel_user.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String cari = txt_cari.getText();
        
        String query = "SELECT * FROM `tb_user` WHERE (`id_user` LIKE '%"+cari+"%' OR `nama` LIKE '%"+cari+"%') AND `admin` = 0";

                
       try{
           Statement sttmnt = connect.createStatement();//membuat statement
           ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
           
           while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_user");
                    String username = rslt.getString("username");
                    String password = rslt.getString("password");
                    String email = rslt.getString("email");
                    String nama = rslt.getString("nama");
                    String alamat = rslt.getString("alamat");
                    String tglLahir = rslt.getString("tanggal_lahir");
                           tglLahir = convertDateFormat(tglLahir, "yyyy-MM-dd", "dd-MMMM-yyyy");
                    String gender = rslt.getString("gender");
                    
                    
                //masukan semua data kedalam array
              String[] data = {id,username,password,email,nama,alamat,tglLahir,gender};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_user.setModel(table);
           
        
    }catch(Exception e){
           System.out.println(e);
    }
}

private void validasi(){
    boolean save = true;  
    boolean id, user = false, pass = false, email = false, nama = false, alamat = false, date = false , gender = false;

    /* validateData(txt_kodebarang,"Kode Barang", 1);
    1. cek empty dan int
    2. cek empty textfield
    3. cek empty dan email
    */
    id = validateData(txt_idUser,"Id User", 1);
   
    if(id){
        user = validateData(txt_username,"Username", 2);

    }else{
        save = false;
    }
    
    if(user){
        pass = validateData(txt_password,"Password", 2);
        
    }else{
        save = false;
    }
    
    if(pass){
        email = validateData(txt_email,"Email", 3);
       
    }else{
        save = false;
    }
    
    if(email){
        nama = validateData(txt_nama,"Nama User", 2);
            }else{
        save = false;
    }
    
    if(nama){
        alamat = validateData(txt_alamat,"Alamat User", 2);
       
    }else{
        save = false;
    }
    
    if(alamat){
        date = validateData(txt_date,"Tanggal Lahir", 2);
        
    }else{
        save = false;
    }
    
    if(date){
        gender = validateRadio(rd_pria, rd_wanita,"Jenis Kelamin");
    }else{
        save = false;
    }
    
    if(!gender){
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

private boolean validateRadio(JRadioButton radioOption1, JRadioButton radioOption2, String fieldName) {
    boolean fik = true;

    if (radioOption1.isSelected() || radioOption2.isSelected()) {
        System.out.println(fieldName + ": " + (radioOption1.isSelected() ? "Option 1" : "Option 2"));
    } else {
        showMessage("Pilih salah satu opsi " + fieldName, "Error");
        fik = false;
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
        txt_idUser = new JpanelKomponen.TextField();
        txt_username = new JpanelKomponen.TextField();
        txt_nama = new JpanelKomponen.TextField();
        txt_date = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        btn_simpan = new JpanelKomponen.Button();
        btn_clear = new JpanelKomponen.Button();
        txt_email = new JpanelKomponen.TextField();
        txt_alamat = new JpanelKomponen.TextField();
        rd_pria = new JpanelKomponen.RadioButtonCustom();
        rd_wanita = new JpanelKomponen.RadioButtonCustom();
        jLabel2 = new javax.swing.JLabel();
        button2 = new JpanelKomponen.Button();
        txt_password = new JpanelKomponen.PasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_user = new TabelKomponen.TableDark();
        txt_cari = new JpanelKomponen.TextField();
        btn_refresh = new JpanelKomponen.Button();
        btn_cari = new JpanelKomponen.Button();

        dateChooser1.setDateFormat("dd-MMMM-yyyy");
        dateChooser1.setTextRefernce(txt_date);

        setPreferredSize(new java.awt.Dimension(1692, 981));

        background1.setBlur(jPanel1);

        jPanel1.setOpaque(false);

        txt_idUser.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_idUser.setHint("Id User");
        txt_idUser.setName(""); // NOI18N
        txt_idUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_idUserActionPerformed(evt);
            }
        });

        txt_username.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_username.setHint("Username");
        txt_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_usernameActionPerformed(evt);
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
        txt_date.setHint("Tanggal Lahir");
        txt_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tambah Pegawai");

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

        txt_email.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_email.setHint("Email");

        txt_alamat.setHint("Alamat");

        jk.add(rd_pria);
        rd_pria.setForeground(new java.awt.Color(255, 255, 255));
        rd_pria.setText("Pria");
        rd_pria.setName(""); // NOI18N

        jk.add(rd_wanita);
        rd_wanita.setForeground(new java.awt.Color(255, 255, 255));
        rd_wanita.setText("Wanita");

        jLabel2.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Jenis Kelamin :");

        button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/calendar.png"))); // NOI18N
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        txt_password.setHint("Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(289, 289, 289)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(27, 27, 27)
                                .addComponent(rd_pria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rd_wanita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_username, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(txt_idUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(txt_idUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rd_pria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rd_wanita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        background1.add(jPanel1);
        jPanel1.setBounds(40, 80, 400, 680);

        tabel_user.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_user.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        tabel_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_userMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_user);

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(470, 120, 820, 470);

        txt_cari.setHint("Cari.......");
        background1.add(txt_cari);
        txt_cari.setBounds(1050, 80, 240, 34);

        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        background1.add(btn_refresh);
        btn_refresh.setBounds(510, 600, 80, 32);

        btn_cari.setForeground(new java.awt.Color(255, 255, 255));
        btn_cari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });
        background1.add(btn_cari);
        btn_cari.setBounds(1000, 80, 40, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 1376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 316, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 796, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txt_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usernameActionPerformed

    private void txt_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        validasi();
        tampilData();

    }//GEN-LAST:event_btn_simpanActionPerformed

    private void tabel_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_userMouseClicked
        int baris = tabel_user.getSelectedRow();
        
        String kode = table.getValueAt(baris,0).toString();
        txt_idUser.setText(kode);
        
        String user = table.getValueAt(baris, 1).toString();
        txt_username.setText(user);
        
        String pass = table.getValueAt(baris, 2).toString();
        txt_password.setText(pass);
        
        String email = table.getValueAt(baris, 3).toString();
        txt_email.setText(email);
        
        String nama = table.getValueAt(baris, 4).toString();
        txt_nama.setText(nama);
        
        String alamat = table.getValueAt(baris, 5).toString();
        txt_alamat.setText(alamat);
        
        String date = table.getValueAt(baris, 6).toString();
        txt_date.setText(date);
        
        String gender = table.getValueAt(baris, 7).toString();

        if ("Pria".equals(gender)) {
                rd_pria.setSelected(true);
        } else if ("Wanita".equals(gender)) {
                rd_wanita.setSelected(true);
        }else{
                jk.clearSelection();
        }
    }//GEN-LAST:event_tabel_userMouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
       tampilData();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_idUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_idUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_idUserActionPerformed

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jk;
    private JpanelKomponen.RadioButtonCustom rd_pria;
    private JpanelKomponen.RadioButtonCustom rd_wanita;
    private TabelKomponen.TableDark tabel_user;
    private JpanelKomponen.TextField txt_alamat;
    private JpanelKomponen.TextField txt_cari;
    private JpanelKomponen.TextField txt_date;
    private JpanelKomponen.TextField txt_email;
    private JpanelKomponen.TextField txt_idUser;
    private JpanelKomponen.TextField txt_nama;
    private JpanelKomponen.PasswordField txt_password;
    private JpanelKomponen.TextField txt_username;
    // End of variables declaration//GEN-END:variables
}
