package threeoone.bigproject.controller.controllers;

import javafx.application.Platform;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.AddNewDocController;
import threeoone.bigproject.controller.viewcontrollers.ScanQrController;
import threeoone.bigproject.controller.viewcontrollers.ViewController;
import threeoone.bigproject.services.QrImageService;
import threeoone.bigproject.util.Alerts;
import threeoone.bigproject.view.ViewSwitcher;

/**
 * Class for handling QR Scan code
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class ActionOnQRController {
  private final Logger logger = LoggerFactory.getLogger(ActionOnQRController.class);

  private final ViewSwitcher viewSwitcher;
  private final AddNewDocController addNewDocController;
  private final ScanQrController scanQrController;

  private final QrImageService qrImageService;

  @Autowired
  private void registerRequestReceiver(
          RequestSender<ViewController> getQRImageFromServer,
          RequestSender<String> setISBNToAddNewPage) {
    getQRImageFromServer.registerReceiver(this::getQRImageFromServer);
    setISBNToAddNewPage.registerReceiver(this::setISBNToAddNewPage);
  }

  /**
   * Call to service to get Image from server then set it to scanQr view
   *
   */
  private void getQRImageFromServer(ViewController viewController) {
    try {
      Image image = qrImageService.getNewestQrImage();
      Platform.runLater(() -> scanQrController.setQrImage(image));
    } catch (Exception e) {
      Alerts.showAlertWarning("Error!", e.getMessage());
      logger.error(e.getMessage());
    }
  }

  /**
   * Take isbn from scan qr view to set for add new doc view
   *
   * @param isbn from scanning qr
   */
  private void setISBNToAddNewPage(String isbn) {
    viewSwitcher.switchToView(addNewDocController);
    addNewDocController.setIsbn(isbn);
  }
}
