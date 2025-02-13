package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class InicioController {

    // Elementos con fx:id
    @FXML
    private TextField txtBuscador;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;
    
    private final List<String> imageUrls = new ArrayList<>();
    private final List<String> gameNames = new ArrayList<>();
    private int currentIndex = 0;
    private final ApiClient apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
    
    @FXML
    public void initialize() {
        try {
            cargarImagenes();
            actualizarCarrusel();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error inicializando carrusel: " + e.getMessage());
        }
    }
    
    private void cargarImagenes() throws IOException, InterruptedException {
      String response = apiClient.fetch("games", "ordering=-popularity&page_size=15");
      imageUrls.clear();
      gameNames.clear();

      org.json.JSONObject json = new org.json.JSONObject(response);
      org.json.JSONArray results = json.getJSONArray("results");

      for (int i = 0; i < results.length(); i++) {
          String imageUrl = results.getJSONObject(i).optString("cover_image", results.getJSONObject(i).getString("background_image"));
          String gameName = results.getJSONObject(i).getString("name");
          imageUrls.add(imageUrl);
          gameNames.add(gameName);
      }
    }


  private void actualizarCarrusel() {
      // Asignar imágenes a img1, img2, img3
      if (imageUrls.isEmpty()) return;

      img1.setImage(new Image(imageUrls.get(currentIndex)));
      img2.setImage(new Image(imageUrls.get((currentIndex + 1) % imageUrls.size())));
      img3.setImage(new Image(imageUrls.get((currentIndex + 2) % imageUrls.size())));
      
      // Ajustar el tamaño de la imagen a la del ImageView, manteniendo la proporción
      img1.setFitWidth(90);
      img1.setFitHeight(111);
      img1.setPreserveRatio(false);

      img2.setFitWidth(90);
      img2.setFitHeight(111);
      img2.setPreserveRatio(false);

      img3.setFitWidth(90);
      img3.setFitHeight(111);
      img3.setPreserveRatio(false);
  }

    // Métodos para manejar eventos
    @FXML
    private void buscar(MouseEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/Buscador.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Buscador");
        stage.show();
        
        // Cerrar la ventana actual
        Stage currentStage = (Stage) txtBuscador.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void juegosMes(MouseEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/JuegosEsteMes.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Juegos este Mes");
        stage.show();
        
        // Cerrar la ventana actual
        Stage currentStage = (Stage) txtBuscador.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void juegosCritica(MouseEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/JuegosAlabadosCritica.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Juegos Alabados por la Crítica");
        stage.show();
        
        // Cerrar la ventana actual
        Stage currentStage = (Stage) txtBuscador.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void juegosYear(MouseEvent event) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/JuegosEsteYear.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Juegos este Año");
        stage.show();
        
        // Cerrar la ventana actual
        Stage currentStage = (Stage) txtBuscador.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void flechaIzquierdaPulsada(MouseEvent event) {
        System.out.println("Desplazando a la izquierda");
        currentIndex = (currentIndex - 1 + imageUrls.size()) % imageUrls.size();
        actualizarCarrusel();
    }

    @FXML
    private void flechaDerechaPulsada(MouseEvent event) {
        System.out.println("Desplazando a la derecha");
        currentIndex = (currentIndex + 1) % imageUrls.size();
        actualizarCarrusel();
    }
    
    @FXML
    private void img1Pulsada(MouseEvent event) {
        String nombreJuego = gameNames.get(currentIndex);
        abrirMostrarJuegos(nombreJuego);
    }
    
    @FXML
    private void img2Pulsada(MouseEvent event) {
      String nombreJuego = gameNames.get(currentIndex + 1);
      abrirMostrarJuegos(nombreJuego);
    }
    
    @FXML
    private void img3Pulsada(MouseEvent event) {
      String nombreJuego = gameNames.get(currentIndex + 2);
      abrirMostrarJuegos(nombreJuego);
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
          Stage currentStage = (Stage) txtBuscador.getScene().getWindow();
          currentStage.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}