package test;
import org.junit.jupiter.api.Test;

import controllers.InicioSesionController;

public class InicioSesionControllerTest {

    @Test
    public void testIniciarSesion_conUsuarioYContraseñaCorrectos() {
        // Arrange
        InicioSesionController controller = new InicioSesionController();
        controller.txtUsuario.setText("usuarioValido");
        controller.txtPassword.setText("contraseñaCorrecta");

        // Act
        controller.iniciarSesion();

        // Assert
        // Aquí debes comprobar que el mensaje de éxito aparece y la ventana nueva se abre
    }

    @Test
    public void testIniciarSesion_conUsuarioIncorrecto() {
        // Arrange
        InicioSesionController controller = new InicioSesionController();
        controller.txtUsuario.setText("usuarioInvalido");
        controller.txtPassword.setText("contraseñaCorrecta");

        // Act
        controller.iniciarSesion();

        // Assert
        // Verificar que aparece el mensaje de error "Usuario no encontrado"
    }

    @Test
    public void testIniciarSesion_conContraseñaIncorrecta() {
        // Arrange
        InicioSesionController controller = new InicioSesionController();
        controller.txtUsuario.setText("usuarioValido");
        controller.txtPassword.setText("contraseñaIncorrecta");

        // Act
        controller.iniciarSesion();

        // Assert
        // Verificar que aparece el mensaje de error "Contraseña incorrecta"
    }

    @Test
    public void testCamposVacios() {
        // Arrange
        InicioSesionController controller = new InicioSesionController();
        controller.txtUsuario.setText("");
        controller.txtPassword.setText("");

        // Act
        controller.iniciarSesion();

        // Assert
        // Verificar que aparece el mensaje de error "Por favor, complete todos los campos"
    }
}
