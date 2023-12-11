package Koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseKoneksi {
    // Informasi koneksi database
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/tugasakhir";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Fungsi untuk membuat koneksi
    public static Connection createConnection() {
        Connection connection = null;
        try {
            // Memuat driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuat koneksi ke database
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Koneksi ke database berhasil.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL Connector/J tidak ditemukan.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Koneksi ke database gagal.");
            e.printStackTrace();
        }
        return connection;
    }

    // Fungsi untuk menutup koneksi
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Koneksi ditutup.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Connection connection = createConnection();

        // Lakukan operasi database di sini

        closeConnection(connection);
    }
}
