package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import models.Juegos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

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
        // Construye la URL con la clave API
        String url = String.format("%s/%s?key=%s", BASE_URL, endpoint, apiKey);

        // Agrega parámetros adicionales si existen
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
     * Busca juegos en la API RAWG con base en una consulta.
     * 
     * @param consulta Texto de búsqueda (nombre del juego)
     * @return Lista de juegos encontrados
     */
    public List<Juegos> buscarJuegos(String consulta) {
        List<Juegos> juegos = new ArrayList<>();

        try {
            String jsonResponse = fetch("games", "search=" + consulta);
            juegos = parsearJuegosDesdeJson(jsonResponse);
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

            for (int i = 0; i < results.size(); i++) {
                if (results.get(i).isJsonObject()) {
                    JsonObject game = results.get(i).getAsJsonObject();

                    String nombre = game.has("name") ? game.get("name").getAsString() : "Desconocido";
                    String metacritic = game.has("metacritic") && !game.get("metacritic").isJsonNull() ? game.get("metacritic").getAsString() : null;
                    String imagenUrl = game.has("background_image") && !game.get("background_image").isJsonNull() ? game.get("background_image").getAsString() : null;

                    Juegos nuevoJuego = new Juegos(nombre, metacritic, "", imagenUrl);
                    juegos.add(nuevoJuego);
                }
            }
        } catch (JsonSyntaxException e) {
            System.err.println("⚠️ Error al parsear JSON: " + e.getMessage());
        }

        return juegos;
    }
}
