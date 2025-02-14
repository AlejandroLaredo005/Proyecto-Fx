package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NavegadorController {
 
  @FXML
  private Label lblInicio;
  
  @FXML
  private void inicioPulsado(MouseEvent event) {
    // Lógica para abrir una nueva ventana
    Stage currentStage = (Stage) lblInicio.getScene().getWindow();
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
  }

  @FXML
  private void bibliotecaPulsada(MouseEvent event) {
    // Lógica para abrir una nueva ventana
    Stage currentStage = (Stage) lblInicio.getScene().getWindow();
    currentStage.close();
    
    // Abrir la nueva ventana principal
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/Biblioteca.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Biblioteca");
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
}
