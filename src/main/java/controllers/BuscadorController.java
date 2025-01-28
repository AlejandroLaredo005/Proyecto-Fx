package controllers;

import api.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListCell;
import models.Juegos;

import java.util.List;

public class BuscadorController {

    @FXML
    private TextField txtBuscador; // El campo de texto para realizar la búsqueda

    @FXML
    private ListView<Juegos> listaResultados; // La lista de resultados de búsqueda

    private ApiClient apiClient; // Cliente para conectarse a la API

    public BuscadorController() {
        // Inicializa el cliente con la clave API de RAWG (reemplaza "TU_API_KEY" con tu clave real)
        this.apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
    }

    @FXML
    public void initialize() {
        // Configura cómo se mostrarán los juegos en el ListView
        listaResultados.setCellFactory(listView -> new ListCell<>() {
            private final ImageView imageView = new ImageView(); // Para mostrar la imagen del juego

            @Override
            protected void updateItem(Juegos juego, boolean empty) {
                super.updateItem(juego, empty);

                if (empty || juego == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Configura la celda con la imagen, nombre y puntuación
                    setText(juego.getNombreJuego() + " (Metacritic: " + 
                           (juego.getPuntuacionMetacritic() != null ? juego.getPuntuacionMetacritic() : "N/A") + ")");
                    
                    if (juego.getImagenUrl() != null) {
                        Image image = new Image(juego.getImagenUrl(), 50, 50, true, true);
                        imageView.setImage(image);
                    } else {
                        imageView.setImage(null); // Sin imagen si no hay URL
                    }
                    setGraphic(imageView);
                }
            }
        });
    }

    /**
     * Método para buscar juegos en la API RAWG en función del texto ingresado.
     */
    @FXML
    private void actualizarJuegos() {
        String busqueda = txtBuscador.getText().trim();

        if (busqueda.isEmpty()) {
            listaResultados.getItems().clear(); // Limpia la lista si no hay texto
            return;
        }

        // Realiza la llamada a la API en un hilo secundario para evitar bloquear la interfaz
        new Thread(() -> {
            List<Juegos> juegosEncontrados = apiClient.buscarJuegos(busqueda);

            // Actualiza la lista de resultados en el hilo de la interfaz
            Platform.runLater(() -> {
                listaResultados.getItems().clear();
                listaResultados.getItems().addAll(juegosEncontrados);
            });
        }).start();
    }
}
