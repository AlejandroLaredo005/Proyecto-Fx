package controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import api.ApiClient;
import dao.BibliotecaDao;
import dao.impl.BibliotecaDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import models.Biblioteca;
import models.Juegos;
import models.Usuarios;
import utils.SesionUsuario;

public class BibliotecaController {

    @FXML
    private TilePane tilePaneJuegos;
    
    @FXML
    private ComboBox<String> comboBoxOrden;
    
    @FXML
    private ComboBox<String> comboBoxFiltro;
    
    @FXML
    private ImageView fotoFlecha;

    // Instancia del DAO para interactuar con la base de datos.
    private BibliotecaDao bibliotecaDao = new BibliotecaDaoImpl();

    // Instancia del cliente de la API con tu API key
    private final ApiClient apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");

    @FXML
    public void initialize() {
        cargarImagenes();
        comboBoxOrden.getItems().addAll("Orden Alfabético", "Puntuación", "Fecha de Lanzamiento");
        comboBoxFiltro.getItems().addAll("Todos los Juegos", "Juegos por Jugar", "Juegos Jugados");
        
        // Asignar un listener al ComboBox para detectar cambios
        comboBoxOrden.setOnAction(event -> mostrarOpcionSeleccionada());
        
        // Asignar un listener al ComboBox para detectar cambios
        comboBoxFiltro.setOnAction(event -> cambiarFiltroJuegos());
    }
    
    @FXML
    private void cambiarOrden() {
      
    }

    /**
     * Consulta en la base de datos los juegos de la biblioteca y, utilizando el nombre del juego,
     * hace una búsqueda en la API para obtener la URL de la imagen.
     */
    private void cargarImagenes() {
        List<Biblioteca> bibliotecas = bibliotecaDao.findAll();
        
        if (bibliotecas == null || bibliotecas.isEmpty()) {
            System.out.println("No se encontraron juegos en la biblioteca.");
            return;
        }
        
        List<String> urls = new ArrayList<>();
        List<String> nombresJuegos = new ArrayList<>();
        
        Usuarios usuario = SesionUsuario.getUsuarioActual();
        for (Biblioteca biblioteca : bibliotecas) {
          if (biblioteca.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            Juegos juego = biblioteca.getJuego();
            if (juego != null) {
                String nombreJuego = juego.getNombreJuego();
                if (nombreJuego != null && !nombreJuego.trim().isEmpty()) {
                    // Codificar el nombre del juego antes de buscar en la API
                    String encodedNombre = URLEncoder.encode(nombreJuego, StandardCharsets.UTF_8);
                    // Buscar el juego en la API utilizando el nombre codificado
                    List<Juegos> juegosApi = apiClient.buscarJuegos(encodedNombre);
                    
                    if (juegosApi != null && !juegosApi.isEmpty()) {
                        Juegos juegoApi = juegosApi.get(0);
                        String imagenUrl = juegoApi.getImagenUrl();
                        
                        if (imagenUrl != null && !imagenUrl.trim().isEmpty()) {
                            urls.add(imagenUrl);
                            nombresJuegos.add(nombreJuego); // Guardar el nombre
                        } else {
                            System.out.println("No se encontró imagen para el juego: " + nombreJuego);
                        }
                    } else {
                        System.out.println("No se encontró el juego en la API para: " + nombreJuego);
                    }
                }
            }
          }
        }
        
        agregarImagenes(urls, nombresJuegos);
    }

    /**
     * Recorre la lista de URLs y, por cada una, crea un ImageView configurado
     * y lo añade al TilePane.
     */
    private void agregarImagenes(List<String> urls, List<String> nombresJuegos) {
      tilePaneJuegos.getChildren().clear(); // Limpia las imágenes previas

      for (int i = 0; i < urls.size(); i++) {
          try {
              String url = urls.get(i);
              String nombreJuego = nombresJuegos.get(i); // Obtener el nombre del juego

              Image image = new Image(url, 250, 150, true, true);
              ImageView imageView = new ImageView(image);

              imageView.setFitWidth(250);
              imageView.setFitHeight(150);
              imageView.setPreserveRatio(true);
              imageView.setSmooth(true);
              imageView.setStyle("-fx-effect: dropshadow(gaussian, black, 5, 0, 0, 0);");

              // Evento para capturar el clic en la imagen
              imageView.setOnMouseClicked(event -> {
                try {
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ch/makery/address/view/MostrarJuegoBiblioteca.fxml"));
                  Parent root = loader.load();

                  // Obtener el controlador y pasarle el nombre del juego
                  MostrarJuegoBibliotecaController controller = loader.getController();
                  controller.setNombreJuego(nombreJuego);

                  Scene scene = new Scene(root);
                  Stage stage = new Stage();
                  stage.setScene(scene);
                  stage.setTitle("Detalles del Juego");
                  stage.show();

                  // Cerrar la ventana actual
                  Stage currentStage = (Stage) tilePaneJuegos.getScene().getWindow();
                  currentStage.close();
              } catch (Exception e) {
                  e.printStackTrace();
              }
              });

              tilePaneJuegos.getChildren().add(imageView);
          } catch (Exception e) {
              System.err.println("Error al cargar la imagen desde la URL: " + urls.get(i));
              e.printStackTrace();
          }
      }
  }
    
    private void mostrarOpcionSeleccionada() {
      String opcionSeleccionada = comboBoxOrden.getValue();
      System.out.println("Opción seleccionada: " + opcionSeleccionada);
    }
    
    private void cambiarFiltroJuegos() {
      String opcionSeleccionada = comboBoxFiltro.getValue();
      System.out.println("Opción seleccionada: " + opcionSeleccionada);
    }
}
