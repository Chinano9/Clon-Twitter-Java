/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package runproyectlogin;

/**
 *
 * @author alan_
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasededatosTwitter {
    
      public static Connection conectar() {
        try {
            String URL = "jdbc:mysql://localhost:3306/twitterdb"; // Direccion de la base de datos xampp
            String USER = "root"; // Usuario por defecto de XAMPP
            String PASSWORD = ""; //  Vacío en XAMPP
            
            Class.forName("com.mysql.cj.jdbc.Driver"); //Cargar el driver
            
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD); //Crear la conexion
            System.out.println("Conexión exitosa a la base de datos.");
            return conexion; //Retornar la conexion
            
        } catch (Exception e) {//Comprobar si se logro hacer la conexion, Exception e captura errores dentro del try
            System.out.println("No se encontró el driver de MySQL.");
            e.printStackTrace();//Detalla la parte del error.
            return null;
        }
    }
      
    /*private static final String URL = "jdbc:mysql://localhost:3306/twitterdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";
 
    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        // Cargar el driver (solo es necesario una vez, se suele poner en un bloque static)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de MySQL.");
            e.printStackTrace();
        }
 
        // Retornar la conexión
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }*/
    
}



