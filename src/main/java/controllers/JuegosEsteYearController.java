package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class JuegosEsteYearController {
  
  @FXML
  private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10;
  
  private ApiClient apiClient;
  private Map<ImageView, String> juegosMap = new HashMap<>(); 
  
  @FXML
  public void initialize() {
      apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
      cargarJuegos();
  }
  
  private void cargarJuegos() {
    try {
        LocalDate fechaActual = LocalDate.now();
        LocalDate inicioAno = LocalDate.of(fechaActual.getYear(), 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaInicio = inicioAno.format(formatter);
        String fechaFin = fechaActual.format(formatter);

        String response = apiClient.fetch("games", "dates=" + fechaInicio + "," + fechaFin + "&page_size=10");
        org.json.JSONObject json = new org.json.JSONObject(response);
        org.json.JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            String imageUrl = results.getJSONObject(i).optString("cover_image", results.getJSONObject(i).getString("background_image"));
            String gameName = results.getJSONObject(i).getString("name"); 
            
            ImageView imgView = getImageViewByIndex(i);
            
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imgView.setImage(new Image(imageUrl));
                imgView.setFitWidth(86);
                imgView.setFitHeight(96);
                imgView.setPreserveRatio(false);
                juegosMap.put(imgView, gameName);
                imgView.setOnMouseClicked(this::mostrarNombreJuego);
            }
        }
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }
  }
  
  private void mostrarNombreJuego(MouseEvent event) {
    ImageView clickedImageView = (ImageView) event.getSource();
    String gameName = juegosMap.get(clickedImageView);
    if (gameName != null) {
        abrirMostrarJuegos(gameName);
    }
  }
  
  private void abrirMostrarJuegos(String nombreJuego) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/MostrarJuego.fxml"));
        Parent root = loader.load();

        MostrarJuegoController controller = loader.getController();
        controller.setNombreJuego(nombreJuego);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Detalles del Juego");
        stage.show();

        Stage currentStage = (Stage) img1.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
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
      
      Stage currentStage = (Stage) img1.getScene().getWindow();
      currentStage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
