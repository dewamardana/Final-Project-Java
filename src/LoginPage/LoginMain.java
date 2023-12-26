
package LoginPage;


import AdminPage.MainAdmin;
import java.awt.Color;
import Komponen.ModelColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import Koneksi.DatabaseKoneksi;
import UserPage.Usermain;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.EventQueue;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Mardana
 */
public class LoginMain extends javax.swing.JFrame {
    
    private Animator animatorLogin;
    boolean action = true;
    boolean state = false;
    

//    private Animator animatorBody;
//    private boolean signIn;

    
    public LoginMain() {
        initComponents();
        setResizable(false);
        getContentPane().setBackground(new Color(255,255,255));
        TimingTarget targetLogin = new TimingTargetAdapter() {
         @Override
            public void timingEvent(float fraction) {
                background1.setAnimate(fraction);
            }
        };
        
        animatorLogin = new Animator(2000, targetLogin);
        //animatorBody = new Animator(500, targetBody);
        animatorLogin.setResolution(0);
//        animatorBody.setResolution(0);
        //jScrollPane1.getViewport().setOpaque(false);
        //jScrollPane1.setViewportBorder(null);
    }

    boolean Empty(){
        String user = txt_user.getText().trim();
        String pass = String.valueOf(txt_pass.getPassword());
        if (user.equals("")) {
                txt_user.setHelperText("Please input user name");
                label_info.setText("");
                txt_user.grabFocus();
                return true;
        }else{
            txt_user.setHelperText("");
        }
        
        if(pass.equals("")) {
                txt_pass.setHelperText("Please input password");
                label_info.setText("");
                if (action) {
                 txt_pass.grabFocus();
                }
                return true;
        }
        return false;
    }
    
    
boolean Akses() {
    try {
        Connection connection = DatabaseKoneksi.createConnection();

        String query = "SELECT * FROM tb_user WHERE username = ? AND password = ? LIMIT 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, txt_user.getText().trim());
        preparedStatement.setString(2, String.valueOf(txt_pass.getPassword()));
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            boolean status = resultSet.getBoolean("admin");

            if (status) {
                state = true;
            }
            return true;
        } else {
            label_info.setText("Username atau Password Salah");
            txt_pass.setText("");
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    
    
    void Login(){
        if (!animatorLogin.isRunning()) {
//            signIn = true;
            if(Empty() != true){
              
                if (Akses() == true) {
                    animatorLogin.start();
                    Timer timer = new Timer(700, (ActionEvent e) -> {
                        if(state == true){
                            new MainAdmin().setVisible(true);
                            EventQueue.invokeLater(() -> {
                            FlatAnimatedLafChange.showSnapshot();
                            FlatDarculaLaf.setup();
                            FlatLaf.updateUI();
                            FlatAnimatedLafChange.hideSnapshotWithAnimation();
                            });
                            dispose();
                        }else{
                            new Usermain().setVisible(true);
                            EventQueue.invokeLater(() -> {
                            FlatAnimatedLafChange.showSnapshot();
                            FlatDarculaLaf.setup();
                            FlatLaf.updateUI();
                            FlatAnimatedLafChange.hideSnapshotWithAnimation();
                            });
                            dispose();
                        }
                    });
                    timer.setRepeats(false); // Agar timer hanya berjalan sekali
                    timer.start();
//                enableLogin(false);
                }
            }
        }
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background1 = new Komponen.Background();
        panelTransparent1 = new Komponen.PanelTransparent();
        label_info = new javax.swing.JLabel();
        button1 = new Komponen.Button();
        txt_user = new Komponen.TextField();
        txt_pass = new Komponen.PasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        label_info.setForeground(new java.awt.Color(255, 51, 51));

        button1.setBackground(new java.awt.Color(157, 153, 255));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Sign in");
        button1.setBorderColor(new java.awt.Color(157, 153, 255));
        button1.setColor(new java.awt.Color(157, 153, 255));
        button1.setColorClick(new java.awt.Color(157, 153, 255));
        button1.setColorOver(new java.awt.Color(220, 219, 255));
        button1.setFont(new java.awt.Font("Insaniburger", 0, 18)); // NOI18N
        button1.setRadius(20);
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        txt_user.setLabelText("Username");

        txt_pass.setLabelText("Password");

        javax.swing.GroupLayout panelTransparent1Layout = new javax.swing.GroupLayout(panelTransparent1);
        panelTransparent1.setLayout(panelTransparent1Layout);
        panelTransparent1Layout.setHorizontalGroup(
            panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent1Layout.createSequentialGroup()
                .addGroup(panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransparent1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label_info, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_user, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_pass, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))))
                    .addGroup(panelTransparent1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        panelTransparent1Layout.setVerticalGroup(
            panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTransparent1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_info, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout background1Layout = new javax.swing.GroupLayout(background1);
        background1.setLayout(background1Layout);
        background1Layout.setHorizontalGroup(
            background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(background1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(panelTransparent1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        background1Layout.setVerticalGroup(
            background1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panelTransparent1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainLogo/Login-logo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Poppins SemiBold", 1, 48)); // NOI18N
        jLabel2.setText("Swift Mart");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        Login();
    }//GEN-LAST:event_button1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLaf.registerCustomDefaultsSource("MainStyle");
        FlatIntelliJLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new splashscreen.SplashScreen(null,true).setVisible(true);
                new LoginMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Komponen.Background background1;
    private Komponen.Button button1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel label_info;
    private Komponen.PanelTransparent panelTransparent1;
    private Komponen.PasswordField txt_pass;
    private Komponen.TextField txt_user;
    // End of variables declaration//GEN-END:variables
}
