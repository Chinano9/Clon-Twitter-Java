/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PantallaInicio;
import java.sql.ResultSet;
import java.sql.SQLException; // 👈 Agregar este si falta
import PantallaInicio.BasededatosTwitter; // 👈 Asegúrate de que esta clase existe y tiene getConnection()
import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Blob; // Si estás trabajando con imágenes almacenadas como BLOB en la base de datos

/**
 *
 * @author Macke
 */
public class Tweet {
    private String nombreUsuario;
    private String alias;
    private String contenido;
    private Date fechaCreacion;
    private ImageIcon imagen; // Para simplificar, usaré ImageIcon directamente

    // Constructor, getters y setters
    public Tweet(String nombreUsuario, String alias, String contenido, Date fechaCreacion, ImageIcon imagen) {
        this.nombreUsuario = nombreUsuario;
        this.alias = alias;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.imagen = imagen;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getAlias() { return alias; }
    public String getContenido() { return contenido; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public ImageIcon getImagen() { return imagen; }

    @Override
    public String toString() {
        return alias + " (" + nombreUsuario + ") - " + contenido;
    }
}
