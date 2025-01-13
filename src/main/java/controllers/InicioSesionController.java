package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InicioSesionController {
  
  @FXML
  private TextField txtUsuario;
  
  @FXML
  private TextField txtPassword;
  
  @FXML
  private void registerUser() {
      try {
          // Cargar el nuevo archivo FXML
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Registro.fxml"));
          Parent root = loader.load();

          // Crear una nueva escena y establecerla en el Stage
          Scene scene = new Scene(root);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.setTitle("Registro");
          stage.show();

          // (Opcional) Cerrar la ventana actual si es necesario
          Stage currentStage = (Stage) txtUsuario.getScene().getWindow();
          currentStage.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
