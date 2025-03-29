/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Explorar;
import PantallaInicio.*;
import java.awt.Insets;  // Este es el import necesario para setMargin()
import TweetVisual.tweets;
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; // Para el cursor de mano
/**
 *
 * @author Jaime Paredes
 */
public class BusquedaTwitter extends javax.swing.JFrame {
/*Jaime*/
    Color colorNormalMenu = new Color(246,234,250);
    Color colorOscuroMenu = new Color(246, 246, 246);

private javax.swing.JTextArea txtTweet;
private File archivoImagen; 


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
    JPanel panelComentario = new JPanel();
    panelComentario.setLayout(new BoxLayout(panelComentario, BoxLayout.Y_AXIS));

    // Campo de texto para el comentario
    JTextArea textoComentario = new JTextArea(5, 30);
    textoComentario.setWrapStyleWord(true);
    textoComentario.setLineWrap(true);
    panelComentario.add(new JScrollPane(textoComentario));

    // Botón para seleccionar un archivo multimedia
    JButton btnSeleccionarMultimedia = new JButton("Seleccionar Multimedia");
    panelComentario.add(btnSeleccionarMultimedia);

    // Usar un contenedor mutable para almacenar el archivo multimedia
    final File[] archivoMultimedia = new File[1];  // Usamos un array para hacerlo "final"

    // Acción del botón para seleccionar el archivo
    btnSeleccionarMultimedia.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            archivoMultimedia[0] = fileChooser.getSelectedFile();
        }
    });

    // Botón para comentar
    JButton btnComentar = new JButton("💬 Comentar");
    btnComentar.addActionListener(e -> {
        String comentarioTexto = textoComentario.getText().trim();
        if (!comentarioTexto.isEmpty()) {
            byte[] multimediaBytes = null;
            if (archivoMultimedia[0] != null) {
                multimediaBytes = cargarArchivoMultimedia(archivoMultimedia[0]); // Convertir archivo en bytes
            }
            agregarComentario(comentarioTexto, idTweet, multimediaBytes); // Guardar comentario y multimedia
        } else {
            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío.");
        }
    });

    panelComentario.add(btnComentar);

    // Mostrar el diálogo
    JDialog ventanaComentario = new JDialog();
    ventanaComentario.setTitle("Comentar Tweet");
    ventanaComentario.setSize(400, 300);
    ventanaComentario.setLocationRelativeTo(null);
    ventanaComentario.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    ventanaComentario.add(panelComentario);
    ventanaComentario.setVisible(true);
}



public class CircularPanel extends JPanel {
    private Image image;

    public void setImage(byte[] imageData) {
        if (imageData != null) {
            this.image = new ImageIcon(imageData).getImage();
            repaint(); // Redibuja el panel
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Dibuja un círculo recortado
            int diameter = Math.min(getWidth(), getHeight());
            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter, diameter);
            g2d.setClip(circle);
            g2d.drawImage(image, 0, 0, diameter, diameter, this);
        }
    }
}

private void agregarComentario(String contenido, int idTweet, byte[] multimedia) {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
            String nombre = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            int idTweet = rs.getInt("id_tweet");
            int idAutor = rs.getInt("usuario_id");
            Blob multimedia = rs.getBlob("multimedia");
            Blob fotoPerfilBlob = rs.getBlob("foto_perfil");  // Nuevo: obtener foto de perfil

            boolean esMio = (idAutor == UsuarioSesion.getUsuarioId());

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
            JLabel labelUsuario = new JLabel("<html><b>" + nombre + "</b> @" + alias + " 🕒 " + fecha + "</html>");
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
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
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




private void cargarTweets() {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.multimedia, t.id_tweet, " +
             "t.usuario_id, u.foto_perfil " +  // Añadido este campo
             "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
             "ORDER BY t.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        panelContenedorTweets.removeAll();
        panelContenedorTweets.setLayout(new BoxLayout(panelContenedorTweets, BoxLayout.Y_AXIS));

        while (rs.next()) {
            String nombre = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            Blob multimedia = rs.getBlob("multimedia");
            int idTweet = rs.getInt("id_tweet");
            int idAutor = rs.getInt("usuario_id");
            Blob fotoPerfilBlob = rs.getBlob("foto_perfil");

            boolean esMio = (idAutor == UsuarioSesion.getUsuarioId());

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
            JLabel labelUsuario = new JLabel("<html><b>" + nombre + "</b> @" + alias + " 🕒 " + fecha + "</html>");
            panelUsuario.add(labelUsuario);
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
                JButton btnEditar = new JButton("✏️ Editar");
                btnEditar.addActionListener(e -> {
                    // Crear ventana de edición
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Editar Tweet");
                    dialog.setModal(true);
                    dialog.setSize(400, 300);
                    dialog.setLayout(new BorderLayout());

                    JPanel panelEdicion = new JPanel(new BorderLayout());
                    JTextField txtContenido = new JTextField(contenido);
                    panelEdicion.add(txtContenido, BorderLayout.CENTER);

                    JPanel panelBotones = new JPanel();
                    JButton btnGuardar = new JButton("Guardar");
                    JButton btnCancelar = new JButton("Cancelar");

                    btnGuardar.addActionListener(ev -> {
                        String nuevoContenido = txtContenido.getText().trim();
                        if (!nuevoContenido.isEmpty()) {
                            try {
                                byte[] multimediaBytes = null;
                                if (multimedia != null) {
                                    multimediaBytes = multimedia.getBytes(1, (int) multimedia.length());
                                }
                                editarTweet(idTweet, nuevoContenido, multimediaBytes);
                                cargarTweets();
                                dialog.dispose();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    btnCancelar.addActionListener(ev -> dialog.dispose());

                    panelBotones.add(btnGuardar);
                    panelBotones.add(btnCancelar);
                    panelEdicion.add(panelBotones, BorderLayout.SOUTH);
                    dialog.add(panelEdicion);
                    dialog.setLocationRelativeTo(null);
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




private String obtenerMultimediaDeTweet(int id_tweet) {
    String rutaMultimedia = null;
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
        String sql = "SELECT 1 FROM likes WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        return !ps.executeQuery().next();
    } catch (SQLException e) { e.printStackTrace(); return false; }
}

private void darLike(int idTweet) {
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
        String sql = "SELECT 1 FROM retweets WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        return !ps.executeQuery().next();
    } catch (SQLException e) { e.printStackTrace(); return false; }
}

private void retweetear(int idTweet) {
    try (Connection c = BasededatosTwitter.getConnection()) {
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
    try (Connection c = BasededatosTwitter.getConnection()) {
        String sql = "SELECT COUNT(*) FROM retweets WHERE tweet_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
}


private void mostrarComentarios(int idTweet, JPanel panelContenedorTweets) {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
   try (Connection conexion = BasededatosTwitter.getConnection()) {
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
    try (Connection conn = BasededatosTwitter.getConnection()) {
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
    try (Connection conn = BasededatosTwitter.getConnection()) {
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
    try (Connection conn = BasededatosTwitter.getConnection()) {
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
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
private void cargarTrendingTopics() {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
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
        
        // Título de la sección (centrado)
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
            panelHashtag.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
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
    /**
     * Creates new form perfilVisual
     */
public BusquedaTwitter() {
    initComponents();  // Método generado por NetBeans GUI Builder
          cargarFotoPerfil(); // Llamamos al método para cargar la imagen de perfil
   cargarTrendingTopics(); 

        // 🔹 Cargar los tweets al iniciar
        cargarTweets();
        setLayout(new BorderLayout());
        panelTrendingTopics = new JPanel();
        panelTrendingTopics.setLayout(new BoxLayout(panelTrendingTopics, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelTrendingTopics);
        add(scrollPane, BorderLayout.CENTER);
// Si tu contenedor es un JFrame
JFrame frame = new JFrame();
frame.setLayout(new BorderLayout());
frame.add(scrollPane, BorderLayout.CENTER);
frame.setSize(600, 400); // Ajusta las dimensiones según sea necesario
frame.setVisible(true);

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

        LabelImagenSerie = new javax.swing.JLabel();
        LabelImagenPeliculas = new javax.swing.JLabel();
        LabelInfoPosts = new javax.swing.JLabel();
        btnInfoPeliculas = new javax.swing.JButton();
        LabelInfoPosts1 = new javax.swing.JLabel();
        btnInfoSerie = new javax.swing.JButton();
        TituloNoticias = new javax.swing.JLabel();
        btnInfoDeportes = new javax.swing.JButton();
        InfoDeportes = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        LabelImagenSerie2 = new javax.swing.JLabel();
        LabelInfoPosts3 = new javax.swing.JLabel();
        btnInfoSerie2 = new javax.swing.JButton();
        TituloNoticias2 = new javax.swing.JLabel();
        btnInfoPeliculas1 = new javax.swing.JButton();
        LabelInfoPosts4 = new javax.swing.JLabel();
        LabelImagenPeliculas1 = new javax.swing.JLabel();
        PanelTrasero = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        btnInicio2 = new javax.swing.JButton();
        btnExprorar2 = new javax.swing.JButton();
        btnNotificaciones2 = new javax.swing.JButton();
        btnPerfil2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        PanelBuscador = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        lblFotoPerfil = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        lblAliasNombre = new javax.swing.JLabel();
        lblImagenPrevia = new javax.swing.JLabel();
        btnForYou = new javax.swing.JButton();
        btnTrending = new javax.swing.JButton();
        btnNews = new javax.swing.JButton();
        btnSports = new javax.swing.JButton();
        btnEntretenimiento = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelContenedorTweets = new javax.swing.JPanel();
        LabelImagenSerie1 = new javax.swing.JLabel();
        LabelInfoPosts2 = new javax.swing.JLabel();
        btnInfoSerie1 = new javax.swing.JButton();
        TituloNoticias1 = new javax.swing.JLabel();
        LabelImagenPeliculas2 = new javax.swing.JLabel();
        LabelInfoPosts5 = new javax.swing.JLabel();
        btnInfoPeliculas2 = new javax.swing.JButton();
        panelTrendingTopics = new javax.swing.JPanel();

        LabelImagenSerie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/ImgSerie.jpg"))); // NOI18N

        LabelImagenPeliculas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/ImgNetflix.png"))); // NOI18N

        LabelInfoPosts.setText("Hace 18 horas - Nuevos - 8K posts");

        btnInfoPeliculas.setText("Las Mejores Peliculas De Netflix 2025");
        btnInfoPeliculas.setBorder(null);
        btnInfoPeliculas.setContentAreaFilled(false);
        btnInfoPeliculas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        LabelInfoPosts1.setText("Hace 15 horas - Nuevos - 16K posts");

        btnInfoSerie.setText("Las Series Mas Populares Del Momento");
        btnInfoSerie.setBorder(null);
        btnInfoSerie.setContentAreaFilled(false);
        btnInfoSerie.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        TituloNoticias.setText("Noticias del Dia");

        btnInfoDeportes.setText("Deportes - Chihuahua");
        btnInfoDeportes.setBorder(null);
        btnInfoDeportes.setContentAreaFilled(false);
        btnInfoDeportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInfoDeportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoDeportesActionPerformed(evt);
            }
        });

        InfoDeportes.setText("Hace 11 horas - Nuevos - 1K posts");

        jLabel1.setText("Noticias - Mexico");
        jLabel1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel1AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        LabelImagenSerie2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/ImgSerie.jpg"))); // NOI18N

        LabelInfoPosts3.setText("Hace 15 horas - Nuevos - 16K posts");

        btnInfoSerie2.setText("Las Series Mas Populares Del Momento");
        btnInfoSerie2.setBorder(null);
        btnInfoSerie2.setContentAreaFilled(false);
        btnInfoSerie2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        TituloNoticias2.setText("Noticias del Dia");

        btnInfoPeliculas1.setText("Las Mejores Peliculas De Netflix 2025");
        btnInfoPeliculas1.setBorder(null);
        btnInfoPeliculas1.setContentAreaFilled(false);
        btnInfoPeliculas1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        LabelInfoPosts4.setText("Hace 18 horas - Nuevos - 8K posts");

        LabelImagenPeliculas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/ImgNetflix.png"))); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelTrasero.setBackground(new java.awt.Color(255, 255, 255));

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

        btnInicio2.setBackground(new java.awt.Color(246, 234, 250));
        btnInicio2.setFont(new java.awt.Font("Eras Bold ITC", 0, 18)); // NOI18N
        btnInicio2.setForeground(new java.awt.Color(102, 0, 153));
        btnInicio2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/brujula.png"))); // NOI18N
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
        btnExprorar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/lupa.png"))); // NOI18N
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
        btnNotificaciones2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/Notificaciones.png"))); // NOI18N
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
        btnPerfil2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/perfil.png"))); // NOI18N
        btnPerfil2.setText("Perfil");
        btnPerfil2.setBorder(null);
        btnPerfil2.setContentAreaFilled(false);
        btnPerfil2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jPanel1.setBackground(new java.awt.Color(246, 234, 250));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 202, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNotificaciones2)
                    .addComponent(btnInicio2)
                    .addComponent(btnExprorar2)
                    .addComponent(btnPerfil2)
                    .addComponent(LogoTwitter2))
                .addGap(9, 9, 9))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                .addGap(96, 96, 96)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        btnForYou.setText("Para ti");
        btnForYou.setBorder(null);
        btnForYou.setContentAreaFilled(false);
        btnForYou.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnForYou.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForYouActionPerformed(evt);
            }
        });

        btnTrending.setText("Trending");
        btnTrending.setBorder(null);
        btnTrending.setContentAreaFilled(false);
        btnTrending.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnNews.setText("News");
        btnNews.setBorder(null);
        btnNews.setContentAreaFilled(false);
        btnNews.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnSports.setText("Sports");
        btnSports.setBorder(null);
        btnSports.setContentAreaFilled(false);
        btnSports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnEntretenimiento.setText("Entretenimiento");
        btnEntretenimiento.setBorder(null);
        btnEntretenimiento.setContentAreaFilled(false);
        btnEntretenimiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout PanelBuscadorLayout = new javax.swing.GroupLayout(PanelBuscador);
        PanelBuscador.setLayout(PanelBuscadorLayout);
        PanelBuscadorLayout.setHorizontalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar))
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(btnForYou)
                        .addGap(18, 18, 18)
                        .addComponent(btnTrending)
                        .addGap(28, 28, 28)
                        .addComponent(btnNews)
                        .addGap(34, 34, 34)
                        .addComponent(btnSports)
                        .addGap(37, 37, 37)
                        .addComponent(btnEntretenimiento)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAliasNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)))
                .addContainerGap())
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblImagenPrevia, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelBuscadorLayout.setVerticalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAliasNombre)
                        .addGap(16, 16, 16))
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnForYou)
                            .addComponent(btnTrending)
                            .addComponent(btnNews)
                            .addComponent(btnSports)
                            .addComponent(btnEntretenimiento))
                        .addGap(28, 28, 28)))
                .addComponent(lblImagenPrevia)
                .addContainerGap(72, Short.MAX_VALUE))
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

        LabelImagenSerie1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/ImgSerie.jpg"))); // NOI18N

        LabelInfoPosts2.setText("Hace 15 horas - Nuevos - 16K posts");

        btnInfoSerie1.setText("Las Series Mas Populares Del Momento");
        btnInfoSerie1.setBorder(null);
        btnInfoSerie1.setContentAreaFilled(false);
        btnInfoSerie1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        TituloNoticias1.setText("Noticias del Dia");

        LabelImagenPeliculas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/ImgNetflix.png"))); // NOI18N

        LabelInfoPosts5.setText("Hace 18 horas - Nuevos - 8K posts");

        btnInfoPeliculas2.setText("Las Mejores Peliculas De Netflix 2025");
        btnInfoPeliculas2.setBorder(null);
        btnInfoPeliculas2.setContentAreaFilled(false);
        btnInfoPeliculas2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout panelContenedorTweetsLayout = new javax.swing.GroupLayout(panelContenedorTweets);
        panelContenedorTweets.setLayout(panelContenedorTweetsLayout);
        panelContenedorTweetsLayout.setHorizontalGroup(
            panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContenedorTweetsLayout.createSequentialGroup()
                .addGroup(panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                        .addGroup(panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelInfoPosts5)
                            .addComponent(btnInfoPeliculas2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LabelImagenPeliculas2))
                    .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                        .addGroup(panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TituloNoticias1)
                                    .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(LabelInfoPosts2))))
                            .addComponent(btnInfoSerie1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                        .addComponent(LabelImagenSerie1)))
                .addGap(107, 107, 107))
        );
        panelContenedorTweetsLayout.setVerticalGroup(
            panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                .addGroup(panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(LabelImagenSerie1))
                    .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(TituloNoticias1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelInfoPosts2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInfoSerie1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContenedorTweetsLayout.createSequentialGroup()
                        .addComponent(btnInfoPeliculas2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LabelInfoPosts5))
                    .addComponent(LabelImagenPeliculas2))
                .addContainerGap(180, Short.MAX_VALUE))
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
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelTraseroLayout = new javax.swing.GroupLayout(PanelTrasero);
        PanelTrasero.setLayout(PanelTraseroLayout);
        PanelTraseroLayout.setHorizontalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelTraseroLayout.createSequentialGroup()
                        .addComponent(PanelBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(PanelTraseroLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(panelTrendingTopics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );
        PanelTraseroLayout.setVerticalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addComponent(panelTrendingTopics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelTrasero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelTrasero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
   txtBuscar.setText("Buscar"); // Establece el texto predeterminado

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
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnNotificaciones2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificaciones2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNotificaciones2ActionPerformed

    private void btnExprorar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExprorar2ActionPerformed
        BusquedaTwitter b = new BusquedaTwitter();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnExprorar2ActionPerformed

    private void btnInicio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicio2ActionPerformed
        BusquedaTwitter h = new BusquedaTwitter();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInicio2ActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        btnBuscar.addActionListener(e -> buscarTweets(txtBuscar.getText()));
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void panelContenedorTweetsAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelContenedorTweetsAncestorAdded

    }//GEN-LAST:event_panelContenedorTweetsAncestorAdded

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        BusquedaTwitter h = new BusquedaTwitter();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        jPanel1.setBackground(colorOscuroMenu);
    }//GEN-LAST:event_jPanel1MouseEntered

    private void jPanel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseExited
        jPanel1.setBackground(colorNormalMenu);
    }//GEN-LAST:event_jPanel1MouseExited

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        BusquedaTwitter h = new BusquedaTwitter();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jPanel1MouseClicked

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

    private void btnForYouActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForYouActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnForYouActionPerformed

    private void btnInfoDeportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoDeportesActionPerformed

    }//GEN-LAST:event_btnInfoDeportesActionPerformed

    private void jLabel1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel1AncestorAdded

    }//GEN-LAST:event_jLabel1AncestorAdded
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
            java.util.logging.Logger.getLogger(BusquedaTwitter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BusquedaTwitter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BusquedaTwitter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BusquedaTwitter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        SwingUtilities.invokeLater(() -> new BusquedaTwitter().setVisible(true));

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BusquedaTwitter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel InfoDeportes;
    private javax.swing.JLabel LabelImagenPeliculas;
    private javax.swing.JLabel LabelImagenPeliculas1;
    private javax.swing.JLabel LabelImagenPeliculas2;
    private javax.swing.JLabel LabelImagenSerie;
    private javax.swing.JLabel LabelImagenSerie1;
    private javax.swing.JLabel LabelImagenSerie2;
    private javax.swing.JLabel LabelInfoPosts;
    private javax.swing.JLabel LabelInfoPosts1;
    private javax.swing.JLabel LabelInfoPosts2;
    private javax.swing.JLabel LabelInfoPosts3;
    private javax.swing.JLabel LabelInfoPosts4;
    private javax.swing.JLabel LabelInfoPosts5;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JPanel PanelBuscador;
    private javax.swing.JPanel PanelTrasero;
    private javax.swing.JLabel TituloNoticias;
    private javax.swing.JLabel TituloNoticias1;
    private javax.swing.JLabel TituloNoticias2;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEntretenimiento;
    private javax.swing.JButton btnExprorar2;
    private javax.swing.JButton btnForYou;
    private javax.swing.JButton btnInfoDeportes;
    private javax.swing.JButton btnInfoPeliculas;
    private javax.swing.JButton btnInfoPeliculas1;
    private javax.swing.JButton btnInfoPeliculas2;
    private javax.swing.JButton btnInfoSerie;
    private javax.swing.JButton btnInfoSerie1;
    private javax.swing.JButton btnInfoSerie2;
    private javax.swing.JButton btnInicio2;
    private javax.swing.JButton btnNews;
    private javax.swing.JButton btnNotificaciones2;
    private javax.swing.JButton btnPerfil2;
    private javax.swing.JButton btnSports;
    private javax.swing.JButton btnTrending;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblImagenPrevia;
    private javax.swing.JPanel panelContenedorTweets;
    private javax.swing.JPanel panelTrendingTopics;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
