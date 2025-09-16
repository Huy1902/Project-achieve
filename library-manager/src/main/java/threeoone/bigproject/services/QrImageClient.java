package threeoone.bigproject.services;

import javafx.scene.image.Image;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import threeoone.bigproject.config.FeignConfig;

/**
 * QrImageClient is a Feign client interface for accessing QR image services.
 * It interacts with an external service defined by the properties
 * {@code qr.api.name} and {@code qr.api.url}, and uses the configuration provided by {@code FeignConfig}.
 * This interface provides a method to retrieve the newest QR image as a byte array wrapped in a ResponseEntity.
 *
 * @author HUY1902
 */
@FeignClient(name = "${qr.api.name}", url = "${qr.api.url}", configuration = FeignConfig.class)
@Component
public interface QrImageClient {

  /**
   * Fetches the newest QR image from the remote service.
   *
   * @return a ResponseEntity containing the image as a byte array
   */
  @GetMapping("/get-newest-image")
  ResponseEntity<byte[]> getNewestQrImage();
}
