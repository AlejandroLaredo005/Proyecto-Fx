package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import api.ApiClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Juegos;

public class BuscadorController {

    @FXML
    private TextField txtBuscador; // El campo de texto para realizar la búsqueda

    @FXML
    private ListView<Juegos> listaResultados; // La lista de resultados de búsqueda
    
    @FXML
    private CheckBox unJugador;

    @FXML
    private CheckBox indie;

    @FXML
    private CheckBox multijugador;

    @FXML
    private CheckBox simulacion;

    @FXML
    private CheckBox aventura;

    @FXML
    private CheckBox accion;

    private ApiClient apiClient; // Cliente para conectarse a la API

    public BuscadorController() {
        // Inicializa el cliente con la clave API de RAWG (reemplaza con tu clave real)
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
        
     // Agregar un evento para detectar el clic en un juego de la lista
        listaResultados.setOnMouseClicked(event -> {
            Juegos juegoSeleccionado = listaResultados.getSelectionModel().getSelectedItem();
            if (juegoSeleccionado != null) {
                // Aquí puedes manejar lo que pasa cuando se hace clic en un juego
                System.out.println("Juego seleccionado: " + juegoSeleccionado.getNombreJuego());
                // Por ejemplo, podrías abrir una nueva ventana con los detalles del juego
                abrirMostrarJuegos(juegoSeleccionado.getNombreJuego());
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

        // Codificar la búsqueda para que funcione correctamente con espacios y caracteres especiales.
        try {
            busqueda = URLEncoder.encode(busqueda, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Creamos una variable final para usarla en el lambda
        final String busquedaFinal = busqueda;

        // Realiza la llamada a la API en un hilo secundario para evitar bloquear la interfaz
        new Thread(() -> {
            List<Juegos> juegosEncontrados = apiClient.buscarJuegos(busquedaFinal);

            // Actualiza la lista de resultados en el hilo de la interfaz
            Platform.runLater(() -> {
                listaResultados.getItems().clear();
                listaResultados.getItems().addAll(juegosEncontrados);
            });
        }).start();
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
    
    @FXML
    private void unJugadorPulsado(MouseEvent event) {
        actualizarEstados();
    }

    @FXML
    private void indiePulsado(MouseEvent event) {
        actualizarEstados();
    }

    @FXML
    private void multijugadorPulsado(MouseEvent event) {
        actualizarEstados();
    }

    @FXML
    private void simulacionPulsada(MouseEvent event) {
        actualizarEstados();
    }

    @FXML
    private void aventuraPulsada(MouseEvent event) {
        actualizarEstados();
    }

    @FXML
    private void accionPulsado(MouseEvent event) {
        actualizarEstados();
    }

    /**
     * Método auxiliar que revisa el estado de todos los CheckBox y realiza
     * la lógica deseada (por ejemplo, imprimir el estado en consola, actualizar
     * variables, etc.)
     */
    private void actualizarEstados() {
        String estadoUnJugador = unJugador.isSelected() ? "Marcado" : "No Marcado";
        String estadoIndie = indie.isSelected() ? "Marcado" : "No Marcado";
        String estadoMultijugador = multijugador.isSelected() ? "Marcado" : "No Marcado";
        String estadoSimulacion = simulacion.isSelected() ? "Marcado" : "No Marcado";
        String estadoAventura = aventura.isSelected() ? "Marcado" : "No Marcado";
        String estadoAccion = accion.isSelected() ? "Marcado" : "No Marcado";

        // Aquí puedes procesar la información como necesites
        System.out.println("Estado de CheckBoxes:");
        System.out.println("  Un Jugador: " + estadoUnJugador);
        System.out.println("  Indie: " + estadoIndie);
        System.out.println("  Multijugador: " + estadoMultijugador);
        System.out.println("  Simulación: " + estadoSimulacion);
        System.out.println("  Aventura: " + estadoAventura);
        System.out.println("  Acción: " + estadoAccion);
        
        // Si necesitas realizar lógica adicional (por ejemplo, habilitar/deshabilitar botones,
        // actualizar variables de filtros, etc.), puedes hacerlo aquí.
    }
}
