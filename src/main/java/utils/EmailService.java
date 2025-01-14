package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final String destinatario; // Dirección de correo del destinatario
    private final String codigo;      // Código a enviar
    private final String remitente = "hhysinc@gmail.com"; // Correo remitente
    private final String claveApp = "bpoi bqhn ggdh amwm"; // Contraseña de aplicación del correo remitente

    // Constructor que recibe el correo del destinatario y el código
    public EmailService(String destinatario, String codigo) {
        this.destinatario = destinatario;
        this.codigo = codigo;
    }

    // Método para enviar el correo
    public void enviarCorreo() throws MessagingException {
        // Configuración de las propiedades del servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
        props.put("mail.smtp.port", "587");           // Puerto TLS
        props.put("mail.smtp.auth", "true");          // Requiere autenticación
        props.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS

        // Sesión de correo con autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Proporcionar las credenciales del remitente
                return new PasswordAuthentication(remitente, claveApp);
            }
        });

        // Crear el mensaje de correo
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(remitente)); // Dirección del remitente
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); // Dirección del destinatario
        msg.setSubject("Código para recuperar contraseña"); // Asunto del correo
        msg.setText("Tu código de recuperación es: " + codigo); // Contenido del mensaje

        // Enviar el correo
        Transport.send(msg);
        System.out.println("Correo enviado exitosamente a: " + destinatario);
    }
}
