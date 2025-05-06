/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Explorar;

import PantallaInicio.Home;
import java.awt.Color;
import ConexionBase.RetornarBaseDedatos;
import UsuarioID.UsuarioSesion;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import UsuarioDatos.UsuarioDAO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.PreparedStatement;
import javax.swing.JScrollPane;
import java.awt.*;
import java.util.ArrayList; // Importa ArrayList

import javax.swing.*;
import java.sql.Blob;
import Notificaciones.notificaciones;
import EditarPerfil.EdiPerfil;
import Perfilusuario.perfilusuario;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import javax.swing.BorderFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import IniciarSesion.Iniciarsesionlogin;
/**
 *
 * @author Jaime Paredes
 */
public class BusquedaTwitter extends javax.swing.JFrame {
private String filtro; 
    private int usuarioIdPerfilMostrado = -1; 

    Color colorNormal = new Color(255, 255, 255);
    Color colorOscuro = new Color(246, 246, 246);
    
    /*Colores de los Hastags*/
    Color colorNomalHastags = new Color(255, 255, 255);
    Color colorOscuroHastags = new Color(246, 246, 246);

    
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
        Image image = imageIcon.getImage().getScaledInstance(
            lblFotoPerfil.getWidth(), lblFotoPerfil.getHeight(), Image.SCALE_SMOOTH); // Escalar la imagen
        lblFotoPerfil.setIcon(new ImageIcon(image)); // Establecer la imagen en el JLabel
    } else {
        // Si no hay imagen, mostrar un mensaje o imagen por defecto
        lblFotoPerfil.setIcon(null); // O puedes poner una imagen por defecto si lo prefieres
        System.out.println("No se encontró imagen para el usuario.");
    }
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

        ContenedordeExplorer.removeAll();
        ContenedordeExplorer.setLayout(new BoxLayout(ContenedordeExplorer, BoxLayout.Y_AXIS));

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
                mostrarComentarios(idTweet, ContenedordeExplorer);
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
                        cargarTweets(filtro);
                    dialog.dispose(); // Cerrar la ventana después de guardar

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                // Si no se seleccionó nueva multimedia, solo editar el contenido del tweet
                editarTweet(idTweet, nuevoContenido, null);
                        cargarTweets(filtro);
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
                        cargarTweets(filtro);
                    }
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
        JOptionPane.showMessageDialog(this, "Error al buscar tweets.");
    }
}

private ArrayList<Tweet> tweets = new ArrayList<>(); // Lista para guardar los tweets

private void cargarTweets(String filtro) {
    try (Connection conexion = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT u.nombre_usuario, u.alias, t.contenido, t.fecha_creacion, t.multimedia, t.id_tweet, " +
                     "t.usuario_id, u.foto_perfil " +
                     "FROM tweets t JOIN usuarios u ON t.usuario_id = u.id_usuarios ";

        // Aplicar filtros según la categoría seleccionada
        if (filtro != null && !filtro.isEmpty()) {
            if (filtro.equals("Trending")) {
                // Solo tweets que contengan al menos un hashtag
                sql += "WHERE t.contenido LIKE '%#%' ";
            } else if (filtro.equals("News")) {
                sql += "WHERE t.contenido LIKE '%#NEWS%' ";
            } else if (filtro.equals("Sports")) {
                sql += "WHERE t.contenido LIKE '%#SPORTS%' ";
            } else if (filtro.equals("Entretenimiento")) {
                sql += "WHERE t.contenido LIKE '%#ENTRETENIMIENTO%' ";
            } else if (filtro.equals("Para Ti")) {
                // Mostrar tweets que NO contengan #NEWS, #SPORTS ni #ENTRETENIMIENTO (sin importar otros hashtags)
                sql += "WHERE LOWER(t.contenido) NOT LIKE '%#news%' " +
                       "AND LOWER(t.contenido) NOT LIKE '%#sports%' " +
                       "AND LOWER(t.contenido) NOT LIKE '%#entretenimiento%' ";
            }
        }

        sql += "ORDER BY t.fecha_creacion DESC";

        PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ContenedordeExplorer.removeAll();
        ContenedordeExplorer.setLayout(new BoxLayout(ContenedordeExplorer, BoxLayout.Y_AXIS));

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
                mostrarComentarios(idTweet, ContenedordeExplorer);
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
                            cargarTweets(filtro); // Ahora se actualiza correctamente
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
                        cargarTweets(filtro);
                    }
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
        JOptionPane.showMessageDialog(this, "Error al cargar los tweets.");
    }
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

private boolean usuarioPuedeDarLike(int idTweet) {
    try (Connection c = RetornarBaseDedatos.getConnection()) {
        String sql = "SELECT 1 FROM likes WHERE tweet_id = ? AND usuario_id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, idTweet);
        ps.setInt(2, UsuarioSesion.getUsuarioId());
        return !ps.executeQuery().next();
    } catch (SQLException e) { e.printStackTrace(); return false; }
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

private void cargarTrendingTopics() {
    try (Connection conexion = ConexionBase.RetornarBaseDedatos.getConnection()) {
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



private String obtenerMultimediaDeTweet(int id_tweet) {
    String rutaMultimedia = null;
    try (Connection c = ConexionBase.RetornarBaseDedatos.getConnection()) {
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


private int obtenerTotalComentarios(int idTweet) {
    try (Connection c = ConexionBase.RetornarBaseDedatos.getConnection()) {
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
                BusquedaTwitter.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemVerPerfil);

        // Opción "Configuración"
        JMenuItem itemConfiguracion = new JMenuItem("Configuración");
        itemConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirConfiguracion();
                BusquedaTwitter.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemConfiguracion);

        // Opción "Cerrar Sesión"
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesion();
                BusquedaTwitter.this.dispose(); // Cerrar la ventana principal
            }
        });
        menu.add(itemCerrarSesion);

        menu.show(evt.getComponent(), evt.getX(), evt.getY());
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
 private void abrirPerfilUsuario(int idUsuario) {
            this.dispose();
    perfilusuario perfil = new perfilusuario(idUsuario); // Abre el perfil del usuario con el idUsuario
    perfil.setVisible(true);
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
    /**
     * Creates new form perfilVisual
     */
public BusquedaTwitter() {
    initComponents();  // Método generado por NetBeans GUI Builder
      cargarFotoPerfil(); // Llamamos al método para cargar la imagen de perfil
       cargarTweets("Para Ti"); // Carga inicial

    cargarTrendingTopics(); 
        agregarMenuFotoPerfil();



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

        PanelPrincipal = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        btnInicio2 = new javax.swing.JButton();
        btnExprorar2 = new javax.swing.JButton();
        btnNotificaciones2 = new javax.swing.JButton();
        PanelBuscador = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
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
        lblFotoPerfil = new javax.swing.JLabel();
        lblAliasNombre = new javax.swing.JLabel();
        panelTrendingTopics = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        PanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        Menu2.setBackground(new java.awt.Color(246, 234, 250));

        LogoTwitter2.setFont(new java.awt.Font("Eras Bold ITC", 0, 36)); // NOI18N
        LogoTwitter2.setForeground(new java.awt.Color(102, 0, 153));
        LogoTwitter2.setText("Twitter");
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
        btnInicio2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnInicio2MouseClicked(evt);
            }
        });
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
        btnExprorar2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExprorar2MouseClicked(evt);
            }
        });
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
        btnNotificaciones2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNotificaciones2MouseClicked(evt);
            }
        });
        btnNotificaciones2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificaciones2ActionPerformed(evt);
            }
        });

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
                .addGap(56, 56, 56)
                .addComponent(btnExprorar2)
                .addGap(55, 55, 55)
                .addComponent(btnNotificaciones2)
                .addContainerGap(469, Short.MAX_VALUE))
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

        btnBusqueda.setBackground(new java.awt.Color(246, 234, 250));
        btnBusqueda.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/ImgHome/lupa.png"))); // NOI18N
        btnBusqueda.setBorder(null);
        btnBusqueda.setContentAreaFilled(false);
        btnBusqueda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusquedaActionPerformed(evt);
            }
        });

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
        btnEntretenimiento.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                btnEntretenimientoAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnEntretenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntretenimientoActionPerformed(evt);
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
                        .addComponent(txtBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBusqueda))
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
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGap(0, 567, Short.MAX_VALUE)
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
                .addContainerGap(54, Short.MAX_VALUE))
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

        javax.swing.GroupLayout panelTrendingTopicsLayout = new javax.swing.GroupLayout(panelTrendingTopics);
        panelTrendingTopics.setLayout(panelTrendingTopicsLayout);
        panelTrendingTopicsLayout.setHorizontalGroup(
            panelTrendingTopicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 167, Short.MAX_VALUE)
        );
        panelTrendingTopicsLayout.setVerticalGroup(
            panelTrendingTopicsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
        PanelPrincipal.setLayout(PanelPrincipalLayout);
        PanelPrincipalLayout.setHorizontalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ScrollBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAliasNombre)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelTrendingTopics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelBuscador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblAliasNombre)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(panelTrendingTopics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            realizarBusqueda(); // Llama al método de búsqueda separado
        }
    });
    }//GEN-LAST:event_txtBuscarActionPerformed

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
     lblFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void lblAliasNombreAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblAliasNombreAncestorAdded
           actualizarNombreYAlias(); // Llamar al método al cargar la interfaz

    }//GEN-LAST:event_lblAliasNombreAncestorAdded

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

    private void btnNotificaciones2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNotificaciones2MouseClicked
       notificaciones h = new notificaciones();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnNotificaciones2MouseClicked

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

    private void btnInicio2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInicio2MouseClicked
     Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnInicio2MouseClicked

    private void btnExprorar2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExprorar2MouseClicked
        BusquedaTwitter h = new BusquedaTwitter();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnExprorar2MouseClicked

    private void btnEntretenimientoAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnEntretenimientoAncestorMoved
        cargarTweets("Entretenimiento");
       // Crear el panel del tweet
                JPanel panelTweet = new JPanel();
                panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 300));
    }//GEN-LAST:event_btnEntretenimientoAncestorMoved
private void realizarBusqueda() {

   
    buscarTweets(txtBuscar.getText()); // Llama a la función buscarTweets()

}


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

    private void btnBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusquedaActionPerformed
      buscarTweets(txtBuscar.getText());

    }//GEN-LAST:event_btnBusquedaActionPerformed

    private void btnEntretenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntretenimientoActionPerformed
       cargarTweets("Entretenimiento");
    // Crear el panel del tweet
                JPanel panelTweet = new JPanel();
                panelTweet.setLayout(new BoxLayout(panelTweet, BoxLayout.Y_AXIS));
                panelTweet.setMaximumSize(new Dimension(ContenedordeExplorer.getWidth(), 300));
    }//GEN-LAST:event_btnEntretenimientoActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    cargarTweets("Para Ti");
  

    }//GEN-LAST:event_formWindowOpened

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
    private javax.swing.JPanel ContenedordeExplorer;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JPanel NoticiasDelDia;
    private javax.swing.JPanel PanelBuscador;
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JPanel PanelPrincipalScroll;
    private javax.swing.JScrollPane ScrollBusqueda;
    private javax.swing.JButton btnBusqueda;
    private javax.swing.JButton btnEntretenimiento;
    private javax.swing.JButton btnExprorar2;
    private javax.swing.JButton btnInicio2;
    private javax.swing.JButton btnNews;
    private javax.swing.JButton btnNotificaciones2;
    private javax.swing.JButton btnParaTi;
    private javax.swing.JButton btnSports;
    private javax.swing.JButton btnTrending;
    private javax.swing.JLabel lblAliasNombre;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JPanel panelTrendingTopics;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables

    private static class RunnableImpl implements Runnable {

        public RunnableImpl() {
        }

        public void run() {
            new BusquedaTwitter().setVisible(true);
        }
    }
}








































































































































//MIGUEL ROBERTO GUZMAN ALATORRE