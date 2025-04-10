/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Perfilusuario;
import runproyectlogin.Iniciarsesionlogin;
import PantallaInicio.BasededatosTwitter;

import Explorar.BusquedaTwitter;
import PantallaInicio.Home;
import Perfil.EdiPerfil;
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
import PantallaInicio.UsuarioDAO; // 👈 Asegúrate de que esta clase existe y tiene getConnection()
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
import javax.swing.JFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; // Para el cursor de mano
import Explorar.BusquedaTwitter;
import PantallaInicio.Home;
import runproyectlogin.Iniciarsesionlogin;
import java.awt.Color;
import javax.swing.SwingConstants;
import PantallaInicio.UsuarioSesion;
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

import javax.swing.table.DefaultTableModel;


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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
/**
 *
 * @author Macke
 */


public class Mensajes extends JFrame {
    private int usuarioId; // ID del usuario que inició sesión
    private DefaultTableModel modelo = new DefaultTableModel(new String[]{"Remitente", "Contenido", "Fecha"}, 0);
    private int idDestinatario;
    

public Mensajes(int usuarioId) {
    this.usuarioId = usuarioId;

    // 🔹 Primero inicializar componentes gráficos (Evita NullPointerException)
    initComponents();

    // 🔹 Inicializar la tabla antes de modificar sus propiedades
    tablaMensajes.setModel(new DefaultTableModel(
        new Object[][]{}, 
        new String[]{"Remitente", "Contenido", "Fecha"}
    ));

    // 🔹 Ajustar tamaño de la tabla
    tablaMensajes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    tablaMensajes.setRowHeight(30);
    tablaMensajes.getTableHeader().setReorderingAllowed(false); // Evita mover columnas

    // 🔹 Centrar el texto en las celdas
    DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
    centrado.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < tablaMensajes.getColumnCount(); i++) {
        tablaMensajes.getColumnModel().getColumn(i).setCellRenderer(centrado);
    }

    // 🔹 Cargar mensajes después de inicializar la tabla
    cargarMensajes();

    // 🔹 Evento para abrir chat al hacer clic en un mensaje
tablaMensajes.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting()) {
        try {
            int filaSeleccionada = tablaMensajes.getSelectedRow();
            if (filaSeleccionada >= 0 && filaSeleccionada < tablaMensajes.getRowCount()) {
                String nombreRemitente = (String) tablaMensajes.getValueAt(filaSeleccionada, 0);
                idDestinatario = obtenerIdUsuario(nombreRemitente); // Obtener el ID del usuario

                if (idDestinatario != 0) {
                    Chat chat = new Chat(usuarioId, idDestinatario);
                    chat.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el usuario.");
                }
            }
        } catch (Exception ex) {
            // Omitimos el error para que no se muestre en la consola
        }
    }
});
    // 🔹 Ajustar la ventana al contenido y centrarla
    setSize(800, 600);
    setLocationRelativeTo(null);
    setResizable(true);

    pack(); // Ajusta la ventana al contenido
}


  public int obtenerIdUsuario(String nombreUsuario) {
        String sql = "SELECT id_usuarios FROM usuarios WHERE nombre_usuario = ?";
        try (Connection con = BasededatosTwitter.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, nombreUsuario);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_usuarios");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Retorna 0 si no se encuentra el usuario
    }
  
 public void cargarMensajes() {
    String sql = "SELECT u.nombre_usuario, m.contenido, m.fecha_envio " +
                 "FROM mensajes m " +
                 "JOIN usuarios u ON m.id_remitente = u.id_usuarios " +
                 "WHERE m.id_destinatario = ? OR m.id_remitente = ? " +
                 "ORDER BY m.fecha_envio DESC";

    try (Connection con = BasededatosTwitter.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, usuarioId);
        pst.setInt(2, usuarioId);
        ResultSet rs = pst.executeQuery();

        // Limpiar tabla antes de agregar nuevos datos
        DefaultTableModel modelo = (DefaultTableModel) tablaMensajes.getModel();
        modelo.setRowCount(0);

        while (rs.next()) {
            String remitente = rs.getString("nombre_usuario");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_envio");

            System.out.println("Mensaje encontrado: " + remitente + " - " + contenido + " - " + fecha); // 🔹 Debugging

            modelo.addRow(new Object[]{remitente, contenido, fecha});
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los mensajes", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

 

public List<String[]> obtenerMensajes(int usuarioId) {
        List<String[]> mensajes = new ArrayList<>();
        String sql = "SELECT u.nombre_usuario, m.contenido, m.fecha_envio " +
                     "FROM mensajes m " +
                     "JOIN usuarios u ON m.id_remitente = u.id_usuarios " +
                     "WHERE m.id_remitente = ? OR m.id_destinatario = ? " +
                     "ORDER BY m.fecha_envio DESC";

        try (Connection con = BasededatosTwitter.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, usuarioId);
            pst.setInt(2, usuarioId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String remitente = rs.getString("nombre_usuario");
                String contenido = rs.getString("contenido");
                String fecha = rs.getString("fecha_envio");

                mensajes.add(new String[]{remitente, contenido, fecha});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mensajes;
    }


public void enviarMensaje(int remitente, int destinatario, String contenido) {
        String sql = "INSERT INTO mensajes (id_remitente, id_destinatario, contenido, fecha_envio) VALUES (?, ?, ?, NOW())";

        try (Connection con = BasededatosTwitter.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, remitente);
            pst.setInt(2, destinatario);
            pst.setString(3, contenido);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Mensaje enviado.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public void mostrarEnVentana() {
        Mensajes ventana = new Mensajes(usuarioId);
        ventana.setVisible(true);
    }

    /**
     * Creates new form Mensajes
     */
public Mensajes() {
    this(0); // Llama al constructor principal con un usuarioId por defecto
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMensajes = new javax.swing.JTable();
        btnAbrirChat = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        jPanel.setBackground(new java.awt.Color(255, 255, 255));

        tablaMensajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaMensajes);

        btnAbrirChat.setText("Abrir Chat");
        btnAbrirChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirChatActionPerformed(evt);
            }
        });

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(btnAbrirChat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar)
                .addGap(208, 208, 208))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAbrirChat)
                    .addComponent(btnCerrar))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAbrirChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirChatActionPerformed
   int filaSeleccionada = tablaMensajes.getSelectedRow();

    if (filaSeleccionada != -1) {
        String nombreRemitente = (String) tablaMensajes.getValueAt(filaSeleccionada, 0);

        // Obtener el ID del remitente basado en el nombre de usuario
        int idRemitente = obtenerIdUsuario(nombreRemitente);

        if (idRemitente == 0) {
            JOptionPane.showMessageDialog(this, "No se pudo encontrar el usuario.");
            return;
        }

        // Determinar con quién chatear
        int otroUsuarioId = (idRemitente == usuarioId) ? idDestinatario : idRemitente;

        // Abrir el chat con el usuario seleccionado
        Chat chat = new Chat(usuarioId, otroUsuarioId);
        chat.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Selecciona un mensaje para abrir el chat.");
    }
    }//GEN-LAST:event_btnAbrirChatActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCerrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirChat;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaMensajes;
    // End of variables declaration//GEN-END:variables
}
