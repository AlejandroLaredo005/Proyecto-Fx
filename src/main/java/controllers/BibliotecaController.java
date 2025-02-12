package controllers;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Método de inicialización de JavaFX. Aquí se configura el TilePane
     * y se carga la lista de imágenes.
     */
    @FXML
    public void initialize() {
        // Configuramos el TilePane para que tenga 3 columnas, de modo que las imágenes no queden demasiado juntas.
        tilePaneJuegos.setPrefColumns(3);
        cargarImagenes();
    }

    /**
     * Consulta en la base de datos los juegos del usuario (o de la biblioteca en general)
     * y extrae la URL de la imagen de cada juego.
     */
    private void cargarImagenes() {
        // Obtenemos la lista de entradas de la biblioteca desde la base de datos.
        List<Biblioteca> bibliotecas = bibliotecaDao.findAll();
        
        if (bibliotecas == null || bibliotecas.isEmpty()) {
            System.out.println("No se encontraron juegos en la biblioteca.");
            return;
        }
        
        // Creamos una lista de URLs de imagen extrayéndolas de cada juego asociado.
        List<String> urls = new ArrayList<>();
        for (Biblioteca biblioteca : bibliotecas) {
            Juegos juego = biblioteca.getJuego();
            if (juego != null && juego.getImagenUrl() != null && !juego.getImagenUrl().trim().isEmpty()) {
                urls.add(juego.getImagenUrl());
            }
        }
        
        // Se pasan las URLs a agregarImagenes para mostrarlas en el TilePane.
        agregarImagenes(urls);
    }

    /**
     * Recorre la lista de URLs y, por cada una, crea un ImageView configurado
     * con tamaño fijo y lo añade al TilePane.
     *
     * @param urls Lista de URLs de imágenes a mostrar.
     */
    private void agregarImagenes(List<String> urls) {
        for (String url : urls) {
            try {
                // Se crea la imagen a partir de la URL.
                // Los parámetros 150 (ancho y alto) se usan para optimizar la carga; sin embargo, se
                // vuelven a establecer en el ImageView para asegurar la configuración.
                Image image = new Image(url, 150, 150, true, true);
                ImageView imageView = new ImageView(image);
                
                // Configuración de la imagen según lo indicado en los comentarios.
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                
                // Se añade el ImageView al TilePane.
                tilePaneJuegos.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen desde la URL: " + url);
                e.printStackTrace();
            }
        }
    }
}
