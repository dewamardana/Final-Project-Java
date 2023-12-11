
package PrintStruk;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mardana
 */
public class cetakStruk {
    
    private static cetakStruk instance;
    private JasperReport jr;
    
    public static cetakStruk getInstace(){
        if(instance == null){
            instance = new cetakStruk();
        }
        
        return instance;
    }
    
    
    private cetakStruk(){
    }
    
    public void compileStruk() throws JRException{
        jr = JasperCompileManager.compileReport(getClass().getResourceAsStream("/PrintStruk/StrukTransaksi.jrxml"));
    }
    
    public void cetakStrukTransaksi(FieldTransaksi data) throws JRException{
        Map m = new HashMap();
        m.put("date", data.getDate());
        m.put("namaMember", data.getNamaMember());
        m.put("poinMember", data.getPoinMember());
        m.put("qrcode", data.getQrcode());
        m.put("subtotal", data.getSubtotal());
        m.put("jumlahUang", data.getJumlahUang());
        m.put("kembalian", data.getKembalian());
        
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data.getFields());
        JasperPrint print = JasperFillManager.fillReport(jr, m, dataSource);
        view(print);
    }
    
     private void view(JasperPrint print) throws JRException {
        JasperViewer.viewReport(print, false);
    }
}

