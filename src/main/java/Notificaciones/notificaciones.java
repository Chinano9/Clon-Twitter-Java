/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Notificaciones;

import Explorar.BusquedaTwitter;
import PantallaInicio.Home;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Perfilusuario.perfilusuario;
import PantallaInicio.Home;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import PantallaInicio.UsuarioSesion;
import java.awt.Insets;  // Este es el import necesario para setMargin()
import Explorar.BusquedaTwitter;
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
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import Explorar.BusquedaTwitter;
import Perfil.EdiPerfil;
import java.awt.Dimension;


import java.util.Base64;

import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.AlphaComposite;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; 
import runproyectlogin.Iniciarsesionlogin;
/**
 *
 * @author Jaime Paredes
 */
public class notificaciones extends javax.swing.JFrame {
    private int usuarioIdPerfilMostrado = -1; // Variable global para almacenar el usuarioId del perfil mostrado

    Color colorNormalMenu = new Color(246,234,250);
    Color colorOscuroMenu = new Color(242, 226, 248);

 public notificaciones() {
        System.out.println("Antes de initComponents(): ScrollPanel = " + ScrollPanel);
        initComponents();
                  cargarFotoPerfil(); 
agregarMenuFotoPerfil();

        System.out.println("Después de initComponents(): ScrollPanel = " + ScrollPanel);
        panelContenedorNotificaciones = new JPanel();
        panelContenedorNotificaciones.setLayout(new BoxLayout(panelContenedorNotificaciones, BoxLayout.Y_AXIS));
        System.out.println("Antes de setViewportView(): ScrollPanel = " + ScrollPanel);
        ScrollPanel.setViewportView(panelContenedorNotificaciones);
        mostrarTodasNotificaciones();
    }
 private void cargarFotoPerfil() {
    int idUsuario = UsuarioSesion.getUsuarioId();  // Obtener el ID del usuario actual
    PantallaInicio.UsuarioDAO usuarioDAO = new PantallaInicio.UsuarioDAO(); // Instancia del DAO
    
    byte[] imgBytes = usuarioDAO.getFotoPerfil(idUsuario); // Obtener la imagen desde la base de datos

    if (imgBytes != null && imgBytes.length > 0) {
        // Si la imagen está presente, crear una ImageIcon
        ImageIcon imageIcon = new ImageIcon(imgBytes);
        
        // Verificar que lblFotoPerfil tenga un tamaño válido
        int width = lblFotoPerfil.getWidth();
        int height = lblFotoPerfil.getHeight();
        
        if (width > 0 && height > 0) {
            // Escalar la imagen solo si las dimensiones son válidas
            Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH); 
            lblFotoPerfil.setIcon(new ImageIcon(image)); // Establecer la imagen en el JLabel
        } else {
            System.out.println("Las dimensiones del JLabel no son válidas.");
        }
    } else {
        // Si no hay imagen, mostrar un mensaje o imagen por defecto
        lblFotoPerfil.setIcon(null); // O puedes poner una imagen por defecto si lo prefieres
        System.out.println("No se encontró imagen para el usuario.");
    }
}


private void mostrarTodasNotificaciones() {
    panelContenedorNotificaciones.removeAll();
    cargarNotificaciones("todas");
    ScrollPanel.revalidate();
    ScrollPanel.repaint();
}

private void mostrarMenciones() {
    panelContenedorNotificaciones.removeAll();
    cargarNotificaciones("menciones");
    ScrollPanel.revalidate();
    ScrollPanel.repaint();
}

 private void cargarNotificaciones(String tipoNotificacion) {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
        panelContenedorNotificaciones.removeAll();
        panelContenedorNotificaciones.setLayout(new BoxLayout(panelContenedorNotificaciones, BoxLayout.Y_AXIS));

        String sql = "";
        PreparedStatement ps = null;

        if (tipoNotificacion.equals("todas")) {
            sql = "SELECT CONCAT(u.alias, ' te ha dado like en un tweet') as contenido, u.alias, u.nombre_usuario, l.fecha_like as fecha_creacion FROM likes l JOIN usuarios u ON l.usuario_id = u.id_usuarios JOIN tweets t ON l.tweet_id = t.id_tweet WHERE t.usuario_id = ? " +
                  "UNION " +
                  "SELECT CONCAT(u.alias, ' ha retuiteado uno de tus tweets'), u.alias, u.nombre_usuario, r.fecha_retweet FROM retweets r JOIN usuarios u ON r.usuario_id = u.id_usuarios JOIN tweets t ON r.tweet_id = t.id_tweet WHERE t.usuario_id = ? " +
                  "UNION " +
                  "SELECT CONCAT(u.alias, ' ha comenzado a seguirte'), u.alias, u.nombre_usuario, f.fecha_reaccion FROM follows f JOIN usuarios u ON f.id_seguidor = u.id_usuarios WHERE f.id_seguido = ?";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, UsuarioSesion.getUsuarioId());
            ps.setInt(2, UsuarioSesion.getUsuarioId());
            ps.setInt(3, UsuarioSesion.getUsuarioId());
        } else if (tipoNotificacion.equals("menciones")) {
            sql = "SELECT t.contenido, u.alias, u.nombre_usuario, t.fecha_creacion " +
                  "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
                  "WHERE t.contenido LIKE CONCAT('%@', (SELECT alias FROM usuarios WHERE id_usuarios = ?), '%') " +
                  "UNION " +
                  "SELECT c.contenido, u.alias, u.nombre_usuario, c.fecha_creacion " +
                  "FROM comentarios c JOIN usuarios u ON c.usuario_id = u.id_usuarios " +
                  "WHERE c.tweet_id IN (SELECT id_tweet FROM tweets WHERE usuario_id = ?)";
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, UsuarioSesion.getUsuarioId());
            ps.setInt(2, UsuarioSesion.getUsuarioId());
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String contenido = rs.getString(1); // Usar el índice 1 para el mensaje construido
            String alias = rs.getString("alias");
            String nombreUsuario = rs.getString("nombre_usuario");
            String fechaCreacion = rs.getString("fecha_creacion");

            // *** Opción 2: Usando JPanels (más estructurado) ***
            JPanel panelNotificacion = new JPanel();
            panelNotificacion.setLayout(new BoxLayout(panelNotificacion, BoxLayout.Y_AXIS));
            panelNotificacion.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            JLabel lblUsuario = new JLabel("<html><b>" + alias + " (" + nombreUsuario + ")</b> - " + fechaCreacion + "</html>");
            JTextArea txtContenido = new JTextArea(contenido);
            txtContenido.setEditable(false);
            txtContenido.setLineWrap(true);
            txtContenido.setWrapStyleWord(true);
            txtContenido.setBackground(panelNotificacion.getBackground());

            panelNotificacion.add(lblUsuario);
            panelNotificacion.add(txtContenido);

            panelContenedorNotificaciones.add(panelNotificacion);
            panelContenedorNotificaciones.add(Box.createVerticalStrut(10));
        }

        ScrollPanel.revalidate();
        ScrollPanel.repaint();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar notificaciones", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void abrirPerfilUsuario(int idUsuario) {
            this.dispose();
    perfilusuario perfil = new perfilusuario(idUsuario); // Abre el perfil del usuario con el idUsuario
    perfil.setVisible(true);
}

 private void mostrarMenuOpciones(java.awt.event.MouseEvent evt) {
        JPopupMenu menu = new JPopupMenu();
        
        // Botón Configuración
        JMenuItem itemConfiguracion = new JMenuItem("Configuración");
        itemConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirConfiguracion();
            }
        });
        menu.add(itemConfiguracion);
        
        // Separador
        menu.addSeparator();
        
        // Botón Cerrar Sesión
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesion();
            }
        });
        menu.add(itemCerrarSesion);
        
// Botón Ver Perfil
JMenuItem itemVerPerfil = new JMenuItem("Ver Perfil");
itemVerPerfil.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        // Obtener el usuarioId del perfil mostrado
        int usuarioId = usuarioIdPerfilMostrado;
        abrirPerfilUsuario(usuarioId);
    }
});
menu.add(itemVerPerfil);

        // Mostrar el menú en la posición del clic
        menu.show(lblFotoPerfil, evt.getX(), evt.getY());
    }
 private void mostrarMenuContextualFotoPerfil(java.awt.event.MouseEvent evt) {
    JPopupMenu menu = new JPopupMenu();

    // Opción "Ver Perfil"
 JMenuItem itemVerPerfil = new JMenuItem("Ver Perfil");
itemVerPerfil.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        int usuarioId = usuarioIdPerfilMostrado; // Obtener el ID del usuario seleccionado
        perfilusuario perfil = new perfilusuario(usuarioId); // Pasar ID al perfil
        perfil.setVisible(true);
    }
});
menu.add(itemVerPerfil);


    // Opción "Configuración"
    JMenuItem itemConfiguracion = new JMenuItem("Configuración");
    itemConfiguracion.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            abrirConfiguracion();
        }
    });
    menu.add(itemConfiguracion);

    // Opción "Cerrar Sesión"
    JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
    itemCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cerrarSesion();
        }
    });
    menu.add(itemCerrarSesion);

    menu.show(evt.getComponent(), evt.getX(), evt.getY());
}
private void agregarMenuFotoPerfil() {
    lblFotoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            mostrarMenuContextualFotoPerfil(evt);
        }
    });
}


  private String obtenerNombreUsuario() {
        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "SELECT nombre_usuario FROM usuarios WHERE id_usuarios = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, UsuarioSesion.getUsuarioId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre_usuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
  
  
    private void abrirPerfilUsuario() {
        perfilusuario perfilUsuario = new perfilusuario(); // Crea una instancia de Perfilusuario
        perfilUsuario.setLocationRelativeTo(this); // Centra la ventana en la pantalla
        perfilUsuario.setVisible(true); // Muestra la ventana
    }
private void abrirConfiguracion() {
    EdiPerfil editarPerfil = new EdiPerfil();
    editarPerfil.setLocationRelativeTo(this);
    editarPerfil.setVisible(true);
}

     private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro que deseas cerrar sesión?", 
            "Confirmar Cierre de Sesión", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            // Cerrar la ventana actual
            this.dispose();
            
            // Abrir el formulario de login
            EventQueue.invokeLater(() -> {
                Iniciarsesionlogin login = new Iniciarsesionlogin();
                login.setVisible(true);
                login.setLocationRelativeTo(null); // Centrar en pantalla
            });
            
            // Aquí puedes agregar lógica adicional de cierre de sesión
            // como limpiar variables de sesión, etc.
        }
    }
     
  
      private void cargarMenciones() {
        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "SELECT c.contenido, u.nombre_usuario, t.contenido AS tweet_contenido " +
                         "FROM comentarios c " +
                         "JOIN usuarios u ON c.usuario_id = u.id_usuarios " +
                         "JOIN tweets t ON c.tweet_id = t.id_tweet " +
                         "WHERE c.contenido LIKE ?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%@" + obtenerAliasUsuarioActual(conexion) + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JPanel panelMencion = crearPanelNotificacion(rs.getString("contenido"), rs.getString("nombre_usuario"), rs.getString("tweet_contenido"), "Mención");
                jpanel.add(panelMencion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar menciones.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cargarComentariosATusTweets() {
        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "SELECT c.contenido, u.nombre_usuario, t.contenido AS tweet_contenido " +
                         "FROM comentarios c " +
                         "JOIN usuarios u ON c.usuario_id = u.id_usuarios " +
                         "JOIN tweets t ON c.tweet_id = t.id_tweet " +
                         "WHERE t.usuario_id = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, UsuarioSesion.getUsuarioId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                JPanel panelComentario = crearPanelNotificacion(rs.getString("contenido"), rs.getString("nombre_usuario"), rs.getString("tweet_contenido"), "Comentario en tu Tweet");
                jpanel.add(panelComentario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar comentarios a tus tweets.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JPanel crearPanelNotificacion(String comentario, String autor, String tweetContenido, String tipoNotificacion) {
        JPanel panelNotificacion = new JPanel();
        panelNotificacion.setLayout(new BoxLayout(panelNotificacion, BoxLayout.Y_AXIS));

        JTextArea textoComentario = new JTextArea("'" + comentario + "' - " + autor + " (" + tipoNotificacion + ")");
        textoComentario.setEditable(false);
        textoComentario.setLineWrap(true);
        textoComentario.setWrapStyleWord(true);
        panelNotificacion.add(textoComentario);

        JTextArea textoTweet = new JTextArea("Tweet: " + tweetContenido);
        textoTweet.setEditable(false);
        textoTweet.setLineWrap(true);
        textoTweet.setWrapStyleWord(true);
        panelNotificacion.add(textoTweet);

        panelNotificacion.add(Box.createVerticalStrut(10)); // Espacio entre notificaciones

        return panelNotificacion;
    }
    
     private String obtenerAliasUsuarioActual(Connection conexion) throws SQLException {
        String sql = "SELECT alias FROM usuarios WHERE id_usuarios = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, UsuarioSesion.getUsuarioId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("alias");
        }
        return "";
    }
     
     
     
    private void actualizarNombreYAlias() {
    int idUsuario = PantallaInicio.UsuarioSesion.getUsuarioId(); // Obtener el ID del usuario en sesión

    if (idUsuario != -1) {
        try (Connection conexion = PantallaInicio.BasededatosTwitter.getConnection()) {
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
    


      private void abrirPerfilUsuario(String nombreUsuario) {
        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "SELECT id_usuarios FROM usuarios WHERE nombre_usuario = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id_usuarios");
                perfilusuario perfil = new perfilusuario(idUsuario); // Asumiendo que tienes perfilusuario.java
                perfil.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al abrir perfil", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanel = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        pInicio = new javax.swing.JPanel();
        Inicio = new javax.swing.JLabel();
        pPerfil = new javax.swing.JPanel();
        pNotificaciones = new javax.swing.JPanel();
        Notificaciones = new javax.swing.JLabel();
        pExplorar = new javax.swing.JPanel();
        Explorar = new javax.swing.JLabel();
        POpciones = new javax.swing.JPanel();
        pTodas = new javax.swing.JPanel();
        pMenciones = new javax.swing.JPanel();
        lblAliasNombre = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        panelContenedorNotificaciones = new javax.swing.JPanel();
        ScrollPanel = new javax.swing.JScrollPane();
        lblFotoPerfil = new javax.swing.JLabel();
        btnTodas = new javax.swing.JButton();
        btnMenciones = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpanel.setBackground(new java.awt.Color(255, 255, 255));

        Menu2.setBackground(new java.awt.Color(246, 234, 250));

        LogoTwitter2.setFont(new java.awt.Font("Eras Bold ITC", 0, 36)); // NOI18N
        LogoTwitter2.setForeground(new java.awt.Color(102, 0, 153));
        LogoTwitter2.setText("Twitter");
        LogoTwitter2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogoTwitter2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LogoTwitter2MouseClicked(evt);
            }
        });

        pInicio.setBackground(new java.awt.Color(246, 234, 250));
        pInicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pInicioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pInicioMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pInicioMouseExited(evt);
            }
        });

        Inicio.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        Inicio.setForeground(new java.awt.Color(102, 0, 153));
        Inicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/brujula.png"))); // NOI18N
        Inicio.setText("Inicio");
        Inicio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Inicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InicioMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pInicioLayout = new javax.swing.GroupLayout(pInicio);
        pInicio.setLayout(pInicioLayout);
        pInicioLayout.setHorizontalGroup(
            pInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInicioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Inicio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pInicioLayout.setVerticalGroup(
            pInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pInicioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Inicio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pPerfil.setBackground(new java.awt.Color(246, 234, 250));
        pPerfil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pPerfilMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pPerfilMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pPerfilMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pPerfilLayout = new javax.swing.GroupLayout(pPerfil);
        pPerfil.setLayout(pPerfilLayout);
        pPerfilLayout.setHorizontalGroup(
            pPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pPerfilLayout.setVerticalGroup(
            pPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        pNotificaciones.setBackground(new java.awt.Color(246, 234, 250));
        pNotificaciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pNotificaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pNotificacionesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pNotificacionesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pNotificacionesMouseExited(evt);
            }
        });

        Notificaciones.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        Notificaciones.setForeground(new java.awt.Color(102, 0, 153));
        Notificaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/Notificaciones.png"))); // NOI18N
        Notificaciones.setText("Notificaciones");
        Notificaciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Notificaciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NotificacionesMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pNotificacionesLayout = new javax.swing.GroupLayout(pNotificaciones);
        pNotificaciones.setLayout(pNotificacionesLayout);
        pNotificacionesLayout.setHorizontalGroup(
            pNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Notificaciones)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pNotificacionesLayout.setVerticalGroup(
            pNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Notificaciones)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pExplorar.setBackground(new java.awt.Color(246, 234, 250));
        pExplorar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pExplorar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pExplorarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pExplorarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pExplorarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pExplorarLayout = new javax.swing.GroupLayout(pExplorar);
        pExplorar.setLayout(pExplorarLayout);
        pExplorarLayout.setHorizontalGroup(
            pExplorarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pExplorarLayout.setVerticalGroup(
            pExplorarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        Explorar.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        Explorar.setForeground(new java.awt.Color(102, 0, 153));
        Explorar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/lupa.png"))); // NOI18N
        Explorar.setText("Explorar");
        Explorar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Explorar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ExplorarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LogoTwitter2)
                .addGap(43, 43, 43))
            .addComponent(pInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pNotificaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pExplorar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Explorar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LogoTwitter2)
                .addGap(45, 45, 45)
                .addComponent(pInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Explorar)
                .addGap(9, 9, 9)
                .addComponent(pExplorar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        POpciones.setBackground(new java.awt.Color(255, 255, 255));

        pTodas.setBackground(new java.awt.Color(255, 255, 255));
        pTodas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pTodas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pTodasMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pTodasMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pTodasLayout = new javax.swing.GroupLayout(pTodas);
        pTodas.setLayout(pTodasLayout);
        pTodasLayout.setHorizontalGroup(
            pTodasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 116, Short.MAX_VALUE)
        );
        pTodasLayout.setVerticalGroup(
            pTodasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        pMenciones.setBackground(new java.awt.Color(255, 255, 255));
        pMenciones.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pMenciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pMencionesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pMencionesMouseExited(evt);
            }
        });

        lblAliasNombre.setText("Nombre y usuario");
        lblAliasNombre.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblAliasNombreAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout pMencionesLayout = new javax.swing.GroupLayout(pMenciones);
        pMenciones.setLayout(pMencionesLayout);
        pMencionesLayout.setHorizontalGroup(
            pMencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMencionesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblAliasNombre)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        pMencionesLayout.setVerticalGroup(
            pMencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMencionesLayout.createSequentialGroup()
                .addComponent(lblAliasNombre)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout POpcionesLayout = new javax.swing.GroupLayout(POpciones);
        POpciones.setLayout(POpcionesLayout);
        POpcionesLayout.setHorizontalGroup(
            POpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(POpcionesLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(pTodas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                .addComponent(pMenciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(159, 159, 159))
        );
        POpcionesLayout.setVerticalGroup(
            POpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, POpcionesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(POpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pMenciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pTodas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        panelContenedorNotificaciones.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelContenedorNotificacionesAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        ScrollPanel.setBackground(new java.awt.Color(255, 255, 255));
        ScrollPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 255)));

        javax.swing.GroupLayout panelContenedorNotificacionesLayout = new javax.swing.GroupLayout(panelContenedorNotificaciones);
        panelContenedorNotificaciones.setLayout(panelContenedorNotificacionesLayout);
        panelContenedorNotificacionesLayout.setHorizontalGroup(
            panelContenedorNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContenedorNotificacionesLayout.setVerticalGroup(
            panelContenedorNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
        );

        lblFotoPerfil.setText("Foto de perrfil");
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

        btnTodas.setText("Todas");
        btnTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodasActionPerformed(evt);
            }
        });

        btnMenciones.setText("Menciones");
        btnMenciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMencionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpanelLayout = new javax.swing.GroupLayout(jpanel);
        jpanel.setLayout(jpanelLayout);
        jpanelLayout.setHorizontalGroup(
            jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanelLayout.createSequentialGroup()
                        .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpanelLayout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(panelContenedorNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpanelLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(POpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jpanelLayout.createSequentialGroup()
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(521, 521, 521)
                                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(188, 188, 188)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jpanelLayout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(btnTodas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMenciones)
                        .addGap(209, 209, 209))))
        );
        jpanelLayout.setVerticalGroup(
            jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jpanelLayout.createSequentialGroup()
                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(POpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTodas)
                    .addComponent(btnMenciones))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelContenedorNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded
        //  lblFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
        //   lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void pExplorarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pExplorarMouseExited
        pExplorar.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pExplorarMouseExited

    private void pExplorarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pExplorarMouseEntered
        pExplorar.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pExplorarMouseEntered

    private void pExplorarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pExplorarMouseClicked
        BusquedaTwitter b = new BusquedaTwitter();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pExplorarMouseClicked

    private void pNotificacionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pNotificacionesMouseExited
        pNotificaciones.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pNotificacionesMouseExited

    private void pNotificacionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pNotificacionesMouseEntered
        pNotificaciones.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pNotificacionesMouseEntered

    private void pNotificacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pNotificacionesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pNotificacionesMouseClicked

    private void pPerfilMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pPerfilMouseExited
        pPerfil.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pPerfilMouseExited

    private void pPerfilMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pPerfilMouseEntered
        pPerfil.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pPerfilMouseEntered

    private void pPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pPerfilMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pPerfilMouseClicked

    private void pInicioMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pInicioMouseExited
        pInicio.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pInicioMouseExited

    private void pInicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pInicioMouseEntered
        pInicio.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pInicioMouseEntered

    private void pInicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pInicioMouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pInicioMouseClicked

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

    private void panelContenedorNotificacionesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelContenedorNotificacionesAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_panelContenedorNotificacionesAncestorAdded

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
        actualizarNombreYAlias(); // Llamar al método al cargar la interfaz
    }//GEN-LAST:event_lblAliasNombreAncestorAdded

    private void pMencionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pMencionesMouseExited
        pMenciones.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pMencionesMouseExited

    private void pMencionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pMencionesMouseEntered
        pMenciones.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pMencionesMouseEntered

    private void btnMencionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMencionesActionPerformed

        System.out.println("Botón 'Menciones' presionado.");
        mostrarMenciones();
        System.out.println("Método mostrarMenciones() ejecutado.");
    }//GEN-LAST:event_btnMencionesActionPerformed

    private void pTodasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pTodasMouseExited
        pTodas.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pTodasMouseExited

    private void pTodasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pTodasMouseEntered
        pTodas.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pTodasMouseEntered

    private void btnTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTodasActionPerformed
        System.out.println("Botón 'Todas' presionado.");
        mostrarTodasNotificaciones();
        System.out.println("Método mostrarTodasNotificaciones() ejecutado.");
    }//GEN-LAST:event_btnTodasActionPerformed

    private void NotificacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NotificacionesMouseClicked
           notificaciones h = new notificaciones();
        h.setVisible(true);
        this.dispose();
                    
    }//GEN-LAST:event_NotificacionesMouseClicked

    private void ExplorarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExplorarMouseClicked
             BusquedaTwitter h = new BusquedaTwitter();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ExplorarMouseClicked

    private void InicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InicioMouseClicked
       Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_InicioMouseClicked

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
            java.util.logging.Logger.getLogger(notificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(notificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(notificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(notificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new notificaciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Explorar;
    private javax.swing.JLabel Inicio;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JLabel Notificaciones;
    private javax.swing.JPanel POpciones;
    private javax.swing.JScrollPane ScrollPanel;
    private javax.swing.JButton btnMenciones;
    private javax.swing.JButton btnTodas;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jpanel;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JPanel pExplorar;
    private javax.swing.JPanel pInicio;
    private javax.swing.JPanel pMenciones;
    private javax.swing.JPanel pNotificaciones;
    private javax.swing.JPanel pPerfil;
    private javax.swing.JPanel pTodas;
    private javax.swing.JPanel panelContenedorNotificaciones;
    // End of variables declaration//GEN-END:variables
}
