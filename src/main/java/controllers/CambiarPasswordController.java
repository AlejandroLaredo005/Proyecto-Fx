package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CambiarPasswordController {

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPassword2;

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
            // Simula el cambio de contraseña exitoso
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La contraseña ha sido cambiada exitosamente.");
            // Aquí podrías incluir la lógica para guardar la nueva contraseña en tu sistema
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
        // Cambia las reglas según tus requisitos
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") && // Al menos una letra mayúscula
               password.matches(".*[a-z].*") && // Al menos una letra minúscula
               password.matches(".*\\d.*");    // Al menos un número
    }
}
