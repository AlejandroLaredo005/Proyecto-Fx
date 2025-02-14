package api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import models.Juegos;

public class ApiClient {
    private static final String BASE_URL = "https://api.rawg.io/api";
    private final String apiKey;
    private final HttpClient httpClient;

    public ApiClient(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Realiza una solicitud HTTP a la API de RAWG.
     *
     * @param endpoint    Endpoint a consultar (ejemplo: "games")
     * @param queryParams Parámetros adicionales (ejemplo: "search=mario")
     * @return Respuesta en formato JSON
     * @throws IOException          Si hay un error en la solicitud
     * @throws InterruptedException Si la solicitud es interrumpida
     */
    public String fetch(String endpoint, String queryParams) throws IOException, InterruptedException {
        String url = String.format("%s/%s?key=%s", BASE_URL, endpoint, apiKey);
        if (queryParams != null && !queryParams.isEmpty()) {
            url += "&" + queryParams;
        }
        System.out.println("🔎 Request URL: " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("❌ Error en la solicitud: " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Busca juegos en la API RAWG en base a una consulta.
     *
     * @param consulta Texto de búsqueda (nombre del juego)
     * @return Lista de juegos encontrados
     */
    public List<Juegos> buscarJuegos(String consulta) {
        List<Juegos> juegos = new ArrayList<>();
        try {
            String consultaCodificada = URLEncoder.encode(consulta, "UTF-8");
            String jsonResponse = fetch("games", "search=" + consultaCodificada);
            juegos = parsearJuegosDesdeJson(jsonResponse);
        } catch (UnsupportedEncodingException e) {
            System.err.println("❌ Error al codificar la consulta: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            System.err.println("⚠️ Error al obtener los juegos: " + e.getMessage());
        }
        return juegos;
    }

    /**
     * Parsea la respuesta JSON de la API y extrae los juegos.
     *
     * @param jsonResponse Respuesta en formato JSON
     * @return Lista de juegos parseados
     */
    private List<Juegos> parsearJuegosDesdeJson(String jsonResponse) {
      List<Juegos> juegos = new ArrayList<>();
      Gson gson = new Gson();

      try {
          JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
          if (!jsonObject.has("results") || !jsonObject.get("results").isJsonArray()) {
              System.err.println("⚠️ Error: No se encontró 'results' en la respuesta o no es un array.");
              return juegos;
          }
          JsonArray results = jsonObject.getAsJsonArray("results");
          for (JsonElement element : results) {
              if (element.isJsonObject()) {
                  JsonObject game = element.getAsJsonObject();
                  String nombre = (game.has("name") && !game.get("name").isJsonNull())
                          ? game.get("name").getAsString() : "Desconocido";
                  String metacritic = (game.has("metacritic") && !game.get("metacritic").isJsonNull())
                          ? game.get("metacritic").getAsString() : null;
                  String imagenUrl = (game.has("background_image") && !game.get("background_image").isJsonNull())
                          ? game.get("background_image").getAsString() : null;

               // Código relevante en ApiClient para obtener géneros
                  String generos = "";
                  if (game.has("genres") && game.get("genres").isJsonArray()) {
                      JsonArray genresArray = game.getAsJsonArray("genres");
                      List<String> generosList = new ArrayList<>();
                      for (JsonElement genreElement : genresArray) {
                          if (genreElement.isJsonObject()) {
                              JsonObject genreObj = genreElement.getAsJsonObject();
                              if (genreObj.has("name") && !genreObj.get("name").isJsonNull()) {
                                  generosList.add(genreObj.get("name").getAsString());
                              }
                          }
                      }
                      generos = String.join(",", generosList); // Unir géneros por comas
                  }


                  // Extraer tags
                  String tags = "";
                  if (game.has("tags") && game.get("tags").isJsonArray()) {
                      JsonArray tagsArray = game.getAsJsonArray("tags");
                      List<String> tagsList = new ArrayList<>();
                      for (JsonElement tagElement : tagsArray) {
                          if (tagElement.isJsonObject()) {
                              JsonObject tagObj = tagElement.getAsJsonObject();
                              if (tagObj.has("slug") && !tagObj.get("slug").isJsonNull()) {
                                  tagsList.add(tagObj.get("slug").getAsString());
                              }
                          }
                      }
                      tags = String.join(",", tagsList); // Unir tags por comas
                  }

                  Juegos nuevoJuego = new Juegos(nombre, metacritic, "", imagenUrl, tags, generos);
                  juegos.add(nuevoJuego);
              }
          }
      } catch (JsonSyntaxException e) {
          System.err.println("⚠️ Error al parsear JSON: " + e.getMessage());
      }
      return juegos;
  }
    

    public String obtenerDescripcionJuego(String id) {
      try {
        String jsonResponse = fetch("games/" + id, null);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        if (jsonObject.has("description") && !jsonObject.get("description").isJsonNull()) {
            String descripcion = jsonObject.get("description").getAsString();

            // 1. Limpiar etiquetas HTML con Jsoup
            String textoLimpio = Jsoup.parse(descripcion).text();

            // 2. Extraer solo la parte en inglés
            String textoEnIngles = extraerTextoEnIngles(textoLimpio);

            return textoEnIngles.isEmpty() ? "Description not available in English." : textoEnIngles;
        }
    } catch (IOException | InterruptedException e) {
        System.err.println("⚠️ Error retrieving description: " + e.getMessage());
    }
    return "Description not available.";
  }

  private String extraerTextoEnIngles(String texto) {
    String[] frases = texto.split("[.!?] "); // Dividir por frases
    StringBuilder resultado = new StringBuilder();

    // Expresión regular para detectar frases en inglés con palabras clave
    Pattern patron = Pattern.compile("\\b(the|a|an|is|are|this|that|game|player|level|mission|story|mode|action|adventure|explore)\\b", Pattern.CASE_INSENSITIVE);

    for (String frase : frases) {
        Matcher matcher = patron.matcher(frase);
        if (matcher.find() && !contienePalabrasEspanol(frase)) { // Verifica que no tenga español
            resultado.append(frase).append(". ");
        }
    }
    
    return resultado.toString().trim();
  }

  // 🔍 Función auxiliar para detectar español y evitar frases mezcladas
  private boolean contienePalabrasEspanol(String texto) {
    String[] palabrasEspanol = {"el", "la", "los", "las", "de", "en", "con", "por", "para", "un", "una", "este", "esta", "es", "juego", "jugador", "historia", "aventura"};
    for (String palabra : palabrasEspanol) {
        if (texto.toLowerCase().contains(" " + palabra + " ")) {
            return true; // Si contiene español, descartamos la frase
        }
    }
    return false;
  }
}
