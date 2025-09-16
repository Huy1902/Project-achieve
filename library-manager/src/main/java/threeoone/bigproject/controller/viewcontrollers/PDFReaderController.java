package threeoone.bigproject.controller.viewcontrollers;

import com.dansoftware.pdfdisplayer.PDFDisplayer;
import com.dansoftware.pdfdisplayer.PdfJSVersion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.entities.Document;

import java.io.File;

/**
 * Controller for handling PDF display and interactions in the application.
 * This class integrates the PDFDisplayer to show PDF documents in a JavaFX application.
 * It also handles view transitions and user interactions.
 *
 * @author HUY1902
 */
@Component
@FxmlView("PDFReader.fxml")
@RequiredArgsConstructor
public class PDFReaderController implements ViewController {
  private final RequestSender<ViewController> switchToDocDetail;

  @FXML
  private Parent root;

  @FXML
  private VBox pdf;

  private PDFDisplayer pdfDisplayer;

  /**
   * Initializes the PDFReaderController by setting up the PDFDisplayer.
   * This method is called automatically after the FXML fields are populated.
   */
  @FXML
  private void initialize() {
    pdfDisplayer = new PDFDisplayer(PdfJSVersion._2_2_228);
    Platform.runLater(() -> {
      pdf.getChildren().add(pdfDisplayer.toNode());
    });
  }

  /**
   * Loads and displays the specified document in the PDF viewer.
   *
   * @param document the document to be displayed
   * @throws Exception if an error occurs while loading the PDF
   */
  public void setDocument(Document document) throws Exception {
    if(document.getPdfUrl() == null || document.getPdfUrl().isEmpty()) {
      throw new Exception("PDF URL is empty");
    }
    pdfDisplayer.loadPDF(new File(document.getPdfUrl()));
  }

  /**
   * Handles the return button action to switch back to the document overview.
   *
   * @param event the action event triggered by the return button
   */
  @FXML
  void pressReturn(ActionEvent event) {
    switchToDocDetail.send(null);
  }

  /**
   * Returns the root parent node of this controller's view.
   *
   * @return the root parent node
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Shows the view associated with this controller.
   * This method can be used to perform any necessary actions when the view is displayed.
   */
  @Override
  public void show() {
    // Implementation for showing the view if needed
  }
}
