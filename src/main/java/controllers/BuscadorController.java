package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

public class BuscadorController {

    @FXML
    private TextField txtBuscador; // El campo de texto para realizar la búsqueda

    @FXML
    private ListView<String> listaResultados; // La lista de resultados de búsqueda

    @FXML
    private ImageView imgCancelar; // La imagen que, al hacer clic, cancela la búsqueda

    // Este método se ejecuta cuando se presiona una tecla en el TextField (para actualizar los juegos)
    @FXML
    private void actualizarJuegos() {
        // Lógica de búsqueda en función del texto ingresado
        String busqueda = txtBuscador.getText().toLowerCase();
        // Aquí podrías llamar a un método para buscar los juegos que coinciden con la búsqueda
        // Por ejemplo, podría ser una lista de juegos, simulada aquí como un arreglo
        String[] juegosDisponibles = {"Juego A", "Juego B", "Juego C", "Juego D"};

        // Filtrar los juegos que coinciden con la búsqueda
        listaResultados.getItems().clear();
        for (String juego : juegosDisponibles) {
            if (juego.toLowerCase().contains(busqueda)) {
                listaResultados.getItems().add(juego);
            }
        }
    }

    // Este método se ejecuta cuando se hace clic en la imagen de cancelar
    @FXML
    private void cancelar(ActionEvent event) {
        // Limpiar el campo de texto y los resultados de la lista
        txtBuscador.clear();
        listaResultados.getItems().clear();
    }
}
