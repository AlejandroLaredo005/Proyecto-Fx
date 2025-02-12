package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    private TextField txtBuscador; // Campo de texto para búsqueda

    @FXML
    private ListView<Juegos> listaResultados; // Lista de resultados

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
        // Reemplaza con tu clave API real de RAWG
        this.apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
    }

    @FXML
    public void initialize() {
        // Configura cómo se muestran los juegos en el ListView
        listaResultados.setCellFactory(listView -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Juegos juego, boolean empty) {
                super.updateItem(juego, empty);
                if (empty || juego == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(juego.getNombreJuego() + " (Metacritic: " +
                           (juego.getPuntuacionMetacritic() != null ? juego.getPuntuacionMetacritic() : "N/A") + ")");
                    if (juego.getImagenUrl() != null) {
                        Image image = new Image(juego.getImagenUrl(), 50, 50, true, true);
                        imageView.setImage(image);
                    } else {
                        imageView.setImage(null);
                    }
                    setGraphic(imageView);
                }
            }
        });
        
        // Evento para clic en un juego de la lista
        listaResultados.setOnMouseClicked(event -> {
            Juegos juegoSeleccionado = listaResultados.getSelectionModel().getSelectedItem();
            if (juegoSeleccionado != null) {
                System.out.println("Juego seleccionado: " + juegoSeleccionado.getNombreJuego());
                abrirMostrarJuegos(juegoSeleccionado.getNombreJuego());
            }
        });
    }

    /**
     * Realiza la búsqueda y aplica el filtrado: se busca por el término y
     * se conservan solo los juegos que contengan TODOS los tags marcados.
     */
    @FXML
    private void actualizarJuegos() {
        String busqueda = txtBuscador.getText().trim();
        if (busqueda.isEmpty()) {
            listaResultados.getItems().clear();
            return;
        }

        try {
            busqueda = URLEncoder.encode(busqueda, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String consultaFinal = busqueda;

        new Thread(() -> {
            List<Juegos> juegosEncontrados = apiClient.buscarJuegos(consultaFinal);

            // Tags marcados en la interfaz (solo se mantienen los tags específicos)
            List<String> requiredTags = new ArrayList<>();
            if (unJugador.isSelected()) requiredTags.add("singleplayer");
            if (multijugador.isSelected()) requiredTags.add("multiplayer");
            // No se incluyen indie, simulación, aventura, acción en los tags

            // Géneros específicos
            List<String> requiredGenres = new ArrayList<>();
            if (aventura.isSelected()) requiredGenres.add("adventure");
            if (accion.isSelected()) requiredGenres.add("action");
            if (indie.isSelected()) requiredGenres.add("indie");
            if (simulacion.isSelected()) requiredGenres.add("simulation");

            // Filtrar los juegos por tags y géneros
            List<Juegos> juegosFiltrados = new ArrayList<>();
            for (Juegos juego : juegosEncontrados) {
                boolean matches = true;

                // Filtrado por tags
                String tags = juego.getTags(); // Asume que getTags devuelve una cadena separada por comas
                if (!requiredTags.isEmpty() && (tags == null || tags.isEmpty())) {
                    matches = false;
                } else {
                    for (String reqTag : requiredTags) {
                        if (!tags.toLowerCase().contains(reqTag.toLowerCase())) {
                            matches = false;
                            break;
                        }
                    }
                }

                // Filtrado por géneros
                List<String> generos = juego.getGeneros(); // Ahora obtenemos la lista de géneros
                if (!requiredGenres.isEmpty() && generos != null) {
                    for (String genre : requiredGenres) {
                        if (!generos.contains(genre)) {
                            matches = false;
                            break;
                        }
                    }
                }

                // Si pasa todos los filtros, agregarlo a la lista
                if (matches) {
                    juegosFiltrados.add(juego);
                }
            }

            Platform.runLater(() -> {
                listaResultados.getItems().clear();
                listaResultados.getItems().addAll(juegosFiltrados);
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
    private void unJugadorPulsado(MouseEvent event) { actualizarEstados(); }

    @FXML
    private void indiePulsado(MouseEvent event) { actualizarEstados(); }

    @FXML
    private void multijugadorPulsado(MouseEvent event) { actualizarEstados(); }

    @FXML
    private void simulacionPulsada(MouseEvent event) { actualizarEstados(); }

    @FXML
    private void aventuraPulsada(MouseEvent event) { actualizarEstados(); }

    @FXML
    private void accionPulsado(MouseEvent event) { actualizarEstados(); }

    /**
     * Método auxiliar para imprimir el estado de los checkboxes.
     */
    private void actualizarEstados() {
        System.out.println("Estado de CheckBoxes:");
        System.out.println("  Un Jugador: " + (unJugador.isSelected() ? "Marcado" : "No Marcado"));
        System.out.println("  Indie: " + (indie.isSelected() ? "Marcado" : "No Marcado"));
        System.out.println("  Multijugador: " + (multijugador.isSelected() ? "Marcado" : "No Marcado"));
        System.out.println("  Simulación: " + (simulacion.isSelected() ? "Marcado" : "No Marcado"));
        System.out.println("  Aventura: " + (aventura.isSelected() ? "Marcado" : "No Marcado"));
        System.out.println("  Acción: " + (accion.isSelected() ? "Marcado" : "No Marcado"));
    }
}
