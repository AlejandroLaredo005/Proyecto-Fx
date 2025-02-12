package utils;

import models.Usuarios;

/**
 * Clase que usaran todas las ventanas para reconocer al usuario que está logueado
 */
public class SesionUsuario {
    private static Usuarios usuarioActual;

    // Evitar instanciación
    private SesionUsuario() {}

    public static void iniciarSesion(Usuarios usuario) {
        usuarioActual = usuario;
    }

    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
