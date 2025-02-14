package test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

//Extiende la clase para usar la integración con TestFX en JUnit5
@ExtendWith(ApplicationExtension.class)
class InicioSesionControllerTest {

  @Start
  public void start(Stage stage) {
    try {
      // Carga el archivo FXML para la vista de Inicio Sesion
      Parent mainNode = FXMLLoader.load(getClass().getResource("/ch/makery/address/view/InicioSesion.fxml"));
      stage.setScene(new Scene(mainNode));
      // Muestra la escena
      stage.show();
      // Trae la ventana al frente
      stage.toFront();
    } catch (Exception e) {
      e.printStackTrace();
      // Lanza error si no se puede cargar el archivo FXML
      throw new RuntimeException("Error al cargar el archivo FXML");
    }
  }

  @BeforeEach
  public void setUp() throws Exception {
  }

  @AfterEach
  public void tearDown() throws Exception {
    // Se ejecuta después de cada prueba, se liberan recursos, en este caso se oculta la ventana
    FxToolkit.hideStage();
  }

  @Test
  void testCamposVisibles() {
    // Test que verifica que los campos y botones en la pantalla de inicio sesion son visibles
    FxRobot robot = new FxRobot();

    // Espera para asegurar que la interfaz esté cargada
    robot.sleep(2000);
    // Verifica que los componentes sean visibles
    FxAssert.verifyThat("#txtUsuario", NodeMatchers.isVisible());
    FxAssert.verifyThat("#txtPassword", NodeMatchers.isVisible());
    FxAssert.verifyThat("#inicioBoton", NodeMatchers.isVisible());
    FxAssert.verifyThat("#registrarBoton", NodeMatchers.isVisible());
    // Espera un poco más para asegurar la visibilidad
    robot.sleep(2000);
  }

  @Test
  void testInicioSesionExitoso() throws TimeoutException {
    // Test que verifica el inicio de sesión exitoso con credenciales válidas
    FxRobot robot = new FxRobot();

    // Escribe las credenciales válidas
    robot.clickOn("#txtUsuario").write("Manolo");
    robot.clickOn("#txtPassword").write("mipassword123");

    // Espera a que el botón de inicio de sesión esté habilitado
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      return robot.lookup("#inicioBoton").query() != null;
    });

    // Hace clic en el botón de inicio de sesión
    robot.clickOn("#inicioBoton");
    
    // Espera para que la nueva vista cargue
    robot.sleep(1000);
    // Verifica que el componente principal esté visible
    FxAssert.verifyThat("#txtBuscador", NodeMatchers.isVisible());
  }

  @Test
  void testInicioSesionFallido() throws TimeoutException {
    // Test que verifica el inicio de sesión fallido con credenciales inválidas
    FxRobot robot = new FxRobot();

    // Escribe las credenciales incorrectas
    robot.clickOn("#txtUsuario").write("usuarioNoExistente");
    robot.clickOn("#txtPassword").write("contrasenaInvalida");

    // Hace clic en el botón de inicio de sesión
    robot.clickOn("#inicioBoton");

    // Espera a que aparezca la alerta de error
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
      return dialogPane != null;
    });

    // Verifica que el texto de la alerta sea el esperado
    DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
    if (dialogPane != null) {
      String alertText = dialogPane.getContentText();
      System.out.println("Texto de la alerta: '" + alertText + "'");

      // Verifica que el texto de la alerta sea el correcto
      assertEquals("El usuario ingresado no existe", alertText);
    }
  }

  @Test
  void testCamposVacios() throws TimeoutException {
    // Test que verifica que se muestre una alerta cuando se dejan los campos vacíos
    FxRobot robot = new FxRobot();

    // Deja los campos vacíos y hace clic en el botón de inicio de sesión
    robot.clickOn("#inicioBoton");

    // Espera para que se muestre la alerta
    robot.sleep(2000); 

    // Espera a que aparezca la alerta
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
      return dialogPane != null;
    });

    // Verifica que el texto de la alerta sea el esperado
    DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
    if (dialogPane != null) {
      String alertText = dialogPane.getContentText();
      System.out.println("Texto de la alerta: '" + alertText + "'");

      // Verifica que el texto de la alerta sea correcto
      assertEquals("Por favor, complete todos los campos.", alertText);
    }
  }

  @Test
  void testBotonRegistro() {
    // Test que verifica la navegación al formulario de registro
    FxRobot robot = new FxRobot();

    // Verifica que el enlace al registro esté visible
    FxAssert.verifyThat("#registrarBoton", NodeMatchers.isVisible());

    // Hace clic en el enlace de registro
    robot.clickOn("#registrarBoton");

    // Espera un poco para verificar que la redirección se haya realizado
    // correctamente
    robot.sleep(2000);
    // Verifica que el txt de nombre esté visible
    FxAssert.verifyThat("#txtNombre", NodeMatchers.isVisible());
    robot.sleep(2000); 
  }
}