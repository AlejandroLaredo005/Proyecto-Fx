package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import api.ApiClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import models.Biblioteca;
import models.Juegos;
import models.Usuarios;
import utils.SesionUsuario;

public class MostrarJuegoBibliotecaController {
  
  @FXML
  private ImageView logo;  // Se usará para mostrar la imagen de portada (cover)
  
  @FXML
  private Label tituloJuego;
  
  @FXML
  private Label descripcionJuego;
  
  @FXML
  private Pane panelMetacritics;
  
  @FXML
  private Label puntuacionMetacritics;
  
  // ImageView donde se mostrará el carrusel de capturas (screenshots)
  @FXML
  private ImageView imagenesJuego;
  
  @FXML
  private RadioButton jugado;
  
  @FXML
  private TextArea comentario;
  
  private String logoUrl;
  
  private final ApiClient apiClient = new ApiClient("8d18e821b4e6491d8a1096ba1a106001");
  
  // Índice para gestionar la navegación en el carrusel
  private int imagenIndex = 0;  
  // Lista de URLs que contendrá únicamente las capturas de pantalla (screenshots)
  private List<String> imagenUrls = new ArrayList<>();
  
  @FXML
  private void pasarDerecha() {
      // Avanzar al siguiente screenshot
      imagenIndex++;
      if (imagenIndex >= imagenUrls.size()) {
          imagenIndex = 0;  // Si es el último, volvemos al inicio
      }
      actualizarImagen();
  }
  
  @FXML
  private void pasarIzquierda() {
      // Retroceder al screenshot anterior
      imagenIndex--;
      if (imagenIndex < 0) {
          imagenIndex = imagenUrls.size() - 1;  // Si es el primero, vamos al último
      }
      actualizarImagen();
  }
  
  @FXML
  private void ponerEnBiblioteca() {
      // Obtener el usuario logueado
      Usuarios usuarioLogueado = SesionUsuario.getUsuarioActual();
      String nombreJuego = tituloJuego.getText();
      
      // Usamos el DAO de Juegos para buscar si ya existe un juego con ese nombre.
      dao.JuegosDao juegosDao = new dao.impl.JuegosDaoImpl();
      Juegos currentJuego = juegosDao.findByNombre(nombreJuego).orElse(null);
      
      if (currentJuego == null) {
          // Si no existe, se crea y se persiste
          currentJuego = new Juegos(
              nombreJuego,
              puntuacionMetacritics.getText(),
              descripcionJuego.getText(),
              logoUrl,null,null
          );
          juegosDao.save(currentJuego);
      }
      
      // Usamos el DAO de Biblioteca para verificar si este juego ya está en la biblioteca del usuario
      dao.BibliotecaDao bibliotecaDao = new dao.impl.BibliotecaDaoImpl();
      // Buscar la entrada en la biblioteca para este usuario y juego
      java.util.Optional<Biblioteca> optionalEntrada = bibliotecaDao.findByUsuarioAndJuego(usuarioLogueado, currentJuego);
      
      if (optionalEntrada.isPresent()) {
          // Si ya existe, lo eliminamos de la biblioteca
          Biblioteca entradaExistente = optionalEntrada.get();
          // Usamos el método delete pasándole el id (convertido a String)
          bibliotecaDao.delete(entradaExistente.getIdBiblioteca().toString());
          System.out.println("Juego removido de la biblioteca exitosamente.");
          return;  // Salimos del método
      }
      
      // Si no existe, se crea la nueva entrada en la biblioteca
      Biblioteca nuevaEntrada = new Biblioteca(usuarioLogueado, currentJuego, "", "Quiero Jugarlo");
      bibliotecaDao.save(nuevaEntrada);
      
      System.out.println("Juego agregado a la biblioteca exitosamente.");
  }
  
  @FXML
  private void cambiarComentario() {
      // Obtener el usuario logueado
      Usuarios usuarioLogueado = SesionUsuario.getUsuarioActual();
      String nombreJuego = tituloJuego.getText();

      // Validar que haya un comentario para actualizar
      String nuevoComentario = comentario.getText().trim();

      // Buscar el juego en la base de datos
      dao.JuegosDao juegosDao = new dao.impl.JuegosDaoImpl();
      Juegos currentJuego = juegosDao.findByNombre(nombreJuego).orElse(null);

      if (currentJuego == null) {
          System.out.println("Error: No se encontró el juego en la base de datos.");
          return;
      }

      // Buscar la entrada en la biblioteca para este usuario y juego
      dao.BibliotecaDao bibliotecaDao = new dao.impl.BibliotecaDaoImpl();
      java.util.Optional<Biblioteca> optionalEntrada = bibliotecaDao.findByUsuarioAndJuego(usuarioLogueado, currentJuego);

      if (optionalEntrada.isPresent()) {
          Biblioteca entradaBiblioteca = optionalEntrada.get();
          
          // Actualizar el comentario y guardar en la BD
          entradaBiblioteca.setComentarios(nuevoComentario);
          bibliotecaDao.update(entradaBiblioteca);

          System.out.println("Comentario actualizado correctamente.");
      } else {
          System.out.println("Error: No se encontró el juego en la biblioteca del usuario.");
      }
  }

  
  @FXML
  private void cambiarEstado() {
      // Obtener el usuario logueado
      Usuarios usuarioLogueado = SesionUsuario.getUsuarioActual();
      String nombreJuego = tituloJuego.getText();

      // Buscar el juego en la base de datos
      dao.JuegosDao juegosDao = new dao.impl.JuegosDaoImpl();
      Juegos currentJuego = juegosDao.findByNombre(nombreJuego).orElse(null);

      if (currentJuego == null) {
          System.out.println("Error: No se encontró el juego en la base de datos.");
          return;
      }

      // Buscar la entrada en la biblioteca del usuario para este juego
      dao.BibliotecaDao bibliotecaDao = new dao.impl.BibliotecaDaoImpl();
      java.util.Optional<Biblioteca> optionalEntrada = bibliotecaDao.findByUsuarioAndJuego(usuarioLogueado, currentJuego);

      if (optionalEntrada.isPresent()) {
          Biblioteca entradaBiblioteca = optionalEntrada.get();
          
          // Actualizar el estado del juego según el estado del RadioButton
          String nuevoEstado = jugado.isSelected() ? "Jugado" : "Quiero Jugarlo";
          entradaBiblioteca.setEstado(nuevoEstado);
          
          // Actualizar la entrada en la base de datos
          bibliotecaDao.update(entradaBiblioteca);

          System.out.println("Estado del juego actualizado correctamente a: " + nuevoEstado);
      } else {
          System.out.println("Error: No se encontró el juego en la biblioteca del usuario.");
      }
  }

  
  private void actualizarImagen() {
      // Actualizar el ImageView con la imagen del carrusel según el índice actual
      if (!imagenUrls.isEmpty() && imagenIndex >= 0 && imagenIndex < imagenUrls.size()) {
          imagenesJuego.setImage(new Image(imagenUrls.get(imagenIndex)));
      }
  }
  
  public void setNombreJuego(String nombreJuego) {
    tituloJuego.setText(nombreJuego);

    try {
        // Codificar el nombre para evitar problemas con caracteres especiales
        String encodedGameName = URLEncoder.encode(nombreJuego, "UTF-8");
        
        // Solicitar a la API la información del juego (búsqueda)
        String response = apiClient.fetch("games", "search=" + encodedGameName + "&page_size=1");
        
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray results = jsonResponse.getJSONArray("results");

        if (results.length() > 0) {
            // Extraer la información del primer resultado
            JSONObject game = results.getJSONObject(0);

            // Obtener y asignar la imagen de portada (para el logo)
            // Se usa cover_image o, de no existir, background_image
            logoUrl = game.optString("cover_image", game.optString("background_image"));
            if (!logoUrl.isEmpty()) {
                logo.setImage(new Image(logoUrl));
            }

            // Asignar descripción y puntuación
            String descripcion = game.optString("short_description", "Descripción no disponible.");
            descripcionJuego.setText(descripcion);
            
            String metacriticsScore = game.optString("metacritic", "No P");
            puntuacionMetacritics.setText("Metacritic: " + metacriticsScore);

            // Construir el carrusel con las capturas de pantalla (screenshots)
            // NO se añade la imagen de fondo al carrusel
            imagenUrls.clear();  // Limpiar la lista antes de agregar nuevas capturas
            try {
                // Obtener el id del juego (convertido a String)
                String gameId = game.get("id").toString();
                // Realizar la petición para obtener las screenshots del juego
                String screenshotsResponse = apiClient.fetch("games/" + gameId + "/screenshots", "");
                JSONObject screenshotsJson = new JSONObject(screenshotsResponse);
                JSONArray screenshotsArray = screenshotsJson.optJSONArray("results");
                if (screenshotsArray != null && screenshotsArray.length() > 0) {
                    for (int i = 0; i < screenshotsArray.length(); i++) {
                        JSONObject screenshot = screenshotsArray.getJSONObject(i);
                        String screenshotUrl = screenshot.optString("image", "");
                        if (!screenshotUrl.isEmpty()) {
                            imagenUrls.add(screenshotUrl);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Si no hay capturas, se usa la imagen de fondo como respaldo (opcional)
            if (imagenUrls.isEmpty()) {
                String fallbackImage = game.optString("background_image", "");
                if (!fallbackImage.isEmpty()) {
                    imagenUrls.add(fallbackImage);
                }
            }

            // Reiniciar el índice del carrusel y mostrar la primera imagen (que serán las screenshots)
            imagenIndex = 0;
            actualizarImagen();

        } else {
            descripcionJuego.setText("Juego no encontrado.");
        }
        
        // Obtener usuario logueado
        Usuarios usuarioLogueado = SesionUsuario.getUsuarioActual();

        // Buscar el juego en la base de datos
        dao.JuegosDao juegosDao = new dao.impl.JuegosDaoImpl();
        Juegos currentJuego = juegosDao.findByNombre(nombreJuego).orElse(null);

        // Buscar en la biblioteca si el usuario ya tiene el juego guardado
        dao.BibliotecaDao bibliotecaDao = new dao.impl.BibliotecaDaoImpl();
        java.util.Optional<Biblioteca> optionalEntrada = bibliotecaDao.findByUsuarioAndJuego(usuarioLogueado, currentJuego);

        if (optionalEntrada.isPresent()) {
            Biblioteca entradaBiblioteca = optionalEntrada.get();
            String comentarioGuardado = entradaBiblioteca.getComentarios();
            
            // Si hay un comentario guardado, ponerlo en el TextArea
            if (comentarioGuardado != null && !comentarioGuardado.isEmpty()) {
                comentario.setText(comentarioGuardado);
            }
            
            // Comprobar el estado y marcar el RadioButton en consecuencia
            String estado = entradaBiblioteca.getEstado();
            if (estado != null && estado.equalsIgnoreCase("jugado")) {
                jugado.setSelected(true);
            } else {
                jugado.setSelected(false);
            }
        }
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        descripcionJuego.setText("Error al codificar el nombre del juego.");
    } catch (Exception e) {
        e.printStackTrace();
        descripcionJuego.setText("Error al cargar el juego.");
    }
}

}
