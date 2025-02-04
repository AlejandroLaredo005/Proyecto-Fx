package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MostrarJuegoController {
    
    @FXML
    private ImageView logo;
    
    @FXML
    private Label tituloJuego;
    
    @FXML
    private Label descripcionJuego;
    
    @FXML
    private Pane panelMetacritics;
    
    @FXML
    private Label puntuacionMetacritics;
    
    @FXML
    private ImageView imagenesJuego;
    
    @FXML
    private ListView<String> listaJuegosRelacionados;
    
    @FXML
    private void pasarDerecha() {
        // Implementar lógica para avanzar en las imágenes
    }
    
    @FXML
    private void pasarIzquierda() {
        // Implementar lógica para retroceder en las imágenes
    }
    
    @FXML
    private void ponerEnBiblioteca() {
        // Implementar lógica para agregar el juego a la biblioteca
    }
}
