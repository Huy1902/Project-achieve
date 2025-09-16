package threeoone.bigproject.controller.viewcontrollers;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.util.Alerts;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

/**
 * Test class for QR Scan Code
 *
 * @author HUY1902
 */
@Component
@FxmlView("ScanQr.fxml")
@RequiredArgsConstructor
public class ScanQrController implements ViewController {
  private final RequestSender<ViewController> getQRImageFromServer;
  private final Logger logger = LoggerFactory.getLogger(ScanQrController.class);

  private final RequestSender<String> setISBNToAddNewPage;
  private final RequestSender<ViewController> switchToAddNewDoc;
  private final RequestSender<String> queryISBNGoogleRequestSender;

  private final Validator validator;

  private String imagePath = "";
  @FXML
  private ImageView qrImage;

  @FXML
  private Parent root;

  @FXML
  private TextField isbn;

  /**
   * Handle event when dragging image over pane in Scan Qr view
   *
   * @param event drag image over
   */
  @FXML
  private void dragOver(DragEvent event) {
    if (event.getDragboard().hasFiles()) {
      event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    }
    event.consume();
  }

  /**
   * Drag image drop on pane
   *
   * @param event drag drop image
   */
  @FXML
  private void dragImage(DragEvent event) {
    Dragboard dragboard = event.getDragboard();
    boolean success = false;
    if (dragboard.hasFiles()) {
      File file = dragboard.getFiles().get(0);
      imagePath = file.getAbsolutePath();
      qrImage.setImage(new Image(file.toURI().toString()));
      success = true;
    }
    event.setDropCompleted(success);
    event.consume();
  }

  /**
   * Handle get isbn button
   *
   * @param event press
   */
  @FXML
  private void pressGetISBN(ActionEvent event) {
    try {
      BufferedImage bf;
      bf = ImageIO.read(new FileInputStream(imagePath));
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
              new BufferedImageLuminanceSource(bf)));

      Result result = new MultiFormatReader().decode(bitmap);

      isbn.setText(result.getText());
    } catch (Exception e) {
      Alerts.showAlertWarning("Error!", e.getMessage());
      logger.warn(e.getMessage());
      isbn.setText("");
    }

  }

  /**
   * Handle choose button
   *
   * @param event press
   */
  @FXML
  private void pressChoose(ActionEvent event) {
    Document document = Document.builder().name("clone").author("clone").coverImageUrl("clone").copies(0).build();
    document.setIsbn(isbn.getText());

    Set<ConstraintViolation<Document>> violations = validator.validate(document);

    if (violations.isEmpty()) {
      setISBNToAddNewPage.send(isbn.getText());
      Thread thread = new Thread(() -> queryISBNGoogleRequestSender.send(isbn.getText()));
      thread.start();
    } else {
      Alerts.showAlertWarning("Error!", violations.iterator().next().getMessage());
      logger.warn(violations.iterator().next().getMessage());
      switchToAddNewDoc.send(null);
    }
  }

  /**
   * @param qrImage qr image to set
   */
  public void setQrImage(Image qrImage) {
    this.qrImage.setImage(qrImage);
  }

  @FXML
  private void pressGetFromServer(ActionEvent event) {
    Thread thread = new Thread(() -> {
      getQRImageFromServer.send(this);
    });
    thread.start();
  }

  /**
   * @return root of scene
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Display view on screen
   */
  @Override
  public void show() {
    qrImage.setImage(null);
    isbn.setText("");
  }
}
