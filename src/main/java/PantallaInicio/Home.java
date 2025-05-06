/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PantallaInicio;
import UsuarioID.UsuarioSesion;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import Perfilusuario.perfilusuario;
import java.awt.Insets;  // Este es el import necesario para setMargin()
import Explorar.BusquedaTwitter;
import java.util.ArrayList; // Importa ArrayList
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
import UsuarioID.UsuarioSesion;
import java.io.FileInputStream;
import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import EditarPerfil.EdiPerfil;
import java.util.Base64;
import IniciarSesion.Iniciarsesionlogin;
import EditarPerfil.EdiPerfil;
import Perfilusuario.perfilusuario;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import Notificaciones.notificaciones;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.AlphaComposite;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
import UsuarioID.UsuarioSesion;
import javax.swing.BorderFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; // Para el cursor de mano
/**
 *
 * @author Jaime Paredes
 */
public class Home extends javax.swing.JFrame {
/*Jaime*/
    Color colorNormalMenu = new Color(246,234,250);
    Color colorOscuroMenu = new Color(242, 226, 248);

private javax.swing.JTextArea txtTweet;
private File archivoImagen; 
    private int usuarioIdPerfilMostrado = -1; // Variable global para almacenar el usuarioId del perfil mostrado



private class Tweet { // Clase interna para representar un tweet
    int usuarioId;
    String nombreUsuario;
    String alias;
    // ... (Otros campos que necesites)

    public Tweet(int usuarioId, String nombreUsuario, String alias) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.alias = alias;
        // ...
    }
}

private void cargarFotoPerfil() {
    int idUsuario = UsuarioSesion.getUsuarioId();  // Obtener el ID del usuario actual
    UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instancia del DAO
    
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




private void abrirVentanaComentario(int idTweet) {
    int usuarioId = UsuarioSesion.getUsuarioId();
    System.out.println("🧠 ID del usuario en sesión: " + usuarioId);

    // Crear la ventana de comentarios
    JDialog ventanaComentario = new JDialog();
    ventanaComentario.setTitle("Comentar Tweet");
    ventanaComentario.setSize(450, 400);
    ventanaComentario.setLocationRelativeTo(null);
    ventanaComentario.setLayout(new BorderLayout());

    // Panel principal
    JPanel panelPrincipal = new JPanel();
    panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
    panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Área de texto para el comentario
    JTextArea textoComentario = new JTextArea(5, 30);
    textoComentario.setWrapStyleWord(true);
    textoComentario.setLineWrap(true);
    JScrollPane scrollTexto = new JScrollPane(textoComentario);
    panelPrincipal.add(new JLabel("Escribe tu comentario:"));
    panelPrincipal.add(Box.createVerticalStrut(5));
    panelPrincipal.add(scrollTexto);
    panelPrincipal.add(Box.createVerticalStrut(10));

    // Panel para la selección de multimedia
    JPanel panelMultimedia = new JPanel();
    panelMultimedia.setLayout(new BoxLayout(panelMultimedia, BoxLayout.Y_AXIS));
    panelMultimedia.setBorder(BorderFactory.createTitledBorder("Multimedia (Opcional)"));

    JLabel lblMultimediaSeleccionada = new JLabel("(No se ha seleccionado ningún archivo)");
    JLabel lblVistaPreviaMultimedia = new JLabel(); // Para una vista previa básica

    final File[] archivoMultimedia = {null}; // Usamos un array para hacerlo "final"

    JButton btnSeleccionarMultimedia = new JButton("Seleccionar Multimedia");
    btnSeleccionarMultimedia.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo multimedia");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
                "Imágenes y otros archivos soportados", "jpg", "jpeg", "png", "gif", /* Agrega otros formatos si es necesario */ "mp4", "avi", "mov" /* etc. */
        ));

        int result = fileChooser.showOpenDialog(ventanaComentario);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoMultimedia[0] = fileChooser.getSelectedFile();
            lblMultimediaSeleccionada.setText("Archivo seleccionado: " + archivoMultimedia[0].getName());

            // Mostrar una vista previa básica (solo para imágenes por simplicidad)
            String nombreArchivo = archivoMultimedia[0].getName().toLowerCase();
            if (nombreArchivo.endsWith(".jpg") || nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".png") || nombreArchivo.endsWith(".gif")) {
                try {
                    ImageIcon icon = new ImageIcon(archivoMultimedia[0].getAbsolutePath());
                    Image imagenEscalada = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    lblVistaPreviaMultimedia.setIcon(new ImageIcon(imagenEscalada));
                    lblVistaPreviaMultimedia.setText("");
                } catch (Exception ex) {
                    lblVistaPreviaMultimedia.setIcon(null);
                    lblVistaPreviaMultimedia.setText("(Vista previa no disponible)");
                }
            } else {
                lblVistaPreviaMultimedia.setIcon(null);
                lblVistaPreviaMultimedia.setText("(Archivo no es una imagen para previsualizar)");
            }
        }
    });

    JButton btnEliminarMultimedia = new JButton("❌ Eliminar Selección");
    btnEliminarMultimedia.addActionListener(e -> {
        archivoMultimedia[0] = null;
        lblMultimediaSeleccionada.setText("(No se ha seleccionado ningún archivo)");
        lblVistaPreviaMultimedia.setIcon(null);
        lblVistaPreviaMultimedia.setText("");
    });

    panelMultimedia.add(btnSeleccionarMultimedia);
    panelMultimedia.add(lblMultimediaSeleccionada);
    panelMultimedia.add(lblVistaPreviaMultimedia);
    panelMultimedia.add(btnEliminarMultimedia);
    panelPrincipal.add(panelMultimedia);
    panelPrincipal.add(Box.createVerticalStrut(10));

    // Panel para botones de acción
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

    // Botón Comentar
    JButton btnComentar = new JButton("💬 Comentar");
    btnComentar.addActionListener(e -> {
        String comentarioTexto = textoComentario.getText().trim();
        if (!comentarioTexto.isEmpty()) {
            byte[] multimediaBytes = null;
            if (archivoMultimedia[0] != null) {
                multimediaBytes = cargarArchivoMultimedia(archivoMultimedia[0]); // Necesitas implementar esta función
            }
            agregarComentario(comentarioTexto, idTweet, multimediaBytes); // Necesitas implementar esta función
            ventanaComentario.dispose();
            // Aquí podrías agregar lógica para refrescar la vista de comentarios
        } else {
            JOptionPane.showMessageDialog(ventanaComentario, "El comentario no puede estar vacío.");
        }
    });
    panelBotones.add(btnComentar);

    // Botón Cancelar
    JButton btnCancelar = new JButton("Cancelar");
    btnCancelar.addActionListener(e -> ventanaComentario.dispose());
    panelBotones.add(btnCancelar);

    ventanaComentario.add(panelPrincipal, BorderLayout.CENTER);
    ventanaComentario.add(panelBotones, BorderLayout.SOUTH);
    ventanaComentario.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    ventanaComentario.setVisible(true);
}




private void agregarComentario(String contenido, int idTweet, byte[] multimedia) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "INSERT INTO comentarios (tweet_id, usuario_id, contenido, multimedia, fecha_creacion) " +
                     "VALUES (?, ?, ?, ?, NOW())";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId()); // Usuario actual
        ps.setString(3, contenido);
        
        if (multimedia != null) {
            ps.setBytes(4, multimedia); // Subir el archivo multimedia como BLOB
        } else {
            ps.setNull(4, java.sql.Types.BLOB); // Si no hay multimedia, insertar NULL
        }

        ps.executeUpdate();
        JOptionPane.showMessageDialog(this, "Comentario publicado.");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al comentar.");
    }
}


private void quitarLike(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "DELETE FROM likes WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void buscarTweets(String textoBusqueda) {
    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
        // Si está vacío, no hacer nada y salir del método
        return;
    }
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        // Consulta modificada para incluir foto_perfil
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.id_tweet, " +
                    "t.usuario_id, t.multimedia, u.foto_perfil " +  // Añadido u.foto_perfil
                    "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
                    "WHERE t.contenido LIKE ? " +
                    "ORDER BY t.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, "%" + textoBusqueda + "%");
        ResultSet rs = ps.executeQuery();

        panelContenedorTweets.removeAll();
        panelContenedorTweets.setLayout(new BoxLayout(panelContenedorTweets, BoxLayout.Y_AXIS));

        while (rs.next()) {
    String nombreUsuario = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            int idTweet = rs.getInt("id_tweet");
            int usuarioId = rs.getInt("usuario_id"); // Obtener el ID del usuario
            Blob multimedia = rs.getBlob("multimedia");
            Blob fotoPerfilBlob = rs.getBlob("foto_perfil");

            boolean esMio = (usuarioId == UsuarioSesion.getUsuarioId());

            JPanel panelTweet = new JPanel(new BorderLayout());
            panelTweet.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panelTweet.setBackground(Color.WHITE);
            panelTweet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            // Panel superior con foto de perfil, nombre y alias
            JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelUsuario.setBackground(Color.WHITE);

            // Mostrar foto de perfil
            if (fotoPerfilBlob != null) {
                try {
                    byte[] fotoBytes = fotoPerfilBlob.getBytes(1, (int) fotoPerfilBlob.length());
                    ImageIcon icon = new ImageIcon(fotoBytes);
                    Image imagenEscalada = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    JLabel labelFoto = new JLabel(new ImageIcon(imagenEscalada));
                    panelUsuario.add(labelFoto);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            
            
            // Añadir nombre, alias y fecha
            JLabel labelUsuarioInfo = new JLabel("<html><b>" + nombreUsuario + "</b> @" + alias + " 🕒 " + fecha + "</html>");
            panelUsuario.add(labelUsuarioInfo);

            JLabel lblNombreUsuarioAlias = new JLabel(nombreUsuario + " @" + alias);
            lblNombreUsuarioAlias.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Agregar MouseListener para abrir el perfil del usuario
            lblNombreUsuarioAlias.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    abrirPerfilUsuario(usuarioId); // Utiliza el usuarioId obtenido de la base de datos
                }
            });
            panelUsuario.add(lblNombreUsuarioAlias);
            panelTweet.add(panelUsuario, BorderLayout.NORTH);
            

            // Añadir nombre, alias y fecha
            JLabel labelUsuario = new JLabel("<html><b>" + nombreUsuario + "</b> @" + alias + " 🕒 " + fecha + "</html>");
            panelUsuario.add(labelUsuario);
            panelTweet.add(panelUsuario, BorderLayout.NORTH);

            // Contenido del tweet (solo texto)
            JTextArea textoTweet = new JTextArea(contenido);
            textoTweet.setEditable(false);
            textoTweet.setLineWrap(true);
            textoTweet.setWrapStyleWord(true);
            textoTweet.setBackground(Color.WHITE);
            panelTweet.add(textoTweet, BorderLayout.CENTER);

            // Multimedia del tweet (si existe)
            if (multimedia != null) {
                try {
                    byte[] imageBytes = multimedia.getBytes(1, (int) multimedia.length());
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image imagenEscalada = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    JLabel imagenLabel = new JLabel(new ImageIcon(imagenEscalada));
                    panelTweet.add(imagenLabel, BorderLayout.WEST);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Resto del código (panel de interacciones) permanece igual
            JPanel panelInteracciones = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // ❤️ Like
            final JButton[] btnLike = new JButton[1];
            final boolean[] yaDioLike = { !usuarioPuedeDarLike(idTweet) };
            btnLike[0] = new JButton((yaDioLike[0] ? "💔 Quitar " : "❤️ ") + obtenerTotalLikes(idTweet));
            btnLike[0].addActionListener(e -> {
                if (yaDioLike[0]) {
                    quitarLike(idTweet);
                    yaDioLike[0] = false;
                } else {
                    darLike(idTweet);
                    yaDioLike[0] = true;
                }
                btnLike[0].setText((yaDioLike[0] ? "💔 Quitar " : "❤️ ") + obtenerTotalLikes(idTweet));
            });
            panelInteracciones.add(btnLike[0]);

            // 🔁 Retweet
            final JButton[] btnRT = new JButton[1];
            final boolean[] yaRT = { !usuarioPuedeRetweetear(idTweet) };
            btnRT[0] = new JButton((yaRT[0] ? "❌ Quitar RT " : "🔁 ") + obtenerTotalRetweets(idTweet));
            btnRT[0].addActionListener(e -> {
                if (yaRT[0]) {
                    quitarRetweet(idTweet);
                    yaRT[0] = false;
                } else {
                    retweetear(idTweet);
                    yaRT[0] = true;
                }
                btnRT[0].setText((yaRT[0] ? "❌ Quitar RT " : "🔁 ") + obtenerTotalRetweets(idTweet));
            });
            panelInteracciones.add(btnRT[0]);

            // 💬 Ver comentarios
            int totalComentarios = obtenerTotalComentarios(idTweet);
            JButton btnVerComentarios = new JButton("💬 Ver comentarios (" + totalComentarios + ")");
            btnVerComentarios.addActionListener(e -> {
                mostrarComentarios(idTweet, panelContenedorTweets);
            });
            panelInteracciones.add(btnVerComentarios);

            // 💬 Comentar
            JButton btnComentar = new JButton("💬 Comentar");
            btnComentar.addActionListener(e -> {
                abrirVentanaComentario(idTweet);
            });
            panelInteracciones.add(btnComentar);

            // 🗑 Eliminar si es mío
             if (esMio) {
            // ✏️ Editar tweet
JButton btnEditar = new JButton("✏️ Editar");
btnEditar.addActionListener(e -> {
    // Obtener el ID del tweet y la multimedia actual
    String multimediaActual = obtenerMultimediaDeTweet(idTweet); // Obtener imagen actual

    // Crear componentes para la edición
    JTextField nuevoContenidoField = new JTextField(contenido);
    JButton btnSeleccionarMultimedia = new JButton("📷 Seleccionar Imagen/GIF");
    JLabel lblMultimediaSeleccionada = new JLabel();
    JButton btnEliminarMultimedia = new JButton("🗑️ Quitar Multimedia");

    final String[] nuevaRutaMultimedia = {multimediaActual};

    // 🔹 Cargar y mostrar imagen actual si existe
    if (multimediaActual != null && !multimediaActual.isEmpty()) {
        File archivoImagen = new File(multimediaActual);
        if (archivoImagen.exists()) { // Verifica que la imagen realmente existe
            ImageIcon icono = new ImageIcon(multimediaActual);
            Image imagen = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Ajuste fijo 100x100
            lblMultimediaSeleccionada.setIcon(new ImageIcon(imagen));
        } else {
            lblMultimediaSeleccionada.setIcon(null); // Si no existe, no muestra nada
        }
    }

    // Crear los paneles para la edición
    JPanel panelEdicion = new JPanel(new BorderLayout());
    JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
    JPanel panelInferior = new JPanel(new FlowLayout());

    panelSuperior.add(nuevoContenidoField);
    panelSuperior.add(lblMultimediaSeleccionada);

    panelInferior.add(btnSeleccionarMultimedia);
    panelInferior.add(btnEliminarMultimedia);

    panelEdicion.add(panelSuperior, BorderLayout.CENTER);
    panelEdicion.add(panelInferior, BorderLayout.SOUTH);

    // Crear el JDialog para la ventana de edición
    JDialog dialog = new JDialog();
    dialog.setTitle("Editar Tweet");
    dialog.setModal(true);
    dialog.setSize(900, 800); // Ajustado el tamaño de la ventana
    dialog.setLocationRelativeTo(null); // Centrar en la pantalla
    dialog.setLayout(new BorderLayout());
    dialog.add(panelEdicion, BorderLayout.CENTER);

    // Acción para seleccionar una nueva multimedia
    btnSeleccionarMultimedia.addActionListener(ev -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes/GIF", "jpg", "png", "gif"));
        int resultado = fileChooser.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            nuevaRutaMultimedia[0] = fileChooser.getSelectedFile().getAbsolutePath();
            ImageIcon iconoNuevo = new ImageIcon(nuevaRutaMultimedia[0]);
            Image imagenNueva = iconoNuevo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Tamaño 100x100
            lblMultimediaSeleccionada.setIcon(new ImageIcon(imagenNueva));
        }
    });

    // Acción para eliminar la multimedia
    btnEliminarMultimedia.addActionListener(ev -> {
        nuevaRutaMultimedia[0] = null;
        lblMultimediaSeleccionada.setIcon(null);
    });

    // Crear panel de botones para guardar o cancelar
    JPanel panelBotones = new JPanel(new FlowLayout());
    JButton btnGuardar = new JButton("Guardar");
    JButton btnCancelar = new JButton("Cancelar");

    // Acción para guardar los cambios
    btnGuardar.addActionListener(ev -> {
        String nuevoContenido = nuevoContenidoField.getText().trim();
        if (!nuevoContenido.isEmpty()) {
            // Guardar el contenido del tweet y la multimedia (convertir la imagen a bytes)
            if (nuevaRutaMultimedia[0] != null) {
                try {
                    File file = new File(nuevaRutaMultimedia[0]);
                    byte[] multimediaBytes = new byte[(int) file.length()];
                    try (FileInputStream fis = new FileInputStream(file)) {
                        fis.read(multimediaBytes);
                    }

                    // Llamar a editarTweet con la nueva imagen
                    editarTweet(idTweet, nuevoContenido, multimediaBytes);
                    cargarTweets(); // Ahora se actualiza correctamente
                    dialog.dispose(); // Cerrar la ventana después de guardar

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                // Si no se seleccionó nueva multimedia, solo editar el contenido del tweet
                editarTweet(idTweet, nuevoContenido, null);
                cargarTweets();
                dialog.dispose();
            }
        }
    });

    // Acción para cancelar la edición
    btnCancelar.addActionListener(ev -> dialog.dispose()); // Cerrar sin guardar

    panelBotones.add(btnGuardar);
    panelBotones.add(btnCancelar);

    dialog.add(panelBotones, BorderLayout.SOUTH);
    dialog.setVisible(true);
});

panelInteracciones.add(btnEditar);


                JButton btnEliminar = new JButton("🗑 Eliminar");
                btnEliminar.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este tweet?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarTweet(idTweet);
                        cargarTweets();
                    }
                });
                panelInteracciones.add(btnEliminar);
            }

            panelTweet.add(panelInteracciones, BorderLayout.SOUTH);
            panelContenedorTweets.add(panelTweet);
            panelContenedorTweets.add(Box.createVerticalStrut(10));
        }

        panelContenedorTweets.revalidate();
        panelContenedorTweets.repaint();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al buscar tweets.");
    }
}



private void quitarRetweet(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "DELETE FROM retweets WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private int obtenerTotalComentarios(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT COUNT(*) FROM comentarios WHERE tweet_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

private void eliminarTweet(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "DELETE FROM tweets WHERE id_tweet = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId()); // protección extra: solo el dueño puede borrar
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al eliminar el tweet.");
    }
}
private void editarTweet(int id_tweet, String contenidoNuevo, byte[] multimediaBytes) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql;
        PreparedStatement ps;

        if (multimediaBytes == null) {
            // Si el usuario eliminó la multimedia, ponemos NULL en la BD
            sql = "UPDATE tweets SET contenido = ?, multimedia = NULL WHERE id_tweet = ? AND usuario_id = ?";
            ps = c.prepareStatement(sql);
            ps.setString(1, contenidoNuevo);
            ps.setInt(2, id_tweet);
            ps.setInt(3, UsuarioSesion.getUsuarioId());
        } else {
            // Si hay una nueva imagen o se mantiene la existente
            sql = "UPDATE tweets SET contenido = ?, multimedia = ? WHERE id_tweet = ? AND usuario_id = ?";
            ps = c.prepareStatement(sql);
            ps.setString(1, contenidoNuevo);
            ps.setBytes(2, multimediaBytes); // Aquí pasamos el array de bytes
            ps.setInt(3, id_tweet);
            ps.setInt(4, UsuarioSesion.getUsuarioId());
        }

        int filasActualizadas = ps.executeUpdate();
        if (filasActualizadas > 0) {
            JOptionPane.showMessageDialog(null, "Tweet editado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "No tienes permiso para editar este tweet.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al editar el tweet.");
    }
}


private ArrayList<Tweet> tweets = new ArrayList<>(); // Lista para guardar los tweets
private void cargarTweets() {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.multimedia, t.id_tweet, " +
                     "t.usuario_id, u.foto_perfil " +
                     "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
                     "ORDER BY t.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        panelContenedorTweets.removeAll();
        panelContenedorTweets.setLayout(new BoxLayout(panelContenedorTweets, BoxLayout.Y_AXIS));

        tweets.clear(); // Limpiar la lista antes de cargar nuevos tweets

        while (rs.next()) {
            int usuarioId = rs.getInt("usuario_id");
            String nombreUsuario = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            Blob multimedia = rs.getBlob("multimedia");
            int idTweet = rs.getInt("id_tweet");
            Blob fotoPerfilBlob = rs.getBlob("foto_perfil");

            tweets.add(new Tweet(usuarioId, nombreUsuario, alias)); // Crear y agregar Tweet a la lista

            boolean esMio = (usuarioId == UsuarioSesion.getUsuarioId());

            JPanel panelTweet = new JPanel(new BorderLayout());
            panelTweet.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panelTweet.setBackground(Color.WHITE);
            panelTweet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            // Panel superior con foto de perfil, nombre y alias
            JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelUsuario.setBackground(Color.WHITE);

            // Mostrar foto de perfil
            if (fotoPerfilBlob != null) {
                try {
                    byte[] fotoBytes = fotoPerfilBlob.getBytes(1, (int) fotoPerfilBlob.length());
                    ImageIcon icon = new ImageIcon(fotoBytes);
                    Image imagenEscalada = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    JLabel labelFoto = new JLabel(new ImageIcon(imagenEscalada));
                    panelUsuario.add(labelFoto);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

           // Añadir nombre, alias y fecha
            JLabel labelUsuario = new JLabel("<html><b>" + nombreUsuario + "</b> @" + alias + " 🕒 " + fecha + "</html>");
            panelUsuario.add(labelUsuario);

            JLabel lblNombreUsuario = new JLabel(nombreUsuario + " @" + alias);
            lblNombreUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Agregar MouseListener para abrir el perfil del usuario
            lblNombreUsuario.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Obtener el usuarioId del Tweet correspondiente al JLabel clickeado
                    for (Tweet tweet : tweets) {
                        if (tweet.nombreUsuario.equals(nombreUsuario) && tweet.alias.equals(alias)) {
                            abrirPerfilUsuario(tweet.usuarioId);
                            return; // Importante: salir del bucle después de encontrar el Tweet
                        }
                    }
                }
            });
            panelUsuario.add(lblNombreUsuario);
            panelTweet.add(panelUsuario, BorderLayout.NORTH);

            // Contenido del tweet
            JTextArea textoTweet = new JTextArea(contenido);
            textoTweet.setEditable(false);
            textoTweet.setLineWrap(true);
            textoTweet.setWrapStyleWord(true);
            textoTweet.setBackground(Color.WHITE);
            panelTweet.add(textoTweet, BorderLayout.CENTER);

            // Multimedia del tweet (si existe)
            if (multimedia != null) {
                try {
                    byte[] imageBytes = multimedia.getBytes(1, (int) multimedia.length());
                    ImageIcon icon = new ImageIcon(imageBytes);
                    Image imagenEscalada = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    JLabel imagenLabel = new JLabel(new ImageIcon(imagenEscalada));
                    panelTweet.add(imagenLabel, BorderLayout.WEST);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // Panel de interacciones
            JPanel panelInteracciones = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // ❤️ Like
            final JButton[] btnLike = new JButton[1];
            final boolean[] yaDioLike = { !usuarioPuedeDarLike(idTweet) };
            btnLike[0] = new JButton((yaDioLike[0] ? "💔 Quitar " : "❤️ ") + obtenerTotalLikes(idTweet));
            btnLike[0].addActionListener(e -> {
                if (yaDioLike[0]) {
                    quitarLike(idTweet);
                    yaDioLike[0] = false;
                } else {
                    darLike(idTweet);
                    yaDioLike[0] = true;
                }
                btnLike[0].setText((yaDioLike[0] ? "💔 Quitar " : "❤️ ") + obtenerTotalLikes(idTweet));
            });
            panelInteracciones.add(btnLike[0]);

            // 🔁 Retweet
            final JButton[] btnRT = new JButton[1];
            final boolean[] yaRT = { !usuarioPuedeRetweetear(idTweet) };
            btnRT[0] = new JButton((yaRT[0] ? "❌ Quitar RT " : "🔁 ") + obtenerTotalRetweets(idTweet));
            btnRT[0].addActionListener(e -> {
                if (yaRT[0]) {
                    quitarRetweet(idTweet);
                    yaRT[0] = false;
                } else {
                    retweetear(idTweet);
                    yaRT[0] = true;
                }
                btnRT[0].setText((yaRT[0] ? "❌ Quitar RT " : "🔁 ") + obtenerTotalRetweets(idTweet));
            });
            panelInteracciones.add(btnRT[0]);

            // 💬 Comentarios
            int totalComentarios = obtenerTotalComentarios(idTweet);
            JButton btnVerComentarios = new JButton("💬 Ver comentarios (" + totalComentarios + ")");
            btnVerComentarios.addActionListener(e -> {
                mostrarComentarios(idTweet, panelContenedorTweets);
            });
            panelInteracciones.add(btnVerComentarios);

            JButton btnComentar = new JButton("💬 Comentar");
            btnComentar.addActionListener(e -> {
                abrirVentanaComentario(idTweet);
            });
            panelInteracciones.add(btnComentar);

            // ✏️ Editar y 🗑 Eliminar si es mío
            if (esMio) {
                // ✏️ Editar tweet
                JButton btnEditar = new JButton("✏️ Editar");
                btnEditar.addActionListener(e -> {
                    // Obtener la multimedia actual
                    byte[] multimediaBytesActual = null;
                    if(multimedia != null){
                        try{
                            multimediaBytesActual = multimedia.getBytes(1,(int)multimedia.length());
                        }catch(SQLException ex){
                            ex.printStackTrace();
                        }
                    }

                    // Crear componentes para la edición
                    JTextField nuevoContenidoField = new JTextField(contenido);
                    JButton btnSeleccionarMultimedia = new JButton("📷 Seleccionar Imagen/GIF");
                    JLabel lblMultimediaSeleccionada = new JLabel();
                    JButton btnEliminarMultimedia = new JButton("🗑️ Quitar Multimedia");

                    final byte[][] nuevaMultimediaBytes = {multimediaBytesActual};

                    // 🔹 Cargar y mostrar imagen actual si existe
                    if (multimediaBytesActual != null) {
                        ImageIcon icono = new ImageIcon(multimediaBytesActual);
                        Image imagen = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Ajuste fijo 100x100
                        lblMultimediaSeleccionada.setIcon(new ImageIcon(imagen));
                    } else {
                        lblMultimediaSeleccionada.setIcon(null); // Si no existe, no muestra nada
                    }

                    // Crear los paneles para la edición
                    JPanel panelEdicion = new JPanel(new BorderLayout());
                    JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
                    JPanel panelInferior = new JPanel(new FlowLayout());

                    panelSuperior.add(nuevoContenidoField);
                    panelSuperior.add(lblMultimediaSeleccionada);

                    panelInferior.add(btnSeleccionarMultimedia);
                    panelInferior.add(btnEliminarMultimedia);

                    panelEdicion.add(panelSuperior, BorderLayout.CENTER);
                    panelEdicion.add(panelInferior, BorderLayout.SOUTH);

                    // Crear el JDialog para la ventana de edición
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Editar Tweet");
                    dialog.setModal(true);
                    dialog.setSize(900, 800); // Ajustado el tamaño de la ventana
                    dialog.setLocationRelativeTo(null); // Centrar en la pantalla
                    dialog.setLayout(new BorderLayout());
                    dialog.add(panelEdicion, BorderLayout.CENTER);

                    // Acción para seleccionar una nueva multimedia
                    btnSeleccionarMultimedia.addActionListener(ev -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes/GIF", "jpg", "png", "gif"));
                        int resultado = fileChooser.showOpenDialog(null);
                        if (resultado == JFileChooser.APPROVE_OPTION) {
                            File archivoSeleccionado = fileChooser.getSelectedFile();
                            try(FileInputStream fis = new FileInputStream(archivoSeleccionado)) {
                                nuevaMultimediaBytes[0] = fis.readAllBytes();
                                ImageIcon iconoNuevo = new ImageIcon(nuevaMultimediaBytes[0]);
                                Image imagenNueva = iconoNuevo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Tamaño 100x100
                                lblMultimediaSeleccionada.setIcon(new ImageIcon(imagenNueva));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Acción para eliminar la multimedia
                    btnEliminarMultimedia.addActionListener(ev -> {
                        nuevaMultimediaBytes[0] = null;
                        lblMultimediaSeleccionada.setIcon(null);
                    });

                    // Crear panel de botones para guardar o cancelar
                    JPanel panelBotones = new JPanel(new FlowLayout());
                    JButton btnGuardar = new JButton("Guardar");
                    JButton btnCancelar = new JButton("Cancelar");

                    // Acción para guardar los cambios
                    btnGuardar.addActionListener(ev -> {
                        String nuevoContenido = nuevoContenidoField.getText().trim();
                        if (!nuevoContenido.isEmpty()) {
                            // Guardar el contenido del tweet y la multimedia (convertir la imagen a bytes)
                            editarTweet(idTweet, nuevoContenido, nuevaMultimediaBytes[0]);
                            cargarTweets(); // Ahora se actualiza correctamente
                            dialog.dispose(); // Cerrar la ventana después de guardar
                        }
                    });

                    // Acción para cancelar la edición
                    btnCancelar.addActionListener(ev -> dialog.dispose()); // Cerrar sin guardar

                    panelBotones.add(btnGuardar);
                    panelBotones.add(btnCancelar);

                    dialog.add(panelBotones, BorderLayout.SOUTH);
                    dialog.setVisible(true);
                });

                panelInteracciones.add(btnEditar);

                JButton btnEliminar = new JButton("🗑 Eliminar");
                btnEliminar.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este tweet?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarTweet(idTweet);
                        cargarTweets();
                    }
                });
                panelInteracciones.add(btnEliminar);
            }

            panelTweet.add(panelInteracciones, BorderLayout.SOUTH);
            panelContenedorTweets.add(panelTweet);
            panelContenedorTweets.add(Box.createVerticalStrut(10));
        }

        panelContenedorTweets.revalidate();
        panelContenedorTweets.repaint();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los tweets.");
    }
}


private void abrirPerfilUsuario(int idUsuario) {
            this.dispose();
    perfilusuario perfil = new perfilusuario(idUsuario); // Abre el perfil del usuario con el idUsuario
    perfil.setVisible(true);
}

private String obtenerMultimediaDeTweet(int id_tweet) {
    String rutaMultimedia = null;
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT multimedia FROM tweets WHERE id_tweet = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, id_tweet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            rutaMultimedia = rs.getString("multimedia");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rutaMultimedia; // Retorna null si el tweet no tiene multimedia
}


private boolean usuarioPuedeDarLike(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT 1 FROM likes WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        return !ps.executeQuery().next();
    } catch (SQLException e) { e.printStackTrace(); return false; }
}

private void darLike(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "INSERT INTO likes (tweet_id, usuario_id, fecha_like) VALUES (?, ?, NOW())";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private int obtenerTotalLikes(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT COUNT(*) FROM likes WHERE tweet_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
}

// --- Lo mismo para retweets ---
private boolean usuarioPuedeRetweetear(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT 1 FROM retweets WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        return !ps.executeQuery().next();
    } catch (SQLException e) { e.printStackTrace(); return false; }
}

private void retweetear(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "INSERT INTO retweets (tweet_id, usuario_id, fecha_retweet) VALUES (?, ?, NOW())";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private int obtenerTotalRetweets(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT COUNT(*) FROM retweets WHERE tweet_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
}


private void mostrarComentarios(int idTweet, JPanel panelContenedorTweets) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT c.id_comentario, c.contenido, c.fecha_creacion, " +
                    "u.nombre_usuario, u.alias, c.usuario_id, c.multimedia " +
                    "FROM comentarios c JOIN usuarios u ON c.usuario_id = u.id_usuarios " +
                    "WHERE c.tweet_id = ? ORDER BY c.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ResultSet rs = ps.executeQuery();

        JPanel panelComentarios = new JPanel();
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));

        while (rs.next()) {
            int idComentario = rs.getInt("id_comentario");
            String nombre = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            byte[] multimedia = rs.getBytes("multimedia");
            int usuarioId = rs.getInt("usuario_id");

            boolean esMio = (usuarioId == UsuarioSesion.getUsuarioId());

            // Panel principal del comentario
            JPanel panelComentario = new JPanel();
            panelComentario.setLayout(new BoxLayout(panelComentario, BoxLayout.Y_AXIS));
            panelComentario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            panelComentario.setBackground(Color.WHITE);
            panelComentario.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));

            // Encabezado del comentario (usuario y fecha)
            JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblUsuario = new JLabel("<html><b>" + alias + "</b> (" + nombre + ")</html>");
            JLabel lblFecha = new JLabel("🕒 " + fecha);
            lblFecha.setForeground(Color.GRAY);
            panelHeader.add(lblUsuario);
            panelHeader.add(Box.createHorizontalStrut(10));
            panelHeader.add(lblFecha);
            panelHeader.setBackground(Color.WHITE);
            panelComentario.add(panelHeader);

            // Contenido del comentario
            JTextArea textoComentario = new JTextArea(contenido);
            textoComentario.setEditable(false);
            textoComentario.setLineWrap(true);
            textoComentario.setWrapStyleWord(true);
            textoComentario.setBackground(Color.WHITE);
            textoComentario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelComentario.add(textoComentario);

            // Multimedia (si existe)
            if (multimedia != null && multimedia.length > 0) {
                try {
                    ImageIcon icon = new ImageIcon(multimedia);
                    if (icon.getIconWidth() > 0) { // Verificar que es una imagen válida
                        // Escalar manteniendo proporción (máx 300px de ancho)
                        int ancho = Math.min(icon.getIconWidth(), 300);
                        int alto = (int)((double)icon.getIconHeight() / icon.getIconWidth() * ancho);
                        
                        Image imagenEscalada = icon.getImage().getScaledInstance(
                            ancho, alto, Image.SCALE_SMOOTH);
                        JLabel imagenLabel = new JLabel(new ImageIcon(imagenEscalada));
                        imagenLabel.setBorder(BorderFactory.createLineBorder(
                            new Color(240, 240, 240), 1));
                        
                        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT));
                        panelImagen.add(imagenLabel);
                        panelComentario.add(panelImagen);
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen del comentario ID " + 
                                     idComentario + ": " + e.getMessage());
                }
            }

            // Botones de interacción (solo si es comentario del usuario)
            if (esMio) {
                JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                panelBotones.setBackground(Color.WHITE);

                // Botón Editar
                JButton btnEditar = new JButton("✏️ Editar");
                btnEditar.addActionListener(e -> editarComentario(idComentario, contenido));
                panelBotones.add(btnEditar);

                // Botón Eliminar
                JButton btnEliminar = new JButton("🗑 Eliminar");
                btnEliminar.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                        panelComentario, 
                        "¿Eliminar este comentario?", 
                        "Confirmar", 
                        JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarComentario(idComentario);
                        mostrarComentarios(idTweet, panelContenedorTweets); // Refrescar
                    }
                });
                panelBotones.add(btnEliminar);

                panelComentario.add(panelBotones);
            }

            panelComentarios.add(panelComentario);
            panelComentarios.add(Box.createVerticalStrut(15));
        }

        // Crear diálogo para mostrar comentarios
        JDialog dialogComentarios = new JDialog();
        dialogComentarios.setTitle("Comentarios del Tweet");
        dialogComentarios.setSize(600, 500);
        dialogComentarios.setLocationRelativeTo(null);
        dialogComentarios.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JScrollPane scrollPane = new JScrollPane(panelComentarios);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialogComentarios.add(scrollPane);
        
        dialogComentarios.setVisible(true);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(panelContenedorTweets, 
            "Error al cargar los comentarios", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}



// Modifica tu método para agregar comentarios:
private void agregarComentario(String comentarioTexto, int idTweet, File archivoMultimedia) {
   try (Connection conexion = RetornarBaseDedatos.getConnection()) {
    String sql = "INSERT INTO comentarios (tweet_id, usuario_id, contenido, multimedia, fecha_creacion) " +
                "VALUES (?, ?, ?, ?, NOW())";
    
    PreparedStatement ps = conexion.prepareStatement(sql);
    ps.setInt(1, idTweet);
    ps.setInt(2, UsuarioSesion.getUsuarioId());
    ps.setString(3, comentarioTexto);
    
    if (archivoMultimedia != null) {
        try (FileInputStream fis = new FileInputStream(archivoMultimedia)) {
            ps.setBinaryStream(4, fis, (int) archivoMultimedia.length());
        }
    } else {
        ps.setNull(4, java.sql.Types.BLOB);
    }
    
    ps.executeUpdate();
    JOptionPane.showMessageDialog(null, "Comentario publicado");
    
} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Error SQL al publicar el comentario: " + e.getMessage());
    e.printStackTrace();
} catch (IOException e) {
    JOptionPane.showMessageDialog(null, "Error al leer el archivo multimedia: " + e.getMessage());
    e.printStackTrace();
}
}

/**
 * Abre una ventana para editar un comentario existente
 * @param idComentario ID del comentario a editar
 * @param contenidoActual Texto actual del comentario
 */
private void editarComentario(int idComentario, String contenidoActual) {
    // Crear la ventana de edición
    JDialog dialogEditar = new JDialog();
    dialogEditar.setTitle("Editar Comentario");
    dialogEditar.setLayout(new BorderLayout());
    dialogEditar.setSize(450, 400);
    dialogEditar.setLocationRelativeTo(null);
    
    // Panel principal
    JPanel panelPrincipal = new JPanel();
    panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
    panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Área de texto para el comentario
    JTextArea textoComentario = new JTextArea(contenidoActual);
    textoComentario.setLineWrap(true);
    textoComentario.setWrapStyleWord(true);
    JScrollPane scrollTexto = new JScrollPane(textoComentario);
    
    // Panel para la imagen actual (si existe)
    JPanel panelImagenActual = new JPanel();
    panelImagenActual.setLayout(new BoxLayout(panelImagenActual, BoxLayout.Y_AXIS));
    panelImagenActual.setBorder(BorderFactory.createTitledBorder("Imagen actual"));
    
    // Obtener la imagen actual del comentario
    byte[] imagenActual = obtenerMultimediaComentario(idComentario);
    JLabel lblImagenActual = new JLabel();
    
    if (imagenActual != null && imagenActual.length > 0) {
        try {
            ImageIcon icon = new ImageIcon(imagenActual);
            Image imagenEscalada = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblImagenActual.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception e) {
            lblImagenActual.setText("(Error al cargar imagen)");
        }
    } else {
        lblImagenActual.setText("(No hay imagen)");
    }
    
    panelImagenActual.add(lblImagenActual);
    
    // Botón para cambiar imagen
    JButton btnCambiarImagen = new JButton("Cambiar Imagen");
    JLabel lblNuevaImagen = new JLabel("(No se ha seleccionado nueva imagen)");
    final File[] archivoMultimedia = {null};
    
    btnCambiarImagen.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar nueva imagen");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(dialogEditar);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoMultimedia[0] = fileChooser.getSelectedFile();
            lblNuevaImagen.setText(archivoMultimedia[0].getName());
            
            // Mostrar vista previa
            try {
                ImageIcon icon = new ImageIcon(archivoMultimedia[0].getAbsolutePath());
                Image imagenEscalada = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblImagenActual.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception ex) {
                lblImagenActual.setText("(Vista previa no disponible)");
            }
        }
    });
    
    // Panel para botones
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    
    // Botón Guardar
    JButton btnGuardar = new JButton("💾 Guardar Cambios");
    btnGuardar.addActionListener(e -> {
        String nuevoContenido = textoComentario.getText().trim();
        if (nuevoContenido.isEmpty()) {
            JOptionPane.showMessageDialog(dialogEditar, 
                "El comentario no puede estar vacío", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Actualizar el comentario
            if (archivoMultimedia[0] != null) {
                // Si hay nueva imagen, convertir a bytes y actualizar
                byte[] nuevaImagenBytes = Files.readAllBytes(archivoMultimedia[0].toPath());
                actualizarComentarioConImagen(idComentario, nuevoContenido, nuevaImagenBytes);
            } else {
                // Si no hay nueva imagen, mantener la existente o eliminar si se desea
                actualizarComentario(idComentario, nuevoContenido);
            }
            
            JOptionPane.showMessageDialog(dialogEditar, 
                "Comentario actualizado correctamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            dialogEditar.dispose();
            
            // Refrescar la vista de comentarios
            // (Necesitarás tener acceso al idTweet desde algún lado)
            // mostrarComentarios(idTweet, panelContenedorTweets);
            
        } catch (IOException | SQLException ex) {
            JOptionPane.showMessageDialog(dialogEditar, 
                "Error al actualizar el comentario: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    });
    
    // Botón Eliminar Imagen
    JButton btnEliminarImagen = new JButton("❌ Eliminar Imagen");
    btnEliminarImagen.addActionListener(e -> {
        archivoMultimedia[0] = null;
        lblImagenActual.setIcon(null);
        lblImagenActual.setText("(Imagen eliminada - se guardará sin imagen)");
        lblNuevaImagen.setText("(No se ha seleccionado nueva imagen)");
    });
    
    // Botón Cancelar
    JButton btnCancelar = new JButton("Cancelar");
    btnCancelar.addActionListener(e -> dialogEditar.dispose());
    
    // Agregar componentes al panel
    panelPrincipal.add(new JLabel("Editar comentario:"));
    panelPrincipal.add(Box.createVerticalStrut(5));
    panelPrincipal.add(scrollTexto);
    panelPrincipal.add(Box.createVerticalStrut(10));
    panelPrincipal.add(panelImagenActual);
    panelPrincipal.add(Box.createVerticalStrut(5));
    panelPrincipal.add(btnCambiarImagen);
    panelPrincipal.add(lblNuevaImagen);
    panelPrincipal.add(Box.createVerticalStrut(10));
    
    panelBotones.add(btnEliminarImagen);
    panelBotones.add(btnCancelar);
    panelBotones.add(btnGuardar);
    
    dialogEditar.add(panelPrincipal, BorderLayout.CENTER);
    dialogEditar.add(panelBotones, BorderLayout.SOUTH);
    dialogEditar.setVisible(true);
}

/**
 * Actualiza un comentario modificando solo su texto
 */
private void actualizarComentario(int idComentario, String nuevoContenido) throws SQLException {
    try (Connection conn = RetornarBaseDedatos.getConnection()) {
        String sql = "UPDATE comentarios SET contenido = ?, fecha_creacion = NOW() " +
                    "WHERE id_comentario = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nuevoContenido);
        ps.setInt(2, idComentario);
        ps.executeUpdate();
    }
}

/**
 * Actualiza un comentario modificando tanto el texto como la imagen
 */
private void actualizarComentarioConImagen(int idComentario, String nuevoContenido, byte[] imagenBytes) 
    throws SQLException {
    try (Connection conn = RetornarBaseDedatos.getConnection()) {
        String sql = "UPDATE comentarios SET contenido = ?, multimedia = ?, fecha_creacion = NOW() " +
                    "WHERE id_comentario = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nuevoContenido);
        ps.setBytes(2, imagenBytes);
        ps.setInt(3, idComentario);
        ps.executeUpdate();
    }
}

/**
 * Obtiene la imagen asociada a un comentario
 */
private byte[] obtenerMultimediaComentario(int idComentario) {
    try (Connection conn = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT multimedia FROM comentarios WHERE id_comentario = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idComentario);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getBytes("multimedia");
        }
        return null;
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}
private void cargarMultimediaComentario(String rutaArchivo, int idComentario) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        // Leer el archivo seleccionado y convertirlo en un array de bytes
        File archivo = new File(rutaArchivo);
        FileInputStream fis = new FileInputStream(archivo);
        byte[] multimediaBytes = new byte[(int) archivo.length()];
        fis.read(multimediaBytes);

        // Crear la consulta SQL para actualizar el comentario con el archivo multimedia
        String sql = "UPDATE comentarios SET multimedia = ? WHERE id_comentario = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setBytes(1, multimediaBytes); // Establecer el contenido multimedia como un Blob
        ps.setInt(2, idComentario); // Identificar el comentario

        // Ejecutar la actualización en la base de datos
        ps.executeUpdate();
        fis.close();

        JOptionPane.showMessageDialog(this, "Multimedia cargada correctamente");

    } catch (IOException | SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar multimedia.");
    }
}

private byte[] cargarArchivoMultimedia(File archivo) {
    try (FileInputStream fis = new FileInputStream(archivo);
         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        return bos.toByteArray();  // Retorna los bytes del archivo
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al leer el archivo multimedia.");
        return null;
    }
}


public void guardarMultimediaComentario(byte[] multimedia, int idTweet) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "UPDATE comentarios SET multimedia = ? WHERE tweet_id = ?";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setBytes(1, multimedia);  // Cargar el archivo como LONGBLOB
        ps.setInt(2, idTweet);  // Asociarlo al tweet correspondiente

        int filasAfectadas = ps.executeUpdate();
        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(this, "Multimedia subida con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al subir multimedia.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al subir multimedia.");
    }
}

public void eliminarComentario(int idComentario) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "DELETE FROM comentarios WHERE id_comentario = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idComentario);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al eliminar el comentario.");
    }
}




private String encodeToBase64(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
}


private void actualizarNombreYAlias() {
    int idUsuario = UsuarioSesion.getUsuarioId(); // Obtener el ID del usuario en sesión

    if (idUsuario != -1) {
        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
            String sql = "SELECT nombre_usuario, alias FROM usuarios WHERE id_usuarios = ?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre_usuario");
                String alias = rs.getString("alias");
          // Almacenar el usuarioId del perfil mostrado
                    usuarioIdPerfilMostrado = idUsuario;
                    
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
private void cargarTrendingTopics() {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        // Consulta para obtener hashtags populares
        String sql = "SELECT " +
                "SUBSTRING_INDEX(SUBSTRING_INDEX(t.contenido, '#', -1), ' ', 1) AS hashtag, " +
                "COUNT(*) AS cantidad " +
                "FROM tweets t " +
                "WHERE t.contenido LIKE '%#%' " +
                "GROUP BY hashtag " +
                "ORDER BY cantidad DESC " +
                "LIMIT 5";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Limpiar y configurar el panel principal
        panelTrendingTopics.removeAll();
        panelTrendingTopics.setLayout(new BoxLayout(panelTrendingTopics, BoxLayout.Y_AXIS));

        // Panel contenedor para centrar los elementos
        JPanel panelCentrado = new JPanel();
        panelCentrado.setLayout(new BoxLayout(panelCentrado, BoxLayout.Y_AXIS));
        panelCentrado.setBackground(new Color(245, 245, 245)); // Mismo color de fondo

        // Título de la sección (arriba)
        JLabel tituloSeccion = new JLabel("TRENDING TOPICS");
        tituloSeccion.setFont(new Font("Arial", Font.BOLD, 18));
        tituloSeccion.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado horizontal
        panelCentrado.add(tituloSeccion);
        panelCentrado.add(Box.createVerticalStrut(15));

        while (rs.next()) {
            String hashtag = rs.getString("hashtag");
            int cantidad = rs.getInt("cantidad");

            // Panel para cada hashtag (con alineación centrada)
            JPanel panelHashtag = new JPanel();
            panelHashtag.setLayout(new BoxLayout(panelHashtag, BoxLayout.Y_AXIS));
            panelHashtag.setBackground(new Color(230, 230, 230));
            panelHashtag.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado horizontal

            // Label para el hashtag (centrado)
            JLabel lblHashtag = new JLabel("#" + hashtag);
            lblHashtag.setFont(new Font("Arial", Font.BOLD, 16));
            lblHashtag.setForeground(new Color(29, 161, 242));
            lblHashtag.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado

            // Label para la cantidad (centrado)
            JLabel lblCantidad = new JLabel(cantidad + " menciones");
            lblCantidad.setFont(new Font("Arial", Font.PLAIN, 12));
            lblCantidad.setForeground(Color.GRAY);
            lblCantidad.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado

            panelHashtag.add(lblHashtag);
            panelHashtag.add(lblCantidad);

            // Hacer clickable el panel
            panelHashtag.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            panelHashtag.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    buscarTweets("#" + hashtag);
                }
            });

            panelCentrado.add(panelHashtag);
            panelCentrado.add(Box.createVerticalStrut(10));
        }

        // Añadir el panel centrado al panel principal
        panelTrendingTopics.add(Box.createVerticalGlue()); // Espacio arriba
        panelTrendingTopics.add(panelCentrado);
        panelTrendingTopics.add(Box.createVerticalGlue()); // Espacio abajo

        panelTrendingTopics.revalidate();
        panelTrendingTopics.repaint();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar trending topics");
    }
}
private void agregarMenuFotoPerfil() {
    lblFotoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            mostrarMenuContextualFotoPerfil(evt);
        }
    });
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
                Home.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemVerPerfil);

        // Opción "Configuración"
        JMenuItem itemConfiguracion = new JMenuItem("Configuración");
        itemConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirConfiguracion();
                Home.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemConfiguracion);

        // Opción "Cerrar Sesión"
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesion();
                Home.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemCerrarSesion);

        menu.show(evt.getComponent(), evt.getX(), evt.getY());
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
     
      private void aplicarEstiloGlobalBotones() {
        // Color rosa pastel
        Color colorRosaPastel = new Color(255, 204, 204);

        // Borde suave
        Border bordeSuave = new LineBorder(colorRosaPastel.darker(), 1, true); // true para bordes redondeados
        Border margen = new EmptyBorder(5, 10, 5, 10); // Márgenes internos
        CompoundBorder bordeCompuesto = new CompoundBorder(bordeSuave, margen);

        // Estilo global
        UIManager.put("Button.background", colorRosaPastel);
        UIManager.put("Button.border", bordeCompuesto);
        UIManager.put("Button.foreground", new Color(50, 50, 50)); // Color del texto
        UIManager.put("Button.font", new Font("Roboto", Font.PLAIN, 14)); // Fuente del texto

    // Actualizar los botones existentes
    SwingUtilities.updateComponentTreeUI(this);
    revalidate();
    repaint();
    }
      
     
public Home() {
    initComponents();  
          cargarFotoPerfil(); 
   cargarTrendingTopics(); 
        agregarMenuFotoPerfil();
        cargarTweets();
// Estilos para labels
        Font fuenteLabels = new Font("Roboto", Font.PLAIN, 16);
        Color colorTextoLabels = new Color(50, 50, 50);
        VentanaUtilidades.configurarResolucionVentana(this);
        aplicarEstiloGlobalBotones();

     
        lblAliasNombre.setFont(new Font("Roboto", Font.BOLD, 16));
        lblAliasNombre.setForeground(colorTextoLabels);

        // Estilos para la imagen de perfil
        lblFotoPerfil.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Estilos para la imagen previa
        lblImagenPrevia.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        setVisible(true);
}
 private void mostrarImagenPrevia(BufferedImage imagen) {
        // Escalar la imagen al tamaño del JLabel
        Image imagenEscalada = imagen.getScaledInstance(lblImagenPrevia.getWidth(), lblImagenPrevia.getHeight(), Image.SCALE_SMOOTH);

        // Crear un ImageIcon a partir de la imagen escalada
        ImageIcon iconoImagen = new ImageIcon(imagenEscalada);

        // Establecer el ImageIcon en el JLabel
        lblImagenPrevia.setIcon(iconoImagen);
    }

public class VentanaUtilidades {

    public static void configurarResolucionVentana(JFrame ventana) {
        ventana.setPreferredSize(new Dimension(1920, 1080));
        // O
        // ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        PanelTrasero = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        pNotificaciones = new javax.swing.JPanel();
        pExplorar = new javax.swing.JPanel();
        Inicio = new javax.swing.JLabel();
        Explorar = new javax.swing.JLabel();
        Notificaciones = new javax.swing.JLabel();
        LogoTwitter2 = new javax.swing.JLabel();
        PanelBuscador = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        lblFotoPerfil = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        lblAliasNombre = new javax.swing.JLabel();
        ScrollTweet = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        b_enviarTweet = new javax.swing.JButton();
        btnSubirImagen = new javax.swing.JButton();
        lblImagenPrevia = new javax.swing.JLabel();
        btnGif = new javax.swing.JButton();
        btnEmoji = new javax.swing.JButton();
        btnCalendario = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelContenedorTweets = new javax.swing.JPanel();
        panelTrendingTopics = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelTrasero.setBackground(new java.awt.Color(255, 255, 255));

        Menu2.setBackground(new java.awt.Color(246, 234, 250));

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

        javax.swing.GroupLayout pNotificacionesLayout = new javax.swing.GroupLayout(pNotificaciones);
        pNotificaciones.setLayout(pNotificacionesLayout);
        pNotificacionesLayout.setHorizontalGroup(
            pNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );
        pNotificacionesLayout.setVerticalGroup(
            pNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
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
            .addGap(0, 209, Short.MAX_VALUE)
        );
        pExplorarLayout.setVerticalGroup(
            pExplorarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

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

        LogoTwitter2.setFont(new java.awt.Font("Eras Bold ITC", 0, 36)); // NOI18N
        LogoTwitter2.setForeground(new java.awt.Color(102, 0, 153));
        LogoTwitter2.setText("Twitter");
        LogoTwitter2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogoTwitter2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LogoTwitter2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pNotificaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pExplorar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LogoTwitter2))
                    .addComponent(Notificaciones)
                    .addComponent(Explorar)
                    .addComponent(Inicio))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(LogoTwitter2)
                .addGap(93, 93, 93)
                .addComponent(Inicio)
                .addGap(58, 58, 58)
                .addComponent(Explorar)
                .addGap(56, 56, 56)
                .addComponent(Notificaciones)
                .addGap(468, 468, 468)
                .addComponent(pExplorar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelBuscador.setBackground(new java.awt.Color(255, 255, 255));

        txtBuscar.setBackground(new java.awt.Color(246, 234, 250));
        txtBuscar.setText("Buscar");
        txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBuscarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBuscarFocusLost(evt);
            }
        });
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
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
        lblFotoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblFotoPerfilMouseClicked(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/lupa.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        lblAliasNombre.setText("Usuarios");
        lblAliasNombre.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblAliasNombreAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

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

        btnSubirImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/multimedia.png"))); // NOI18N
        btnSubirImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirImagenActionPerformed(evt);
            }
        });

        btnGif.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/gif.png"))); // NOI18N
        btnGif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGifActionPerformed(evt);
            }
        });

        btnEmoji.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/emoji.png"))); // NOI18N
        btnEmoji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmojiActionPerformed(evt);
            }
        });

        btnCalendario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/calendario.png"))); // NOI18N
        btnCalendario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalendarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelBuscadorLayout = new javax.swing.GroupLayout(PanelBuscador);
        PanelBuscador.setLayout(PanelBuscadorLayout);
        PanelBuscadorLayout.setHorizontalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar))
                            .addComponent(ScrollTweet, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAliasNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscadorLayout.createSequentialGroup()
                                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(lblImagenPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSubirImagen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGif)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEmoji)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCalendario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_enviarTweet)
                        .addGap(198, 198, 198))))
        );
        PanelBuscadorLayout.setVerticalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ScrollTweet, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAliasNombre)))
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSubirImagen)
                            .addComponent(btnGif)
                            .addComponent(btnEmoji)
                            .addComponent(btnCalendario))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscadorLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                                .addComponent(lblImagenPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscadorLayout.createSequentialGroup()
                                .addComponent(b_enviarTweet)
                                .addGap(37, 37, 37))))))
        );

        panelContenedorTweets.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedorTweets.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelContenedorTweetsAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout panelContenedorTweetsLayout = new javax.swing.GroupLayout(panelContenedorTweets);
        panelContenedorTweets.setLayout(panelContenedorTweetsLayout);
        panelContenedorTweetsLayout.setHorizontalGroup(
            panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        panelContenedorTweetsLayout.setVerticalGroup(
            panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 745, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(panelContenedorTweets);

        panelTrendingTopics.setBackground(new java.awt.Color(255, 255, 255));
        panelTrendingTopics.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelTrendingTopics.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelTrendingTopicsAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        panelTrendingTopics.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                panelTrendingTopicsCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        javax.swing.GroupLayout panelTrendingTopicsLayout = new javax.swing.GroupLayout(panelTrendingTopics);
        panelTrendingTopics.setLayout(panelTrendingTopicsLayout);
        panelTrendingTopicsLayout.setHorizontalGroup(
            panelTrendingTopicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 188, Short.MAX_VALUE)
        );
        panelTrendingTopicsLayout.setVerticalGroup(
            panelTrendingTopicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelTraseroLayout = new javax.swing.GroupLayout(PanelTrasero);
        PanelTrasero.setLayout(PanelTraseroLayout);
        PanelTraseroLayout.setHorizontalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PanelTraseroLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panelTrendingTopics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelTraseroLayout.setVerticalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTrendingTopics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addComponent(PanelTrasero, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
        actualizarNombreYAlias(); // Llamar al método al cargar la interfaz
    }//GEN-LAST:event_lblAliasNombreAncestorAdded

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded
lblFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
    // Agregar un FocusListener al JTextField
    txtBuscar.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (txtBuscar.getText().equals("Buscar")) {
                txtBuscar.setText(""); // Borra el texto cuando el campo recibe el foco
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (txtBuscar.getText().trim().isEmpty()) {
                txtBuscar.setText("Buscar"); // Vuelve a mostrar "Buscar" si el campo está vacío
            }
        }
    });

    // Agregar un ActionListener para el evento de presionar Enter
    txtBuscar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            String textoBusqueda = txtBuscar.getText();
            if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
                // Si está vacío, no hacer nada y salir del método actionPerformed
                return;
            }
            buscarTweets(textoBusqueda); // Llama al método de búsqueda si el texto no está vacío
        }
    });
    }//GEN-LAST:event_txtBuscarActionPerformed
private void realizarBusqueda() {

   
    buscarTweets(txtBuscar.getText()); // Llama a la función buscarTweets()

}


    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
 String textoBusqueda = txtBuscar.getText();
    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
        // Si está vacío, no hacer nada y salir del método
        return;
    }
    buscarTweets(textoBusqueda);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void panelContenedorTweetsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelContenedorTweetsAncestorAdded

    }//GEN-LAST:event_panelContenedorTweetsAncestorAdded

    private void txtBuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFocusGained
         // Si el texto es "Buscar", lo borra cuando el campo recibe el foco
    if (txtBuscar.getText().equals("Buscar")) {
        txtBuscar.setText(""); 
    }
    }//GEN-LAST:event_txtBuscarFocusGained

    private void txtBuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBuscarFocusLost
       // Si el campo está vacío, vuelve a mostrar "Buscar"
    if (txtBuscar.getText().trim().isEmpty()) {
        txtBuscar.setText("Buscar");
    }
    }//GEN-LAST:event_txtBuscarFocusLost

    private void panelTrendingTopicsCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_panelTrendingTopicsCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_panelTrendingTopicsCaretPositionChanged

    private void panelTrendingTopicsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelTrendingTopicsAncestorAdded
    if (panelTrendingTopics == null) {
        System.out.println("El panelContenedordehashtags es null. Inicializándolo...");
        
        
        
        // Crear un JScrollPane para manejar el desplazamiento
        JScrollPane scrollPane = new JScrollPane(panelTrendingTopics);
        scrollPane.setBounds(10, 10, 500, 300);  // Ajusta las dimensiones según sea necesario
        add(scrollPane); // Agregar el JScrollPane al contenedor principal
        
        // Asegúrate de que la ventana se actualice (esto puede ser necesario si agregas componentes dinámicamente)
        revalidate();
        repaint();
    }
setVisible(true);
    }//GEN-LAST:event_panelTrendingTopicsAncestorAdded

    private void b_enviarTweetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_enviarTweetActionPerformed
        String contenido = jTextArea1.getText().trim();
        int usuarioId = UsuarioSesion.getUsuarioId();

        if (contenido.isEmpty() && archivoImagen == null) {
            JOptionPane.showMessageDialog(this, "No puedes publicar un tweet vacío.");
            return;
        }

        try (Connection conexion = RetornarBaseDedatos.getConnection()) {
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

    private void lblFotoPerfilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblFotoPerfilMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblFotoPerfilMouseClicked

    private void btnGifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGifActionPerformed
         JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes y GIFs", "jpg", "png", "gif"));

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivoImagen = fileChooser.getSelectedFile();

            // Mostrar vista previa de la imagen
            ImageIcon imagen = new ImageIcon(archivoImagen.getAbsolutePath());
            lblImagenPrevia.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }
    }//GEN-LAST:event_btnGifActionPerformed

    private void btnEmojiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmojiActionPerformed
          JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes y GIFs", "jpg", "png", "gif"));

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivoImagen = fileChooser.getSelectedFile();

            // Mostrar vista previa de la imagen
            ImageIcon imagen = new ImageIcon(archivoImagen.getAbsolutePath());
            lblImagenPrevia.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }
    }//GEN-LAST:event_btnEmojiActionPerformed

    private void btnCalendarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalendarioActionPerformed
          JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes y GIFs", "jpg", "png", "gif"));

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivoImagen = fileChooser.getSelectedFile();

            // Mostrar vista previa de la imagen
            ImageIcon imagen = new ImageIcon(archivoImagen.getAbsolutePath());
            lblImagenPrevia.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        }
    }//GEN-LAST:event_btnCalendarioActionPerformed

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

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

    private void pNotificacionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pNotificacionesMouseExited
        pNotificaciones.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pNotificacionesMouseExited

    private void pNotificacionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pNotificacionesMouseEntered
        pNotificaciones.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pNotificacionesMouseEntered

    private void pNotificacionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pNotificacionesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pNotificacionesMouseClicked

    private void pExplorarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pExplorarMouseExited
        pExplorar.setBackground(colorNormalMenu);
    }//GEN-LAST:event_pExplorarMouseExited

    private void pExplorarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pExplorarMouseEntered
        pExplorar.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_pExplorarMouseEntered

    private void pExplorarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pExplorarMouseClicked
        BusquedaTwitter busquedaTwitter = new BusquedaTwitter();
        busquedaTwitter.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_pExplorarMouseClicked
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
    private javax.swing.JLabel Explorar;
    private javax.swing.JLabel Inicio;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JLabel Notificaciones;
    private javax.swing.JPanel PanelBuscador;
    private javax.swing.JPanel PanelTrasero;
    private javax.swing.JScrollPane ScrollTweet;
    private javax.swing.JButton b_enviarTweet;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCalendario;
    private javax.swing.JButton btnEmoji;
    private javax.swing.JButton btnGif;
    private javax.swing.JButton btnSubirImagen;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblImagenPrevia;
    private javax.swing.JPanel pExplorar;
    private javax.swing.JPanel pNotificaciones;
    private javax.swing.JPanel panelContenedorTweets;
    private javax.swing.JPanel panelTrendingTopics;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
