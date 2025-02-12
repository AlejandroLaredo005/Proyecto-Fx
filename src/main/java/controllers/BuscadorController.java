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
        
        // Se realiza la búsqueda con el término (sin filtros de tags en la URL)
        final String consultaFinal = busqueda;
        
        new Thread(() -> {
            // Obtener juegos de la API
            List<Juegos> juegosEncontrados = apiClient.buscarJuegos(consultaFinal);
            
            // Preparar la lista de tags requeridos (sólo se añaden si el checkbox está marcado)
            List<String> requiredTags = new ArrayList<>();
            if (unJugador.isSelected()) requiredTags.add("singleplayer");
            if (indie.isSelected()) requiredTags.add("indie");
            if (multijugador.isSelected()) requiredTags.add("multiplayer");
            if (simulacion.isSelected()) requiredTags.add("simulation");
            if (aventura.isSelected()) requiredTags.add("adventure");
            if (accion.isSelected()) requiredTags.add("action");
            
            // Filtrar los juegos para conservar solo aquellos que tengan TODOS los tags requeridos
            List<Juegos> juegosFiltrados = new ArrayList<>();
            for (Juegos juego : juegosEncontrados) {
                boolean matches = true;
                // Se asume que el método getTags() devuelve una cadena con los tags separados por comas
                String tags = juego.getTags();
                if (!requiredTags.isEmpty()) {
                    if (tags == null || tags.isEmpty()) {
                        matches = false;
                    } else {
                        String[] tagsArray = tags.split(",");
                        for (String reqTag : requiredTags) {
                            boolean found = false;
                            for (String tag : tagsArray) {
                                if (tag.trim().equalsIgnoreCase(reqTag)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                matches = false;
                                break;
                            }
                        }
                    }
                }
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
