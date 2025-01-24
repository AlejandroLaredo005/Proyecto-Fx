package api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
  private static final String BASE_URL = "https://api.rawg.io/api";
  private final String apiKey;
  private final HttpClient httpClient;

  public ApiClient(String apiKey) {
      this.apiKey = apiKey;
      this.httpClient = HttpClient.newHttpClient();
  }

  public String fetch(String endpoint, String queryParams) throws IOException, InterruptedException {
      // Construye la URL base con la clave API
      String url = String.format("%s/%s?key=%s", BASE_URL, endpoint, apiKey);

      // Agrega parámetros adicionales si existen
      if (queryParams != null && !queryParams.isEmpty()) {
          url += "&" + queryParams;
      }

      // Debugging: imprime la URL generada
      System.out.println("Request URL: " + url);

      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(url))
              .GET()
              .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
          throw new IOException("Error fetching data: " + response.statusCode());
      }

      return response.body();
  }
}

