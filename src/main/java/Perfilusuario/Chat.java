/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Perfilusuario;
import IniciarSesion.Iniciarsesionlogin;
import Explorar.BusquedaTwitter;
import PantallaInicio.Home;
import EditarPerfil.EdiPerfil;
import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
import UsuarioID.UsuarioSesion;
import java.awt.Color;
import java.awt.Insets;  // Este es el import necesario para setMargin()
import Explorar.BusquedaTwitter;
import java.awt.Dimension;
import Explorar.BusquedaTwitter;
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
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import Explorar.BusquedaTwitter;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.ByteArrayOutputStream; // Importar ByteArrayOutputStream
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; // Para el cursor de mano
import Explorar.BusquedaTwitter;
import PantallaInicio.Home;
import IniciarSesion.Iniciarsesionlogin;
import java.awt.Color;
import javax.swing.SwingConstants;
import UsuarioID.UsuarioSesion;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent; // También necesitas importar ActionEvent
import java.util.List;
import java.awt.BorderLayout; // Importar BorderLayout
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.ByteArrayInputStream;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Macke
 */
public class Chat extends  JFrame {
    private int idUsuario, idDestinatario;
        private Connection conexion;

    private DefaultTableModel modelo = new DefaultTableModel(new String[]{"Remitente", "Contenido", "Fecha"}, 0);

    public Chat(int idUsuario, int idDestinatario) {
        this.idUsuario = idUsuario;
        this.idDestinatario = idDestinatario;
                conexion = RetornarBaseDedatos.getConnection();

        setTitle("Chat con Usuario " + obtenerNombreUsuario(idDestinatario));
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal del chat
        JPanel panelChat = new JPanel(new BorderLayout());

        // Área de mensajes con scroll
        txtChat = new JTextPane();
        txtChat.setEditable(false);
        JScrollPane scrollChat = new JScrollPane(txtChat);

        // Campo de texto y botones
        txtMensaje = new JTextField(30);
        btnEnviar = new JButton("Enviar");
        btnAdjuntar = new JButton("📎 Adjuntar");
        btnCerrarChat = new JButton("❌ Cerrar");

        // Panel inferior con el campo de texto y botones
        JPanel panelEscribir = new JPanel(new FlowLayout());
        panelEscribir.add(txtMensaje);
        panelEscribir.add(btnEnviar);
        panelEscribir.add(btnAdjuntar);

        // Agregar todo al panel principal
        panelChat.add(scrollChat, BorderLayout.CENTER);
        panelChat.add(panelEscribir, BorderLayout.SOUTH);
        panelChat.add(btnCerrarChat, BorderLayout.NORTH);

        // Agregar panel al JFrame
        add(panelChat);

        // Cargar historial de mensajes
        cargarHistorialMensajes();

        // Listeners
     btnEnviar.addActionListener(e -> enviarMensaje(null));
        txtMensaje.addActionListener(e -> enviarMensaje(null));
        btnAdjuntar.addActionListener(e -> adjuntarArchivo());
        btnCerrarChat.addActionListener(e -> dispose());

        setVisible(true);
    }


  private void adjuntarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            enviarMensaje(archivo);
        }
    }
  
private void cargarMensajes(int idDestinatario) {
    // Aquí iría la lógica para cargar mensajes desde la base de datos
    System.out.println("Cargando mensajes con usuario ID: " + idDestinatario);
}


public void cargarMensajesChat() {
    int usuarioId = UsuarioSesion.getUsuarioId();
    obtenerMensajes(usuarioId);
}
  private void cargarHistorialMensajes() {
        try {
            String sql = "SELECT u.nombre_usuario, m.contenido, m.fecha_envio, m.multimedia FROM mensajes m " +
                         "JOIN usuarios u ON m.id_remitente = u.id_usuarios " +
                         "WHERE (m.id_remitente = ? AND m.id_destinatario = ?) " +
                         "   OR (m.id_remitente = ? AND m.id_destinatario = ?) " +
                         "ORDER BY m.fecha_envio ASC";

            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idDestinatario);
            stmt.setInt(3, idDestinatario);
            stmt.setInt(4, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre_usuario");
                String mensaje = rs.getString("contenido");
                String fecha = rs.getString("fecha_envio");
                byte[] multimedia = rs.getBytes("multimedia");

                agregarMensaje(nombre, mensaje, fecha, multimedia);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
   private void enviarMensaje(File archivo) {
        String mensaje = txtMensaje.getText().trim();
        byte[] archivoBytes = null;

        if (archivo != null) {
            try {
                FileInputStream fis = new FileInputStream(archivo);
                archivoBytes = fis.readAllBytes();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al adjuntar archivo.");
                return;
            }
        }

        try {
            String sql = "INSERT INTO mensajes (id_remitente, id_destinatario, contenido, fecha_envio, multimedia) VALUES (?, ?, ?, NOW(), ?)";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idDestinatario);
            stmt.setString(3, mensaje.isEmpty() ? "[Archivo adjunto]" : mensaje);
            stmt.setBytes(4, archivoBytes);
            stmt.executeUpdate();
            stmt.close();

            // Agregar mensaje al chat con la hora actual
            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            agregarMensaje("Tú", mensaje, fecha, archivoBytes);
            txtMensaje.setText(""); // Limpiar campo
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
private void agregarMensaje(String remitente, String mensaje, String fecha, byte[] multimedia) {
    StyledDocument doc = txtChat.getStyledDocument();
    SimpleAttributeSet negrita = new SimpleAttributeSet();
    StyleConstants.setBold(negrita, true);

    try {
        // Insertar remitente y hora
        doc.insertString(doc.getLength(), remitente + " (" + fecha + "): ", negrita);
        
        if (multimedia != null) {
            // Convertir el array de bytes en una imagen y mostrarla en el chat
            ImageIcon imagen = new ImageIcon(multimedia);
            JLabel lblImagen = new JLabel(imagen);

            // Crear un icono para `JTextPane`
            Style estiloImagen = doc.addStyle("estiloImagen", null);
            StyleConstants.setIcon(estiloImagen, imagen);
            doc.insertString(doc.getLength(), " ", estiloImagen); // Espacio donde va la imagen
            
        } else {
            // Insertar solo texto si no hay imagen adjunta
            doc.insertString(doc.getLength(), mensaje + "\n", null);
        }
        
        // Insertar un salto de línea
        doc.insertString(doc.getLength(), "\n", null);

    } catch (BadLocationException e) {
        e.printStackTrace();
    }
}


   private String obtenerNombreUsuario(int id) {
        try {
            String sql = "SELECT nombre_usuario FROM usuarios WHERE id_usuarios = ?";
            PreparedStatement stmt = conexion.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre_usuario");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Desconocido";
    }


public void obtenerMensajes(int usuarioId) {
        String sql = "SELECT * FROM mensajes WHERE id_remitente = ? OR id_destinatario = ? ORDER BY fecha_envio DESC";

        try (Connection con = RetornarBaseDedatos.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, usuarioId);
            pst.setInt(2, usuarioId);
            ResultSet rs = pst.executeQuery();

            modelo.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

            while (rs.next()) {
                String remitente = rs.getString("id_remitente");
                String contenido = rs.getString("contenido");
                String fecha = rs.getString("fecha_envio");

                modelo.addRow(new Object[]{remitente, contenido, fecha});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

     public Chat() {
       initComponents();

    }
    /**
     * Creates new form Chat
     */


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hola233 = new javax.swing.JPanel();
        txtMensaje = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        btnCerrarChat = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        btnAdjuntar = new javax.swing.JButton();
        lblFotoPerfil = new javax.swing.JLabel();

        hola233.setBackground(new java.awt.Color(255, 255, 255));

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnCerrarChat.setText("Cerrar");

        txtChat.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 255)));
        jScrollPane1.setViewportView(txtChat);

        jLabel1.setText("CHAT");

        btnAdjuntar.setText("Adjuntar");

        lblFotoPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/darzks9-1de75000-56bc-4ef0-b8cf-5e048609b271.png"))); // NOI18N
        lblFotoPerfil.setText("jLabel2");
        lblFotoPerfil.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblFotoPerfilAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout hola233Layout = new javax.swing.GroupLayout(hola233);
        hola233.setLayout(hola233Layout);
        hola233Layout.setHorizontalGroup(
            hola233Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hola233Layout.createSequentialGroup()
                .addGroup(hola233Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hola233Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(btnEnviar)
                        .addGap(36, 36, 36)
                        .addComponent(btnAdjuntar)
                        .addGap(55, 55, 55)
                        .addComponent(btnCerrarChat))
                    .addGroup(hola233Layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(hola233Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(hola233Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(hola233Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        hola233Layout.setVerticalGroup(
            hola233Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hola233Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(hola233Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(btnCerrarChat)
                    .addComponent(btnAdjuntar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(264, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hola233, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hola233, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed

    }//GEN-LAST:event_btnEnviarActionPerformed

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded
ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/imagenes/perfil.png")); // Asegúrate de que la ruta sea correcta
Image imagenOriginal = iconoOriginal.getImage();
Image imagenEscalada = imagenOriginal.getScaledInstance(lblFotoPerfil.getWidth(), lblFotoPerfil.getHeight(), Image.SCALE_SMOOTH);
ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
lblFotoPerfil.setIcon(iconoEscalado);
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdjuntar;
    private javax.swing.JButton btnCerrarChat;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JPanel hola233;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JTextPane txtChat;
    private javax.swing.JTextField txtMensaje;
    // End of variables declaration//GEN-END:variables
}
