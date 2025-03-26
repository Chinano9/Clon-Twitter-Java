/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TweetVisual; // Asegúrate de que el paquete sea correcto



public class UsuarioSesion {
    private static int usuarioId; // Variable estática para mantener el ID del usuario en sesión

    public static void setUsuarioId(int id) {
        usuarioId = id;
        
    }

    public static int getUsuarioId() {  // Método corregido
        return usuarioId;
    }
}