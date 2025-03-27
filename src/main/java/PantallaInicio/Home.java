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


private void abrirVentanaComentario(int idTweet) {
    int usuarioId = UsuarioSesion.getUsuarioId();
System.out.println("🧠 ID del usuario en sesión: " + usuarioId);
    String comentario = JOptionPane.showInputDialog(this, "Escribe tu comentario:");


    if (comentario != null && !comentario.trim().isEmpty()) {
        try (Connection conexion = BasededatosTwitter.getConnection()) {
            String sql = "INSERT INTO comentarios (tweet_id, usuario_id, contenido, fecha_creacion) VALUES (?, ?, ?, NOW())";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTweet);
            ps.setInt(2, UsuarioSesion.getUsuarioId());
            ps.setString(3, comentario);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Comentario publicado.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al comentar.");
        }
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
        // Usamos LIKE para buscar contenido que contenga la palabra clave o hashtag
     String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.id_tweet, t.usuario_id, t.multimedia " +
             "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios " +
             "WHERE t.contenido LIKE ? " +
             "ORDER BY t.fecha_creacion DESC";


        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, "%" + textoBusqueda + "%");  // Agregar % para búsqueda parcial
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

            boolean esMio = (idAutor == UsuarioSesion.getUsuarioId());

            JPanel panelTweet = new JPanel(new BorderLayout());
            panelTweet.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panelTweet.setBackground(Color.WHITE);
            panelTweet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            JTextArea textoTweet = new JTextArea(alias + " (" + nombre + ") 🕒 " + fecha + "\n" + contenido);
            textoTweet.setEditable(false);
            textoTweet.setLineWrap(true);
            textoTweet.setWrapStyleWord(true);
            textoTweet.setBackground(Color.WHITE);
            panelTweet.add(textoTweet, BorderLayout.CENTER);

            // Añadir multimedia si está presente (imagen, video, etc.)
            Blob multimedia = rs.getBlob("multimedia");
            if (multimedia != null) {
                byte[] imageBytes = multimedia.getBytes(1, (int) multimedia.length());
                ImageIcon icon = new ImageIcon(imageBytes);
                Image imagenEscalada = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imagenLabel = new JLabel(new ImageIcon(imagenEscalada));
                panelTweet.add(imagenLabel, BorderLayout.WEST);
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
                    String nuevoContenido = JOptionPane.showInputDialog(this, "Editar tweet:", contenido);
                    if (nuevoContenido != null && !nuevoContenido.trim().isEmpty()) {
                        editarTweet(idTweet, nuevoContenido.trim());
                        cargarTweets(); // Recargar vista después de editar
                    }
                });
                panelInteracciones.add(btnEditar);

                // 🗑 Eliminar tweet
                JButton btnEliminar = new JButton("🗑 Eliminar");
                btnEliminar.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este tweet?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarTweet(idTweet);
                        cargarTweets(); // Recargar después de borrar
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
private void editarTweet(int idTweet, String contenidoNuevo) {
    try (Connection c = BasededatosTwitter.getConnection()) {
        String sql = "UPDATE tweets SET contenido = ? WHERE id_tweet = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, contenidoNuevo);
        ps.setInt(2, idTweet);
        ps.setInt(3, UsuarioSesion.getUsuarioId()); // seguridad: solo el dueño puede editar
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al editar el tweet.");
    }
}


private void cargarTweets() {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.multimedia, t.id_tweet, t.usuario_id " +
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
            int idAutor = rs.getInt("usuario_id"); // ⚠️ Lo nuevo

            boolean esMio = (idAutor == UsuarioSesion.getUsuarioId());

            JPanel panelTweet = new JPanel(new BorderLayout());
            panelTweet.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panelTweet.setBackground(Color.WHITE);
            panelTweet.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            JTextArea textoTweet = new JTextArea(alias + " (" + nombre + ") 🕒 " + fecha + "\n" + contenido);
            textoTweet.setEditable(false);
            textoTweet.setLineWrap(true);
            textoTweet.setWrapStyleWord(true);
            textoTweet.setBackground(Color.WHITE);
            panelTweet.add(textoTweet, BorderLayout.CENTER);

            if (multimedia != null) {
                byte[] imageBytes = multimedia.getBytes(1, (int) multimedia.length());
                ImageIcon icon = new ImageIcon(imageBytes);
                Image imagenEscalada = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imagenLabel = new JLabel(new ImageIcon(imagenEscalada));
                panelTweet.add(imagenLabel, BorderLayout.WEST);
            }

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

            // 🗑 Eliminar si es mío
            if (esMio) {
                // ✏️ Editar tweet
JButton btnEditar = new JButton("✏️ Editar");
btnEditar.addActionListener(e -> {
    String nuevoContenido = JOptionPane.showInputDialog(this, "Editar tweet:", contenido);
    if (nuevoContenido != null && !nuevoContenido.trim().isEmpty()) {
        editarTweet(idTweet, nuevoContenido.trim());
        cargarTweets(); // Recargar vista después de editar
    }
});
panelInteracciones.add(btnEditar);

             
                JButton btnEliminar = new JButton("🗑 Eliminar");
                btnEliminar.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este tweet?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarTweet(idTweet);
                        cargarTweets(); // recargar después de borrar
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
public void editarComentario(int idComentario, String nuevoContenido) {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
        String sql = "UPDATE comentarios SET contenido = ?, fecha_creacion = NOW() WHERE id_comentario = ?";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, nuevoContenido);
        ps.setInt(2, idComentario);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al editar el comentario.");
    }
}

// Método para mostrar los comentarios de un tweet
public void mostrarComentarios(int idTweet, JPanel panelContenedorTweets) {
    try (Connection conexion = BasededatosTwitter.getConnection()) {
        String sql = "SELECT c.id_comentario, c.contenido, c.fecha_creacion, u.nombre_usuario, u.alias, c.usuario_id " +
                     "FROM comentarios c JOIN usuarios u ON c.usuario_id = u.id_usuarios WHERE c.tweet_id = ? " +
                     "ORDER BY c.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ResultSet rs = ps.executeQuery();

        // Crear un panel para los comentarios
        JPanel panelComentarios = new JPanel();
        panelComentarios.setLayout(new BoxLayout(panelComentarios, BoxLayout.Y_AXIS));

        while (rs.next()) {
            int idComentario = rs.getInt("id_comentario");
            String nombre = rs.getString("nombre_usuario");
            String alias = rs.getString("alias");
            String contenido = rs.getString("contenido");
            String fecha = rs.getString("fecha_creacion");
            int usuarioId = rs.getInt("usuario_id");

            boolean esMio = (usuarioId == UsuarioSesion.getUsuarioId()); // Verificar si el comentario es del usuario actual

            // Panel para cada comentario
            JPanel panelComentario = new JPanel(new BorderLayout());
            panelComentario.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panelComentario.setBackground(Color.WHITE);

            JTextArea textoComentario = new JTextArea(alias + " (" + nombre + ") 🕒 " + fecha + "\n" + contenido);
            textoComentario.setEditable(false);
            textoComentario.setLineWrap(true);
            textoComentario.setWrapStyleWord(true);
            textoComentario.setBackground(Color.WHITE);
            panelComentario.add(textoComentario, BorderLayout.CENTER);

            JPanel panelInteraccionesComentario = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // Si el comentario es del usuario, mostrar los botones de editar y eliminar
            if (esMio) {
                // ✏️ Editar comentario
                JButton btnEditarComentario = new JButton("✏️ Editar comentario");
                btnEditarComentario.addActionListener(e -> {
                    String nuevoContenido = JOptionPane.showInputDialog(this, "Editar comentario:", contenido);
                    if (nuevoContenido != null && !nuevoContenido.trim().isEmpty()) {
                        editarComentario(idComentario, nuevoContenido.trim());
                        cargarTweets(); // Recargar tweets después de editar el comentario
                    }
                });
                panelInteraccionesComentario.add(btnEditarComentario);

                // 🗑 Eliminar comentario
                JButton btnEliminarComentario = new JButton("🗑 Eliminar comentario");
                btnEliminarComentario.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar este comentario?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarComentario(idComentario);
                        cargarTweets(); // Recargar tweets después de eliminar el comentario
                    }
                });
                panelInteraccionesComentario.add(btnEliminarComentario);
            }

            panelComentario.add(panelInteraccionesComentario, BorderLayout.SOUTH);
            panelComentarios.add(panelComentario);
            panelComentarios.add(Box.createVerticalStrut(10));
        }

        // Crear el JDialog para mostrar los comentarios
        JDialog dialogComentarios = new JDialog();
        dialogComentarios.setTitle("Comentarios del Tweet");
        dialogComentarios.setSize(600, 400);
        dialogComentarios.setLocationRelativeTo(null); // Centrar el diálogo en la pantalla
        dialogComentarios.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Cerrar el diálogo al hacer clic en la cruz

        // Añadir el panel de comentarios al JDialog
        dialogComentarios.add(new JScrollPane(panelComentarios));

        // Hacer visible el JDialog
        dialogComentarios.setVisible(true);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los comentarios.");
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
        txtBuscar = new javax.swing.JTextField();
        btnBusqueda = new javax.swing.JButton();
        lblFotoPerfil = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
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
        lblAliasNombre = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelContenedorTweets = new javax.swing.JPanel();

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelBuscador.setBackground(new java.awt.Color(255, 255, 255));

        txtBuscar.setBackground(new java.awt.Color(246, 234, 250));
        txtBuscar.setText("Buscar");
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        btnBusqueda.setBackground(new java.awt.Color(246, 234, 250));

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

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelBuscadorLayout = new javax.swing.GroupLayout(PanelBuscador);
        PanelBuscador.setLayout(PanelBuscadorLayout);
        PanelBuscadorLayout.setHorizontalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelBuscadorLayout.setVerticalGroup(
            PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBuscadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelBuscadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBusqueda)
                        .addComponent(btnBuscar))
                    .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        panelContenedorTweets.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelContenedorTweetsLayout = new javax.swing.GroupLayout(panelContenedorTweets);
        panelContenedorTweets.setLayout(panelContenedorTweetsLayout);
        panelContenedorTweetsLayout.setHorizontalGroup(
            panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        panelContenedorTweetsLayout.setVerticalGroup(
            panelContenedorTweetsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(panelContenedorTweets);

        javax.swing.GroupLayout PanelTraseroLayout = new javax.swing.GroupLayout(PanelTrasero);
        PanelTrasero.setLayout(PanelTraseroLayout);
        PanelTraseroLayout.setHorizontalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelTraseroLayout.createSequentialGroup()
                        .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelTraseroLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PanelTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(PanelTraseroLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 128, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelTraseroLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAliasNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelTraseroLayout.setVerticalGroup(
            PanelTraseroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelTraseroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAliasNombre)
                .addGap(24, 24, 24)
                .addComponent(PanelTweet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(74, 74, 74)
                .addComponent(PanelTrasero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
btnBuscar.addActionListener(e -> buscarTweets(txtBuscar.getText()));
    }//GEN-LAST:event_btnBuscarActionPerformed
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
    private javax.swing.JLabel LabelPerfil;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JPanel PanelBuscador;
    private javax.swing.JPanel PanelTrasero;
    private javax.swing.JPanel PanelTweet;
    private javax.swing.JScrollPane ScrollTweet;
    private javax.swing.JButton b_enviarTweet;
    private javax.swing.JButton btnBuscar;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblImagenPrevia;
    private javax.swing.JPanel panelContenedorTweets;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
