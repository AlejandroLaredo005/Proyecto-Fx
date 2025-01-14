package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class InicioController {

    // Elementos con fx:id
    @FXML
    private TextField txtBuscador;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    // Métodos para manejar eventos
    @FXML
    private void buscar(MouseEvent event) {
        String textoBuscado = txtBuscador.getText();
        System.out.println("Buscando: " + textoBuscado);
        // Implementar lógica para buscar juegos
    }

    @FXML
    private void juegosMes(MouseEvent event) {
        System.out.println("Mostrando juegos que han salido este mes");
        // Implementar lógica para mostrar juegos del mes
    }

    @FXML
    private void juegosCritica(MouseEvent event) {
        System.out.println("Mostrando juegos alabados por la crítica");
        // Implementar lógica para mostrar juegos alabados por la crítica
    }

    @FXML
    private void juegosYear(MouseEvent event) {
        System.out.println("Mostrando juegos que han salido este año");
        // Implementar lógica para mostrar juegos del año
    }

    @FXML
    private void flechaIzquierdaPulsada(MouseEvent event) {
        System.out.println("Desplazando a la izquierda");
        // Implementar lógica para desplazar imágenes a la izquierda
    }

    @FXML
    private void flechaDerechaPulsada(MouseEvent event) {
        System.out.println("Desplazando a la derecha");
        // Implementar lógica para desplazar imágenes a la derecha
    }
}
