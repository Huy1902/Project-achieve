package threeoone.bigproject.controller.viewcontrollers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import javafx.application.Platform;
import threeoone.bigproject.controller.RequestSender;

/**
 * Controller class for managing the book recommendation view.
 * This controller handles user interactions and communicates with the RequestSender to fetch data.
 *
 * @author HUY1902
 */
@Component
@FxmlView("Recommender.fxml")
@RequiredArgsConstructor
public class RecommenderController implements ViewController {

  private final RequestSender<String> queryRecommendDocRequestSender;

  private final double CELL_SIZE = 40;

  @FXML
  private ListView<String> listView;

  @FXML
  private Parent root;

  @FXML
  private TextField search;

  @FXML
  private Button searchButton;

  /**
   * Initializes the controller, setting up the toggle buttons, list view, and popup.
   * This method is called after the FXML fields are injected.
   */
  @FXML
  private void initialize() {
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
          setText(item);
        }
      }
    });

    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        // TODO: switch to Doc detail if exist; otherwise, show alert
      }
    });

    search.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        searchButton.fire();
      }
    });
    listView.setFixedCellSize(CELL_SIZE);
    listView.setPrefHeight(CELL_SIZE * 5);
  }

  /**
   * Handles the action of pressing the "New Issue" button.
   *
   * @param event the action event
   */
  @FXML
  private void pressNewIssue(ActionEvent event) {
    // TODO: Implement action for "New Issue" button press
  }

  /**
   * Sets the search result to the list view and displays it.
   *
   * @param result the search result to be displayed
   */
  public synchronized void setResult(ObservableList<String> result) {
    Platform.runLater(() -> {
      listView.getItems().clear();
      listView.getItems().addAll(result);
    });
  }

  /**
   * Handles the action of pressing the "Return" button.
   *
   * @param event the action event
   */
  @FXML
  private void pressReturn(ActionEvent event) {
    // TODO: Implement action for "Return" button press
  }

  /**
   * Handles the action of pressing the "Search" button.
   * Initiates a search query and updates the list view with the results.
   *
   * @param event the action event
   */
  @FXML
  private void pressSearch(ActionEvent event) {
    if (search.getText().isEmpty()) {
      return;
    }
    Thread thread = new Thread(() -> {
      queryRecommendDocRequestSender.send(search.getText());
    });
    thread.start();
  }

  /**
   * Gets the parent root node of this controller.
   *
   * @return the parent root node
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Shows the view controlled by this controller.
   * Implement this method to provide custom behavior when the view is displayed.
   */
  @Override
  public void show() {
    listView.getItems().clear();
    search.setText("");
  }
}
