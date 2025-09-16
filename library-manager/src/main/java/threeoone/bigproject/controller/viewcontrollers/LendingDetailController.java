package threeoone.bigproject.controller.viewcontrollers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.requestbodies.LendingDetail;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.util.Alerts;

/**
 * Manages the lending detail view and updates its content based on member and document data.
 * Handles interactions and view management.
 * Provides methods to set member and document information, and handles actions such as
 * submitting or returning from the lending detail view.
 * Relies on various {@link RequestSender} instances for communication with other parts
 * of the application.
 *
 * @author HUY1902
 */
@Component
@FxmlView("LendingDetail.fxml")
@RequiredArgsConstructor
public class LendingDetailController implements ViewController {

  private final RequestSender<LendingDetail> saveNewLending;
  private final RequestSender<ViewController> switchToMainMenu;
  private final RequestSender<Integer> queryMemByIdFromLendingRequestSender;

  @FXML
  private Parent root;

  @FXML
  private Label docAuthor;

  @FXML
  private Label docCategory;

  @FXML
  private Label docID;

  @FXML
  private Label docName;

  @FXML
  private ImageView docPhoto;

  @FXML
  private TextField memberID;

  @FXML
  private Label memberName;

  @FXML
  private Label memberAddress;

  @FXML
  private Label memberPhone;

  @FXML
  private Label memberEmail;

  @FXML
  private Label docDescription;

  private Member member;

  private Document document;


  @FXML
  private void initialize() {
    memberID.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        try {
          if (memberID.getText() == null || memberID.getText().isEmpty()) {
            throw new NumberFormatException();
          }

          Integer id = Integer.parseInt(memberID.getText());
          queryMemByIdFromLendingRequestSender.send(id);
        }
        catch (NumberFormatException e) {
          Alerts.showAlertWarning("Error!", "The entered member ID must be a number");
        }
      }
    });
  }

  /**
   * Sets the member information in the view.
   *
   * @param member the member whose information is to be displayed
   */
  public void setMember(Member member) {
    if (member == null) {
      Alerts.showAlertWarning("Not found!", "Try another ID");
      return;
    }
    this.member = member;
    memberID.setText(member.getId().toString());
    memberName.setText(member.getName());
    memberAddress.setText(member.getAddress());
    memberEmail.setText(member.getEmail());
    memberPhone.setText(member.getPhoneNumber());
  }

  /**
   * Sets the document information in the view.
   *
   * @param document the document whose information is to be displayed
   */
  public void setDocument(Document document) {
    this.document = document;
    docID.setText(document.getId().toString());
    docName.setText(document.getName());
    docDescription.setText(document.getDescription());
    docAuthor.setText(document.getAuthor());
    docCategory.setText(document.getCategory());
    Thread thread = new Thread(() -> {
      Image temp = new Image(document.getCoverImageUrl());
      Platform.runLater(() -> {
        docPhoto.setImage(temp);
      });
    });
    thread.start();
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

  private void clearData() {
    member = null;
    memberID.setText(null);
    memberName.setText(null);
    memberPhone.setText(null);
    memberEmail.setText(null);
    memberAddress.setText(null);
  }

  /**
   * Notify the {@link ViewController} that its {@code View} is displayed.
   */
  @Override
  public void show() {
    clearData();
  }


  /**
   * Handles the action event when the return button is pressed.
   * Unsubscribes from member and document updates and switches to the main menu scene.
   *
   * @param event the action event triggered by pressing the return button
   */
  @FXML
  private void pressReturn(ActionEvent event) {
    switchToMainMenu.send(null);
  }

  /**
   * Handles the action event when the submit button is pressed.
   * If both a member and a document are selected, it saves the new lending detail.
   * Otherwise, it shows an alert warning to choose both member and document.
   *
   * @param event the action event triggered by pressing the submit button
   */
  @FXML
  private void pressSubmit(ActionEvent event) {
    if (member != null && document != null) {
      saveNewLending.send(new LendingDetail(member, document));
      switchToMainMenu.send(this);
      return;
    }
    Alerts.showAlertWarning("Error!!!", "Choose both member and document");
  }

}
