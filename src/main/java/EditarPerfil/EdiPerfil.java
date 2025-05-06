/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package EditarPerfil;

import ConexionBase.RetornarBaseDedatos;
import UsuarioDatos.UsuarioDAO;
import UsuarioID.UsuarioSesion;
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

import javax.swing.BorderFactory;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.ByteArrayOutputStream; // Importar ByteArrayOutputStream

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
public class EdiPerfil extends javax.swing.JFrame {

    Color colorNormalMenu = new Color(246,234,250);
    Color colorOscuroMenu = new Color(242, 226, 248);
    private int idUsuario;
private BufferedImage fotoPerfil;
private BufferedImage fotoPortada;
    private File archivoFondoPortadaSeleccionada = null; // Variable para almacenar el archivo seleccionado
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

       public EdiPerfil() {
        initComponents();
        this.idUsuario = UsuarioSesion.getUsuarioId();
        cargarDatosUsuario();
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

   private void cargarDatosUsuario() {
        Connection con = RetornarBaseDedatos.getConnection();
        if (con != null) {
            try {
                String sql = "SELECT nombre_usuario, alias, email, password, foto_perfil, fondo_portada FROM usuarios WHERE id_usuarios = ?"; // Incluir fondo_portada
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    txtNombreUsuario.setForeground(Color.GRAY);
                    txtNombreUsuario.setText(rs.getString("nombre_usuario"));

                    txtAlias.setForeground(Color.GRAY);
                    txtAlias.setText(rs.getString("alias"));

                    txtEmail.setForeground(Color.GRAY);
                    txtEmail.setText(rs.getString("email"));

                    txtPasswordAnterior.setForeground(Color.GRAY);
                    txtPasswordAnterior.setText(rs.getString("password"));
                   

                    // Mostrar la foto de perfil
                    byte[] fotoPerfilBytes = rs.getBytes("foto_perfil");
                    if (fotoPerfilBytes != null && fotoPerfilBytes.length > 0) {
                        mostrarFotoEnJLabel(lblFotoPerfil, fotoPerfilBytes);
                    } else {
                        lblFotoPerfil.setIcon(null);
                        lblFotoPerfil.setText("No hay foto de perfil");
                    }

                    // Mostrar el fondo de portada
                    byte[] fondoPortadaBytes = rs.getBytes("fondo_portada");
                    if (fondoPortadaBytes != null && fondoPortadaBytes.length > 0) {
                        mostrarFondoPortadaEnJLabel(lblFondoPortada, fondoPortadaBytes);
                    } else {
                        lblFondoPortada.setIcon(null);
                        lblFondoPortada.setText("No hay fondo de portada");
                    }
                }

                rs.close();
                ps.close();
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar datos del usuario.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
        }
    }

    private void mostrarFotoEnJLabel(JLabel label, byte[] fotoPerfilBytes) {
        ImageIcon imageIcon = new ImageIcon(fotoPerfilBytes);
        Image imagenEscalada = imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(imagenEscalada));
        label.setText("");
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
          
private void actualizarDatosUsuario() {
        String antiguaPassword = new String(txtPasswordAnterior.getPassword());
        String nuevaPassword = new String(txtPasswordNueva.getPassword());
        String confirmarPassword = new String(txtConfirmarPassword.getPassword());

        // Obtener la contraseña antigua de la base de datos
        String contraseñaAntiguaBD = obtenerContraseñaAntigua(idUsuario);

        if (!antiguaPassword.equals(contraseñaAntiguaBD)) {
            JOptionPane.showMessageDialog(this, "La contraseña antigua es incorrecta.");
            return; // Detener la actualización si la contraseña antigua es incorrecta
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas nuevas no coinciden.");
            return; // Detener la actualización si las contraseñas nuevas no coinciden
        }

        System.out.println("ID del usuario a actualizar: " + idUsuario); // Depuración

        Connection con = RetornarBaseDedatos.getConnection();
        if (con != null) {
            FileInputStream fisFondoPortada = null; // Declarar fisFondoPortada aquí
            try {
                String sql = "UPDATE usuarios SET nombre_usuario = ?, alias = ?, email = ?, password = ?, foto_perfil = ?, fondo_portada = ? WHERE id_usuarios = ?"; // Incluir fondo_portada
                PreparedStatement ps = con.prepareStatement(sql);

                // Obtener valores existentes de la base de datos
                String nombreUsuarioExistente = obtenerValorExistente("nombre_usuario", idUsuario);
                String aliasExistente = obtenerValorExistente("alias", idUsuario);
                String emailExistente = obtenerValorExistente("email", idUsuario);
                String passwordExistente = obtenerValorExistente("password", idUsuario);

                // Establecer valores solo si no están vacíos o si se ha seleccionado un archivo
                ps.setString(1, txtNombreUsuario.getText().isEmpty() ? nombreUsuarioExistente : txtNombreUsuario.getText());
                ps.setString(2, txtAlias.getText().isEmpty() ? aliasExistente : txtAlias.getText());
                ps.setString(3, txtEmail.getText().isEmpty() ? emailExistente : txtEmail.getText());
                ps.setString(4, nuevaPassword.isEmpty() ? passwordExistente : nuevaPassword);

               byte[] imagenBytes = null;
                if (fotoPerfil != null) {
                    imagenBytes = convertirImagenABytes(fotoPerfil);
                    ps.setBytes(5, imagenBytes); // Convertir la imagen a bytes
                } else {
                    // Mantener el valor existente si no se selecciona imagen
                    ps.setBytes(5, obtenerBytesExistentes("foto_perfil", idUsuario));
                }

               
                if (archivoFondoPortadaSeleccionada != null) {
                    System.out.println("Ruta del archivo de portada: " + archivoFondoPortadaSeleccionada.getAbsolutePath());
                    System.out.println("Tamaño del archivo de portada: " + archivoFondoPortadaSeleccionada.length());
                    fisFondoPortada = new FileInputStream(archivoFondoPortadaSeleccionada);
                    ps.setBinaryStream(6, fisFondoPortada, (int) archivoFondoPortadaSeleccionada.length());
                } else {
                    // Solo mantener el valor existente si ya hay un valor en la base de datos
                    if (obtenerBytesExistentes("fondo_portada", idUsuario) != null) {
                        ps.setBytes(6, obtenerBytesExistentes("fondo_portada", idUsuario));
                    } else {
                        ps.setNull(6, java.sql.Types.BLOB); // Establecer a NULL si no hay valor existente
                    }
                }
                System.out.println("Consulta SQL: " + ps.toString());
                
                ps.setInt(7, idUsuario);

                int filasAfectadas = ps.executeUpdate();
                System.out.println("Filas afectadas: " + filasAfectadas); // Depuración

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.");
                }

                ps.close();
                con.close();

            } catch (SQLException | FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar datos.");
            } finally {
                try {
                    if (fisFondoPortada != null) {
                        fisFondoPortada.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
        }
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

    
     // Método para obtener los bytes existentes de una imagen en la base de datos
    private byte[] obtenerBytesExistentes(String columna, int idUsuario) {
        Connection con = RetornarBaseDedatos.getConnection();
        byte[] bytesExistentes = null;
        if (con != null) {
            try {
                String sql = "SELECT " + columna + " FROM usuarios WHERE id_usuarios = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    bytesExistentes = rs.getBytes(columna);
                }
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bytesExistentes;
    }

    // Método para obtener la contraseña antigua de la base de datos
private String obtenerContraseñaAntigua(int idUsuario) {
    String contraseñaAntigua = null;
    Connection con = RetornarBaseDedatos.getConnection();
    if (con != null) {
        try {
            String sql = "SELECT password FROM usuarios WHERE id_usuarios = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                contraseñaAntigua = rs.getString("password");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener la contraseña antigua.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
    }
    return contraseñaAntigua;
}
    
  private File seleccionarFoto(JPanel panel, String tipoFoto) { // Usar JPanel en lugar de JLabel
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                mostrarFotoEnJPanel(panel, convertirImagenABytes(image)); // Usar mostrarFotoEnJPanel
                if (tipoFoto.equals("foto de perfil")) {
                    fotoPerfil = image;
                } else {
                    fotoPortada = image;
                }
                return selectedFile; // Devuelve el archivo seleccionado
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen.");
            }
        }
        return null; // Devuelve null si no se selecciona un archivo
    }

   
   
   private void mostrarFoto(JPanel panel, BufferedImage image) {
    // Redimensionar la imagen para ajustarla al JPanel
    Image scaledImage = image.getScaledInstance(panel.getWidth(), panel.getHeight(), Image.SCALE_SMOOTH);

    // Crear un JLabel para mostrar la imagen
    JLabel label = new JLabel(new ImageIcon(scaledImage));

    // Limpiar el JPanel y agregar el JLabel
    panel.removeAll();
    panel.add(label);
    panel.revalidate();
    panel.repaint();
}
   
   
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panelPrincipal = new javax.swing.JPanel();
        Menu2 = new javax.swing.JPanel();
        LogoTwitter2 = new javax.swing.JLabel();
        Explorar = new javax.swing.JLabel();
        Notificaciones = new javax.swing.JLabel();
        Inicio = new javax.swing.JLabel();
        panelDatos = new javax.swing.JPanel();
        Panel1 = new javax.swing.JPanel();
        TextoEP = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        Portada = new javax.swing.JPanel();
        panelFotoPortada = new javax.swing.JPanel();
        lblFondoPortada = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtNombreUsuario = new javax.swing.JTextField();
        txtAlias = new javax.swing.JTextField();
        btnMostrarContraseña = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtPasswordNueva = new javax.swing.JPasswordField();
        txtConfirmarPassword = new javax.swing.JPasswordField();
        txtPasswordAnterior = new javax.swing.JPasswordField();
        btnSeleccionarPortada = new javax.swing.JButton();
        btnSeleccionarPerfil = new javax.swing.JButton();
        lblFotoPerfil = new javax.swing.JLabel();
        panelFotoPerfil = new javax.swing.JPanel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

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
                .addContainerGap()
                .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Menu2Layout.createSequentialGroup()
                        .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Explorar)
                            .addComponent(Inicio))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu2Layout.createSequentialGroup()
                                .addComponent(LogoTwitter2)
                                .addGap(43, 43, 43))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Menu2Layout.createSequentialGroup()
                                .addComponent(Notificaciones)
                                .addContainerGap())))))
        );
        Menu2Layout.setVerticalGroup(
            Menu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Menu2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(LogoTwitter2)
                .addGap(32, 32, 32)
                .addComponent(Inicio)
                .addGap(51, 51, 51)
                .addComponent(Explorar)
                .addGap(59, 59, 59)
                .addComponent(Notificaciones)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelDatos.setBackground(new java.awt.Color(246, 234, 250));

        Panel1.setBackground(new java.awt.Color(246, 234, 250));

        TextoEP.setFont(new java.awt.Font("Arial Black", 0, 24)); // NOI18N
        TextoEP.setForeground(new java.awt.Color(102, 0, 153));
        TextoEP.setText("Editar Perfil");

        btnGuardar.setBackground(new java.awt.Color(246, 234, 250));
        btnGuardar.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(102, 0, 153));
        btnGuardar.setText("Aplicar");
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextoEP)
                .addContainerGap(544, Short.MAX_VALUE))
            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                    .addContainerGap(576, Short.MAX_VALUE)
                    .addComponent(btnGuardar)
                    .addGap(16, 16, 16)))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextoEP)
                .addContainerGap(7, Short.MAX_VALUE))
            .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                    .addContainerGap(10, Short.MAX_VALUE)
                    .addComponent(btnGuardar)
                    .addGap(9, 9, 9)))
        );

        Portada.setBackground(new java.awt.Color(255, 255, 255));

        panelFotoPortada.setBackground(new java.awt.Color(255, 255, 255));
        panelFotoPortada.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                panelFotoPortadaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblFondoPortada.setText("Foto de portada");

        javax.swing.GroupLayout panelFotoPortadaLayout = new javax.swing.GroupLayout(panelFotoPortada);
        panelFotoPortada.setLayout(panelFotoPortadaLayout);
        panelFotoPortadaLayout.setHorizontalGroup(
            panelFotoPortadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFondoPortada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFotoPortadaLayout.setVerticalGroup(
            panelFotoPortadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoPortadaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFondoPortada, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout PortadaLayout = new javax.swing.GroupLayout(Portada);
        Portada.setLayout(PortadaLayout);
        PortadaLayout.setHorizontalGroup(
            PortadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PortadaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFotoPortada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PortadaLayout.setVerticalGroup(
            PortadaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFotoPortada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Nombre de Usuario");

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Nombre Completo");

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Correo");

        txtNombreUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreUsuarioActionPerformed(evt);
            }
        });

        btnMostrarContraseña.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resource/hidepassword10.png"))); // NOI18N
        btnMostrarContraseña.setBorder(null);
        btnMostrarContraseña.setBorderPainted(false);
        btnMostrarContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarContraseñaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("Contraseña anterior");

        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 153, 153));
        jLabel6.setText("Contraseña nueva");

        jLabel7.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 153, 153));
        jLabel7.setText("Confirmar contraseña");

        btnSeleccionarPortada.setText("Subir portada");
        btnSeleccionarPortada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarPortadaActionPerformed(evt);
            }
        });

        btnSeleccionarPerfil.setText("Cambiar foto");
        btnSeleccionarPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarPerfilActionPerformed(evt);
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

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnSeleccionarPortada))
                    .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Portada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelDatosLayout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSeleccionarPerfil))
                            .addGap(18, 18, 18)
                            .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel3)
                                .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)
                                .addComponent(txtAlias, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)
                                .addComponent(txtPasswordNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtConfirmarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelDatosLayout.createSequentialGroup()
                                    .addComponent(txtPasswordAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnMostrarContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 114, Short.MAX_VALUE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(Panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Portada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSeleccionarPortada)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelDatosLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelDatosLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addGap(17, 17, 17))
                    .addComponent(btnSeleccionarPerfil))
                .addGap(18, 18, 18)
                .addComponent(txtAlias, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPasswordAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrarContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(txtPasswordNueva, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtConfirmarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout panelPrincipalLayout = new javax.swing.GroupLayout(panelPrincipal);
        panelPrincipal.setLayout(panelPrincipalLayout);
        panelPrincipalLayout.setHorizontalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addComponent(Menu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPrincipalLayout.setVerticalGroup(
            panelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelFotoPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LogoTwitter2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoTwitter2MouseClicked
        Home h = new Home();
        h.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoTwitter2MouseClicked

    private void txtNombreUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreUsuarioActionPerformed
           txtNombreUsuario.setForeground(Color.BLACK);

    }//GEN-LAST:event_txtNombreUsuarioActionPerformed
private boolean mostrarContraseña = false;
    private void btnMostrarContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarContraseñaActionPerformed
     if (mostrarContraseña) {
        // Ocultar contraseñas
        txtPasswordAnterior.setEchoChar('*');
        txtPasswordNueva.setEchoChar('*');
        txtConfirmarPassword.setEchoChar('*');
        btnMostrarContraseña.setText("Mostrar Contraseña");
        mostrarContraseña = false;
    } else {
        // Mostrar contraseñas
        txtPasswordAnterior.setEchoChar((char) 0); // (char) 0 para mostrar texto plano
        txtPasswordNueva.setEchoChar((char) 0);
        txtConfirmarPassword.setEchoChar((char) 0);
        btnMostrarContraseña.setText("Ocultar Contraseña");
        mostrarContraseña = true;
    }
    }//GEN-LAST:event_btnMostrarContraseñaActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
            actualizarDatosUsuario();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnSeleccionarPortadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarPortadaActionPerformed
        archivoFondoPortadaSeleccionada = seleccionarFoto(panelFotoPortada, "foto de portada");
        
    }//GEN-LAST:event_btnSeleccionarPortadaActionPerformed

    private void btnSeleccionarPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarPerfilActionPerformed
            seleccionarFoto(panelFotoPerfil, "foto de perfil");
    }//GEN-LAST:event_btnSeleccionarPerfilActionPerformed

    private void panelFotoPortadaAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelFotoPortadaAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_panelFotoPortadaAncestorAdded

    private void lblFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblFotoPerfilAncestorAdded
        lblFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
        lblFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
    }//GEN-LAST:event_lblFotoPerfilAncestorAdded

    private void panelFotoPerfilAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_panelFotoPerfilAncestorAdded
        panelFotoPerfil.setPreferredSize(new Dimension(100, 100)); // Ajusta según necesites
    }//GEN-LAST:event_panelFotoPerfilAncestorAdded

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
            java.util.logging.Logger.getLogger(EdiPerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EdiPerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EdiPerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EdiPerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EdiPerfil().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Explorar;
    private javax.swing.JLabel Inicio;
    private javax.swing.JLabel LogoTwitter2;
    private javax.swing.JPanel Menu2;
    private javax.swing.JLabel Notificaciones;
    private javax.swing.JPanel Panel1;
    private javax.swing.JPanel Portada;
    private javax.swing.JLabel TextoEP;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMostrarContraseña;
    private javax.swing.JButton btnSeleccionarPerfil;
    private javax.swing.JButton btnSeleccionarPortada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblFondoPortada;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JPanel panelFotoPerfil;
    private javax.swing.JPanel panelFotoPortada;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JTextField txtAlias;
    private javax.swing.JPasswordField txtConfirmarPassword;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombreUsuario;
    private javax.swing.JPasswordField txtPasswordAnterior;
    private javax.swing.JPasswordField txtPasswordNueva;
    // End of variables declaration//GEN-END:variables
}
