package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MostrarJuegoController {
    
    @FXML
    private ImageView logo;
    
    @FXML
    private Label tituloJuego;
    
    @FXML
    private Label descripcionJuego;
    
    @FXML
    private Pane panelMetacritics;
    
    @FXML
    private Label puntuacionMetacritics;
    
    @FXML
    private ImageView imagenesJuego;
    
    @FXML
    private ListView<String> listaJuegosRelacionados;
    
    private final ApiClient apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
    
    @FXML
    private void pasarDerecha() {
        // Implementar lógica para avanzar en las imágenes
    }
    
    @FXML
    private void pasarIzquierda() {
        // Implementar lógica para retroceder en las imágenes
    }
    
    @FXML
    private void ponerEnBiblioteca() {
        // Implementar lógica para agregar el juego a la biblioteca
    }

    public void setNombreJuego(String nombreJuego) {
      tituloJuego.setText(nombreJuego);

      try {
          // Codificar el nombre del juego para evitar problemas con caracteres especiales
          String encodedGameName = URLEncoder.encode(nombreJuego, "UTF-8");
          
          // Realizar la solicitud a la API con el nombre codificado
          String response = apiClient.fetch("games", "search=" + encodedGameName + "&page_size=1");
          
          JSONObject jsonResponse = new JSONObject(response);
          JSONArray results = jsonResponse.getJSONArray("results");

          if (results.length() > 0) {
              // Extraemos la información del primer resultado
              JSONObject game = results.getJSONObject(0);

              // Logo de la imagen
              String logoUrl = game.optString("cover_image", game.optString("background_image"));
              if (!logoUrl.isEmpty()) {
                  logo.setImage(new Image(logoUrl));
              }

              // Descripción
              String descripcion = game.optString("short_description", "Descripción no disponible.");
              descripcionJuego.setText(descripcion);

              // Puntuación de Metacritics
              String metacriticsScore = game.optString("metacritic", "Sin puntuación");
              puntuacionMetacritics.setText("Metacritic: " + metacriticsScore);

              // Imagen principal del juego
              String imageUrl = game.optString("background_image", "");
              if (!imageUrl.isEmpty()) {
                  imagenesJuego.setImage(new Image(imageUrl));
              }

              // Juegos relacionados (si están disponibles)
              JSONArray relatedGames = game.optJSONArray("related_games");
              if (relatedGames != null) {
                  for (int i = 0; i < relatedGames.length(); i++) {
                      String relatedGame = relatedGames.getJSONObject(i).getString("name");
                      listaJuegosRelacionados.getItems().add(relatedGame);
                  }
              }
          } else {
              descripcionJuego.setText("Juego no encontrado.");
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          descripcionJuego.setText("Error al codificar el nombre del juego.");
      } catch (Exception e) {
          e.printStackTrace();
          descripcionJuego.setText("Error al cargar el juego.");
      }
  }
}
