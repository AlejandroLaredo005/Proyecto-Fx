package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RecuperarPasswordController {

    @FXML
    private TextField txtCorreo;

    // Método para manejar el evento del botón "Enviar"
    @FXML
    private void mandarCodigo() {
        String correo = txtCorreo.getText();

        if (correo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "Por favor, introduce tu correo electrónico.");
        } else if (!esCorreoValido(correo)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Correo no válido", "Por favor, introduce un correo válido.");
        } else {
            // Simula el envío de un código al correo electrónico
            mostrarAlerta(Alert.AlertType.INFORMATION, "Código enviado", "Se ha enviado un código a " + correo);
            // Aquí podrías implementar la lógica para enviar el código real
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

    // Valida si el correo tiene un formato válido
    private boolean esCorreoValido(String correo) {
        // Expresión regular para validar el formato de un correo electrónico
        String correoRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return correo.matches(correoRegex);
    }
}
