  package ch.makery.address;

import api.ApiClient;
import utils.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private SplitPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Inicializar la conexión a la base de datos al iniciar la aplicación
        HibernateUtil.getSessionFactory();
        System.out.println("Conexión a la base de datos establecida.");

        // Realizar una llamada a la API
        String apiKey = "8d18e821b4e6491d8a1096ba1a106001"; 
        ApiClient apiClient = new ApiClient(apiKey);

        try {
            String endpoint = "games";
            String queryParams = "page=1&page_size=5"; // Ejemplo de parámetros
            String response = apiClient.fetch(endpoint, queryParams);

            // Imprimir la respuesta de la API
            System.out.println("Respuesta de la API: " + response);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al realizar la solicitud a la API: " + e.getMessage());
        }

        // Inicializar el layout principal
        initInicioLayout();
    }

    @Override
    public void stop() {
        // Cerrar la conexión a la base de datos al finalizar la aplicación
        HibernateUtil.getSessionFactory().close();
        System.out.println("Conexión a la base de datos cerrada correctamente.");
    }

    public void initInicioLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/InicioSesion.fxml"));
            rootLayout = loader.load();

            // Mostrar la escena principal
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
