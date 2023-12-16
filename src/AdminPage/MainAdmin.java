
package AdminPage;

import LoginPage.LoginMain;
import com.formdev.flatlaf.FlatIntelliJLaf;
import MainKomponen.EventMenu;
import MainKomponen.MainForm;
import MainKomponen.MenuItem;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author Mardana
 */
public class MainAdmin extends javax.swing.JFrame {

   
    public MainAdmin() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.addEventMenuSelected(new EventMenu() {
            @Override
            public void mainMenuSelected(MainForm mainForm, int index, MenuItem menuItem) {
                if (index ==  5){
                    new LoginMain().setVisible(true);
                    dispose();
                        EventQueue.invokeLater(() -> {
                            FlatAnimatedLafChange.showSnapshot();
                            FlatIntelliJLaf.setup();
                            FlatLaf.updateUI();
                            FlatAnimatedLafChange.hideSnapshotWithAnimation();
                        });
                }
            }

            @Override
            public void subMenuSelected(MainForm mainForm, int index, int subMenuIndex, Component menuItem) {
                if(index == 0 && subMenuIndex == 0){
                    mainForm.displayForm(new Panel1());
                }else if (index == 0 && subMenuIndex == 1){
                    mainForm.displayForm(new Panel2());
                }else if (index == 0 && subMenuIndex == 2){
                    mainForm.displayForm(new Panel3());
                }else if (index == 1 && subMenuIndex == 0){
                    mainForm.displayForm(new Panel4());
                }else if (index == 1 && subMenuIndex == 1){
                    mainForm.displayForm(new Panel5());
                }else if (index == 1 && subMenuIndex == 2){
                    mainForm.displayForm(new Panel6());
                }else if (index == 2 && subMenuIndex == 0){
                    mainForm.displayForm(new Panel7());
                }else if (index == 2 && subMenuIndex == 1){
                    mainForm.displayForm(new Panel8());
                }else if (index == 2 && subMenuIndex == 2){
                    mainForm.displayForm(new Panel9());
                }else if (index == 3 && subMenuIndex == 0){
                    mainForm.displayForm(new Panel10()); 
                }else if (index == 4 && subMenuIndex == 0){
                    mainForm.displayForm(new Panel11());
                }else if (index == 4 && subMenuIndex == 1){
                    mainForm.displayForm(new Panel12());
                }else if (index == 4 && subMenuIndex == 2){
                    mainForm.displayForm(new Panel13());
                }else{
                    mainForm.displayForm(new Panel14());
                }
            }
        });
        menu.setSelectedIndex(0); 
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        subMenuPanel1 = new MainKomponen.SubMenuPanel();
        menu = new MainKomponen.Menu();
        jLabel1 = new javax.swing.JLabel();
        menuItem1 = new MainKomponen.MenuItem();
        menuItem2 = new MainKomponen.MenuItem();
        menuItem3 = new MainKomponen.MenuItem();
        menuItem5 = new MainKomponen.MenuItem();
        menuItem4 = new MainKomponen.MenuItem();
        menuItem6 = new MainKomponen.MenuItem();
        mainForm = new MainKomponen.MainForm();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuDarkMode = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        subMenuPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(80, 1, 1, 1));

        menu.setMainForm(mainForm);
        menu.setSubMenuPanel(subMenuPanel1);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/logo.png"))); // NOI18N
        menu.add(jLabel1);

        menuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/produk-0.png"))); // NOI18N
        menuItem1.setText("menuItem1");
        menuItem1.setMenuIcon(new javax.swing.AbstractListModel() {
            String[] strings = { "MainLogo/produk-1.png", "MainLogo/produk-2.png", "MainLogo/produk-3.png" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem1.setMenuModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tambah Produk", "Edit Produk", "Hapus Produk" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menu.add(menuItem1);

        menuItem2.setBackground(new java.awt.Color(255, 255, 51));
        menuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/pegawai-0.png"))); // NOI18N
        menuItem2.setText("menuItem2");
        menuItem2.setMenuIcon(new javax.swing.AbstractListModel() {
            String[] strings = { "MainLogo/pegawai-1.png", "MainLogo/pegawai-2.png", "MainLogo/pegawai-3.png" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem2.setMenuModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tambah Pegawai", "Edit Pegawai", "Hapus Pegawai", " ", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem2ActionPerformed(evt);
            }
        });
        menu.add(menuItem2);

        menuItem3.setBackground(new java.awt.Color(0, 255, 0));
        menuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/diskon-0.png"))); // NOI18N
        menuItem3.setText("menuItem3");
        menuItem3.setMenuIcon(new javax.swing.AbstractListModel() {
            String[] strings = { "MainLogo/diskon-1.png", "MainLogo/diskon-2.png", "MainLogo/diskon-3.png", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem3.setMenuModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tambah Diskon", "Edit Diskon", "Hapus Diskon", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItem3ActionPerformed(evt);
            }
        });
        menu.add(menuItem3);

        menuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/data-transaksi-0.png"))); // NOI18N
        menuItem5.setText("menuItem5");
        menuItem5.setMenuIcon(new javax.swing.AbstractListModel() {
            String[] strings = { "MainLogo/data-transaksi-1.png" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem5.setMenuModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Data Transaksi" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menu.add(menuItem5);

        menuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/transaksi-0.png"))); // NOI18N
        menuItem4.setText("menuItem4");
        menuItem4.setMenuIcon(new javax.swing.AbstractListModel() {
            String[] strings = { "MainLogo/produk-1.png", "MainLogo/produk-2.png", "MainLogo/produk-3.png" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menuItem4.setMenuModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Tambah Suppliyer", "Edit Suppliyer", "Hapus Suppliyer" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        menu.add(menuItem4);

        menuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/EXIT.png"))); // NOI18N
        menuItem6.setText("menuItem6");
        menu.add(menuItem6);

        jMenu1.setText("Mode");

        menuDarkMode.setText("Light Mode");
        menuDarkMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuDarkModeActionPerformed(evt);
            }
        });
        jMenu1.add(menuDarkMode);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(subMenuPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainForm, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
            .addComponent(subMenuPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItem3ActionPerformed

    private void menuDarkModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuDarkModeActionPerformed
        if (!menuDarkMode.isSelected()) {
            EventQueue.invokeLater(() -> {
                FlatAnimatedLafChange.showSnapshot();
                FlatDarculaLaf.setup();
                FlatLaf.updateUI();
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
            });
        } else {
            EventQueue.invokeLater(() -> {
                FlatAnimatedLafChange.showSnapshot();
                FlatIntelliJLaf.setup();
                FlatLaf.updateUI();
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
            });
        }
    }//GEN-LAST:event_menuDarkModeActionPerformed

    private void menuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItem2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLaf.registerCustomDefaultsSource("MainStyle");
        FlatDarculaLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private MainKomponen.MainForm mainForm;
    private MainKomponen.Menu menu;
    private javax.swing.JCheckBoxMenuItem menuDarkMode;
    private MainKomponen.MenuItem menuItem1;
    private MainKomponen.MenuItem menuItem2;
    private MainKomponen.MenuItem menuItem3;
    private MainKomponen.MenuItem menuItem4;
    private MainKomponen.MenuItem menuItem5;
    private MainKomponen.MenuItem menuItem6;
    private MainKomponen.SubMenuPanel subMenuPanel1;
    // End of variables declaration//GEN-END:variables
}
