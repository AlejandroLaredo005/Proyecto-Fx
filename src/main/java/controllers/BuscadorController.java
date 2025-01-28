package controllers;

import java.io.IOException;
import java.util.List;

import api.ApiClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Juegos;

public class BuscadorController {
    private static final String API_KEY = "TU_API_KEY";  
    private final ApiClient apiClient = new ApiClient(API_KEY);  

    @FXML
    private TextField txtBuscador;

    @FXML
    private ListView<Juegos> listaResultados;

    @FXML
    private ImageView imgCancelar;

    @FXML
    private void initialize() {
        listaResultados.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Juegos juego, boolean empty) {
                super.updateItem(juego, empty);
                if (empty || juego == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(juego.getNombreJuego() + " - Metacritic: " + juego.getPuntuacionMetacritic());
                    if (juego.getImagenUrl() != null) {
                        imageView.setImage(new Image(juego.getImagenUrl(), 50, 50, true, true));
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }
    @FXML
    private void cancelar(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Inicio.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) txtBuscador.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio - Hysinc Games");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error volviendo a la pantalla de inicio: " + e.getMessage());
        }
    }


    @FXML
    private void actualizarJuegos() {
        String busqueda = txtBuscador.getText().trim();
        if (busqueda.isEmpty()) {
            listaResultados.getItems().clear();
            return;
        }

        new Thread(() -> {
            List<Juegos> juegos = apiClient.buscarJuegos(busqueda);

            Platform.runLater(() -> {
                listaResultados.getItems().clear();
                listaResultados.getItems().addAll(juegos);
            });
        }).start();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        txtBuscador.clear();
        listaResultados.getItems().clear();
    }
}
