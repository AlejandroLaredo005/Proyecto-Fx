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

import java.util.List;

public class InicioSesionController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtPassword;

    // Método de inicio de sesión
    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String contrasena = txtPassword.getText();
        
     

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, complete todos los campos.");
            return;
        }

        UsuariosDaoImpl usuariosDao = new UsuariosDaoImpl();
        List<Usuarios> usuarios = usuariosDao.findAll(); // Obtener todos los usuarios

        Usuarios usuarioEncontrado = usuarios.stream()
                .filter(u -> u.getNombreUsuario().equals(usuario))
                .findFirst()
                .orElse(null);

        if (usuarioEncontrado != null) {
            if (usuarioEncontrado.getContraseña().equals(contrasena)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Inicio de sesión exitoso", "¡Bienvenido, " + usuarioEncontrado.getNombre() + "!");
                // Lógica para abrir una nueva ventana
                Stage currentStage = (Stage) txtUsuario.getScene().getWindow();
                currentStage.close();
                
                // Abrir la nueva ventana principal
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/Inicio.fxml"));
                    Parent root = loader.load();
                    
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Inicio");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de autenticación", "Contraseña incorrecta.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Usuario no encontrado", "El usuario ingresado no existe.");
        }
    }

    // Método de registro
    @FXML
    private void registerUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/Registro.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Registro");
            stage.show();

            Stage currentStage = (Stage) txtUsuario.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void recuperarPassword() {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/RecuperarPassword.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Recuperar Contraseña");
        stage.show();

        Stage currentStage = (Stage) txtUsuario.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
      Alert alert = new Alert(tipo);
      alert.setTitle(titulo);
      alert.setHeaderText(null);
      alert.setContentText(mensaje);
      alert.showAndWait();
    }
}