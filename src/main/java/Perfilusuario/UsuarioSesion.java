/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Perfilusuario; // Asegúrate de que el paquete sea correcto

import Perfil.*;
import PantallaInicio.*;

import PantallaInicio.BasededatosTwitter; // 👈 Asegúrate de que esta clase existe y tiene getConnection()

public class UsuarioSesion {
    private static int usuarioId = -1;

    public static void setUsuarioId(int id) {
        usuarioId = id;
    }

    public static int getUsuarioId() {
        return usuarioId;
    }
}
