package controllers;

import dao.impl.UsuariosDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Usuarios;

import java.util.Optional;

public class RegistroController {

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

    @FXML
    private void crearCuenta() {
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        String password2 = txtPassword2.getText();
        String email = txtEmail.getText();

        // Validar campos vacíos
        if (nombre.isEmpty() || apellidos.isEmpty() || usuario.isEmpty() || password.isEmpty() || email.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(password2)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden.", Alert.AlertType.ERROR);
            return;
        }

        UsuariosDaoImpl usuariosDao = new UsuariosDaoImpl();

        // Comprobar si el nombre de usuario ya existe
        Optional<Usuarios> usuarioExistente = usuariosDao.findByUsuario(usuario);
        if (usuarioExistente.isPresent()) {
            mostrarAlerta("Error", "El nombre de usuario ya existe. Por favor, elige otro.", Alert.AlertType.ERROR);
            return;
        }

        // Comprobar si el correo electrónico ya está registrado
        Optional<Usuarios> correoExistente = usuariosDao.findByCorreo(email);
        if (correoExistente.isPresent()) {
            mostrarAlerta("Error", "El correo electrónico ya está registrado. Por favor, utiliza otro correo.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Crear y guardar el nuevo usuario
            Usuarios nuevoUsuario = new Usuarios(nombre, apellidos, usuario, password, email);
            usuariosDao.save(nuevoUsuario);
            mostrarAlerta("Éxito", "Usuario creado exitosamente.", Alert.AlertType.INFORMATION);

            // Redirigir a la pantalla de inicio de sesión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/InicioSesion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setTitle("Inicio de Sesión");
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtNombre.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Ocurrió un error al crear el usuario. Inténtalo de nuevo.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Muestra una alerta con un título, un mensaje y un tipo de alerta.
     *
     * @param titulo   Título de la alerta.
     * @param mensaje  Mensaje a mostrar en la alerta.
     * @param tipo     Tipo de alerta (ERROR, INFORMATION, etc.).
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
