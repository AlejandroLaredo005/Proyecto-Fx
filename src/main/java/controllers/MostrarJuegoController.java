package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Biblioteca;
import models.Juegos;
import models.Usuarios;
import utils.SesionUsuario;

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
    
    // ListView para mostrar los juegos relacionados
    @FXML
    private ListView<Juegos> listaJuegosRelacionados;
    
    private String logoUrl;
    
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
   // Obtener el usuario logueado
      Usuarios usuarioLogueado = SesionUsuario.getUsuarioActual();
      String nombreJuego = tituloJuego.getText();
      
      // Usamos el DAO de Juegos para buscar si ya existe un juego con ese nombre.
      dao.JuegosDao juegosDao = new dao.impl.JuegosDaoImpl();
      Juegos currentJuego = juegosDao.findByNombre(nombreJuego).orElse(null);
      
      if (currentJuego == null) {
          // Si no existe, se crea y se persiste
          currentJuego = new Juegos(
              nombreJuego,
              puntuacionMetacritics.getText(),
              descripcionJuego.getText(),
              logoUrl
          );
          juegosDao.save(currentJuego);
      }
      
      // Ahora usamos el DAO de Biblioteca para verificar si este juego ya está en la biblioteca del usuario
      dao.BibliotecaDao bibliotecaDao = new dao.impl.BibliotecaDaoImpl();
      if (bibliotecaDao.findByUsuarioAndJuego(usuarioLogueado, currentJuego).isPresent()) {
          // Si ya existe, no se añade de nuevo (puedes mostrar una alerta o un mensaje)
          System.out.println("El juego ya está en tu biblioteca.");
          return;  // Salir del método sin añadir duplicado.
      }
      
      // Si no existe, se crea la nueva entrada en la biblioteca
      Biblioteca nuevaEntrada = new Biblioteca(usuarioLogueado, currentJuego, "", "Quiero Jugarlo");
      bibliotecaDao.save(nuevaEntrada);
      
      System.out.println("Juego agregado a la biblioteca exitosamente.");
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
            
            // Solicitar a la API la información del juego (búsqueda)
            String response = apiClient.fetch("games", "search=" + encodedGameName + "&page_size=1");
            
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray results = jsonResponse.getJSONArray("results");
 
            if (results.length() > 0) {
                // Extraer la información del primer resultado
                JSONObject game = results.getJSONObject(0);
 
                // Obtener y asignar la imagen de portada (para el logo)
                // Se usa cover_image o, de no existir, background_image
                logoUrl = game.optString("cover_image", game.optString("background_image"));
                if (!logoUrl.isEmpty()) {
                    logo.setImage(new Image(logoUrl));
                }
 
                // Asignar descripción y puntuación
                String descripcion = game.optString("short_description", "Descripción no disponible.");
                descripcionJuego.setText(descripcion);
                
                String metacriticsScore = game.optString("metacritic", "No P");
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
 
                // Actualizar la lista de juegos relacionados usando la búsqueda similar a BuscadorController
                actualizarJuegosRelacionados(nombreJuego);
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
    
    /**
     * Actualiza la lista de juegos relacionados usando el nombre del juego actual como consulta.
     * Se comporta de forma similar al método actualizarJuegos() del BuscadorController.
     */
    private void actualizarJuegosRelacionados(String gameName) {
        String busqueda = gameName.trim();
        if (busqueda.isEmpty()) {
            listaJuegosRelacionados.getItems().clear();
            return;
        }
 
        try {
            busqueda = URLEncoder.encode(busqueda, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
 
        final String busquedaFinal = busqueda;
 
        // Realizar la búsqueda en un hilo secundario para no bloquear la interfaz
        new Thread(() -> {
            List<Juegos> juegosEncontrados = apiClient.buscarJuegos(busquedaFinal);

            // Actualizar la lista de resultados en el hilo de la interfaz
            Platform.runLater(() -> {
                listaJuegosRelacionados.getItems().clear();
                // Añadimos solo el nombre, imagen y puntuación a la lista
                for (Juegos juego : juegosEncontrados) {
                    listaJuegosRelacionados.getItems().add(juego);  // Agregar el objeto completo
                }
            });
        }).start();
    }

    // Definir la celda personalizada para mostrar nombre, imagen y puntuación
    @FXML
    public void initialize() {
        // Configurar cómo se mostrarán los juegos en el ListView de juegos relacionados
        listaJuegosRelacionados.setCellFactory(listView -> new ListCell<Juegos>() {
            private final ImageView imageView = new ImageView();
            private final Label nombreLabel = new Label();
            private final Label puntuacionLabel = new Label();

            @Override
            protected void updateItem(Juegos juego, boolean empty) {
                super.updateItem(juego, empty);
                if (empty || juego == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Configura la celda con el nombre, puntuación y la imagen
                    nombreLabel.setText(juego.getNombreJuego());
                    puntuacionLabel.setText("Puntuación: " + (juego.getPuntuacionMetacritic() != null ? juego.getPuntuacionMetacritic() : "N/A"));
                    
                    if (juego.getImagenUrl() != null) {
                        imageView.setImage(new Image(juego.getImagenUrl(), 50, 50, true, true));
                    }

                    // Layout de la celda
                    Pane pane = new Pane();
                    imageView.setLayoutX(10);
                    imageView.setLayoutY(10);
                    nombreLabel.setLayoutX(60);
                    nombreLabel.setLayoutY(10);
                    puntuacionLabel.setLayoutX(60);
                    puntuacionLabel.setLayoutY(30);
                    pane.getChildren().addAll(imageView, nombreLabel, puntuacionLabel);
                    setGraphic(pane);
                }
            }
        });
        
        // Agregar un evento para detectar el clic en un juego de la lista
        listaJuegosRelacionados.setOnMouseClicked(event -> {
            Juegos juegoSeleccionado = listaJuegosRelacionados.getSelectionModel().getSelectedItem();
            if (juegoSeleccionado != null) {
                // Aquí puedes manejar lo que pasa cuando se hace clic en un juego
                System.out.println("Juego seleccionado: " + juegoSeleccionado.getNombreJuego());
                // Por ejemplo, podrías abrir una nueva ventana con los detalles del juego
                abrirMostrarJuegos(juegoSeleccionado.getNombreJuego());
            }
        });
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
          Stage currentStage = (Stage) tituloJuego.getScene().getWindow();
          currentStage.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
