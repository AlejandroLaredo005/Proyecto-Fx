package controllers;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

public class BibliotecaController {
  
  @FXML
  private TilePane tilePaneJuegos;
  
  @FXML
  public void initialize() {
      cargarImagenes();
  }

  private void cargarImagenes() {
    // Aqui haces todo lo de la api para meter las imagenes, en un principio aqui llamas a la base de datos ocn los juegos del usuario y los recoges en una lista,
    // Luego le pasas la lista al metodo agregarImagenes y ahi ya vas de una en una añadiendola con un for o algo
    
  }
  
  private void agregarImagenes(List<String> urls) {
    // Ve jugando con el tamaño y tal, en un principio haz que tenga 3 columnas, no 5 porque van a quedar apepinadas las fotos
    /*
    ImageView imageView = new ImageView(new Image(url));
    imageView.setFitWidth(150); // Tamaño fijo
    imageView.setFitHeight(150);
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);

    tilePaneJuegos.getChildren().add(imageView);
    */
}
}
