/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package IniciarSesion;
//Hola
/**
 *
 * @author alan_
 */
import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
import UsuarioID.UsuarioSesion;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import UsuarioID.UsuarioSesion; // Agregar esta línea arriba de la clase
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import EditarPerfil.EdiPerfil;

public class Iniciarsesionlogin extends javax.swing.JFrame {
    
private void ejecutarInicioSesion() {
    Connection conexion = null;
    try {
        // Obtener conexión a la base de datos
        conexion = RetornarBaseDedatos.getConnection();
        if (conexion == null) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
            return;
        }

        // Tomar los datos ingresados por el usuario
        String usuarioOEmail = tf_correoyacuenta.getText().trim(); // Puede ser usuario o email
        String password = tf_passwordyacuenta.getText().trim();

        // Verificar que los campos no estén vacíos
        if (usuarioOEmail.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.");
            return;
        }

        int idUsuario = obtenerIdUsuario(usuarioOEmail, password);
        if (idUsuario != -1) {
            System.out.println("ID del usuario obtenido: " + idUsuario); // Depuración
            UsuarioSesion.setUsuarioId(idUsuario);

            // Cambiar a pantalla de inicio
            PantallaInicio.Home inicio1 = new PantallaInicio.Home();
            inicio1.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la cuenta, usuario/correo y/o contraseña incorrectos.");
        }
    } finally {
        try {
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    /**
     * Creates new form login1123213
     */
public int obtenerIdUsuario(String usuarioOEmail, String password) {
    Connection conexion = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int idUsuario = -1;

    try {
        conexion = RetornarBaseDedatos.getConnection();
        String consulta = "SELECT id_usuarios FROM usuarios WHERE (email = ? OR nombre_usuario = ?) AND password = ?";
        pst = conexion.prepareStatement(consulta);
        pst.setString(1, usuarioOEmail);
        pst.setString(2, usuarioOEmail);
        pst.setString(3, password);

        rs = pst.executeQuery();
        if (rs.next()) {
            idUsuario = rs.getInt("id_usuarios");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return idUsuario;
}

 private void agregarListenersEnter() {
        tf_correoyacuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarInicioSesion();
            }
        });

        tf_passwordyacuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarInicioSesion();
            }
        });
    }

    //Crear verificacion de si se encuentra la cuenta
   public static boolean verificarExistencianombreusuario(String nombreusuario) {
Connection conexion = RetornarBaseDedatos.getConnection();
    
    if (conexion == null) {
        return false;
    }

    String consulta = "SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?"; 

    try {
        PreparedStatement ps = conexion.prepareStatement(consulta);
        ps.setString(1, nombreusuario);  
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0; // Si count > 0, el usuario existe
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
   
public class VentanaUtilidades {

    public static void configurarResolucionVentana(JFrame ventana) {
        ventana.setPreferredSize(new Dimension(1920, 1080));
        // O
        // ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
    
    public Iniciarsesionlogin() 
    {
        initComponents();
        // b_iniciarsesionyacuenta.setEnabled(false); // Inicialmente deshabilitado hasta marcar los terminos.
                 VentanaUtilidades.configurarResolucionVentana(this);
        agregarListenersEnter();

        //Letras azules boton iniciar sesion
        b_crearcuentayacuenta.setText("Crea una");
        b_crearcuentayacuenta.setBorder(null);
        b_crearcuentayacuenta.setContentAreaFilled(false);
        b_crearcuentayacuenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        b_crearcuentayacuenta.addActionListener(new java.awt.event.ActionListener() 
        {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) 
        {
       b_crearcuentayacuentaActionPerformed(evt);
        }
        });

        // Agrega el MouseListener para cambiar el color del texto al pasar el mouse
        b_crearcuentayacuenta.addMouseListener(new java.awt.event.MouseAdapter() 
        {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) 
        {
        // Cambiar el color del texto cuando el mouse pasa por encima
        b_crearcuentayacuenta.setForeground(java.awt.Color.BLUE);
        }
    
        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) 
        {
        // Restaurar el color original cuando el mouse sale
        b_crearcuentayacuenta.setForeground(java.awt.Color.BLACK);
        }
        });
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelizquierdo_loginyacuenta = new javax.swing.JPanel();
        l_texttwitterloginyacuenta = new javax.swing.JLabel();
        l_fondoiniciosesionyacuenta = new javax.swing.JLabel();
        panelderecho_loginyacuenta = new javax.swing.JPanel();
        l_textcraeteaccountyacuenta = new javax.swing.JLabel();
        l_correoyacuenta = new javax.swing.JLabel();
        l_passwordyacuenta = new javax.swing.JLabel();
        tf_correoyacuenta = new javax.swing.JTextField();
        tf_passwordyacuenta = new javax.swing.JPasswordField();
        b_showpasswordyacuenta = new javax.swing.JButton();
        l1_passwordconditionsyacuenta = new javax.swing.JLabel();
        l2_passwordconditions = new javax.swing.JLabel();
        b_iniciarsesionyacuenta = new javax.swing.JButton();
        l_iniciarsesionyacuenta = new javax.swing.JLabel();
        b_crearcuentayacuenta = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelizquierdo_loginyacuenta.setBackground(new java.awt.Color(255, 255, 255));
        panelizquierdo_loginyacuenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_texttwitterloginyacuenta.setFont(new java.awt.Font("Times New Roman", 2, 60)); // NOI18N
        l_texttwitterloginyacuenta.setForeground(new java.awt.Color(255, 255, 255));
        l_texttwitterloginyacuenta.setText("Twitter");
        panelizquierdo_loginyacuenta.add(l_texttwitterloginyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, 40));

        l_fondoiniciosesionyacuenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/Fondologinvertical.jpg"))); // NOI18N
        panelizquierdo_loginyacuenta.add(l_fondoiniciosesionyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 460));

        panelderecho_loginyacuenta.setBackground(new java.awt.Color(255, 255, 255));
        panelderecho_loginyacuenta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_textcraeteaccountyacuenta.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        l_textcraeteaccountyacuenta.setText("Inicio de sesion");
        panelderecho_loginyacuenta.add(l_textcraeteaccountyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

        l_correoyacuenta.setText("Nombre usuario / Correo");
        panelderecho_loginyacuenta.add(l_correoyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        l_passwordyacuenta.setText("Contraseña");
        panelderecho_loginyacuenta.add(l_passwordyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));

        tf_correoyacuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_correoyacuentaActionPerformed(evt);
            }
        });
        panelderecho_loginyacuenta.add(tf_correoyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 278, -1));

        tf_passwordyacuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_passwordyacuentaActionPerformed(evt);
            }
        });
        panelderecho_loginyacuenta.add(tf_passwordyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 278, -1));

        b_showpasswordyacuenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/hidepassword10.png"))); // NOI18N
        b_showpasswordyacuenta.setBorder(null);
        b_showpasswordyacuenta.setBorderPainted(false);
        b_showpasswordyacuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_showpasswordyacuentaActionPerformed(evt);
            }
        });
        panelderecho_loginyacuenta.add(b_showpasswordyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, 20, 20));

        l1_passwordconditionsyacuenta.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        l1_passwordconditionsyacuenta.setForeground(new java.awt.Color(153, 153, 153));
        l1_passwordconditionsyacuenta.setText("Ingresa la contraseña de tu cuenta Twitter.");
        panelderecho_loginyacuenta.add(l1_passwordconditionsyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        l2_passwordconditions.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        l2_passwordconditions.setForeground(new java.awt.Color(153, 153, 153));
        l2_passwordconditions.setText("La contraseña es sensible al uso de mayusculas y minusculas.");
        panelderecho_loginyacuenta.add(l2_passwordconditions, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, -1, -1));

        b_iniciarsesionyacuenta.setBackground(new java.awt.Color(237, 186, 250));
        b_iniciarsesionyacuenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        b_iniciarsesionyacuenta.setForeground(new java.awt.Color(255, 255, 255));
        b_iniciarsesionyacuenta.setText("Iniciar sesion");
        b_iniciarsesionyacuenta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        b_iniciarsesionyacuenta.setBorderPainted(false);
        b_iniciarsesionyacuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_iniciarsesionyacuentaActionPerformed(evt);
            }
        });
        panelderecho_loginyacuenta.add(b_iniciarsesionyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 136, 35));

        l_iniciarsesionyacuenta.setText("¿No tienes cuenta?");
        panelderecho_loginyacuenta.add(l_iniciarsesionyacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, -1, -1));

        b_crearcuentayacuenta.setText("Crea una ");
        b_crearcuentayacuenta.setBorder(null);
        b_crearcuentayacuenta.setContentAreaFilled(false);
        b_crearcuentayacuenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        b_crearcuentayacuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_crearcuentayacuentaActionPerformed(evt);
            }
        });
        panelderecho_loginyacuenta.add(b_crearcuentayacuenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 420, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelizquierdo_loginyacuenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelderecho_loginyacuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelizquierdo_loginyacuenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelderecho_loginyacuenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_showpasswordyacuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_showpasswordyacuentaActionPerformed
        // TODO add your handling code here:
        //MOSTRAR Y OCULTAR CONTRASEÑA EN CUADRO DE PASSWORD
        if (tf_passwordyacuenta.getEchoChar() == '\u2022') { // Si está oculta
        tf_passwordyacuenta.setEchoChar((char) 0); // Mostrar la contraseña
        b_showpasswordyacuenta.setIcon(new javax.swing.ImageIcon("src\\main\\resources\\Resource\\hidepassword10.png")); // Cambiar icono a "mostrar"
    } else { // Si está visible
        tf_passwordyacuenta.setEchoChar('\u2022'); // Ocultar la contraseña
        b_showpasswordyacuenta.setIcon(new javax.swing.ImageIcon("src\\main\\resources\\Resource\\showpassword10.png")); // Cambiar icono a "ocultar"
    }
    }//GEN-LAST:event_b_showpasswordyacuentaActionPerformed

    private void b_iniciarsesionyacuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_iniciarsesionyacuentaActionPerformed

    ejecutarInicioSesion();
    }//GEN-LAST:event_b_iniciarsesionyacuentaActionPerformed
    private static IniciarSesion.login ventanaCrearCuentaVisible = null;

    private void b_crearcuentayacuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_crearcuentayacuentaActionPerformed
         
 if (ventanaCrearCuentaVisible == null || !ventanaCrearCuentaVisible.isVisible()) {
            // Si no existe una instancia visible, creamos una nueva y la mostramos
            ventanaCrearCuentaVisible = new IniciarSesion.login();
            ventanaCrearCuentaVisible.setVisible(true);
            this.dispose(); // Cierra la ventana actual (asumiendo que es la de inicio de sesión)
        } else {
            // Si ya existe una instancia visible, puedes simplemente enfocarla
            ventanaCrearCuentaVisible.toFront();
        }
 
    }//GEN-LAST:event_b_crearcuentayacuentaActionPerformed

    private void tf_correoyacuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_correoyacuentaActionPerformed
      tf_correoyacuenta.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        ejecutarInicioSesion();
    }
});
      
    }//GEN-LAST:event_tf_correoyacuentaActionPerformed

    private void tf_passwordyacuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_passwordyacuentaActionPerformed
       tf_passwordyacuenta.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        ejecutarInicioSesion();
    }
});
    }//GEN-LAST:event_tf_passwordyacuentaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Iniciarsesionlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Iniciarsesionlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Iniciarsesionlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Iniciarsesionlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Iniciarsesionlogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_crearcuentayacuenta;
    private javax.swing.JButton b_iniciarsesionyacuenta;
    private javax.swing.JButton b_showpasswordyacuenta;
    private javax.swing.JLabel l1_passwordconditionsyacuenta;
    private javax.swing.JLabel l2_passwordconditions;
    private javax.swing.JLabel l_correoyacuenta;
    private javax.swing.JLabel l_fondoiniciosesionyacuenta;
    private javax.swing.JLabel l_iniciarsesionyacuenta;
    private javax.swing.JLabel l_passwordyacuenta;
    private javax.swing.JLabel l_textcraeteaccountyacuenta;
    private javax.swing.JLabel l_texttwitterloginyacuenta;
    private javax.swing.JPanel panelderecho_loginyacuenta;
    private javax.swing.JPanel panelizquierdo_loginyacuenta;
    private javax.swing.JTextField tf_correoyacuenta;
    private javax.swing.JPasswordField tf_passwordyacuenta;
    // End of variables declaration//GEN-END:variables
}
