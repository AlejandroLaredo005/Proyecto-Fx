package controllers;

import java.io.IOException;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class JuegosEsteMesController {
  
  @FXML
  private ImageView img1;
  
  @FXML
  private ImageView img2;
  
  @FXML
  private ImageView img3;
  
  @FXML
  private ImageView img4;
  
  @FXML
  private ImageView img5;
  
  @FXML
  private ImageView img6;
  
  @FXML
  private ImageView img7;
  
  @FXML
  private ImageView img8;
  
  @FXML
  private ImageView img9;
  
  @FXML
  private ImageView img10;
  
  private ApiClient apiClient;
  
  @FXML
  public void initialize() {
      // Cargar las imágenes al inicializar el controlador
      apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
      cargarJuegos();
  }
  
  private void cargarJuegos() {
    try {
      String response = apiClient.fetch("games", "dates=2025-01-01,2025-01-31&page_size=10");
      
      org.json.JSONObject json = new org.json.JSONObject(response);
      org.json.JSONArray results = json.getJSONArray("results");
      
      for (int i = 0; i < results.length(); i++) {
        String imageUrl = results.getJSONObject(i).optString("cover_image", results.getJSONObject(i).getString("background_image"));
        
        // Obtener el ImageView correspondiente a la iteración
        ImageView imgView = getImageViewByIndex(i);
        
        if (imageUrl != null && !imageUrl.isEmpty()) {
          imgView.setImage(new Image(imageUrl));
      } 
      }
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }
  }
  
  private ImageView getImageViewByIndex(int index) {
    switch (index) {
        case 0: return img1;
        case 1: return img2;
        case 2: return img3;
        case 3: return img4;
        case 4: return img5;
        case 5: return img6;
        case 6: return img7;
        case 7: return img8;
        case 8: return img9;
        case 9: return img10;
        default: return null;
    }
}

  @FXML
  private void atras(MouseEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/Inicio.fxml"));
      Parent root = loader.load();
      
      Scene scene = new Scene(root);
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("Inicio");
      stage.show();
      
      // Obtener el stage de la ventana actual
      Stage currentStage = (Stage) img1.getScene().getWindow(); 

      // Cerrar la ventana actual
      currentStage.close();
  } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
