package controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EscribirCodigoController {

    @FXML
    private Label labelPassword;

    @FXML
    private TextField txtCodigo;

    private String correo;

    // Método para configurar el correo después de cargar el controlador
    public void setCorreo(String correo) {
        this.correo = correo;
        labelPassword.setText("Introduce el código de seguridad enviado a tu correo " + correo + " para recuperar la contraseña.");
    }

    // Método para manejar el evento del botón "Enviar"
    @FXML
    private void mandarCodigo() {
        String codigo = txtCodigo.getText();

        if (codigo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo vacío", "Por favor, introduce el código de seguridad.");
        } else if (!esCodigoValido(codigo)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Código no válido", "El código introducido no es correcto.");
        } else if (!esCodigoCorrecto(codigo)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Código incorrecto", "El código introducido no coincide con el código enviado.");
        } else {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Código aceptado", "El código de seguridad es válido. Ahora puedes proceder a cambiar tu contraseña.");
            // Aquí podrías redirigir a una nueva escena para cambiar la contraseña
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

    // Valida si el código tiene el formato esperado (ejemplo: solo números y longitud fija)
    private boolean esCodigoValido(String codigo) {
        return codigo.matches("\\d{6}"); // Por ejemplo, un código de 6 dígitos numéricos
    }

    // Método para comprobar si el código ingresado es correcto comparándolo con el generado (estático)
    private boolean esCodigoCorrecto(String codigo) {
        return RecuperarPasswordController.codigoGenerado != null && RecuperarPasswordController.codigoGenerado.equals(codigo);
    }
}
