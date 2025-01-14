package controllers;

import dao.impl.UsuariosDaoImpl;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import models.Usuarios;

public class CambiarPasswordController {

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPassword2;

    private String correo;

    // Método para configurar el correo recibido de EscribirCodigoController
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Método para manejar el evento del botón "Cambiar"
    @FXML
    private void changePassword() {
        String password = txtPassword.getText();
        String confirmPassword = txtPassword2.getText();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, completa ambos campos de contraseña.");
        } else if (!password.equals(confirmPassword)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseñas no coinciden", "Las contraseñas introducidas no coinciden.");
        } else if (!esPasswordValida(password)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Contraseña no válida", "La contraseña debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una minúscula y un número.");
        } else {
            // Buscar al usuario con el correo
            UsuariosDaoImpl usuariosDao = new UsuariosDaoImpl();
            Usuarios usuario = usuariosDao.findByCorreo(correo).orElse(null);

            if (usuario != null) {
                // Actualizar la contraseña del usuario
                usuario.setContraseña(password);
                usuariosDao.update(usuario);  // Asegúrate de que el método update funcione

                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La contraseña ha sido cambiada exitosamente.");

                // Volver a la página de inicio
                volverAInicio();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario no encontrado", "No se encontró un usuario con ese correo.");
            }
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Valida si la contraseña cumple con los requisitos de seguridad
    private boolean esPasswordValida(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") && // Al menos una letra mayúscula
               password.matches(".*[a-z].*") && // Al menos una letra minúscula
               password.matches(".*\\d.*");    // Al menos un número
    }

    // Método para volver a la pantalla de inicio
    private void volverAInicio() {
        try {
            // Cargar el archivo FXML de la página de inicio
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/InicioSesion.fxml"));
            Scene escenaInicio = new Scene(loader.load());

            // Mostrar la nueva pantalla de inicio
            Stage stage = (Stage) txtPassword.getScene().getWindow();
            stage.setScene(escenaInicio);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();  // Imprimir la excepción para depurar
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al cargar la página de inicio.");
        }
    }
}
