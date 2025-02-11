package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
 
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
    private ImageView logo;  // Se usará para mostrar la imagen de portada (cover)
    
    @FXML
    private Label tituloJuego;
    
    @FXML
    private Label descripcionJuego;
    
    @FXML
    private Pane panelMetacritics;
    
    @FXML
    private Label puntuacionMetacritics;
    
    // ImageView donde se mostrará el carrusel de capturas (screenshots)
    @FXML
    private ImageView imagenesJuego;
    
    @FXML
    private ListView<String> listaJuegosRelacionados;  // Lista de juegos relacionados
    
    private final ApiClient apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
    
    // Índice para gestionar la navegación en el carrusel
    private int imagenIndex = 0;  
    // Lista de URLs que contendrá únicamente las capturas de pantalla (screenshots)
    private List<String> imagenUrls = new ArrayList<>();
    
    @FXML
    private void pasarDerecha() {
        // Avanzar al siguiente screenshot
        imagenIndex++;
        if (imagenIndex >= imagenUrls.size()) {
            imagenIndex = 0;  // Si es el último, volvemos al inicio
        }
        actualizarImagen();
    }
    
    @FXML
    private void pasarIzquierda() {
        // Retroceder al screenshot anterior
        imagenIndex--;
        if (imagenIndex < 0) {
            imagenIndex = imagenUrls.size() - 1;  // Si es el primero, vamos al último
        }
        actualizarImagen();
    }
    
    @FXML
    private void ponerEnBiblioteca() {
        // Implementar la lógica para agregar el juego a la biblioteca
    }
    
    private void actualizarImagen() {
        // Actualizar el ImageView con la imagen del carrusel según el índice actual
        if (!imagenUrls.isEmpty() && imagenIndex >= 0 && imagenIndex < imagenUrls.size()) {
            imagenesJuego.setImage(new Image(imagenUrls.get(imagenIndex)));
        }
    }
    
    public void setNombreJuego(String nombreJuego) {
        tituloJuego.setText(nombreJuego);
 
        try {
            // Codificar el nombre para evitar problemas con caracteres especiales
            String encodedGameName = URLEncoder.encode(nombreJuego, "UTF-8");
            
            // Solicitar a la API la información del juego (busqueda)
            String response = apiClient.fetch("games", "search=" + encodedGameName + "&page_size=1");
            
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONArray("results");
 
            if (results.length() > 0) {
                // Extraer la información del primer resultado
                JSONObject game = results.getJSONObject(0);
 
                // Obtener y asignar la imagen de portada (para el logo)
                // Se usa cover_image o, de no existir, background_image
                String logoUrl = game.optString("cover_image", game.optString("background_image"));
                if (!logoUrl.isEmpty()) {
                    logo.setImage(new Image(logoUrl));
                }
 
                // Asignar descripción y puntuación
                String descripcion = game.optString("short_description", "Descripción no disponible.");
                descripcionJuego.setText(descripcion);
                
                String metacriticsScore = game.optString("metacritic", "Sin puntuación");
                puntuacionMetacritics.setText("Metacritic: " + metacriticsScore);
 
                // Construir el carrusel con las capturas de pantalla (screenshots)
                // NO se añade la imagen de fondo al carrusel
                imagenUrls.clear();  // Limpiar la lista antes de agregar nuevas capturas
 
                try {
                    // Obtener el id del juego (convertido a String)
                    String gameId = game.get("id").toString();
                    // Realizar la petición para obtener las screenshots del juego
                    String screenshotsResponse = apiClient.fetch("games/" + gameId + "/screenshots", "");
                    JSONObject screenshotsJson = new JSONObject(screenshotsResponse);
                    JSONArray screenshotsArray = screenshotsJson.optJSONArray("results");
                    if (screenshotsArray != null && screenshotsArray.length() > 0) {
                        for (int i = 0; i < screenshotsArray.length(); i++) {
                            JSONObject screenshot = screenshotsArray.getJSONObject(i);
                            String screenshotUrl = screenshot.optString("image", "");
                            if (!screenshotUrl.isEmpty()) {
                                imagenUrls.add(screenshotUrl);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
 
                // Si no hay capturas, se usa la imagen de fondo como respaldo (opcional)
                if (imagenUrls.isEmpty()) {
                    String fallbackImage = game.optString("background_image", "");
                    if (!fallbackImage.isEmpty()) {
                        imagenUrls.add(fallbackImage);
                    }
                }
 
                // Reiniciar el índice del carrusel y mostrar la primera imagen (que serán las screenshots)
                imagenIndex = 0;
                actualizarImagen();
 
                // (Opcional) Cargar la lista de juegos relacionados, si aplica:
                JSONArray relatedGames = game.optJSONArray("related_games");
                if (relatedGames != null) {
                    listaJuegosRelacionados.getItems().clear();
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
