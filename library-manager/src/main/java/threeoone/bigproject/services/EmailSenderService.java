package threeoone.bigproject.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import threeoone.bigproject.util.Alerts;

import java.io.File;

/**
 * Service class for sending emails.
 * This class uses JavaMailSender to send emails with attachments.
 *
 * @author HUY1902
 */
@Service
@RequiredArgsConstructor
public class EmailSenderService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  /**
   * Sends an email with the specified subject, body, and attachment to the specified recipient.
   *
   * @param to          the recipient email address
   * @param subject     the subject of the email
   * @param body        the body of the email
   * @param attachment  the path to the file to be attached
   * @throws MessagingException if an error occurs while creating or sending the email
   */
  public void sendEmail(String to, String subject, String body, String attachment) throws MessagingException {
    MimeMessage mimeMailMessage = mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMailMessage, true);

    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);

    FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
    message.addAttachment(fileSystemResource.getFilename(), fileSystemResource);

    Thread thread = new Thread(() -> {
      try {
        mailSender.send(mimeMailMessage);
        Platform.runLater(() -> Alerts.showAlertInfo("Send successfully", "Email Sent"));
      } catch (Exception e) {
        Platform.runLater(() -> Alerts.showAlertWarning("Error!", e.getMessage()));
      }
    });
    thread.start();
  }

  /**
   * Sends an email with the specified subject and body to the specified recipient.
   *
   * @param to          the recipient email address
   * @param subject     the subject of the email
   * @param body        the body of the email
   *
   * @throws MessagingException if an error occurs while creating or sending the email
   */
  public void sendEmail(String to, String subject, String body) throws MessagingException {

    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);

    Thread thread = new Thread(() -> {
      try {
        mailSender.send(message);
        Platform.runLater(() -> Alerts.showAlertInfo("Send successfully", "Email Sent"));
      } catch (Exception e) {
        Platform.runLater(() -> Alerts.showAlertWarning("Error!", e.getMessage()));
      }
    });
    thread.start();
  }
}
