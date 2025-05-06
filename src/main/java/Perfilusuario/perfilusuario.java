/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; // Para el cursor de mano
import Explorar.BusquedaTwitter;
import Notificaciones.notificaciones;
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
/**
 *
 * @author Jaime Paredes
 */
public class perfilusuario extends javax.swing.JFrame {
private int idUsuario;
private int idDestinatario;
    Color colorNormalMenu = new Color(246,234,250);
    Color colorOscuroMenu = new Color(242, 226, 248);
    
        private boolean esPerfilPropio; // Bandera para indicar si es el perfil propio


        private void inicializarPerfil() {
    agregarMenuFotoPerfil();
    // ... (Mueve aquí el resto del código de inicialización que tenías en el constructor)
        cargarFondoPortada();
       cargarFotoPerfil();
    cargarTweetsUsuario(); // Cargar tweets por defecto
}
       

        // Método para cargar el fondo de portada
private void cargarFondoPortada() {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT fondo_portada FROM usuarios WHERE id_usuarios = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, this.idUsuario);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            byte[] fondoPortadaBytes = rs.getBytes("fondo_portada");
            if (fondoPortadaBytes != null) {
                mostrarFondoPortadaEnLabel(fondoPortadaBytes);
            } else {
                lblFondoPortada.setIcon(null);
                lblFondoPortada.setText("No hay fondo de portada");
            }
        } else {
            lblFondoPortada.setIcon(null);
            lblFondoPortada.setText("Usuario no encontrado");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar fondo de portada", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
  public perfilusuario(int idUsuario) {
    initComponents();
    this.idUsuario = idUsuario; // Asigna el ID del perfil visitado
    this.esPerfilPropio = (idUsuario == UsuarioSesion.getUsuarioId()); // Comprueba si es su perfil
 btnUnfollow.setVisible(false); // Asegurar que unfollow esté oculto inicialmente
    verificarBotones(); // Ahora que idUsuario está asignado, podemos llamar a este método
cargarEstadoSeguimiento();
    inicializarPerfil();
    cargarTweetsUsuario(); // Cargar tweets por defecto
    cargarFollowingYFollowers(); // Cargar "following" y "followers" al inicio

    // Modificar el ActionListener del botón de mensajes
    btnMensajes.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Botón Mensajes clickeado.");

            if (esPerfilPropio) {
                System.out.println("Abriendo ventana Mensajes.");
                Mensajes mensajes = new Mensajes(idUsuario); // idUsuario ya está disponible
                mensajes.setVisible(true); // Mostrar la ventana de mensajes
            } else {
                System.out.println("Abriendo ventana Chat.");
Chat chat = new Chat(idUsuario, idDestinatario);
                chat.setVisible(true);
            }
        }
    });
}

private void verificarBotones() {
        if (esPerfilPropio || UsuarioSesion.getUsuarioId() == this.idUsuario) {
            btnMensajes.setVisible(true);
            btnAbrirChat.setVisible(false);
            btnSeguir1.setVisible(false);
            btnUnfollow.setVisible(false);
        } else {
            btnMensajes.setVisible(false);
            btnAbrirChat.setVisible(true);
            cargarEstadoSeguimiento();
        }
    }
    

private void abrirMensajes() {
    Mensajes ventanaMensajes = new Mensajes();
    ventanaMensajes.mostrarEnVentana();
}


    

        // Método para mostrar el fondo de portada en el JLabel
private void mostrarFondoPortadaEnLabel(byte[] fondoPortadaBytes) {
    ImageIcon imageIcon = new ImageIcon(fondoPortadaBytes);
    Image image = imageIcon.getImage().getScaledInstance(lblFondoPortada.getWidth(), lblFondoPortada.getHeight(), Image.SCALE_SMOOTH);
    lblFondoPortada.setIcon(new ImageIcon(image));
    lblFondoPortada.setText(""); // Limpiar el texto si hay imagen
}

    public perfilusuario() {
       initComponents();
       
        this.idUsuario = UsuarioSesion.getUsuarioId();
        this.esPerfilPropio = true; // Es el perfil propio
       
              // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);
            verificarBotones(); // Llamar también en este constructor si es necesario

        // Botón de Mensaje
        btnMensajes = new JButton("Enviar Mensaje");
        estilizarBoton(btnMensajes);
        panelBotones.add(btnMensajes);
        
     
        
        agregarMenuFotoPerfil();
btnTweets = new JButton("Tweets");
        btnRetweets = new JButton("Retweets");
        btnLikes = new JButton("Likes");
      
       
       
        
    this.idUsuario = UsuarioSesion.getUsuarioId();
       
         panelFotoPortada.setLayout(new BorderLayout()); // Establecer BorderLayout
        panelFotoPortada.add(lblFondoPortada, BorderLayout.CENTER); // Agregar lblFondoPortada al panel
        cargarFotos(); // Llama al método para cargar las fotos
        
        
        // Obtener dimensiones del panel
        int panelAncho = panelFotoPortada.getWidth();
        int panelAlto = panelFotoPortada.getHeight();

        // Mostrar dimensiones en la consola
        System.out.println("Ancho del panel: " + panelAncho);
        System.out.println("Alto del panel: " + panelAlto);
    }
    
     private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(new Color(30, 144, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(30, 144, 255));
            }
        });
    }
     private boolean siguiendo = false; // Para rastrear el estado de seguimiento

private void cargarEstadoSeguimiento() {
        if (UsuarioSesion.getUsuarioId() == this.idUsuario) {
            // Es el perfil propio, ocultar ambos botones
            btnSeguir1.setVisible(false);
            btnUnfollow.setVisible(false);
        } else {
            // No es el perfil propio, verificar el estado de seguimiento
            try (Connection conexion = RetornarBaseDedatos.getConnection()) {
                String sqlVerificar = "SELECT COUNT(*) FROM follows WHERE id_seguidor = ? AND id_seguido = ?";
                PreparedStatement psVerificar = conexion.prepareStatement(sqlVerificar);
                psVerificar.setInt(1, UsuarioSesion.getUsuarioId());
                psVerificar.setInt(2, this.idUsuario);
                ResultSet rsVerificar = psVerificar.executeQuery();

                if (rsVerificar.next() && rsVerificar.getInt(1) > 0) {
                    // Ya seguimos a este usuario
                    btnSeguir1.setVisible(false);
                    btnUnfollow.setVisible(true);
                } else {
                    // No seguimos a este usuario
                    btnSeguir1.setVisible(true);
                    btnUnfollow.setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al verificar el estado de seguimiento", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

// Método para seguir a un usuario (sin la verificación redundante)
private void seguirUsuario() {
    if (UsuarioSesion.getUsuarioId() == this.idUsuario) {
        JOptionPane.showMessageDialog(this, "No puedes seguirte a ti mismo.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sqlSeguir = "INSERT INTO follows (id_seguidor, id_seguido) VALUES (?, ?)";
        PreparedStatement psSeguir = conexion.prepareStatement(sqlSeguir);
        psSeguir.setInt(1, UsuarioSesion.getUsuarioId());
        psSeguir.setInt(2, this.idUsuario);
        psSeguir.executeUpdate();

        JOptionPane.showMessageDialog(this, "Usuario seguido correctamente", "Seguir", JOptionPane.INFORMATION_MESSAGE);
        cargarFollowingYFollowers();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al seguir usuario", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Método para dejar de seguir a un usuario
private void dejarDeSeguirUsuario() {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sqlDejarDeSeguir = "DELETE FROM follows WHERE id_seguidor = ? AND id_seguido = ?";
        PreparedStatement psDejarDeSeguir = conexion.prepareStatement(sqlDejarDeSeguir);
        psDejarDeSeguir.setInt(1, UsuarioSesion.getUsuarioId());
        psDejarDeSeguir.setInt(2, this.idUsuario);
        int filasEliminadas = psDejarDeSeguir.executeUpdate();

        if (filasEliminadas > 0) {
            JOptionPane.showMessageDialog(this, "Dejaste de seguir a este usuario", "Dejar de seguir", JOptionPane.INFORMATION_MESSAGE);
            cargarFollowingYFollowers();
        } else {
            JOptionPane.showMessageDialog(this, "No estabas siguiendo a este usuario", "Información", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al dejar de seguir usuario", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


private BufferedImage fotoPerfil;
private BufferedImage fotoPortada;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();


    
    
       
private void cargarContenido(int idUsuario, String tipoContenido) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "";
        switch (tipoContenido) {
            case "tweets":
                sql = "SELECT t.contenido, u.alias, u.nombre_usuario, t.usuario_id " +
                      "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
                      "WHERE t.usuario_id = ?";
                break;
            case "likes":
                sql = "SELECT t.contenido, u_like.alias AS alias_like, u_like.nombre_usuario AS nombre_like, u_like.id_usuarios AS usuario_id_like " +
                      "FROM tweets t JOIN likes l ON t.id_tweet = l.tweet_id " +
                      "JOIN usuarios u_like ON l.usuario_id = u_like.id_usuarios " +
                      "WHERE t.id_tweet IN (SELECT tweet_id FROM likes WHERE usuario_id = ?)";
                break;
            case "retweets":
                sql = "SELECT t.contenido, u_rt.alias AS alias_rt, u_rt.nombre_usuario AS nombre_rt, u_rt.id_usuarios AS usuario_id_rt " +
                      "FROM tweets t JOIN retweets r ON t.id_tweet = r.tweet_id " +
                      "JOIN usuarios u_rt ON r.usuario_id = u_rt.id_usuarios " +
                      "WHERE t.id_tweet IN (SELECT tweet_id FROM retweets WHERE usuario_id = ?)";
                break;
        }

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();

         JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));

        while (rs.next()) {
            String contenido = rs.getString("contenido");
            String alias = "";
            String nombreUsuario = "";
            int usuarioIdAutor = 0;

            if (tipoContenido.equals("likes")) {
                alias = rs.getString("alias_like");
                nombreUsuario = rs.getString("nombre_like");
                usuarioIdAutor = rs.getInt("usuario_id_like");
            } else if (tipoContenido.equals("retweets")) {
                alias = rs.getString("alias_rt");
                nombreUsuario = rs.getString("nombre_rt");
                usuarioIdAutor = rs.getInt("usuario_id_rt");
            } else {
                alias = rs.getString("alias");
                nombreUsuario = rs.getString("nombre_usuario");
                usuarioIdAutor = rs.getInt("usuario_id");
            }

            // Crear copias finales o efectivamente finales de las variables
            final int finalUsuarioIdAutor = usuarioIdAutor;

            JPanel panelTweet = new JPanel();
            panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));

            JLabel lblUsuario = new JLabel("<html><b>" + alias + " (" + nombreUsuario + ")</b></html>");
            JTextArea textArea = new JTextArea(contenido);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setBackground(panelTweet.getBackground());

            panelTweet.add(lblUsuario);
            panelTweet.add(textArea);
           

            // Hacer el JLabel del usuario clickable para visitar el perfil
            lblUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lblUsuario.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirPerfilUsuario(finalUsuarioIdAutor); // Usar la copia final
                }
            });

            panelContenido.add(panelTweet);
        }

        JScrollPane scrollPane = new JScrollPane(panelContenido);
        panelContenedorContenido.setViewportView(scrollPane);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar " + tipoContenido + ".", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void abrirPerfilUsuario(int idUsuario) {
            this.dispose();
    perfilusuario perfil = new perfilusuario(idUsuario); // Abre el perfil del usuario con el idUsuario
    perfil.setVisible(true);
}


    private void mostrarFotoEnJLabel(JLabel label, byte[] fotoPerfilBytes) {
        ImageIcon imageIcon = new ImageIcon(fotoPerfilBytes);
        Image imagenEscalada = imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(imagenEscalada));
        label.setText("");
    }
private void cargarFotoPerfil() {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT foto_perfil FROM usuarios WHERE id_usuarios = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, this.idUsuario);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            byte[] fotoPerfilBytes = rs.getBytes("foto_perfil");
            if (fotoPerfilBytes != null) {
                mostrarFotoPerfilEnLabel(fotoPerfilBytes);
            } else {
                lblFotoPerfil.setIcon(null);
                lblFotoPerfil.setText("No hay foto de perfil");
            }
        } else {
            lblFotoPerfil.setIcon(null);
            lblFotoPerfil.setText("Usuario no encontrado");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar foto de perfil", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// Método para mostrar la foto de perfil en el JLabel
private void mostrarFotoPerfilEnLabel(byte[] fotoPerfilBytes) {
    ImageIcon imageIcon = new ImageIcon(fotoPerfilBytes);
    Image image = imageIcon.getImage().getScaledInstance(lblFotoPerfil.getWidth(), lblFotoPerfil.getHeight(), Image.SCALE_SMOOTH);
    lblFotoPerfil.setIcon(new ImageIcon(image));
    lblFotoPerfil.setText(""); // Limpiar el texto si hay imagen
}

  private void mostrarFondoPortadaEnJLabel(JLabel label, byte[] fondoPortadaBytes) {
        ImageIcon imageIcon = new ImageIcon(fondoPortadaBytes);
        Image imagenOriginal = imageIcon.getImage();

        // Establecer ancho y alto del icono
        int nuevoAncho = 680; // Ancho del panel
        int nuevoAlto = 122; // Alto del panel

        Image imagenEscalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(imagenEscalada));
        label.setText("");
    }


 private void cargarFollowingYFollowers() {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        // Cargar cantidad de "Following"
        String sqlFollowing = "SELECT COUNT(*) FROM follows WHERE id_seguidor = ?";
        PreparedStatement psFollowing = conexion.prepareStatement(sqlFollowing);
        psFollowing.setInt(1, this.idUsuario);
        ResultSet rsFollowing = psFollowing.executeQuery();

        if (rsFollowing.next()) {
            lblFollowing.setText("Following: " + rsFollowing.getInt(1));
        } else {
            lblFollowing.setText("Following: 0");
        }

        // Cargar cantidad de "Followers"
        String sqlFollowers = "SELECT COUNT(*) FROM follows WHERE id_seguido = ?";
        PreparedStatement psFollowers = conexion.prepareStatement(sqlFollowers);
        psFollowers.setInt(1, this.idUsuario);
        ResultSet rsFollowers = psFollowers.executeQuery();

        if (rsFollowers.next()) {
            lblFollowers.setText("Followers: " + rsFollowers.getInt(1));
        } else {
            lblFollowers.setText("Followers: 0");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar following/followers", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
 
 
 // Métodos para cargar listas de "Following" y "Followers"
private void cargarListaFollowing() {
    cargarListaUsuarios("Following", "id_seguido", this.idUsuario);
}

private void cargarListaFollowers() {
    cargarListaUsuarios("Followers", "id_seguidor", this.idUsuario);
}

private void cargarListaUsuarios(String titulo, String columnaId, int idUsuario) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT u.nombre_usuario FROM usuarios u JOIN follows f ON u.id_usuarios = f." + columnaId + " WHERE f.id_seguido = ?"; // Cambiado a id_seguido
        if (titulo.equals("Following")) {
            sql = "SELECT u.nombre_usuario FROM usuarios u JOIN follows f ON u.id_usuarios = f." + columnaId + " WHERE f.id_seguidor = ?";
        }

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();

        StringBuilder listaUsuarios = new StringBuilder();
        while (rs.next()) {
            listaUsuarios.append(rs.getString("nombre_usuario")).append("\n");
        }

        JOptionPane.showMessageDialog(this, listaUsuarios.toString(), titulo, JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar lista de usuarios", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // Método para mostrar la foto en el JPanel
    private void mostrarFotoEnJPanel(JPanel panel, byte[] imageData) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            Image scaledImage = image.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            panel.removeAll();
            panel.add(label);
            panel.revalidate();
            panel.repaint();

            System.out.println("Foto de perfil mostrada en JPanel.");

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar la foto de perfil.");
        }
    }

    
        private void cargarFotos() {
        System.out.println("Cargando foto de perfil...");
        cargarFotoPerfilEnJPanel(panelFotoPerfil); // Cargar la foto en el JPanel
        // Puedes agregar lógica similar para la foto de portada si es necesario
    }

          private void cargarFotoPerfilEnJPanel(JPanel panel) {
        Connection con = RetornarBaseDedatos.getConnection();
        if (con != null) {
            try {
                String sql = "SELECT foto_perfil FROM usuarios WHERE id_usuarios = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    byte[] fotoPerfilBytes = rs.getBytes("foto_perfil");
                    if (fotoPerfilBytes != null && fotoPerfilBytes.length > 0) {
                        mostrarFotoEnJPanel(panel, fotoPerfilBytes);
                    } else {
                        panel.removeAll();
                        panel.repaint();
                        System.out.println("No hay foto de perfil para este usuario.");
                    }
                }

                rs.close();
                ps.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar la foto de perfil.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
        }
    }
          

    private void cargarTweetsUsuario() {
        cargarContenido(this.idUsuario, "tweets");
    }

    private void cargarLikesUsuario() {
        cargarContenido(this.idUsuario, "likes");
    }

    private void cargarRetweetsUsuario() {
        cargarContenido(this.idUsuario, "retweets");
    }

    
    // Método para obtener el valor existente de un campo en la base de datos
    private String obtenerValorExistente(String columna, int idUsuario) {
        Connection con = RetornarBaseDedatos.getConnection();
        String valorExistente = null;
        if (con != null) {
            try {
                String sql = "SELECT " + columna + " FROM usuarios WHERE id_usuarios = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    valorExistente = rs.getString(columna);
                }
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return valorExistente;
    }


    // Método para convertir BufferedImage a byte[]
    private byte[] convertirImagenABytes(BufferedImage image) {
        if (image == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos); // Asegúrate de que el formato sea correcto
            byte[] bytes = baos.toByteArray();
            System.out.println("Bytes de la imagen: " + bytes.length); // Verificar la longitud de los bytes
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

     private void agregarMenuFotoPerfil() {
        lblFotoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarMenuOpciones(evt);
            }
        });
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
        
        // Mostrar el menú en la posición del clic
        menu.show(lblFotoPerfil, evt.getX(), evt.getY());
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
private void abrirConfiguracion() {
    EdiPerfil editarPerfil = new EdiPerfil();
    editarPerfil.setLocationRelativeTo(this);
    editarPerfil.setVisible(true);
}


  private void mostrarTweets(int idUsuario) {
        List<String> tweets = obtenerTweets(idUsuario);
        // Mostrar los tweets en el JLabel correspondiente
        // Ejemplo:
        // jLabelTweets.setText(String.join("<br>", tweets));
    }
  
  
    private List<String> obtenerTweets(int idUsuario) {
        List<String> tweets = new ArrayList<>();
        Connection con = RetornarBaseDedatos.getConnection();
        if (con != null) {
            try {
                String sql = "SELECT contenido FROM tweets WHERE id_usuario = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    tweets.add(rs.getString("contenido"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }
    
    private void mostrarRetweets(int idUsuario) {
        List<String> retweets = obtenerRetweets(idUsuario);
        // Mostrar los retweets en el JLabel correspondiente
    }
    
    private List<String> obtenerRetweets(int idUsuario) {
        List<String> retweets = new ArrayList<>();
        Connection con = RetornarBaseDedatos.getConnection();
        if (con != null) {
            try {
                String sql = "SELECT t.contenido FROM tweets t JOIN retweets r ON t.id_tweet = r.id_tweet WHERE r.id_usuario = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    retweets.add(rs.getString("contenido"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return retweets;
    }
    private void mostrarLikes(int idUsuario) {
        List<String> likes = obtenerLikes(idUsuario);
        // Mostrar los likes en el JLabel correspondiente
    }

    private List<String> obtenerLikes(int idUsuario) {
        List<String> likes = new ArrayList<>();
        Connection con = RetornarBaseDedatos.getConnection();
        if (con != null) {
            try {
                String sql = "SELECT t.contenido FROM tweets t JOIN likes l ON t.id_tweet = l.id_tweet WHERE l.id_usuario = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    likes.add(rs.getString("contenido"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return likes;
    }
    
    
private void actualizarNombreYAlias() {
    // Usar el idUsuario de la instancia de perfilusuario
    if (this.idUsuario != -1) {
        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
            String sql = "SELECT nombre_usuario, alias FROM usuarios WHERE id_usuarios = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, this.idUsuario); // Usar this.idUsuario
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        Notificaciones = new javax.swing.JLabel();
        Explorar = new javax.swing.JLabel();
        Inicio = new javax.swing.JLabel();
        PPerfil = new javax.swing.JPanel();
        panelFotoPortada = new javax.swing.JPanel();
        lblFondoPortada = new javax.swing.JLabel();
        btnUnfollow = new javax.swing.JButton();
        lblAliasNombre = new javax.swing.JLabel();
        lblFollowing = new javax.swing.JLabel();
        lblFollowers = new javax.swing.JLabel();
        panelFotoPerfil = new javax.swing.JPanel();
        btnTweets = new javax.swing.JButton();
        btnRetweets = new javax.swing.JButton();
        btnLikes = new javax.swing.JButton();
        btnMensajes = new javax.swing.JButton();
        lblFotoPerfil = new javax.swing.JLabel();
        btnAbrirChat = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelContenedorContenido = new javax.swing.JScrollPane();
        btnSeguir1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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
                    .addComponent(Notificaciones)
                    .addComponent(Explorar)
                    .addComponent(Inicio))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LogoTwitter2)
                .addGap(93, 93, 93)
                .addComponent(Inicio)
                .addGap(58, 58, 58)
                .addComponent(Explorar)
                .addGap(56, 56, 56)
                .addComponent(Notificaciones)
                .addContainerGap(621, Short.MAX_VALUE))
        );

        PPerfil.setBackground(new java.awt.Color(255, 255, 255));
        PPerfil.setDoubleBuffered(false);
        PPerfil.setEnabled(false);
        PPerfil.setFocusable(false);

        panelFotoPortada.setBackground(new java.awt.Color(255, 255, 255));
        panelFotoPortada.setDoubleBuffered(false);
        panelFotoPortada.setEnabled(false);
        panelFotoPortada.setFocusable(false);
        panelFotoPortada.setOpaque(false);
        panelFotoPortada.setRequestFocusEnabled(false);
        panelFotoPortada.setVerifyInputWhenFocusTarget(false);
        panelFotoPortada.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelFotoPortadaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblFondoPortada.setText("portada");
        lblFondoPortada.setEnabled(false);
        lblFondoPortada.setFocusable(false);

        javax.swing.GroupLayout panelFotoPortadaLayout = new javax.swing.GroupLayout(panelFotoPortada);
        panelFotoPortada.setLayout(panelFotoPortadaLayout);
        panelFotoPortadaLayout.setHorizontalGroup(
            panelFotoPortadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoPortadaLayout.createSequentialGroup()
                .addComponent(lblFondoPortada, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
        panelFotoPortadaLayout.setVerticalGroup(
            panelFotoPortadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoPortadaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFondoPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        btnUnfollow.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnUnfollow.setForeground(new java.awt.Color(102, 0, 153));
        btnUnfollow.setText("Unfollow");
        btnUnfollow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnfollowActionPerformed(evt);
            }
        });

        lblAliasNombre.setText("@Alias");
        lblAliasNombre.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblAliasNombreAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblFollowing.setText("Following");
        lblFollowing.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFollowingMouseClicked(evt);
            }
        });

        lblFollowers.setText("Followers");
        lblFollowers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFollowersMouseClicked(evt);
            }
        });

        panelFotoPerfil.setBackground(new java.awt.Color(255, 255, 255));
        panelFotoPerfil.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelFotoPerfilAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout panelFotoPerfilLayout = new javax.swing.GroupLayout(panelFotoPerfil);
        panelFotoPerfil.setLayout(panelFotoPerfilLayout);
        panelFotoPerfilLayout.setHorizontalGroup(
            panelFotoPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        panelFotoPerfilLayout.setVerticalGroup(
            panelFotoPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );

        btnTweets.setText("Tweets");
        btnTweets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTweetsActionPerformed(evt);
            }
        });

        btnRetweets.setText("Retweets");
        btnRetweets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetweetsActionPerformed(evt);
            }
        });

        btnLikes.setText("Likes");
        btnLikes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLikesActionPerformed(evt);
            }
        });

        btnMensajes.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnMensajes.setForeground(new java.awt.Color(102, 0, 153));
        btnMensajes.setText("Mensaje");
        btnMensajes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMensajesMouseClicked(evt);
            }
        });
        btnMensajes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMensajesActionPerformed(evt);
            }
        });

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

        btnAbrirChat.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnAbrirChat.setForeground(new java.awt.Color(102, 0, 153));
        btnAbrirChat.setText("Chat");
        btnAbrirChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAbrirChatMouseClicked(evt);
            }
        });
        btnAbrirChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirChatActionPerformed(evt);
            }
        });

        panelContenedorContenido.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedorContenido.setBorder(null);
        jScrollPane1.setViewportView(panelContenedorContenido);

        btnSeguir1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btnSeguir1.setForeground(new java.awt.Color(102, 0, 153));
        btnSeguir1.setText("Seguir");
        btnSeguir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeguir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PPerfilLayout = new javax.swing.GroupLayout(PPerfil);
        PPerfil.setLayout(PPerfilLayout);
        PPerfilLayout.setHorizontalGroup(
            PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PPerfilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PPerfilLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTweets))
                        .addGap(32, 32, 32)
                        .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PPerfilLayout.createSequentialGroup()
                                .addComponent(lblFollowing)
                                .addGap(60, 60, 60)
                                .addComponent(lblFollowers))
                            .addComponent(lblAliasNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PPerfilLayout.createSequentialGroup()
                                .addComponent(btnRetweets)
                                .addGap(254, 254, 254)
                                .addComponent(btnLikes))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(PPerfilLayout.createSequentialGroup()
                                    .addComponent(btnUnfollow)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btnSeguir1))
                                .addGroup(PPerfilLayout.createSequentialGroup()
                                    .addComponent(btnAbrirChat)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnMensajes))))
                        .addGap(44, 44, 44))
                    .addGroup(PPerfilLayout.createSequentialGroup()
                        .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelFotoPortada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PPerfilLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(panelFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PPerfilLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        PPerfilLayout.setVerticalGroup(
            PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PPerfilLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(panelFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFotoPortada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PPerfilLayout.createSequentialGroup()
                        .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUnfollow)
                            .addComponent(lblAliasNombre)
                            .addComponent(btnSeguir1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnMensajes)
                            .addComponent(lblFollowing)
                            .addComponent(lblFollowers)
                            .addComponent(btnAbrirChat)))
                    .addComponent(lblFotoPerfil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(PPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLikes)
                    .addComponent(btnRetweets)
                    .addComponent(btnTweets))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(PPerfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 68, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 68;
        gridBagConstraints.ipady = -132;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLikesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLikesActionPerformed
        cargarLikesUsuario();
        btnLikes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnLikesActionPerformed(evt);
            }
        });
    }//GEN-LAST:event_btnLikesActionPerformed

    private void btnRetweetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetweetsActionPerformed
        cargarRetweetsUsuario();
        btnRetweets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnRetweetsActionPerformed(evt);
            }
        });
    }//GEN-LAST:event_btnRetweetsActionPerformed

    private void btnTweetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTweetsActionPerformed
        cargarTweetsUsuario();
        btnTweets.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnTweetsActionPerformed(evt);
            }
        });
    }//GEN-LAST:event_btnTweetsActionPerformed

    private void panelFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelFotoPerfilAncestorAdded
        panelFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
    }//GEN-LAST:event_panelFotoPerfilAncestorAdded

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void lblFollowersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFollowersMouseClicked
        cargarListaFollowers();

    }//GEN-LAST:event_lblFollowersMouseClicked

    private void lblFollowingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFollowingMouseClicked
        cargarListaFollowing();

    }//GEN-LAST:event_lblFollowingMouseClicked

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
        actualizarNombreYAlias(); // Llamar al método al cargar la interfaz
    }//GEN-LAST:event_lblAliasNombreAncestorAdded

    private void btnUnfollowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnfollowActionPerformed
   dejarDeSeguirUsuario();
    // Después de dejar de seguir, actualiza la visibilidad de los botones
    btnSeguir1.setVisible(true);
    btnUnfollow.setVisible(false);
    siguiendo = false;
    }//GEN-LAST:event_btnUnfollowActionPerformed

    private void panelFotoPortadaAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelFotoPortadaAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_panelFotoPortadaAncestorAdded

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

    private void btnMensajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMensajesActionPerformed
btnMensajes.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        abrirMensajes();
    }
});

    }//GEN-LAST:event_btnMensajesActionPerformed

    private void btnMensajesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMensajesMouseClicked
btnMensajes.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        int usuarioId = UsuarioSesion.getUsuarioId(); // Obtener el ID del usuario actual
        Mensajes mensajes = new Mensajes(usuarioId); // Pasar el ID del usuario
        mensajes.setVisible(true); // Mostrar la ventana de mensajes
    }
});
    }//GEN-LAST:event_btnMensajesMouseClicked

    private void btnAbrirChatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbrirChatMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAbrirChatMouseClicked


    private void btnAbrirChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirChatActionPerformed
 idDestinatario = this.idUsuario; // Asegura que tomas el ID correcto del perfil visitado
    System.out.println("idUsuario obtenido: " + UsuarioSesion.getUsuarioId());
    System.out.println("idDestinatario obtenido: " + idDestinatario);

    if (idDestinatario == 0) {
        System.err.println("Error: idDestinatario no está inicializado correctamente.");
        return; // Evita abrir el chat si no hay destinatario
    }

    Chat chat = new Chat(UsuarioSesion.getUsuarioId(), idDestinatario);
    chat.setVisible(true);
    }//GEN-LAST:event_btnAbrirChatActionPerformed

    private void InicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InicioMouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_InicioMouseClicked

    private void ExplorarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ExplorarMouseClicked
         BusquedaTwitter h = new BusquedaTwitter();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ExplorarMouseClicked

    private void NotificacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NotificacionesMouseClicked
           notificaciones h = new notificaciones();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_NotificacionesMouseClicked

    private void btnSeguir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeguir1ActionPerformed
           seguirUsuario();
    // Después de seguir, actualiza la visibilidad de los botones
    btnSeguir1.setVisible(false);
    btnUnfollow.setVisible(true);
    siguiendo = true;
    }//GEN-LAST:event_btnSeguir1ActionPerformed

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
            java.util.logging.Logger.getLogger(perfilusuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(perfilusuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(perfilusuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(perfilusuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new perfilusuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Explorar;
    private javax.swing.JLabel Inicio;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JLabel Notificaciones;
    private javax.swing.JPanel PPerfil;
    private javax.swing.JButton btnAbrirChat;
    private javax.swing.JButton btnLikes;
    private javax.swing.JButton btnMensajes;
    private javax.swing.JButton btnRetweets;
    private javax.swing.JButton btnSeguir1;
    private javax.swing.JButton btnTweets;
    private javax.swing.JButton btnUnfollow;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFollowers;
    private javax.swing.JLabel lblFollowing;
    private javax.swing.JLabel lblFondoPortada;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JScrollPane panelContenedorContenido;
    private javax.swing.JPanel panelFotoPerfil;
    private javax.swing.JPanel panelFotoPortada;
    // End of variables declaration//GEN-END:variables
}
