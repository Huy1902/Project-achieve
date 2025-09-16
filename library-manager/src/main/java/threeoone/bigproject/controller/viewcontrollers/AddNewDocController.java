package threeoone.bigproject.controller.viewcontrollers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.util.Alerts;
import threeoone.bigproject.util.FileOperation;
import java.io.File;
import java.util.Set;

/**
 * Controller class for the Add New Doc scene.
 * Handles the text field and button for Add New Doc view.
 * <p>
 * Provide method for interacting with button and text field
 * in Add New Doc view
 * </p>
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
@FxmlView("AddNewDoc.fxml")
public class AddNewDocController implements ViewController {
  private final Logger logger = LoggerFactory.getLogger(AddNewDocController.class);
  private final Validator validator;

  private final RequestSender<ViewController> switchToDocOverview;
  private final RequestSender<ViewController> switchToScanQR;
  private final RequestSender<Document> commitChangeDocRequestSender;
  private final RequestSender<Document> addDocumentRequestSender;

  private final RequestSender<String> queryISBNGoogleRequestSender;


  private final MenuBarController menuBarController;
  @FXML
  private TextField author;

  @FXML
  private TextField categories;

  @FXML
  private Button chooseButton;

  @FXML
  private TextArea description;

  @FXML
  private TextField isbn;

  @FXML
  private TextField name;

  @FXML
  private TextField numOfCopies;

  @FXML
  private SplitPane root;

  @FXML
  private Button submitButton;

  @FXML
  private ImageView docCover;

  private String coverPhotoPath = "";

  @Value("${document.photo-path.default}")
  private String defaultCoverPhotoPath;

  private File lastDirectory = null;
  private File selectedFile = null;

  @Setter
  private Document document;

  /**
   * Initializes the controller class. This method is automatically called
   * after the fxml file has been loaded.
   * Set default date for user (today)
   */
  public void initialize() {
    name.setOnAction(event -> author.requestFocus());
    author.setOnAction(event -> categories.requestFocus());
    categories.setOnAction(event -> isbn.requestFocus());
    isbn.setOnAction(event -> description.requestFocus());
    description.setOnKeyPressed(event -> {
      if(event.getCode() == KeyCode.TAB) {
        numOfCopies.requestFocus();
      }
    });
    categories.setText("Unknown");
    numOfCopies.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        submitButton.fire();
      }
    });

    menuBarController.highlight(SceneName.ADD_NEW_DOC);

    chooseButton.setOnAction(event -> openFileChooser());
  }

  private Integer parseCopies(String text) {
    try {
      return Integer.parseInt(text);
    }
    catch (NumberFormatException e) {
      Alerts.showAlertWarning("Error!", "Document's copies must be a number");
      logger.error(e.getMessage());
    }
    return null;
  }

  @FXML
  private void pressQR(ActionEvent event) {
    switchToScanQR.send(null);
  }

  /**
   * Handler for submit button
   *
   * @param event event trigger submit button
   */
  @FXML
  private void pressSubmit(ActionEvent event) {
    if(categories.getText() == null || categories.getText().isEmpty()) {
      categories.setText("Unknown");
    }

    Integer copies = parseCopies(numOfCopies.getText());

    if (copies == null) {
      return;
    }

    Document.DocumentBuilder documentBuilder = Document.builder()
            .name(name.getText())
            .description(description.getText())
            .copies(copies)
            .author(author.getText())
            .category(categories.getText());

    if(!isbn.getText().isEmpty()) {
      documentBuilder.isbn(isbn.getText());
    }

    if(!coverPhotoPath.isEmpty()) {
      documentBuilder.coverImageUrl(coverPhotoPath);
    } else {
      documentBuilder.coverImageUrl(defaultCoverPhotoPath);
    }

    document = documentBuilder.build();

    Set<ConstraintViolation<Document>> violations = validator.validate(document);

    if(violations.isEmpty()) {
      coverPhotoPath = defaultCoverPhotoPath;
      addDocumentRequestSender.send(document);
    } else {
      Alerts.showAlertWarning("Error!", violations.iterator().next().getMessage());
      logger.warn(violations.iterator().next().getMessage());
      return;
    }

    if (selectedFile == null) {
      Alerts.showAlertWarning("Warning!", "Unselected digital version for document!");
    } else {
      document.setPdfUrl(FileOperation.copyFile(selectedFile.getPath(), "", document.getId().toString()));
      commitChangeDocRequestSender.send(document);
      Alerts.showAlertInfo("Successfully!", "Adding with digital document.");
    }
    switchToDocOverview.send(null);
  }

  @FXML
  private void fulfillISBN(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      queryISBNGoogleRequestSender.send(isbn.getText());
    }
  }

  public void setIsbn(String isbnString) {
    isbn.setText(isbnString);
  }


  /**
   * Auto fulfill information text field with give document's info
   *
   * @param document given document to fulfill
   */
  public void fulfillInfo(Document document) {
    name.setText(document.getName());
    description.setText(document.getDescription());
    author.setText(document.getAuthor());
    categories.setText(document.getCategory());
    coverPhotoPath = document.getCoverImageUrl();
    docCover.setImage(new Image(coverPhotoPath));
  }

  /**
   * Gets the root {@link Parent} of the {@code View} managed by the {@link ViewController}.
   *
   * @return the root {@link Parent} of the {@code View}
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Notify the {@link ViewController} that its {@code View} is displayed.
   */
  @Override
  public void show() {
    docCover.setImage(new Image(defaultCoverPhotoPath));
    name.setText("");
    author.setText("");
    description.setText("");
    categories.setText("");
    isbn.setText("");
    numOfCopies.setText("");
    chooseButton.setText("Select File");
    document = null;
  }

  /**
   * Open window file chooser (belong to OS) to choosing document file
   */
  private void openFileChooser() {
    FileChooser fileChooser = new FileChooser();
    if (lastDirectory != null) {
      fileChooser.setInitialDirectory(lastDirectory);
    }
    selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
    if (selectedFile != null) {
      lastDirectory = selectedFile.getParentFile();
      chooseButton.setText("Selected File: " + selectedFile.getName());
    }
  }
}
