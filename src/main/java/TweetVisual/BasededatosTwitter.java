/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TweetVisual;

/**
 *
 * @author Macke
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class BasededatosTwitter {

    private static final String URL = "jdbc:mysql://localhost:3306/twitterdb";
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
    }
}
