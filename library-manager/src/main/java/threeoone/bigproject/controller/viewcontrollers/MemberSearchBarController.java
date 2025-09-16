package threeoone.bigproject.controller.viewcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.*;
import threeoone.bigproject.entities.Member;

/**
 * The {@code SearchBarController} class manages the search functionality in the UI.
 * It allows users to search for members by user ID or username and displays the results
 * in a popup list.
 *
 * @author HUY1902
 */
@Component
@FxmlView("MemberSearchBar.fxml")
@RequiredArgsConstructor
@Getter
public class MemberSearchBarController {

  private final RequestSender<String> queryMemByNameRequestSender;
  private final RequestSender<Integer> queryMemByIdRequestSender;
  private final RequestSender<Member> getAllMembersRequestSender;

  private enum Type {
    Name,
    Id
  }

  @FXML
  private TextField search;

  @FXML
  private Button searchButton;

  @FXML
  private ChoiceBox<Type> type;

  /**
   * Initializes the controller, setting up the toggle buttons, list view, and popup.
   */
  @FXML
  private void initialize() {
    type.getItems().addAll(Type.values());
    type.setValue(Type.Id);

    search.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        searchButton.fire();
      }
    });
  }

  /**
   * Handles the search button action. Sends member queries based on the selected toggle buttons and
   * displays the results in a popup.
   *
   * @param event the action event triggered by clicking the search button
   */
  @FXML
  private void pressSearch(ActionEvent event) {
    if (search.getText().isEmpty() || search.getText().matches("\\s+")) {
      getAllMembersRequestSender.send(null);
      return;
    }

    switch (type.getValue()) {
      case Name -> queryMemByNameRequestSender.send(search.getText());
      case Id -> queryMemByIdRequestSender.send(Integer.valueOf(search.getText()));
    }
  }
}
