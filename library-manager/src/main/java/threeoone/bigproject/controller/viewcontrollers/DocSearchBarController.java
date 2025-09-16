package threeoone.bigproject.controller.viewcontrollers;

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.util.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.*;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;

/**
 * The {@code SearchBarController} class manages the search functionality in the UI.
 *
 * @author HUY1902
 */

@Component
@FxmlView("DocSearchBar.fxml")
@RequiredArgsConstructor
@Getter
public class
DocSearchBarController {
  private final RequestSender<ViewController> switchToDocDetail;
  private final RequestSender<Integer> queryDocByIdRequestSender;
  private final RequestSender<Document> documentDetailRequestSender;
  private final RequestSender<String> queryDocByNameRequestSender;
  private final RequestSender<String> queryDocByAuthorRequestSender;
  private final RequestSender<String> queryDocByCategoryRequestSender;
  private final RequestSender<User> getListAllDocumentRequestSender;


  private enum Type {
    Author,
    Name,
    Category
  }

  @FXML
  private TextField search;

  @FXML
  private Button searchButton;

  @FXML
  private GridPane searchRoot;

  @FXML
  private ChoiceBox<Type> type;


  /**
   * Initializes the controller, setting up the toggle buttons
   */
  @FXML
  private void initialize() {
    type.getItems().addAll(Type.values());
    type.setValue(Type.Name);

    search.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        searchButton.fire();
      }
    });
  }

  /**
   * Handles the search button action. Sends member queries based on the selected toggle buttons
   *
   * @param event the action event triggered by clicking the search button
   */
  @FXML
  private void pressSearch(ActionEvent event) {
    if (search.getText().isEmpty() || search.getText().matches("\\s+")) {
      getListAllDocumentRequestSender.send(null);
      return;
    }

    switch (type.getValue()) {
      case Name -> queryDocByNameRequestSender.send(search.getText());
      case Author -> queryDocByAuthorRequestSender.send(search.getText());
      case Category -> queryDocByCategoryRequestSender.send(search.getText());
    }

  }


}
