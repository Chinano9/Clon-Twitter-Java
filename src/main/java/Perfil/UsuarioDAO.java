/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Perfil; // Asegúrate de que el paquete sea correcto

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import PantallaInicio.BasededatosTwitter; // 👈 Asegúrate de que esta clase existe y tiene getConnection()

public class UsuarioDAO {
   public byte[] getFotoPerfil(int idUsuario) {
    String query = "SELECT foto_perfil FROM usuarios WHERE id_usuarios = ?";
    byte[] foto = null;

    try (Connection con = BasededatosTwitter.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {
        
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();
        
       if (rs.next()) {
    foto = rs.getBytes("foto_perfil");
    
    if (foto != null && foto.length > 0) {
        System.out.println("Imagen obtenida para ID: " + idUsuario);
    } else {
        System.out.println("No hay imagen para este usuario.");
    }
} else {
    System.out.println("Usuario no encontrado.");
}

    } catch (Exception e) {
        e.printStackTrace();
    }
    return foto;
}



}

