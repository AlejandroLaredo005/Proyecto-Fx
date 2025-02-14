package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.List;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONArray;

public class JuegosAlabadosCriticaController {
  
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
  
  private Map<ImageView, String> juegosMap = new HashMap<>(); // Mapeo de ImageView a nombres de juegos
  
  @FXML
  public void initialize() {
      // Se inicializa el cliente y se cargan los juegos
      apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
      cargarJuegos();
  }
  
  private void cargarJuegos() {
    try {
        // Solicitar los 10 juegos mejor valorados según metacritic (orden descendente)
        String response = apiClient.fetch("games", "ordering=-metacritic&page_size=10");
        JSONObject json = new JSONObject(response);
        JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject game = results.getJSONObject(i);
            // Se intenta obtener primero "cover_image", si no se encuentra se usa "background_image"
            String imageUrl = game.optString("cover_image", game.optString("background_image", ""));
            String gameName = game.optString("name", "Sin nombre");

            // Mostrar el juego solo si existe una URL de imagen válida
            if (imageUrl != null && !imageUrl.isEmpty()) {
                ImageView imgView = getImageViewByIndex(i);
                if (imgView != null) {
                    // Si la URL no es completa, se podría agregar un prefijo (descomenta la siguiente línea si es necesario)
                    // if (!imageUrl.startsWith("http")) { imageUrl = "https://media.rawg.io/media/games/" + imageUrl; }

                    imgView.setImage(new Image(imageUrl));
                    imgView.setFitWidth(86);
                    imgView.setFitHeight(96);
                    imgView.setPreserveRatio(false);
                    juegosMap.put(imgView, gameName);
                    imgView.setOnMouseClicked(this::mostrarNombreJuego);
                }
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

        // Obtener el controlador y pasarle el nombre del juego
        MostrarJuegoController controller = loader.getController();
        controller.setNombreJuego(nombreJuego);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Detalles del Juego");
        stage.show();

        // Cerrar la ventana actual
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
      scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
      stage.setTitle("Inicio");
      stage.show();
      
      // Cerrar la ventana actual
      Stage currentStage = (Stage) img1.getScene().getWindow(); 
      currentStage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
