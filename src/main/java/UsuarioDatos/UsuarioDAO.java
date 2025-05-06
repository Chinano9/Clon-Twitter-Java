package UsuarioDatos;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Macke
 */

import java.sql.ResultSet;
import java.sql.SQLException; // 👈 Agregar este si falta
import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ConexionBase.RetornarBaseDedatos;



public class UsuarioDAO {
   public byte[] getFotoPerfil(int idUsuario) {
    String query = "SELECT foto_perfil FROM usuarios WHERE id_usuarios = ?";
    byte[] foto = null;

    try (Connection con = RetornarBaseDedatos.getConnection();
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

