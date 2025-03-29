/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Explorar;

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


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; 
/**
 *
 * @author Jaime Paredes
 */
public class BusquedaTwitter extends javax.swing.JFrame {

    Color colorNormal = new Color(255, 255, 255);
    Color colorOscuro = new Color(246, 246, 246);
    
    /*Colores de los Hastags*/
    Color colorNomalHastags = new Color(255, 255, 255);
    Color colorOscuroHastags = new Color(246, 246, 246);
    // Este método carga la imagen automáticamente
    // Este método carga la imagen automáticamente
private void cargarFotoPerfil() {
    int idUsuario = UsuarioSesion.getUsuarioId();  // Obtener el ID del usuario actual
    UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instancia del DAO
    
    byte[] imgBytes = usuarioDAO.getFotoPerfil(idUsuario); // Obtener la imagen desde la base de datos

    if (imgBytes != null && imgBytes.length > 0) {
        // Si la imagen está presente, crear una ImageIcon
        ImageIcon imageIcon = new ImageIcon(imgBytes);
        Image image = imageIcon.getImage().getScaledInstance(
            lblFotoPerfil1.getWidth(), lblFotoPerfil1.getHeight(), Image.SCALE_SMOOTH); // Escalar la imagen
        lblFotoPerfil1.setIcon(new ImageIcon(image)); // Establecer la imagen en el JLabel
    } else {
        // Si no hay imagen, mostrar un mensaje o imagen por defecto
        lblFotoPerfil1.setIcon(null); // O puedes poner una imagen por defecto si lo prefieres
        System.out.println("No se encontró imagen para el usuario.");
    }
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
public void cargarTweets(String filtro) {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.id_tweet, " +
                     "t.usuario_id, t.multimedia, u.foto_perfil " +
                     "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios ";

       
  // Aplicar filtros según la categoría seleccionada
        if (filtro.equals("Trending")) {
            sql += "WHERE t.contenido NOT LIKE '%#NEWS%' AND t.contenido NOT LIKE '%#ENTRETENIMIENTO%' " +
                   "AND t.contenido NOT LIKE '%#SPORTS%' AND t.contenido NOT LIKE '%#NEWS%' ";
        } else if (filtro.equals("News")) {
            sql += "WHERE t.contenido LIKE '%#NEWS%' ";
        } else if (filtro.equals("Sports")) {
            sql += "WHERE t.contenido LIKE '%#SPORTS%' ";
        } else if (filtro.equals("Entretenimiento")) {
            sql += "WHERE t.contenido LIKE '%#ENTRETENIMIENTO%' ";
        } else if (filtro.equals("Para Ti")) {
            // Filtro para "Para Ti" (combinación de #NEWS y #SPORTS)
            sql += "WHERE t.contenido LIKE '%#NEWS%' OR t.contenido LIKE '%#SPORTS%' ";
        }

        sql += "ORDER BY t.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ContenedordeExplorer.removeAll();
        ContenedordeExplorer.setLayout(new BoxLayout(ContenedordeExplorer, BoxLayout.Y_AXIS));

        while (rs.next()) {
            String nombre = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            int idTweet = rs.getInt("id_tweet");
            int idAutor = rs.getInt("usuario_id");
            Blob multimedia = rs.getBlob("multimedia");
            Blob fotoPerfilBlob = rs.getBlob("foto_perfil");

            boolean esMio = (idAutor == UsuarioSesion.getUsuarioId());

           JPanel panelTweet = new JPanel();
                    panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                    panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 150));
                    
            // 📌 Panel superior con foto de perfil, nombre y alias
            JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelUsuario.setBackground(Color.WHITE);

            // ✅ Foto de perfil
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

            
            JLabel labelUsuario = new JLabel("<html><b>" + nombre + "</b> @" + alias + " 🕒 " + fecha + "</html>");
            panelUsuario.add(labelUsuario);
            panelTweet.add(panelUsuario, BorderLayout.NORTH);

            // 📌 Contenido del tweet
            JTextArea textoTweet = new JTextArea(contenido);
            textoTweet.setEditable(false);
            textoTweet.setLineWrap(true);
            textoTweet.setWrapStyleWord(true);
            textoTweet.setBackground(Color.WHITE);
            panelTweet.add(textoTweet, BorderLayout.CENTER);

            // 📌 Multimedia
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

            // 📌 Panel de interacciones
            JPanel panelInteracciones = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // ❤️ Like
            JButton btnLike = new JButton("❤️ " + obtenerTotalLikes(idTweet));
            btnLike.addActionListener(e -> {
                if (usuarioPuedeDarLike(idTweet)) {
                    darLike(idTweet);
                } else {
                    quitarLike(idTweet);
                }
                btnLike.setText("❤️ " + obtenerTotalLikes(idTweet));
            });
            panelInteracciones.add(btnLike);

            // 🔁 Retweet
            JButton btnRT = new JButton("🔁 " + obtenerTotalRetweets(idTweet));
            btnRT.addActionListener(e -> {
                if (usuarioPuedeRetweetear(idTweet)) {
                    retweetear(idTweet);
                } else {
                    quitarRetweet(idTweet);
                }
                btnRT.setText("🔁 " + obtenerTotalRetweets(idTweet));
            });
            panelInteracciones.add(btnRT);

            // 💬 Ver comentarios
            JButton btnVerComentarios = new JButton("💬 Ver comentarios");
            btnVerComentarios.addActionListener(e -> {
                mostrarComentarios(idTweet, ContenedordeExplorer);
            });
            panelInteracciones.add(btnVerComentarios);

            // ✏️ Comentar
            JButton btnComentar = new JButton("💬 Comentar");
            btnComentar.addActionListener(e -> {
                abrirVentanaComentario(idTweet);
            });
            panelInteracciones.add(btnComentar);

            // 🗑 Editar y eliminar si es del usuario actual
            if (esMio) {
                JButton btnEditar = new JButton("✏️ Editar");
                btnEditar.addActionListener(e -> {
                    editarTweet(idTweet, contenido, null);
                });
                panelInteracciones.add(btnEditar);

                JButton btnEliminar = new JButton("🗑 Eliminar");
                btnEliminar.addActionListener(e -> {
                    eliminarTweet(idTweet);
                    cargarTweets(filtro); // ✅ Volver a cargar los tweets con el mismo filtro
                });
                panelInteracciones.add(btnEliminar);
            }

            panelTweet.add(panelInteracciones, BorderLayout.SOUTH);
            ContenedordeExplorer.add(panelTweet);
            ContenedordeExplorer.add(Box.createVerticalStrut(10));
        }

        ContenedordeExplorer.revalidate();
        ContenedordeExplorer.repaint();

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar tweets.");
    }
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

private boolean usuarioPuedeDarLike(int idTweet) {
    try (Connection c = BasededatosTwitter.getConnection()) {
        String sql = "SELECT 1 FROM likes WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        return !ps.executeQuery().next();
    } catch (SQLException e) { e.printStackTrace(); return false; }
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

public void cargarTodosLosTweets() {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitterdb", "root", "");
        Statement stmt = con.createStatement();
        String consulta = "SELECT t.*, u.nombre_usuario, u.alias, u.foto_perfil FROM tweets t INNER JOIN usuarios u ON t.usuario_id = u.id ORDER BY t.fecha_creacion DESC";
        ResultSet rs = stmt.executeQuery(consulta);

        ContenedordeExplorer.removeAll(); // Limpiar el panel antes de cargar nuevos tweets
        ContenedordeExplorer.revalidate();
        ContenedordeExplorer.repaint();

        while (rs.next()) {
            // Se crea un JPanel para cada tweet y se agrega a ContenedordeExplorer
            JPanel panelTweet = new JPanel();
            panelTweet.setLayout(new BorderLayout());
            panelTweet.setPreferredSize(new Dimension(ContenedordeExplorer.getWidth(), 150));
            panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 150));

            // Obtener datos del tweet
            String nombreUsuario = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            String multimedia = rs.getString("multimedia");
            String fotoPerfil = rs.getString("foto_perfil");

            // Panel para la información del usuario
            JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lblFotoPerfil = new JLabel(new ImageIcon(fotoPerfil)); // Imagen del usuario
            JLabel lblNombreUsuario = new JLabel(nombreUsuario + " @" + alias);

            panelUsuario.add(lblFotoPerfil);
            panelUsuario.add(lblNombreUsuario);

            // Contenido del tweet
            JTextArea txtContenido = new JTextArea(contenido);
            txtContenido.setLineWrap(true);
            txtContenido.setWrapStyleWord(true);
            txtContenido.setEditable(false);

            // Agregar imagen si tiene multimedia
            JLabel lblMultimedia = new JLabel();
            if (multimedia != null && !multimedia.isEmpty()) {
                lblMultimedia.setIcon(new ImageIcon(multimedia));
            }

            // Agregar componentes al panel del tweet
            panelTweet.add(panelUsuario, BorderLayout.NORTH);
            panelTweet.add(txtContenido, BorderLayout.CENTER);
            if (multimedia != null && !multimedia.isEmpty()) {
                panelTweet.add(lblMultimedia, BorderLayout.SOUTH);
            }

            ContenedordeExplorer.add(panelTweet);
            ContenedordeExplorer.add(Box.createVerticalStrut(10));
        }

        ContenedordeExplorer.revalidate();
        ContenedordeExplorer.repaint();
        rs.close();
        stmt.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    /**
     * Creates new form perfilVisual
     */
public BusquedaTwitter() {
    initComponents();  // Método generado por NetBeans GUI Builder
      cargarFotoPerfil(); // Llamamos al método para cargar la imagen de perfil
    cargarTweets("Para Ti");



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

        lblFotoPerfil = new javax.swing.JLabel();
        PanelPrincipal = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        btnInicio2 = new javax.swing.JButton();
        btnExprorar2 = new javax.swing.JButton();
        btnNotificaciones2 = new javax.swing.JButton();
        btnPerfil2 = new javax.swing.JButton();
        PanelBuscador = new javax.swing.JPanel();
        Buscador = new javax.swing.JTextField();
        btnBusqueda = new javax.swing.JButton();
        btnParaTi = new javax.swing.JButton();
        btnTrending = new javax.swing.JButton();
        btnNews = new javax.swing.JButton();
        btnSports = new javax.swing.JButton();
        btnEntretenimiento = new javax.swing.JButton();
        ScrollBusqueda = new javax.swing.JScrollPane();
        PanelPrincipalScroll = new javax.swing.JPanel();
        NoticiasDelDia = new javax.swing.JPanel();
        ContenedordeExplorer = new javax.swing.JPanel();
        lblFotoPerfil1 = new javax.swing.JLabel();
        lblAliasNombre = new javax.swing.JLabel();
        hashtags = new javax.swing.JPanel();
        ScrollHashtags = new javax.swing.JScrollPane();
        PanelPrincipalHashtags = new javax.swing.JPanel();
        Pr1Hashtags = new javax.swing.JPanel();
        Pr2Hashtags = new javax.swing.JPanel();
        Pr3Hashtags = new javax.swing.JPanel();
        Pr4Hashtags = new javax.swing.JPanel();

        lblFotoPerfil.setText("Foto de Perfil");
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        Menu2.setBackground(new java.awt.Color(246, 234, 250));

        LogoTwitter2.setFont(new java.awt.Font("Eras Bold ITC", 0, 36)); // NOI18N
        LogoTwitter2.setForeground(new java.awt.Color(102, 0, 153));
        LogoTwitter2.setText("Twitter");

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

        javax.swing.GroupLayout Menu2Layout = new javax.swing.GroupLayout(Menu2);
        Menu2.setLayout(Menu2Layout);
        Menu2Layout.setHorizontalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu2Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
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
                .addContainerGap(432, Short.MAX_VALUE))
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
        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/lupa.png"))); // NOI18N
        btnBusqueda.setBorder(null);
        btnBusqueda.setContentAreaFilled(false);
        btnBusqueda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnParaTi.setText("Para ti");
        btnParaTi.setBorder(null);
        btnParaTi.setContentAreaFilled(false);
        btnParaTi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnParaTi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParaTiActionPerformed(evt);
            }
        });

        btnTrending.setText("Trending");
        btnTrending.setBorder(null);
        btnTrending.setContentAreaFilled(false);
        btnTrending.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTrending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrendingActionPerformed(evt);
            }
        });

        btnNews.setText("News");
        btnNews.setBorder(null);
        btnNews.setContentAreaFilled(false);
        btnNews.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewsActionPerformed(evt);
            }
        });

        btnSports.setText("Sports");
        btnSports.setBorder(null);
        btnSports.setContentAreaFilled(false);
        btnSports.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSportsActionPerformed(evt);
            }
        });

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
                        .addComponent(Buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBusqueda)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PanelBuscadorLayout.createSequentialGroup()
                        .addComponent(btnParaTi)
                        .addGap(49, 49, 49)
                        .addComponent(btnTrending)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                        .addComponent(btnNews)
                        .addGap(75, 75, 75)
                        .addComponent(btnSports)
                        .addGap(71, 71, 71)
                        .addComponent(btnEntretenimiento)))
                .addContainerGap())
        );
        PanelBuscadorLayout.setVerticalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Buscador, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBusqueda))
                .addGap(18, 18, 18)
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnParaTi)
                        .addComponent(btnEntretenimiento))
                    .addComponent(btnSports)
                    .addComponent(btnNews)
                    .addComponent(btnTrending))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        NoticiasDelDia.setBackground(new java.awt.Color(255, 255, 255));

        ContenedordeExplorer.setBackground(new java.awt.Color(255, 255, 255));
        ContenedordeExplorer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ContenedordeExplorer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ContenedordeExplorerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ContenedordeExplorerMouseExited(evt);
            }
        });

        javax.swing.GroupLayout ContenedordeExplorerLayout = new javax.swing.GroupLayout(ContenedordeExplorer);
        ContenedordeExplorer.setLayout(ContenedordeExplorerLayout);
        ContenedordeExplorerLayout.setHorizontalGroup(
            ContenedordeExplorerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 343, Short.MAX_VALUE)
        );
        ContenedordeExplorerLayout.setVerticalGroup(
            ContenedordeExplorerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout NoticiasDelDiaLayout = new javax.swing.GroupLayout(NoticiasDelDia);
        NoticiasDelDia.setLayout(NoticiasDelDiaLayout);
        NoticiasDelDiaLayout.setHorizontalGroup(
            NoticiasDelDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NoticiasDelDiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ContenedordeExplorer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(278, Short.MAX_VALUE))
        );
        NoticiasDelDiaLayout.setVerticalGroup(
            NoticiasDelDiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NoticiasDelDiaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ContenedordeExplorer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelPrincipalScrollLayout = new javax.swing.GroupLayout(PanelPrincipalScroll);
        PanelPrincipalScroll.setLayout(PanelPrincipalScrollLayout);
        PanelPrincipalScrollLayout.setHorizontalGroup(
            PanelPrincipalScrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalScrollLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NoticiasDelDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        PanelPrincipalScrollLayout.setVerticalGroup(
            PanelPrincipalScrollLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalScrollLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NoticiasDelDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(839, Short.MAX_VALUE))
        );

        ScrollBusqueda.setViewportView(PanelPrincipalScroll);

        lblFotoPerfil1.setText("Foto de Perfil");
        lblFotoPerfil1.setPreferredSize(new java.awt.Dimension(150, 150));
        lblFotoPerfil1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblFotoPerfil1AncestorAdded(evt);
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

        hashtags.setBackground(new java.awt.Color(255, 255, 255));
        hashtags.setForeground(new java.awt.Color(255, 255, 255));

        ScrollHashtags.setBackground(new java.awt.Color(255, 255, 255));
        ScrollHashtags.setForeground(new java.awt.Color(255, 255, 255));

        PanelPrincipalHashtags.setBackground(new java.awt.Color(255, 255, 255));
        PanelPrincipalHashtags.setForeground(new java.awt.Color(255, 255, 255));

        Pr1Hashtags.setBackground(new java.awt.Color(255, 255, 255));
        Pr1Hashtags.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Pr1Hashtags.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Pr1HashtagsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Pr1HashtagsMouseExited(evt);
            }
        });

        Pr2Hashtags.setBackground(new java.awt.Color(255, 255, 255));
        Pr2Hashtags.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Pr2Hashtags.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Pr2HashtagsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Pr2HashtagsMouseExited(evt);
            }
        });

        javax.swing.GroupLayout Pr2HashtagsLayout = new javax.swing.GroupLayout(Pr2Hashtags);
        Pr2Hashtags.setLayout(Pr2HashtagsLayout);
        Pr2HashtagsLayout.setHorizontalGroup(
            Pr2HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
        );
        Pr2HashtagsLayout.setVerticalGroup(
            Pr2HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Pr1HashtagsLayout = new javax.swing.GroupLayout(Pr1Hashtags);
        Pr1Hashtags.setLayout(Pr1HashtagsLayout);
        Pr1HashtagsLayout.setHorizontalGroup(
            Pr1HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pr1HashtagsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pr2Hashtags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        Pr1HashtagsLayout.setVerticalGroup(
            Pr1HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Pr1HashtagsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Pr2Hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Pr3Hashtags.setBackground(new java.awt.Color(255, 255, 255));
        Pr3Hashtags.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Pr3Hashtags.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Pr3HashtagsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Pr3HashtagsMouseExited(evt);
            }
        });

        javax.swing.GroupLayout Pr3HashtagsLayout = new javax.swing.GroupLayout(Pr3Hashtags);
        Pr3Hashtags.setLayout(Pr3HashtagsLayout);
        Pr3HashtagsLayout.setHorizontalGroup(
            Pr3HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );
        Pr3HashtagsLayout.setVerticalGroup(
            Pr3HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        Pr4Hashtags.setBackground(new java.awt.Color(255, 255, 255));
        Pr4Hashtags.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Pr4Hashtags.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Pr4HashtagsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Pr4HashtagsMouseExited(evt);
            }
        });

        javax.swing.GroupLayout Pr4HashtagsLayout = new javax.swing.GroupLayout(Pr4Hashtags);
        Pr4Hashtags.setLayout(Pr4HashtagsLayout);
        Pr4HashtagsLayout.setHorizontalGroup(
            Pr4HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 263, Short.MAX_VALUE)
        );
        Pr4HashtagsLayout.setVerticalGroup(
            Pr4HashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelPrincipalHashtagsLayout = new javax.swing.GroupLayout(PanelPrincipalHashtags);
        PanelPrincipalHashtags.setLayout(PanelPrincipalHashtagsLayout);
        PanelPrincipalHashtagsLayout.setHorizontalGroup(
            PanelPrincipalHashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalHashtagsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPrincipalHashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pr1Hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pr3Hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
            .addComponent(Pr4Hashtags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        PanelPrincipalHashtagsLayout.setVerticalGroup(
            PanelPrincipalHashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalHashtagsLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(Pr1Hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(Pr3Hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pr4Hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
        );

        ScrollHashtags.setViewportView(PanelPrincipalHashtags);

        javax.swing.GroupLayout hashtagsLayout = new javax.swing.GroupLayout(hashtags);
        hashtags.setLayout(hashtagsLayout);
        hashtagsLayout.setHorizontalGroup(
            hashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hashtagsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollHashtags, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );
        hashtagsLayout.setVerticalGroup(
            hashtagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollHashtags, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
        PanelPrincipal.setLayout(PanelPrincipalLayout);
        PanelPrincipalLayout.setHorizontalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ScrollBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(PanelBuscador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lblFotoPerfil1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblAliasNombre))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPrincipalLayout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addComponent(lblFotoPerfil1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAliasNombre)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addComponent(hashtags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInicio2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicio2ActionPerformed
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInicio2ActionPerformed

    private void btnExprorar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExprorar2ActionPerformed
        BusquedaTwitter b = new BusquedaTwitter();
        b.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnExprorar2ActionPerformed

    private void btnNotificaciones2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificaciones2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNotificaciones2ActionPerformed

    private void BuscadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscadorActionPerformed

    private void btnParaTiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParaTiActionPerformed
    cargarTweets("Para Ti");

// Crear el panel del tweet
                JPanel panelTweet = new JPanel();
                panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 300));
    }//GEN-LAST:event_btnParaTiActionPerformed

    private void ContenedordeExplorerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ContenedordeExplorerMouseEntered
        ContenedordeExplorer.setBackground(colorOscuro);
    }//GEN-LAST:event_ContenedordeExplorerMouseEntered

    private void ContenedordeExplorerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ContenedordeExplorerMouseExited
        ContenedordeExplorer.setBackground(colorNormal);
    }//GEN-LAST:event_ContenedordeExplorerMouseExited

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded

    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void lblFotoPerfil1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfil1AncestorAdded
     lblFotoPerfil1.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
lblFotoPerfil1.setHorizontalAlignment(SwingConstants.CENTER);
    }//GEN-LAST:event_lblFotoPerfil1AncestorAdded

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
           actualizarNombreYAlias(); // Llamar al método al cargar la interfaz

    }//GEN-LAST:event_lblAliasNombreAncestorAdded

    private void Pr1HashtagsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr1HashtagsMouseEntered
        Pr1Hashtags.setBackground(colorOscuroHastags);
    }//GEN-LAST:event_Pr1HashtagsMouseEntered

    private void Pr1HashtagsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr1HashtagsMouseExited
        Pr1Hashtags.setBackground(colorNomalHastags);
    }//GEN-LAST:event_Pr1HashtagsMouseExited

    private void Pr2HashtagsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr2HashtagsMouseEntered
        Pr2Hashtags.setBackground(colorOscuroHastags);
    }//GEN-LAST:event_Pr2HashtagsMouseEntered

    private void Pr3HashtagsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr3HashtagsMouseEntered
        Pr3Hashtags.setBackground(colorOscuroHastags);
    }//GEN-LAST:event_Pr3HashtagsMouseEntered

    private void Pr4HashtagsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr4HashtagsMouseEntered
        Pr4Hashtags.setBackground(colorOscuroHastags);
    }//GEN-LAST:event_Pr4HashtagsMouseEntered

    private void Pr2HashtagsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr2HashtagsMouseExited
        Pr2Hashtags.setBackground(colorNomalHastags);
    }//GEN-LAST:event_Pr2HashtagsMouseExited

    private void Pr3HashtagsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr3HashtagsMouseExited
        Pr3Hashtags.setBackground(colorNomalHastags);
    }//GEN-LAST:event_Pr3HashtagsMouseExited

    private void Pr4HashtagsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pr4HashtagsMouseExited
        Pr4Hashtags.setBackground(colorNomalHastags);
    }//GEN-LAST:event_Pr4HashtagsMouseExited

    private void btnTrendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrendingActionPerformed
    cargarTweets("Trending");
       // Crear el panel del tweet
                JPanel panelTweet = new JPanel();
                panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 300));
    }//GEN-LAST:event_btnTrendingActionPerformed

    private void btnNewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewsActionPerformed
    cargarTweets("News");
    // Crear el panel del tweet
                JPanel panelTweet = new JPanel();
                panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 300));
    }//GEN-LAST:event_btnNewsActionPerformed

    private void btnSportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSportsActionPerformed
    cargarTweets("Sports");
    // Crear el panel del tweet
                JPanel panelTweet = new JPanel();
                panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 300));
    }//GEN-LAST:event_btnSportsActionPerformed

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new RunnableImpl());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Buscador;
    private javax.swing.JPanel ContenedordeExplorer;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JPanel NoticiasDelDia;
    private javax.swing.JPanel PanelBuscador;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelPrincipalHashtags;
    private javax.swing.JPanel PanelPrincipalScroll;
    private javax.swing.JPanel Pr1Hashtags;
    private javax.swing.JPanel Pr2Hashtags;
    private javax.swing.JPanel Pr3Hashtags;
    private javax.swing.JPanel Pr4Hashtags;
    private javax.swing.JScrollPane ScrollBusqueda;
    private javax.swing.JScrollPane ScrollHashtags;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnEntretenimiento;
    private javax.swing.JButton btnExprorar2;
    private javax.swing.JButton btnInicio2;
    private javax.swing.JButton btnNews;
    private javax.swing.JButton btnNotificaciones2;
    private javax.swing.JButton btnParaTi;
    private javax.swing.JButton btnPerfil2;
    private javax.swing.JButton btnSports;
    private javax.swing.JButton btnTrending;
    private javax.swing.JPanel hashtags;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblFotoPerfil1;
    // End of variables declaration//GEN-END:variables

    private static class RunnableImpl implements Runnable {

        public RunnableImpl() {
        }

        public void run() {
            new BusquedaTwitter().setVisible(true);
        }
    }
}
