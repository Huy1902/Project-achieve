package threeoone.bigproject.services;

import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import threeoone.bigproject.util.Alerts;

import java.io.ByteArrayInputStream;

/**
 * QrImageService is a service class for handling operations related to QR images.
 * It interacts with the {@link QrImageClient} to fetch the newest QR image from a remote service.
 * The fetched image is then converted to a {@link javafx.scene.image.Image} object.
 * In case of an error during the fetch process, an alert is shown and an exception is thrown.
 *
 * @author HUY1902
 */
@Service
@RequiredArgsConstructor
public class QrImageService {
  /**
   * Feign client for interacting with the QR image service.
   */
  private final QrImageClient qrImageClient;

  /**
   * Fetches the newest QR image from the {@link QrImageClient} and converts it to a JavaFX {@link Image}.
   * If the fetch operation fails, an alert is shown and an exception is thrown.
   *
   * @return the newest QR image as a JavaFX {@link Image}
   * @throws Exception if the image could not be fetched from the server
   */
  public Image getNewestQrImage() throws Exception {
    try {
      ResponseEntity<byte[]> response = qrImageClient.getNewestQrImage();
      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getBody());
        Image image = new Image(byteArrayInputStream);
        return image;
      }
    } catch (Exception e) {
      Alerts.showAlertWarning("Error!!", e.getMessage());
    }
    throw new Exception("Cannot get image from server");
  }
}
