package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegistroController {

    // Campos de texto (vinculados por fx:id)
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPassword2;

    @FXML
    private TextField txtEmail;

    /**
     * Método que se ejecuta cuando se presiona el botón "Crear Cuenta".
     */
    @FXML
    private void crearCuenta() {
        // Obtén los valores de los campos de texto
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        String password2 = txtPassword2.getText();
        String email = txtEmail.getText();

        // Validación simple de ejemplo
        if (password.equals(password2)) {
            // Intentamos crear la cuenta
        } else {
            System.out.println("Las contraseñas no coinciden. Inténtalo de nuevo.");
        }
    }
}
