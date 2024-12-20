package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application{
  
  private Stage primaryStage;
  private BorderPane rootLayout;

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    initInicioLayout();
  }
  
  /**
   * Initializes the root layout.
   */
  public void initInicioLayout() {
      try {
          // Load root layout from fxml file.
          FXMLLoader loader = new FXMLLoader();
          loader.setLocation(MainApp.class.getResource("../views/InicioSesion.fxml"));
          rootLayout = (BorderPane) loader.load();
          
          // Show the scene containing the root layout.
          Scene scene = new Scene(rootLayout);
          primaryStage.setScene(scene);
          primaryStage.show();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
  
  /**
   * Returns the main stage.
   * @return
   */
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public static void main(String[] args) {
    launch(args);
  }

}
