
package PrintStruk;

/**
 *
 * @author Mardana
 */
public class FieldBarang {

    /**
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }

    /**
     * @return the harga
     */
    public int getHarga() {
        return harga;
    }

    /**
     * @param harga the harga to set
     */
    public void setHarga(int harga) {
        this.harga = harga;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    public FieldBarang() {
    }

    public FieldBarang(String nama, int qty, int harga, int total) {
        this.nama = nama;
        this.qty = qty;
        this.harga = harga;
        this.total = total;
    }

   

  
    private String nama;
    private int qty;
    private int harga;
    private int total;
}
