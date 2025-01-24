package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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

      org.json.JSONObject json = new org.json.JSONObject(response);
      org.json.JSONArray results = json.getJSONArray("results");

      for (int i = 0; i < results.length(); i++) {
          String imageUrl = results.getJSONObject(i).optString("cover_image", results.getJSONObject(i).getString("background_image"));
          imageUrls.add(imageUrl);
      }
    }


  private void actualizarCarrusel() {
      // Asignar imágenes a img1, img2, img3
      if (imageUrls.isEmpty()) return;

      img1.setImage(new Image(imageUrls.get(currentIndex)));
      img2.setImage(new Image(imageUrls.get((currentIndex + 1) % imageUrls.size())));
      img3.setImage(new Image(imageUrls.get((currentIndex + 2) % imageUrls.size())));
      
      // Ajustar el tamaño de la imagen a la del ImageView, manteniendo la proporción
      img1.setFitWidth(72);
      img1.setFitHeight(111);
      img1.setPreserveRatio(false);

      img2.setFitWidth(72);
      img2.setFitHeight(111);
      img2.setPreserveRatio(false);

      img3.setFitWidth(72);
      img3.setFitHeight(111);
      img3.setPreserveRatio(false);
  }

    // Métodos para manejar eventos
    @FXML
    private void buscar(MouseEvent event) {
        String textoBuscado = txtBuscador.getText();
        System.out.println("Buscando: " + textoBuscado);
        // Implementar lógica para buscar juegos
    }

    @FXML
    private void juegosMes(MouseEvent event) {
        System.out.println("Mostrando juegos que han salido este mes");
        // Implementar lógica para mostrar juegos del mes
    }

    @FXML
    private void juegosCritica(MouseEvent event) {
        System.out.println("Mostrando juegos alabados por la crítica");
        // Implementar lógica para mostrar juegos alabados por la crítica
    }

    @FXML
    private void juegosYear(MouseEvent event) {
        System.out.println("Mostrando juegos que han salido este año");
        // Implementar lógica para mostrar juegos del año
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
        System.out.println("Mostrando img1");
        // Implementar lógica para desplazar imágenes a la derecha
    }
    
    @FXML
    private void img2Pulsada(MouseEvent event) {
        System.out.println("Mostrando img2");
        // Implementar lógica para desplazar imágenes a la derecha
    }
    
    @FXML
    private void img3Pulsada(MouseEvent event) {
        System.out.println("Mostrando img3");
        // Implementar lógica para desplazar imágenes a la derecha
    }
}
