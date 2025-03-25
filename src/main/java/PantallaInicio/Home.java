/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PantallaInicio;

import TweetVisual.tweets;
import Explorar.Buscador;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JLabel;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JScrollPane;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.sql.Blob;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;

import java.util.Base64;
import java.io.InputStream;
import java.io.IOException;






/**
 *
 * @author Jaime Paredes
 */
public class Home extends javax.swing.JFrame {
/*Jaime*/
private javax.swing.JTextArea txtTweet;
private File archivoImagen; 


    // Este método carga la imagen automáticamente
private void cargarFotoPerfil() {
    int idUsuario = UsuarioSesion.getUsuarioId();  // Obtener el ID del usuario actual
    UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instancia del DAO
    
    byte[] imgBytes = usuarioDAO.getFotoPerfil(idUsuario); // Obtener la imagen desde la base de datos

    if (imgBytes != null && imgBytes.length > 0) {
        // Si la imagen está presente, crear una ImageIcon
        ImageIcon imageIcon = new ImageIcon(imgBytes);
        Image image = imageIcon.getImage().getScaledInstance(
            lblFotoPerfil.getWidth(), lblFotoPerfil.getHeight(), Image.SCALE_SMOOTH); // Escalar la imagen
        lblFotoPerfil.setIcon(new ImageIcon(image)); // Establecer la imagen en el JLabel
    } else {
        // Si no hay imagen, mostrar un mensaje o imagen por defecto
        lblFotoPerfil.setIcon(null); // O puedes poner una imagen por defecto si lo prefieres
        System.out.println("No se encontró imagen para el usuario.");
    }
}




private void cargarTweets() {
    if (jTextPaneTweets == null) {
        System.err.println("jTextPaneTweets es null. No se pueden cargar tweets.");
        return;
    }

    try (Connection conexion = BasededatosTwitter.getConnection()) {
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.multimedia " +
                     "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
                     "ORDER BY t.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Obtener el documento del JTextPane
        StyledDocument doc = jTextPaneTweets.getStyledDocument();
        jTextPaneTweets.setText(""); // Limpiar contenido previo

        while (rs.next()) {
            String nombre = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            Blob multimedia = rs.getBlob("multimedia");

            // Formato del tweet
            String tweetTexto = alias + " (" + nombre + ") 📅 " + fecha + "\n" + contenido + "\n\n";
            doc.insertString(doc.getLength(), tweetTexto, null);

            // Si hay imagen, la convertimos y la mostramos
            if (multimedia != null) {
                byte[] imageBytes = multimedia.getBytes(1, (int) multimedia.length());
                ImageIcon imageIcon = new ImageIcon(imageBytes);

                // Redimensionar la imagen para que se vea bien
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(image);

                // Insertar imagen en JTextPane
                jTextPaneTweets.setCaretPosition(doc.getLength());
                jTextPaneTweets.insertIcon(imageIcon);
            }

            doc.insertString(doc.getLength(), "\n---------------------------------\n", null);
        }

    } catch (SQLException | BadLocationException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los tweets.");
    }
}





private String encodeToBase64(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
}


private void actualizarNombreYAlias() {
    int idUsuario = UsuarioSesion.getUsuarioId(); // Obtener el ID del usuario en sesión

    if (idUsuario != -1) {
        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "SELECT nombre_usuario, alias FROM usuarios WHERE id_usuarios = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre_usuario");
                String alias = rs.getString("alias");

                // Verifica si los valores son nulos
                if (nombre == null || alias == null) {
                    lblAliasNombre.setText("Datos no disponibles");
                } else {
                    lblAliasNombre.setText(nombre + "  @" + alias);
                }
            } else {
                lblAliasNombre.setText("Usuario no encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lblAliasNombre.setText("Error SQL: " + e.getMessage());
        }
    } else {
        lblAliasNombre.setText("No hay usuario en sesión");
    }
}


    /**
     * Creates new form perfilVisual
     */
public Home() {
    initComponents();  // Método generado por NetBeans GUI Builder
          cargarFotoPerfil(); // Llamamos al método para cargar la imagen de perfil

        // 🔹 Cargar los tweets al iniciar
        cargarTweets();
        
    

        setVisible(true);

}




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelTrasero = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        btnInicio2 = new javax.swing.JButton();
        btnExprorar2 = new javax.swing.JButton();
        btnNotificaciones2 = new javax.swing.JButton();
        btnPerfil2 = new javax.swing.JButton();
        PanelBuscador = new javax.swing.JPanel();
        Buscador = new javax.swing.JTextField();
        btnBusqueda = new javax.swing.JButton();
        lblFotoPerfil = new javax.swing.JLabel();
        PanelTweet = new javax.swing.JPanel();
        ScrollTweet = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        LabelPerfil = new javax.swing.JLabel();
        b_enviarTweet = new javax.swing.JButton();
        btnMultimedia = new javax.swing.JButton();
        btnGif = new javax.swing.JButton();
        btnEmojis = new javax.swing.JButton();
        btnCalendario = new javax.swing.JButton();
        lblImagenPrevia = new javax.swing.JLabel();
        btnSubirImagen = new javax.swing.JButton();
        ScrollHome = new javax.swing.JScrollPane();
        jTextPaneTweets = new javax.swing.JTextPane();
        lblAliasNombre = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelTrasero.setBackground(new java.awt.Color(255, 255, 255));

        Menu2.setBackground(new java.awt.Color(246, 234, 250));

        LogoTwitter2.setFont(new java.awt.Font("Eras Bold ITC", 0, 36)); // NOI18N
        LogoTwitter2.setForeground(new java.awt.Color(102, 0, 153));
        LogoTwitter2.setText("Twitter");

        btnInicio2.setBackground(new java.awt.Color(246, 234, 250));
        btnInicio2.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        btnInicio2.setForeground(new java.awt.Color(102, 0, 153));
        btnInicio2.setText("Inicio");
        btnInicio2.setBorder(null);
        btnInicio2.setContentAreaFilled(false);
        btnInicio2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInicio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicio2ActionPerformed(evt);
            }
        });

        btnExprorar2.setBackground(new java.awt.Color(246, 234, 250));
        btnExprorar2.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        btnExprorar2.setForeground(new java.awt.Color(102, 0, 153));
        btnExprorar2.setText("Explorar");
        btnExprorar2.setBorder(null);
        btnExprorar2.setContentAreaFilled(false);
        btnExprorar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExprorar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExprorar2ActionPerformed(evt);
            }
        });

        btnNotificaciones2.setBackground(new java.awt.Color(246, 234, 250));
        btnNotificaciones2.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        btnNotificaciones2.setForeground(new java.awt.Color(102, 0, 153));
        btnNotificaciones2.setText("Notificaciones");
        btnNotificaciones2.setBorder(null);
        btnNotificaciones2.setContentAreaFilled(false);
        btnNotificaciones2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNotificaciones2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificaciones2ActionPerformed(evt);
            }
        });

        btnPerfil2.setBackground(new java.awt.Color(246, 234, 250));
        btnPerfil2.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        btnPerfil2.setForeground(new java.awt.Color(102, 0, 153));
        btnPerfil2.setText("Perfil");
        btnPerfil2.setBorder(null);
        btnPerfil2.setContentAreaFilled(false);
        btnPerfil2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu2Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNotificaciones2)
                    .addComponent(btnInicio2)
                    .addComponent(btnExprorar2)
                    .addComponent(btnPerfil2)
                    .addComponent(LogoTwitter2))
                .addGap(9, 9, 9))
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LogoTwitter2)
                .addGap(35, 35, 35)
                .addComponent(btnInicio2)
                .addGap(34, 34, 34)
                .addComponent(btnPerfil2)
                .addGap(41, 41, 41)
                .addComponent(btnExprorar2)
                .addGap(43, 43, 43)
                .addComponent(btnNotificaciones2)
                .addContainerGap(390, Short.MAX_VALUE))
        );

        PanelBuscador.setBackground(new java.awt.Color(255, 255, 255));

        Buscador.setBackground(new java.awt.Color(246, 234, 250));
        Buscador.setText("Buscar");
        Buscador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscadorActionPerformed(evt);
            }
        });

        btnBusqueda.setBackground(new java.awt.Color(246, 234, 250));

        lblFotoPerfil.setText("jLabel1");
        lblFotoPerfil.setPreferredSize(new java.awt.Dimension(150, 150));
        lblFotoPerfil.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblFotoPerfilAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout PanelBuscadorLayout = new javax.swing.GroupLayout(PanelBuscador);
        PanelBuscador.setLayout(PanelBuscadorLayout);
        PanelBuscadorLayout.setHorizontalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelBuscadorLayout.setVerticalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBusqueda))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        PanelTweet.setBackground(new java.awt.Color(255, 255, 255));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        ScrollTweet.setViewportView(jTextArea1);

        b_enviarTweet.setBackground(new java.awt.Color(246, 234, 250));
        b_enviarTweet.setText("Tweet");
        b_enviarTweet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_enviarTweetActionPerformed(evt);
            }
        });

        btnMultimedia.setBorder(null);
        btnMultimedia.setContentAreaFilled(false);
        btnMultimedia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnGif.setBorder(null);
        btnGif.setContentAreaFilled(false);
        btnGif.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEmojis.setBorder(null);
        btnEmojis.setContentAreaFilled(false);
        btnEmojis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnCalendario.setBorder(null);
        btnCalendario.setContentAreaFilled(false);
        btnCalendario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnSubirImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/multimedia.png"))); // NOI18N
        btnSubirImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirImagenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelTweetLayout = new javax.swing.GroupLayout(PanelTweet);
        PanelTweet.setLayout(PanelTweetLayout);
        PanelTweetLayout.setHorizontalGroup(
            PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTweetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelPerfil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelTweetLayout.createSequentialGroup()
                        .addComponent(ScrollTweet, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_enviarTweet))
                    .addGroup(PanelTweetLayout.createSequentialGroup()
                        .addComponent(btnMultimedia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblImagenPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelTweetLayout.createSequentialGroup()
                                .addComponent(btnGif)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEmojis)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCalendario))
                            .addComponent(btnSubirImagen))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelTweetLayout.setVerticalGroup(
            PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTweetLayout.createSequentialGroup()
                .addGroup(PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelTweetLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(LabelPerfil))
                    .addGroup(PanelTweetLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(b_enviarTweet)
                            .addComponent(ScrollTweet, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblImagenPrevia)
                    .addComponent(btnSubirImagen))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelTweetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMultimedia)
                    .addComponent(btnGif)
                    .addComponent(btnEmojis)
                    .addComponent(btnCalendario))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextPaneTweets.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jTextPaneTweetsAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        ScrollHome.setViewportView(jTextPaneTweets);

        lblAliasNombre.setText("jLabel1");
        lblAliasNombre.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblAliasNombreAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout PanelTraseroLayout = new javax.swing.GroupLayout(PanelTrasero);
        PanelTrasero.setLayout(PanelTraseroLayout);
        PanelTraseroLayout.setHorizontalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelTraseroLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelTraseroLayout.createSequentialGroup()
                                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PanelTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ScrollHome, javax.swing.GroupLayout.PREFERRED_SIZE, 797, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(PanelTraseroLayout.createSequentialGroup()
                        .addGap(591, 591, 591)
                        .addComponent(lblAliasNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74))
        );
        PanelTraseroLayout.setVerticalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(lblAliasNombre)
                .addGap(18, 18, 18)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PanelTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(ScrollHome, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelTrasero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelTrasero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
        actualizarNombreYAlias(); // Llamar al método al cargar la interfaz
    }//GEN-LAST:event_lblAliasNombreAncestorAdded

    private void btnSubirImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirImagenActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes y GIFs", "jpg", "png", "gif"));

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivoImagen = fileChooser.getSelectedFile();

            // Mostrar vista previa de la imagen
            ImageIcon imagen = new ImageIcon(archivoImagen.getAbsolutePath());
            lblImagenPrevia.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }

    }//GEN-LAST:event_btnSubirImagenActionPerformed

    private void b_enviarTweetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_enviarTweetActionPerformed
        String contenido = jTextArea1.getText().trim();
        int usuarioId = UsuarioSesion.getUsuarioId();

        if (contenido.isEmpty() && archivoImagen == null) {
            JOptionPane.showMessageDialog(this, "No puedes publicar un tweet vacío.");
            return;
        }

        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "INSERT INTO tweets (usuario_id, contenido, fecha_creacion, multimedia) VALUES (?, ?, NOW(), ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ps.setString(2, contenido);

            if (archivoImagen != null) {
                FileInputStream fis = new FileInputStream(archivoImagen);
                ps.setBinaryStream(3, fis, (int) archivoImagen.length());
            } else {
                ps.setNull(3, java.sql.Types.BLOB); // Si no hay imagen, se pone NULL
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "¡Tweet publicado con éxito!");

            // Limpiar los campos después de publicar
            jTextArea1.setText("");
            lblImagenPrevia.setIcon(null);
            archivoImagen = null;

        } catch (SQLException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error al publicar el tweet: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_b_enviarTweetActionPerformed

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded
        lblFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void BuscadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscadorActionPerformed

    private void btnNotificaciones2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificaciones2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNotificaciones2ActionPerformed

    private void btnExprorar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExprorar2ActionPerformed
        Buscador b = new Buscador();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnExprorar2ActionPerformed

    private void btnInicio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicio2ActionPerformed
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInicio2ActionPerformed

    private void jTextPaneTweetsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTextPaneTweetsAncestorAdded
jTextPaneTweets = new JTextPane();  // Crear una nueva instancia en caso de que sea null
jTextPaneTweets.setContentType("text/html"); // Permite mostrar imágenes y texto con formato
jTextPaneTweets.setEditable(false); // Evita que el usuario escriba en él
jScrollPane1.setViewportView(jTextPaneTweets); // Asignar el JTextPane al JScrollPane


    }//GEN-LAST:event_jTextPaneTweetsAncestorAdded
byte[] fotoBytes = null; // Declaración

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Buscador;
    private javax.swing.JLabel LabelPerfil;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JPanel PanelBuscador;
    private javax.swing.JPanel PanelTrasero;
    private javax.swing.JPanel PanelTweet;
    private javax.swing.JScrollPane ScrollHome;
    private javax.swing.JScrollPane ScrollTweet;
    private javax.swing.JButton b_enviarTweet;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnCalendario;
    private javax.swing.JButton btnEmojis;
    private javax.swing.JButton btnExprorar2;
    private javax.swing.JButton btnGif;
    private javax.swing.JButton btnInicio2;
    private javax.swing.JButton btnMultimedia;
    private javax.swing.JButton btnNotificaciones2;
    private javax.swing.JButton btnPerfil2;
    private javax.swing.JButton btnSubirImagen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextPane jTextPaneTweets;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblImagenPrevia;
    // End of variables declaration//GEN-END:variables
}
