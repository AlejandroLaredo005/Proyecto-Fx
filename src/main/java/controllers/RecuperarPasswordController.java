package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import utils.EmailService;
import dao.impl.UsuariosDaoImpl;

public class RecuperarPasswordController {

    @FXML
    private TextField txtCorreo;

    private String codigoGenerado;

    @FXML
    private void mandarCodigo() {
        String correo = txtCorreo.getText();

        if (correo.isEmpty()) {
            mostrarAlerta("Error", "El correo no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }

        // Validar si el correo está registrado en la base de datos
        UsuariosDaoImpl usuariosDao = new UsuariosDaoImpl();
        boolean existeCorreo = usuariosDao.findByCorreo(correo).isPresent();

        if (!existeCorreo) {
            mostrarAlerta("Error", "El correo ingresado no está registrado en el sistema.", Alert.AlertType.ERROR);
            return;
        }

        // Generar y enviar el código
        codigoGenerado = generarCodigo();

        try {
            EmailService emailService = new EmailService(correo, codigoGenerado);
            emailService.enviarCorreo();
            mostrarAlerta("Éxito", "El código ha sido enviado al correo ingresado.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al enviar el correo.", Alert.AlertType.ERROR);
        }
    }

    // Método para generar un código aleatorio
    private String generarCodigo() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // Código de 6 dígitos
    }

    // Método para validar el código ingresado
    public boolean validarCodigo(String codigoIngresado) {
        return codigoGenerado != null && codigoGenerado.equals(codigoIngresado);
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
