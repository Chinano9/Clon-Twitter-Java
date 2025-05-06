/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Perfilusuario;
import IniciarSesion.Iniciarsesionlogin;
import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
import UsuarioID.UsuarioSesion;
import Explorar.BusquedaTwitter;
import PantallaInicio.Home;
import EditarPerfil.EdiPerfil;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JFrame;

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

import javax.swing.table.DefaultTableModel;


import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashSet;
import java.util.Set;
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

    private int idDestinatario;

public Mensajes(int usuarioId) {
    this.usuarioId = usuarioId;

    initComponents();

   // 🔹 Inicializar la tabla con un modelo personalizado no editable
tablaMensajes.setModel(new DefaultTableModel(
    new Object[][]{},
    new String[]{"Remitente", "Contenido", "Fecha"}
) {
    @Override
    public boolean isCellEditable(int row, int column) {
        // Devolver siempre false para que ninguna celda sea editable
        return false;
    }
});
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


    // 🔹 Ajustar la ventana al contenido y centrarla
    setSize(800, 600);
    setLocationRelativeTo(null);
    setResizable(true);

    pack(); // Ajusta la ventana al contenido
}


  public int obtenerIdUsuario(String nombreUsuario) {
        String sql = "SELECT id_usuarios FROM usuarios WHERE nombre_usuario = ?";
        try (Connection con = RetornarBaseDedatos.getConnection();
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
    // 🔹 Deseleccionar cualquier fila previamente seleccionada
    tablaMensajes.clearSelection();

    String sql = "SELECT u.nombre_usuario, m.contenido, m.fecha_envio " +
                 "FROM mensajes m " +
                 "JOIN usuarios u ON m.id_remitente = u.id_usuarios " +
                 "WHERE m.id_destinatario = ? OR m.id_remitente = ? " +
                 "ORDER BY m.fecha_envio DESC";

    try (Connection con = RetornarBaseDedatos.getConnection();
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

        try (Connection con = RetornarBaseDedatos.getConnection();
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

        try (Connection con = RetornarBaseDedatos.getConnection();
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
        jLabel1 = new javax.swing.JLabel();

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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaMensajes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMensajesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMensajes);

        jLabel1.setText("TUS MENSAJES");
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(134, Short.MAX_VALUE))
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
private final Map<Integer, Chat> chatsAbiertos = new HashMap<>();

    private void tablaMensajesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMensajesMouseClicked
  if (evt.getClickCount() == 1) {
        int filaSeleccionada = tablaMensajes.rowAtPoint(evt.getPoint());
        if (filaSeleccionada >= 0) {
            String nombreRemitente = (String) tablaMensajes.getValueAt(filaSeleccionada, 0);
            idDestinatario = obtenerIdUsuario(nombreRemitente);

            if (idDestinatario != 0) {
                // Verificar si ya hay un chat abierto con ese destinatario
                if (!chatsAbiertos.containsKey(idDestinatario)) {
                    Chat chat = new Chat(usuarioId, idDestinatario);
                    chatsAbiertos.put(idDestinatario, chat);
                    
                    // Detectar cuando se cierre la ventana para eliminarla del mapa
                    chat.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            chatsAbiertos.remove(idDestinatario);
                        }
                    });

                    chat.setVisible(true);
                } else {
                    // Opcional: traer al frente la ventana ya abierta
                    Chat chatExistente = chatsAbiertos.get(idDestinatario);
                    chatExistente.toFront();
                    chatExistente.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el usuario.");
            }
        }
    }
    }//GEN-LAST:event_tablaMensajesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaMensajes;
    // End of variables declaration//GEN-END:variables
}
