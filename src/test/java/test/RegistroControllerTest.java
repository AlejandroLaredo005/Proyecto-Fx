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
class RegistroControllerTest {

  @Start
  public void start(Stage stage) {
    try {
      // Carga el archivo FXML para la vista de Registro
      Parent mainNode = FXMLLoader.load(getClass().getResource("/ch/makery/address/view/Registro.fxml"));
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
    // Test que verifica que los campos y botones en la pantalla de registro son visibles
    FxRobot robot = new FxRobot();

    // Espera para asegurar que la interfaz esté cargada
    robot.sleep(2000);
    // Verifica que los componentes sean visibles
    FxAssert.verifyThat("#txtNombre", NodeMatchers.isVisible());
    FxAssert.verifyThat("#txtApellidos", NodeMatchers.isVisible());
    FxAssert.verifyThat("#txtPassword", NodeMatchers.isVisible());
    FxAssert.verifyThat("#txtPassword2", NodeMatchers.isVisible());
    FxAssert.verifyThat("#txtEmail", NodeMatchers.isVisible());
    FxAssert.verifyThat("#txtUsuario", NodeMatchers.isVisible());
    FxAssert.verifyThat("#inicioBoton", NodeMatchers.isVisible());
    FxAssert.verifyThat("#botonCrearCuenta", NodeMatchers.isVisible());
    // Espera un poco más para asegurar la visibilidad
    robot.sleep(2000);
  }

  @Test
  void testCrearCuentaExitosa() throws TimeoutException {
    // Test que verifica el inicio de sesión exitoso con credenciales válidas
    FxRobot robot = new FxRobot();

    // Escribe las credenciales válidas
    robot.clickOn("#txtNombre").write("Manol");
    robot.clickOn("#txtPassword").write("mipassword123");
    robot.clickOn("#txtPassword2").write("mipassword123");
    robot.clickOn("#txtApellidos").write("Lopez");
    robot.clickOn("#txtUsuario").write("Manol");
    robot.clickOn("#txtEmail").write("Manol@gmail.com");

    // Espera a que el botón de inicio de sesión esté habilitado
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      return robot.lookup("#botonCrearCuenta").query() != null;
    });

    // Hace clic en el botón de inicio de sesión
    robot.clickOn("#botonCrearCuenta");
    
    // Espera para que la nueva vista cargue
    robot.sleep(1000);
    
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
      assertEquals("Usuario creado exitosamente", alertText);
    }
  }
  
  @Test
  void testPasswordDistintas() throws TimeoutException {
    // Test que verifica el inicio de sesión exitoso con credenciales válidas
    FxRobot robot = new FxRobot();

    // Escribe las credenciales válidas
    robot.clickOn("#txtNombre").write("Manol");
    robot.clickOn("#txtPassword").write("mipassword123");
    robot.clickOn("#txtPassword2").write("mipassword12");
    robot.clickOn("#txtApellidos").write("Lopez");
    robot.clickOn("#txtUsuario").write("Manol");
    robot.clickOn("#txtEmail").write("Manol@gmail.com");

    // Espera a que el botón de inicio de sesión esté habilitado
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      return robot.lookup("#botonCrearCuenta").query() != null;
    });

    // Hace clic en el botón de inicio de sesión
    robot.clickOn("#botonCrearCuenta");
    
    // Espera para que la nueva vista cargue
    robot.sleep(1000);
    
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
      assertEquals("Las contraseñas no coinciden.", alertText);
    }
  }
  
  @Test
  void testUsuarioExistente() throws TimeoutException {
    // Test que verifica el inicio de sesión exitoso con credenciales válidas
    FxRobot robot = new FxRobot();

    // Escribe las credenciales válidas
    robot.clickOn("#txtNombre").write("Manol");
    robot.clickOn("#txtPassword").write("mipassword123");
    robot.clickOn("#txtPassword2").write("mipassword12");
    robot.clickOn("#txtApellidos").write("Lopez");
    robot.clickOn("#txtUsuario").write("Manol");
    robot.clickOn("#txtEmail").write("Manol@gmail.com");

    // Espera a que el botón de inicio de sesión esté habilitado
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      return robot.lookup("#botonCrearCuenta").query() != null;
    });

    // Hace clic en el botón de inicio de sesión
    robot.clickOn("#botonCrearCuenta");
    
    // Espera para que la nueva vista cargue
    robot.sleep(1000);
    
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
      assertEquals("El nombre de usuario ya existe. Por favor, elige otro.", alertText);
    }
  }
  
  @Test
  void testCorreoExistente() throws TimeoutException {
    // Test que verifica el inicio de sesión exitoso con credenciales válidas
    FxRobot robot = new FxRobot();

    // Escribe las credenciales válidas
    robot.clickOn("#txtNombre").write("Manol");
    robot.clickOn("#txtPassword").write("mipassword123");
    robot.clickOn("#txtPassword2").write("mipassword12");
    robot.clickOn("#txtApellidos").write("Lopez");
    robot.clickOn("#txtUsuario").write("Manol");
    robot.clickOn("#txtEmail").write("Manol@gmail.com");

    // Espera a que el botón de inicio de sesión esté habilitado
    WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () -> {
      return robot.lookup("#botonCrearCuenta").query() != null;
    });

    // Hace clic en el botón de inicio de sesión
    robot.clickOn("#botonCrearCuenta");
    
    // Espera para que la nueva vista cargue
    robot.sleep(1000);
    
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
      assertEquals("El correo electrónico ya está registrado. Por favor, utiliza otro correo.", alertText);
    }
  }

  @Test
  void testCamposVacios() throws TimeoutException {
    // Test que verifica que se muestre una alerta cuando se dejan los campos vacíos
    FxRobot robot = new FxRobot();

    // Deja los campos vacíos y hace clic en el botón de inicio de sesión
    robot.clickOn("#botonCrearCuenta");

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
      assertEquals("Todos los campos son obligatorios.", alertText);
    }
  }
}