package UserPage;

import Koneksi.DatabaseKoneksi;
import PrintStruk.FieldBarang;
import PrintStruk.FieldTransaksi;
import PrintStruk.cetakStruk;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextField;


/**
 *
 * @author Mardana
 */
public class Panel4 extends javax.swing.JPanel {

    private Connection connect = DatabaseKoneksi.createConnection();
    DefaultTableModel table = new DefaultTableModel();
    
    public Panel4() {
       initComponents();
       txt_namaProduk.setEnabled(false);
       txt_harga.setEnabled(false);
       txt_stok.setEnabled(false);
       txt_namaMember.setEnabled(false);
       txt_satuan.setEnabled(false);
       txt_poin.setEnabled(false);
       txt_totalHarga.setEnabled(false);
       txt_totalBayar.setEnabled(false);
       txt_kembalian.setEnabled(false);
       
        tabel_cart.setModel(table);
        
        tampilData();
        totalnya();
    }
    
        private void cariMember(){
        table.setRowCount(0);
        table.setColumnIdentifiers(new Object[]{"ID Member", "Nama", "Alamat", "Jumlah Poin", "Tanggal Daftar"});
        
        String cari = txt_cariMember.getText();
        
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
                tabel_cart.setModel(table);
           
        
    }catch(Exception e){
           System.out.println(e);
    }
            
}

    private void cariBarang(){
        table.setRowCount(0);
        
        table.setColumnIdentifiers(new Object[]{"ID Barang", "Nama Barang", "Harga", "Stok", "Satuan", "Masuk Pada"});
        
        String cari = txt_cariBarang.getText();
        
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
                tabel_cart.setModel(table);
           
        
    }catch(Exception e){
           System.out.println(e);
    }
            
}
    
    private void tampilData(){
        table.setRowCount(0);
        table.setColumnIdentifiers(new Object[]{"Id", "Nama Barang", "Harga", "Qty", "Total Harga"});
        
        String query = "SELECT * FROM `tb_cart` WHERE status_cart = 'cart'";
        
        try{ 
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id");
                    String nama = rslt.getString("nama");
                    String harga = rslt.getString("harga");
                    String qty = rslt.getString("qty");
                    String total = rslt.getString("total_harga");
           
                //masukan semua data kedalam array
                String[] data = {id,nama,harga,qty,total};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tabel_cart.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public int AmbilIdCart() {
    int latestCartId = 0;

    // Query untuk mengambil cart_id terakhir dari tabel transaksi
    String query = "SELECT MAX(id_cart) FROM tb_transaksi";

    try {
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Mengambil nilai cart_id terakhir
            latestCartId = resultSet.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Menetapkan nilai cart_id berikutnya
    int nextCartId = latestCartId + 1;

    return nextCartId;
}

    private void clear(){
        txt_kodeBarang.setText("");
        txt_namaProduk.setText("");
        txt_harga.setText("");
        txt_stok.setText("");
        txt_satuan.setText("");
        txt_jumlah.setText("");
        txt_totalHarga.setText("");
        txt_cariBarang.setText("");
        txt_totalBayar.setText("");
        txt_jumlahUang.setText("");
        txt_kembalian.setText("");
        
    } 
    
    private void clearMember(){
        txt_kodeMember.setText("");
        txt_namaMember.setText("");
        txt_poin.setText("");
    }
       
    private  String convertDateFormat(String dateStr, String inputFormat, String outputFormat) {
        // Parsing tanggal dengan format input
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);

        // Menggunakan formatter untuk mengubah format tanggal
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(outputFormatter);
    }
    
    private void validasi(int a){
    
    if(a == 1){
        boolean add = true;
    
        boolean kode, nama = false, harga = false, stok = false, satuan = false,
                jumlah = false, totalHarga = false;

        /* validateData(txt_kodebarang,"Kode Barang", 1);
        1. cek empty dan int
        2. cek empty textfield
        3. cek empty dan email
        */
        kode = validateData(txt_kodeBarang,"Kode Barang", 1);
        if(kode){
            nama = validateData(txt_namaProduk,"Nama Barang", 2);
        }else{
            add = false;
        }
    
        if(nama){
            harga = validateData(txt_harga,"Harga Barang", 1);
        }else{
            add = false;
        }
    
        if(harga){
            stok = validateData(txt_stok,"Stok Barang", 1);
        }else{
            add = false;
        }
        
        if(stok){
            satuan = validateData(txt_satuan,"Satuan", 2);
        }else{
            add = false;
        }
        
        if(satuan){
            jumlah = validateData(txt_jumlah,"Jumlah Barang", 1);
        }else{
            add = false;
        }
        
        if(jumlah){
            totalHarga = validateData(txt_totalHarga,"Total Harga", 1);
        }else{
            add = false;
        }
    
        if(!totalHarga){
            add = false;
        }
    
        if(add){
            addToCart();
            totalnya();
        }
        
    }else if (a == 2){
        boolean save = true;
        
        boolean subtotal = false, date = false, jumlahUang, kodeMember = false,
                namaMember = false, poin = false;
        
        int totalBelanja = Integer.parseInt(txt_totalBayar.getText());
            if(totalBelanja <= 0){
                save = false;
                showMessage(" Tidak Ada Barang Pada Keranjang Belanja", "Error");
            }else{
                subtotal = validateData(txt_totalBayar,"Total Bayar", 1);
            }
        if(subtotal){
           date = validateData(txt_date,"Tanggal Transaksi", 2);
        }else{
            save = false;
        }
        
        if(date){
           jumlahUang = validateData(txt_jumlahUang,"Jumlah Uang", 1);
           
        }else{
            save = false;
        }
        
        String cekKodeMember = txt_kodeMember.getText();
        if(!cekKodeMember.isEmpty()){
           kodeMember = validateData(txt_kodeMember,"Kode Member", 1);
            if(kodeMember){
                namaMember = validateData(txt_date,"Nama Member", 2);
            }else{
               save = false;
            }
           
            if(namaMember){
                poin = validateData(txt_poin,"Poin", 1);
            }else{
               save = false;
            }
           
            if(!poin){
                save = false;
            }  
        }
        
        
        if(save){
            prosesPembayaran();
        }
        
    }else{
        JOptionPane.showMessageDialog(null,"Transaksi Gagal");
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

    private void addToCart(){
        int cartId = AmbilIdCart();
        String barangId = txt_kodeBarang.getText();
        String nama = txt_namaProduk.getText();
        String status = "cart";
        int hargabarang = 0;
        int jumlah = 0;
        int total = 0;
            try {
                hargabarang = Integer.parseInt(txt_harga.getText());
                jumlah = Integer.parseInt(txt_jumlah.getText());
                total = Integer.parseInt(txt_totalHarga.getText());
            
            } catch (NumberFormatException e) {
                // Tangani eksepsi jika nilai harga tidak dapat diubah menjadi angka
                JOptionPane.showMessageDialog(null, "Masukkan nilai harga yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
       
        //query untuk memasukan data
        String query = "INSERT INTO `tb_cart` (`id_cart`, `id_barang`, `nama`, `harga`,`qty`,`total_harga`,`status_cart`) "
                     + "VALUES ('"+cartId+"', '"+barangId+"', '"+nama+"', '"+hargabarang+"','"+jumlah+"','"+total+"','"+status+"')";
        
        try{
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Data Gagal Disimpan");
            
        }finally{
            tampilData();
            clear();
            
        }
    }

    private void totalnya() {
    String query = "SELECT SUM(total_harga) AS total_bayar FROM tb_cart WHERE status_cart = 'cart'";

    try (PreparedStatement ps = connect.prepareStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Ambil nilai total bayar dari hasil query
                int totalBayar = rs.getInt("total_bayar");

                // Set nilai total bayar ke dalam JTextField txt_totalBayar
                txt_totalBayar.setText(String.valueOf(totalBayar));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menghitung total bayar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }
  
    private void loadBarang(String productCode) {
        // Query SQL untuk mendapatkan informasi produk berdasarkan kode produk
        String query = "SELECT nama, harga,stok, satuan FROM tb_barang WHERE id_barang = ?";
        
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Memuat data ke dalam JTextField sesuai dengan hasil query
                    txt_namaProduk.setText(resultSet.getString("nama"));
                    txt_harga.setText(resultSet.getString("harga"));
                    txt_stok.setText(resultSet.getString("stok"));
                    txt_satuan.setText(resultSet.getString("satuan"));
                } else {
                    // Kode produk tidak ditemukan
                    txt_namaProduk.setText("Produk Tidak Ditemukan");
                    txt_harga.setText("-");
                    txt_stok.setText("-");
                    txt_satuan.setText("-");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle eksepsi SQL sesuai dengan kebutuhan aplikasi Anda
        }
    }
    
    private void loadMember(String kodeMember) {
        // Query SQL untuk mendapatkan informasi produk berdasarkan kode produk
        String query = "SELECT nama, poin FROM tb_member WHERE id_member = ?";
        
        try (PreparedStatement preparedStatement = connect.prepareStatement(query)) {
            preparedStatement.setString(1, kodeMember);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Memuat data ke dalam JTextField sesuai dengan hasil query
                    txt_namaMember.setText(resultSet.getString("nama"));
                    txt_poin.setText(resultSet.getString("poin"));
                } else {
                    // Kode produk tidak ditemukan
                    txt_namaMember.setText("Member Tidak Ditemukan");
                    txt_poin.setText("-");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle eksepsi SQL sesuai dengan kebutuhan aplikasi Anda
        }
    }
   
    private void cetak() {
    String date = txt_date.getText();
    String namaMember = "-";
    if (!txt_namaMember.getText().isEmpty()){
       namaMember = txt_namaMember.getText();
    }
    int poinMember = 0;
    if (!txt_poin.getText().isEmpty()){
        
        try {
            // Query SQL untuk mengambil nilai poin
            String sqlQuery = "SELECT poin FROM tb_member WHERE id_member = ?";
            
            // ID anggota yang ingin diambil nilainya
            int idMember = Integer.parseInt(txt_kodeMember.getText());
            

            // Membuat prepared statement
            PreparedStatement preparedStatement = connect.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, idMember);

            // Mengeksekusi query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Mengambil nilai poin dari hasil query
            if (resultSet.next()) {
                poinMember = resultSet.getInt("poin");
            }
        } catch (SQLException ex) {
            System.out.println("Gagal Mengambil Poin");
        }
    }
    int subtotal = Integer.parseInt(txt_totalBayar.getText());
    int jumlahUang = Integer.parseInt(txt_jumlahUang.getText());
    int kembalian = Integer.parseInt(txt_kembalian.getText());
    
    try {
        cetakStruk.getInstace().compileStruk();
    } catch (Exception e) {
           e.printStackTrace();
           System.out.println("Error compiling the report: " + e.getMessage());
    }
    String query = "SELECT * FROM `tb_cart` WHERE status_cart = 'cart'";
    List<FieldBarang> fields = new ArrayList<> ();
    
    try {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        // Loop melalui hasil kueri dan tambahkan ke daftar fields
        while (resultSet.next()) {
            String productName = resultSet.getString("nama");
            int qty = resultSet.getInt("qty");
            int price = resultSet.getInt("harga");
            int total = resultSet.getInt("total_harga");
            
            // Pastikan productName tidak null sebelum ditambahkan ke fields
            fields.add(new FieldBarang(productName, qty, price, total));
        }
        
        FieldTransaksi dataprint = new FieldTransaksi(date, namaMember, poinMember, subtotal, jumlahUang, kembalian, generateQrcode(), fields);
        cetakStruk.getInstace().cetakStrukTransaksi(dataprint);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    
}

    private InputStream generateQrcode() throws WriterException, IOException {
        NumberFormat nf = new DecimalFormat("0000000");
        Random ran = new Random();
        String invoice = nf.format(ran.nextInt(9999999) + 1);
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(invoice, BarcodeFormat.QR_CODE, 100, 100, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void totalHarga(String Harga, String qty) {
    try {
        // Mengambil nilai harga dan jumlah dari JTextField dan mengonversinya ke integer
        int harga = Integer.parseInt(Harga);
        int jumlah = Integer.parseInt(qty);

        // Menghitung total harga
        int totalHarga = harga * jumlah;

        // Menetapkan nilai totalHarga ke dalam JTextField txtTotalHarga
        txt_totalHarga.setText(String.valueOf(totalHarga));
    } catch (NumberFormatException e) {
        // Tangani eksepsi jika nilai harga atau jumlah tidak valid (bukan angka)
        JOptionPane.showMessageDialog(null, "Masukkan nilai harga dan jumlah yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void deleteCart() {
    // Ambil indeks baris yang dipilih pada tabel
    int selectedRow = tabel_cart.getSelectedRow();

    // Pastikan ada baris yang dipilih
    if (selectedRow >= 0) {
        // Ambil nilai kolom id_transaksi dari baris yang dipilih
        String id = table.getValueAt(selectedRow, 0).toString();


        // Query untuk menghapus data berdasarkan id_transaksi
        String query = "DELETE FROM `tb_cart` WHERE `tb_cart`.`id` = ?";

        try (PreparedStatement ps = connect.prepareStatement(query)) {
            // Set parameter id_transaksi
            ps.setString(1, id);

            // Eksekusi query penghapusan
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Refresh tampilan tabel dan bersihkan inputan
            tampilData();
            clear();

        }
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}
    
    private void ubahStatusCart() {
    String query = "UPDATE tb_cart SET status_cart = 'checkout' WHERE status_cart = 'cart'";

    try (PreparedStatement ps = connect.prepareStatement(query)) {
        // Eksekusi query
        int rowsAffected = ps.executeUpdate();

        // Tampilkan pesan berhasil jika terdapat baris yang berubah
        if (rowsAffected > 0) {
            System.out.println("Status cart berhasil diubah menjadi checkout untuk " + rowsAffected + " baris.");
        } else {
            System.out.println("Tidak ada baris yang memenuhi kriteria untuk diubah.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Gagal mengubah status cart: " + e.getMessage());
    }
}

    private void prosesPembayaran() {
    // Ambil nilai total bayar dan jumlah uang dari JTextField
        int totalBayar = Integer.parseInt(txt_totalBayar.getText());
        int jumlahUang = Integer.parseInt(txt_jumlahUang.getText());

        // Hitung kembalian
        int kembalian = jumlahUang - totalBayar;

        // Tampilkan kembalian di JTextField txt_kembalian
        if (kembalian >= 0) {
            txt_kembalian.setText(String.valueOf(kembalian));
            
            String cekKodeMember = txt_kodeMember.getText();
            if (!cekKodeMember.isEmpty()) {
                hitungPoin();
            }
            tambahDataTransaksi();
            cetak();
            ubahStatusCart();  
            tampilData();
            clear();
            // Tambahkan logika tambahan di sini, misalnya, menyimpan transaksi ke database, dll.
        } else {
            // Jika jumlah uang kurang dari total bayar, beri peringatan
            JOptionPane.showMessageDialog(null, "Jumlah uang kurang dari total pembayaran.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            txt_kembalian.setText("0"); // Atur kembalian menjadi 0
        }

}
   
    private void tambahDataTransaksi(){
        int cartId = AmbilIdCart();
        
        int memberId = 0;
        if (!txt_kodeMember.getText().isEmpty()) {
            memberId = Integer.parseInt(txt_kodeMember.getText());
        }
            
       
        int totalHarga = Integer.parseInt(txt_totalBayar.getText());
        int totalBayar = Integer.parseInt(txt_jumlahUang.getText());
        int kembalian = Integer.parseInt(txt_kembalian.getText());
        String date = txt_date.getText();
        String dateIn = convertDateFormat(date, "dd-MMMM-yyyy", "yyyy/MM/dd");
         
        
        //query untuk memasukan data
        
        String  query = "INSERT INTO `tb_transaksi` (`id_cart`, `id_member`, `total_harga`, `total_bayar`,`jumlah_kembalian`,`tanggal_transaksi`) "
                     + "VALUES ('"+cartId+"', '"+memberId+"', '"+totalHarga+"', '"+totalBayar+"','"+kembalian+"','"+dateIn+"')";
        
        if(memberId == 0){
            query = "INSERT INTO `tb_transaksi` (`id_cart`, `total_harga`, `total_bayar`,`jumlah_kembalian`,`tanggal_transaksi`) "
                     + "VALUES ('"+cartId+"', '"+totalHarga+"', '"+totalBayar+"','"+kembalian+"','"+dateIn+"')";
        }
        
        try{
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan. Kesalahan SQL: " + e.getMessage());
            
     }  
       
}
   
    private void hitungPoin() {
    try {
        // Ambil nilai jumlah belanja dari JTextField txt_jumlahBayar
        int jumlahBelanja = Integer.parseInt(txt_totalBayar.getText());

            // Hitung jumlah poin
            int jumlahPoin = jumlahBelanja / 10000;

            // Update nilai poin di tabel member
            updatePoinMember(jumlahPoin);
        
    } catch (NumberFormatException e) {
        // Tangani eksepsi jika nilai yang dimasukkan bukan angka
        JOptionPane.showMessageDialog(null, "Masukkan nilai jumlah belanja yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void updatePoinMember(int jumlahPoin) {
   
    try {
        // Ambil id_member dari JTextField txt_kode Member
        int idMember = Integer.parseInt(txt_kodeMember.getText());


        // Query untuk memperbarui nilai poin di tabel member
        String query = "UPDATE tb_member SET poin = poin + ? WHERE id_member = ?";
        
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            // Set nilai parameter pada statement
            ps.setInt(1, jumlahPoin);
            ps.setInt(2, idMember);

            // Eksekusi query
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Poin berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (NumberFormatException | SQLException e) {
        // Tangani eksepsi jika terjadi kesalahan
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal memperbarui poin: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}

    public void cekDanUbahHarga(int idProduk) {
        try {
            // Ambil data diskon dari tabel diskon berdasarkan id_produk
            String query = "SELECT harga_awal, harga_diskon, berlaku, sampai FROM tb_diskon WHERE id_barang = ?";
            try (PreparedStatement stmn = connect.prepareStatement(query)) {
                // Set parameter untuk id_barang
                stmn.setInt(1, idProduk);

                // Eksekusi query untuk mendapatkan data diskon
                ResultSet rs = stmn.executeQuery();

                if (rs.next()) {
                    // Ambil nilai diskon dari hasil query
                    int hargaAwal = rs.getInt("harga_awal");
                    int hargaDiskon = rs.getInt("harga_diskon");
                    LocalDate berlakuStr = rs.getDate("berlaku").toLocalDate();
                    LocalDate sampaiStr = rs.getDate("sampai").toLocalDate();

                    // Ambil tanggal sekarang
                    
                    LocalDate tanggalSekarang = LocalDate.now();

                    // Cek apakah tanggal sekarang berada di antara berlaku dan sampai
                    if (tanggalSekarang.compareTo(berlakuStr) >= 0 && tanggalSekarang.compareTo(sampaiStr) <= 0) {
                        //Jika iya, set harga_produk menjadi harga_diskon
                        updateHargaProduk(idProduk, hargaDiskon);
                    } else {
                        // Jika tidak, set harga_produk menjadi harga_awal
                        updateHargaProduk(idProduk, hargaAwal);
                    }
                } else {
                    System.out.println("Tidak ada data diskon untuk id_barang = " + idProduk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateHargaProduk(int idProduk, int hargaBaru) {
        // Update harga pada tb_barang
        String updateQuery = "UPDATE tb_barang SET harga = ? WHERE id_barang = ?";
        try (Connection connection = DatabaseKoneksi.createConnection();
             PreparedStatement stmn = connection.prepareStatement(updateQuery)) {

            // Set parameter untuk harga baru dan id_barang
            stmn.setInt(1, hargaBaru);
            stmn.setInt(2, idProduk);

            // Eksekusi query update
            int rowsAffected = stmn.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Harga berhasil diubah menjadi " + hargaBaru + " untuk id_barang = " + idProduk);
            } else {
                System.out.println("Tidak ada baris yang terpengaruh oleh query update.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cekAdaDiskon(int idProduk) {
    // Query untuk memeriksa apakah id_barang ada di tb_diskon
    String query = "SELECT COUNT(*) FROM tb_diskon WHERE id_barang = ?";

    try {
        
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        // Set parameter untuk id_barang
        preparedStatement.setInt(1, idProduk);

        // Eksekusi query
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            // Ambil hasil query
            if (resultSet.next()) {
                int jumlahDiskon = resultSet.getInt(1);

                // Cek apakah ada diskon
                if (jumlahDiskon > 0) {
                    // Jika ada, jalankan fungsi cekDiskon
                    cekDanUbahHarga(idProduk);
                } else {
                    // Jika tidak, cetak pesan "Tidak ada diskon"
                    System.out.println("Tidak ada diskon untuk produk dengan ID " + idProduk);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Gagal melakukan pengecekan diskon: " + e.getMessage());
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jk = new javax.swing.ButtonGroup();
        dateChooser1 = new date.DateChooser();
        background1 = new JpanelKomponen.Background();
        jPanel1 = new javax.swing.JPanel();
        txt_kodeBarang = new JpanelKomponen.TextField();
        jLabel1 = new javax.swing.JLabel();
        txt_namaProduk = new JpanelKomponen.TextField();
        txt_harga = new JpanelKomponen.TextField();
        txt_jumlah = new JpanelKomponen.TextField();
        txt_totalHarga = new JpanelKomponen.TextField();
        txt_stok = new JpanelKomponen.TextField();
        btn_addToCart = new JpanelKomponen.Button();
        txt_satuan = new JpanelKomponen.TextField();
        btn_clear = new JpanelKomponen.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel_cart = new TabelKomponen.TableDark();
        txt_cariBarang = new JpanelKomponen.TextField();
        btn_refresh = new JpanelKomponen.Button();
        txt_date = new JpanelKomponen.TextField();
        txt_poin = new JpanelKomponen.TextField();
        txt_kodeMember = new JpanelKomponen.TextField();
        txt_namaMember = new JpanelKomponen.TextField();
        btn_bayar = new JpanelKomponen.Button();
        txt_totalBayar = new JpanelKomponen.TextField();
        txt_kembalian = new JpanelKomponen.TextField();
        txt_jumlahUang = new JpanelKomponen.TextField();
        btn_hapusData = new JpanelKomponen.Button();
        btn_cariBarang = new JpanelKomponen.Button();
        button2 = new JpanelKomponen.Button();
        btn_cariMember = new JpanelKomponen.Button();
        txt_cariMember = new JpanelKomponen.TextField();
        btn_clearMember = new JpanelKomponen.Button();

        dateChooser1.setDateFormat("dd-MMMM-yyyy");
        dateChooser1.setTextRefernce(txt_date);

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

        jLabel1.setFont(new java.awt.Font("Prompt", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tabel Transaksi");

        txt_namaProduk.setHint("Nama Barang");

        txt_harga.setHint("Harga");
        txt_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hargaActionPerformed(evt);
            }
        });

        txt_jumlah.setHint("Jumlah");
        txt_jumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jumlahActionPerformed(evt);
            }
        });

        txt_totalHarga.setHint("Total Harga");

        txt_stok.setHint("Stok");

        btn_addToCart.setForeground(new java.awt.Color(255, 255, 255));
        btn_addToCart.setText("ADD");
        btn_addToCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addToCartActionPerformed(evt);
            }
        });

        txt_satuan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_satuan.setHint("Satuan");

        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setText("clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(btn_addToCart, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_satuan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txt_jumlah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                            .addComponent(txt_harga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_kodeBarang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_namaProduk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_totalHarga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(54, 54, 54))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txt_kodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namaProduk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_satuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_totalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_addToCart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

        background1.add(jPanel1);
        jPanel1.setBounds(30, 30, 350, 470);

        tabel_cart.setModel(new javax.swing.table.DefaultTableModel(
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
        tabel_cart.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        tabel_cart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabel_cartMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabel_cart);

        background1.add(jScrollPane1);
        jScrollPane1.setBounds(420, 90, 630, 290);

        txt_cariBarang.setHint("Cari Barang");
        background1.add(txt_cariBarang);
        txt_cariBarang.setBounds(470, 50, 110, 34);

        btn_refresh.setForeground(new java.awt.Color(255, 255, 255));
        btn_refresh.setText("Refresh");
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        background1.add(btn_refresh);
        btn_refresh.setBounds(420, 390, 100, 32);

        txt_date.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_date.setHint("Atur Tanggal");
        txt_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_dateActionPerformed(evt);
            }
        });
        background1.add(txt_date);
        txt_date.setBounds(860, 50, 140, 30);

        txt_poin.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_poin.setHint("Jumlah Poin");
        txt_poin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_poinActionPerformed(evt);
            }
        });
        background1.add(txt_poin);
        txt_poin.setBounds(530, 460, 210, 38);

        txt_kodeMember.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_kodeMember.setHint("Kode Member");
        txt_kodeMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodeMemberActionPerformed(evt);
            }
        });
        background1.add(txt_kodeMember);
        txt_kodeMember.setBounds(530, 380, 210, 38);

        txt_namaMember.setFont(new java.awt.Font("Prompt", 0, 12)); // NOI18N
        txt_namaMember.setHint("Nama Member");
        txt_namaMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaMemberActionPerformed(evt);
            }
        });
        background1.add(txt_namaMember);
        txt_namaMember.setBounds(530, 420, 210, 38);

        btn_bayar.setForeground(new java.awt.Color(255, 255, 255));
        btn_bayar.setText("Bayar");
        btn_bayar.setToolTipText("");
        btn_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bayarActionPerformed(evt);
            }
        });
        background1.add(btn_bayar);
        btn_bayar.setBounds(1090, 230, 110, 32);

        txt_totalBayar.setHint("Total Bayar");
        txt_totalBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalBayarActionPerformed(evt);
            }
        });
        background1.add(txt_totalBayar);
        txt_totalBayar.setBounds(1060, 90, 180, 30);

        txt_kembalian.setHint("Kembalian");
        txt_kembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kembalianActionPerformed(evt);
            }
        });
        background1.add(txt_kembalian);
        txt_kembalian.setBounds(1060, 170, 180, 34);

        txt_jumlahUang.setHint("Jumlah Uang");
        background1.add(txt_jumlahUang);
        txt_jumlahUang.setBounds(1060, 130, 180, 34);

        btn_hapusData.setForeground(new java.awt.Color(255, 255, 255));
        btn_hapusData.setText("Hapus");
        btn_hapusData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusDataActionPerformed(evt);
            }
        });
        background1.add(btn_hapusData);
        btn_hapusData.setBounds(960, 390, 90, 32);

        btn_cariBarang.setForeground(new java.awt.Color(255, 255, 255));
        btn_cariBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cariBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariBarangActionPerformed(evt);
            }
        });
        background1.add(btn_cariBarang);
        btn_cariBarang.setBounds(420, 50, 40, 30);

        button2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/calendar.png"))); // NOI18N
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });
        background1.add(button2);
        button2.setBounds(810, 50, 40, 31);

        btn_cariMember.setForeground(new java.awt.Color(255, 255, 255));
        btn_cariMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/search.png"))); // NOI18N
        btn_cariMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariMemberActionPerformed(evt);
            }
        });
        background1.add(btn_cariMember);
        btn_cariMember.setBounds(600, 50, 40, 30);

        txt_cariMember.setHint("Cari Member");
        background1.add(txt_cariMember);
        txt_cariMember.setBounds(650, 50, 140, 34);

        btn_clearMember.setForeground(new java.awt.Color(255, 255, 255));
        btn_clearMember.setText("Clear Member");
        btn_clearMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearMemberActionPerformed(evt);
            }
        });
        background1.add(btn_clearMember);
        btn_clearMember.setBounds(420, 430, 100, 32);

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

    private void btn_addToCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addToCartActionPerformed
        validasi(1);
        tampilData();
    }//GEN-LAST:event_btn_addToCartActionPerformed

    private void tabel_cartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabel_cartMouseClicked
 
    }//GEN-LAST:event_tabel_cartMouseClicked

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clear();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed
       tampilData();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void txt_kodeBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodeBarangActionPerformed
        int idProduk = Integer.parseInt(txt_kodeBarang.getText()); // Gantilah dengan ID produk yang sesuai
            cekAdaDiskon(idProduk);
            loadBarang(txt_kodeBarang.getText());
    }//GEN-LAST:event_txt_kodeBarangActionPerformed

    private void txt_poinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_poinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_poinActionPerformed

    private void txt_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_dateActionPerformed

    private void txt_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hargaActionPerformed

    private void txt_kodeMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodeMemberActionPerformed
    loadMember(txt_kodeMember.getText());
    }//GEN-LAST:event_txt_kodeMemberActionPerformed

    private void txt_namaMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namaMemberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namaMemberActionPerformed

    private void txt_jumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jumlahActionPerformed
        totalHarga(txt_harga.getText(), txt_jumlah.getText());
    }//GEN-LAST:event_txt_jumlahActionPerformed

    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bayarActionPerformed

        validasi(2);
    }//GEN-LAST:event_btn_bayarActionPerformed

    private void txt_totalBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalBayarActionPerformed

    private void txt_kembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kembalianActionPerformed

    private void btn_hapusDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusDataActionPerformed
        deleteCart();
        tampilData();
        totalnya();
    }//GEN-LAST:event_btn_hapusDataActionPerformed

    private void btn_cariBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariBarangActionPerformed
        cariBarang();
    }//GEN-LAST:event_btn_cariBarangActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        dateChooser1.showPopup();
    }//GEN-LAST:event_button2ActionPerformed

    private void btn_cariMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariMemberActionPerformed
        cariMember();
    }//GEN-LAST:event_btn_cariMemberActionPerformed

    private void btn_clearMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearMemberActionPerformed
        clearMember();
    }//GEN-LAST:event_btn_clearMemberActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JpanelKomponen.Background background1;
    private JpanelKomponen.Button btn_addToCart;
    private JpanelKomponen.Button btn_bayar;
    private JpanelKomponen.Button btn_cariBarang;
    private JpanelKomponen.Button btn_cariMember;
    private JpanelKomponen.Button btn_clear;
    private JpanelKomponen.Button btn_clearMember;
    private JpanelKomponen.Button btn_hapusData;
    private JpanelKomponen.Button btn_refresh;
    private JpanelKomponen.Button button2;
    private date.DateChooser dateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jk;
    private TabelKomponen.TableDark tabel_cart;
    private JpanelKomponen.TextField txt_cariBarang;
    private JpanelKomponen.TextField txt_cariMember;
    private JpanelKomponen.TextField txt_date;
    private JpanelKomponen.TextField txt_harga;
    private JpanelKomponen.TextField txt_jumlah;
    private JpanelKomponen.TextField txt_jumlahUang;
    private JpanelKomponen.TextField txt_kembalian;
    private JpanelKomponen.TextField txt_kodeBarang;
    private JpanelKomponen.TextField txt_kodeMember;
    private JpanelKomponen.TextField txt_namaMember;
    private JpanelKomponen.TextField txt_namaProduk;
    private JpanelKomponen.TextField txt_poin;
    private JpanelKomponen.TextField txt_satuan;
    private JpanelKomponen.TextField txt_stok;
    private JpanelKomponen.TextField txt_totalBayar;
    private JpanelKomponen.TextField txt_totalHarga;
    // End of variables declaration//GEN-END:variables
}
