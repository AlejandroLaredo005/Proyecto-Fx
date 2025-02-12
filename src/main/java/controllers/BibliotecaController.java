package controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import api.ApiClient;
import dao.BibliotecaDao;
import dao.impl.BibliotecaDaoImpl;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import models.Biblioteca;
import models.Juegos;

public class BibliotecaController {

    @FXML
    private TilePane tilePaneJuegos;

    // Instancia del DAO para interactuar con la base de datos.
    private BibliotecaDao bibliotecaDao = new BibliotecaDaoImpl();

    // Instancia del cliente de la API con tu API key
    private final ApiClient apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");

    @FXML
    public void initialize() {
        cargarImagenes();
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
        
        for (Biblioteca biblioteca : bibliotecas) {
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
                        } else {
                            System.out.println("No se encontró imagen para el juego: " + nombreJuego);
                        }
                    } else {
                        System.out.println("No se encontró el juego en la API para: " + nombreJuego);
                    }
                }
            }
        }
        
        agregarImagenes(urls);
    }

    /**
     * Recorre la lista de URLs y, por cada una, crea un ImageView configurado
     * y lo añade al TilePane.
     */
    private void agregarImagenes(List<String> urls) {
      tilePaneJuegos.getChildren().clear(); // Limpia las imágenes previas

      for (String url : urls) {
          try {
              Image image = new Image(url, 250, 150, true, true); // Ajusta el tamaño aquí
              ImageView imageView = new ImageView(image);

              imageView.setFitWidth(250);  // Ajusta el ancho de cada imagen
              imageView.setFitHeight(150); // Ajusta la altura de cada imagen
              imageView.setPreserveRatio(true);
              imageView.setSmooth(true);
              imageView.setStyle("-fx-effect: dropshadow(gaussian, black, 5, 0, 0, 0);");

              tilePaneJuegos.getChildren().add(imageView);
          } catch (Exception e) {
              System.err.println("Error al cargar la imagen desde la URL: " + url);
              e.printStackTrace();
          }
      }
  }

}
