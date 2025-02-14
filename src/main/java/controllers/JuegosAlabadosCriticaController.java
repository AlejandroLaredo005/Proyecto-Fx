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
      // Cargar las imágenes al inicializar el controlador
      apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
      cargarJuegos();
  }
  
  private void cargarJuegos() {
    try {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaInicio = fechaMesAnterior.format(formatter);
        String fechaFin = fechaActual.format(formatter);

        String response = apiClient.fetch("games", "dates=" + fechaInicio + "," + fechaFin + "&page_size=10");
        org.json.JSONObject json = new org.json.JSONObject(response);
        org.json.JSONArray results = json.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            String imageUrl = results.getJSONObject(i).optString("cover_image", results.getJSONObject(i).getString("background_image"));
            String gameName = results.getJSONObject(i).getString("name"); // Obtener el nombre del juego
            
            ImageView imgView = getImageViewByIndex(i);
            
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imgView.setImage(new Image(imageUrl));
                imgView.setFitWidth(86);  // Asegurarse de que todas las imágenes tengan el ancho correcto
                imgView.setFitHeight(96); // Asegurarse de que todas las imágenes tengan la altura correcta
                imgView.setPreserveRatio(false);  // Asegurar que las imágenes se ajusten sin mantener la proporción
                juegosMap.put(imgView, gameName); // Asociar ImageView con el nombre del juego
                imgView.setOnMouseClicked(this::mostrarNombreJuego); // Agregar evento de clic
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
      
      // Obtener el stage de la ventana actual
      Stage currentStage = (Stage) img1.getScene().getWindow(); 

      // Cerrar la ventana actual
      currentStage.close();
  } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
