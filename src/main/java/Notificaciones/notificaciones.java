/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Notificaciones;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
import Perfilusuario.perfilusuario;
import PantallaInicio.Home;
import java.awt.Color;

import UsuarioID.UsuarioSesion;


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Explorar.BusquedaTwitter;
import EditarPerfil.EdiPerfil;

import java.awt.Image;

import javax.swing.BorderFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BoxLayout;

import IniciarSesion.Iniciarsesionlogin;
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
    UsuarioDatos.UsuarioDAO usuarioDAO = new UsuarioDatos.UsuarioDAO(); // Instancia del DAO
    
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
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
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
                notificaciones.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemVerPerfil);

        // Opción "Configuración"
        JMenuItem itemConfiguracion = new JMenuItem("Configuración");
        itemConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirConfiguracion();
                notificaciones.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemConfiguracion);

        // Opción "Cerrar Sesión"
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesion();
                notificaciones.this.dispose(); // Cerrar la ventana principal
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
        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
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
        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
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
        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
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
    int idUsuario = UsuarioID.UsuarioSesion.getUsuarioId(); // Obtener el ID del usuario en sesión

    if (idUsuario != -1) {
        try (Connection conexion = ConexionBase.RetornarBaseDedatos.getConnection()) {
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
        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
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
        Explorar = new javax.swing.JLabel();
        Notificaciones = new javax.swing.JLabel();
        Inicio = new javax.swing.JLabel();
        POpciones = new javax.swing.JPanel();
        btnMenciones = new javax.swing.JButton();
        btnTodas = new javax.swing.JButton();
        panelContenedorNotificaciones = new javax.swing.JPanel();
        ScrollPanel = new javax.swing.JScrollPane();
        lblFotoPerfil = new javax.swing.JLabel();
        lblAliasNombre = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpanel.setBackground(new java.awt.Color(255, 255, 255));

        Menu2.setBackground(new java.awt.Color(246, 234, 250));
        Menu2.setDoubleBuffered(false);
        Menu2.setEnabled(false);
        Menu2.setFocusable(false);
        Menu2.setRequestFocusEnabled(false);
        Menu2.setVerifyInputWhenFocusTarget(false);

        LogoTwitter2.setFont(new java.awt.Font("Eras Bold ITC", 0, 36)); // NOI18N
        LogoTwitter2.setForeground(new java.awt.Color(102, 0, 153));
        LogoTwitter2.setText("Twitter");
        LogoTwitter2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogoTwitter2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LogoTwitter2MouseClicked(evt);
            }
        });

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

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LogoTwitter2)
                .addGap(43, 43, 43))
            .addGroup(Menu2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Explorar)
                    .addComponent(Notificaciones)
                    .addComponent(Inicio))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LogoTwitter2)
                .addGap(34, 34, 34)
                .addComponent(Inicio)
                .addGap(51, 51, 51)
                .addComponent(Explorar)
                .addGap(52, 52, 52)
                .addComponent(Notificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(436, Short.MAX_VALUE))
        );

        POpciones.setBackground(new java.awt.Color(255, 255, 255));

        btnMenciones.setText("Menciones");
        btnMenciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMencionesActionPerformed(evt);
            }
        });

        btnTodas.setText("Todas");
        btnTodas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout POpcionesLayout = new javax.swing.GroupLayout(POpciones);
        POpciones.setLayout(POpcionesLayout);
        POpcionesLayout.setHorizontalGroup(
            POpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, POpcionesLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(btnTodas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(399, 399, 399)
                .addComponent(btnMenciones)
                .addGap(188, 188, 188))
        );
        POpcionesLayout.setVerticalGroup(
            POpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, POpcionesLayout.createSequentialGroup()
                .addGap(0, 22, Short.MAX_VALUE)
                .addGroup(POpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMenciones)
                    .addComponent(btnTodas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        ScrollPanel.setOpaque(false);

        javax.swing.GroupLayout panelContenedorNotificacionesLayout = new javax.swing.GroupLayout(panelContenedorNotificaciones);
        panelContenedorNotificaciones.setLayout(panelContenedorNotificacionesLayout);
        panelContenedorNotificacionesLayout.setHorizontalGroup(
            panelContenedorNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorNotificacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelContenedorNotificacionesLayout.setVerticalGroup(
            panelContenedorNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jpanelLayout = new javax.swing.GroupLayout(jpanel);
        jpanel.setLayout(jpanelLayout);
        jpanelLayout.setHorizontalGroup(
            jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanelLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(panelContenedorNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpanelLayout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(POpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jpanelLayout.createSequentialGroup()
                                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblAliasNombre))
                                .addGap(188, 188, 188)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpanelLayout.setVerticalGroup(
            jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelLayout.createSequentialGroup()
                .addGroup(jpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAliasNombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(POpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(panelContenedorNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1105, Short.MAX_VALUE)
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

    private void panelContenedorNotificacionesAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelContenedorNotificacionesAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_panelContenedorNotificacionesAncestorAdded

    private void btnMencionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMencionesActionPerformed

        System.out.println("Botón 'Menciones' presionado.");
        mostrarMenciones();
        System.out.println("Método mostrarMenciones() ejecutado.");
    }//GEN-LAST:event_btnMencionesActionPerformed

    private void btnTodasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTodasActionPerformed
        System.out.println("Botón 'Todas' presionado.");
        mostrarTodasNotificaciones();
        System.out.println("Método mostrarTodasNotificaciones() ejecutado.");
    }//GEN-LAST:event_btnTodasActionPerformed

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
        actualizarNombreYAlias(); // Llamar al método al cargar la interfaz
    }//GEN-LAST:event_lblAliasNombreAncestorAdded

    private void InicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InicioMouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_InicioMouseClicked

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

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

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
    private javax.swing.JPanel jpanel;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JPanel panelContenedorNotificaciones;
    // End of variables declaration//GEN-END:variables
}
