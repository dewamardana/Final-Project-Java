
package PrintStruk;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Mardana
 */
public class FieldTransaksi {

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the namaMember
     */
    public String getNamaMember() {
        return namaMember;
    }

    /**
     * @param namaMember the namaMember to set
     */
    public void setNamaMember(String namaMember) {
        this.namaMember = namaMember;
    }

    /**
     * @return the poinMember
     */
    public int getPoinMember() {
        return poinMember;
    }

    /**
     * @param poinMember the poinMember to set
     */
    public void setPoinMember(int poinMember) {
        this.poinMember = poinMember;
    }

    /**
     * @return the subtotal
     */
    public int getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the jumlahUang
     */
    public int getJumlahUang() {
        return jumlahUang;
    }

    /**
     * @param jumlahUang the jumlahUang to set
     */
    public void setJumlahUang(int jumlahUang) {
        this.jumlahUang = jumlahUang;
    }

    /**
     * @return the kembalian
     */
    public int getKembalian() {
        return kembalian;
    }

    /**
     * @param kembalian the kembalian to set
     */
    public void setKembalian(int kembalian) {
        this.kembalian = kembalian;
    }

    /**
     * @return the qrcode
     */
    public InputStream getQrcode() {
        return qrcode;
    }

    /**
     * @param qrcode the qrcode to set
     */
    public void setQrcode(InputStream qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * @return the fields
     */
    public List<FieldBarang> getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(List<FieldBarang> fields) {
        this.fields = fields;
    }

    public FieldTransaksi() {
    }

    public FieldTransaksi(String date, String namaMember, int poinMember, int subtotal, int jumlahUang, int kembalian, InputStream qrcode, List<FieldBarang> fields) {
        this.date = date;
        this.namaMember = namaMember;
        this.poinMember = poinMember;
        this.subtotal = subtotal;
        this.jumlahUang = jumlahUang;
        this.kembalian = kembalian;
        this.qrcode = qrcode;
        this.fields = fields;
    }

   
    

       private String date;
       private String namaMember;
       private int poinMember;
       private int subtotal;
       private int jumlahUang;
       private int kembalian;
       private InputStream qrcode;
       private List<FieldBarang> fields;
       
}
