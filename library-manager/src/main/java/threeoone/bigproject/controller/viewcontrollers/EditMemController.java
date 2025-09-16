package threeoone.bigproject.controller.viewcontrollers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.util.Alerts;

/**
 * Controller class for the "Edit Member" view.
 *
 * @author HUY1902
 */
@Component
@FxmlView("EditMem.fxml")
@RequiredArgsConstructor
@Setter
public class EditMemController implements ViewController {
  private final Validator validator;

  private final RequestSender<ViewController> switchToMemList;
  private final RequestSender<Member> commitChangeMemberRequestSender;
  private Member chosenMember;

  @FXML
  private TextField address;

  @FXML
  private TextField email;

  @FXML
  private Label id;

  @FXML
  private TextField name;

  @FXML
  private TextField phoneNumber;

  @FXML
  private Parent root;

  @FXML
  private Button resetButton;

  /**
   * Initializes the controller class. This method is automatically called
   * after the FXML file has been loaded.
   */
  @FXML
  private void initialize() {
    name.setOnAction(event -> phoneNumber.requestFocus());
    phoneNumber.setOnAction(event -> address.requestFocus());
    address.setOnAction(event -> email.requestFocus());
    email.setOnAction(this::pressSubmit);
  }


  private Member constructValidatedMember() {
    Member member = new Member();

    member.setName(name.getText());
    member.setPhoneNumber(phoneNumber.getText());
    member.setAddress(address.getText());
    member.setEmail(email.getText());

    Set<ConstraintViolation<Member>> violations = validator.validate(member);

    if (violations.isEmpty()) {
      return member;
    }

    Alerts.showAlertWarning("Error!", violations.iterator().next().getMessage());

    return null;
  }

  /**
   * Handles the action when the submit button is pressed.
   * If the name field is empty, an alert is shown. Otherwise, the member's name
   * is updated and the action is sent to the server.
   *
   * @param event the event triggered when the submit button is pressed
   */
  @FXML
  private void pressSubmit(ActionEvent event) {
    Member toBeUpdatedMember = constructValidatedMember();

    if (toBeUpdatedMember == null) {
      return;
    }

    toBeUpdatedMember.setId(chosenMember.getId());

    commitChangeMemberRequestSender.send(toBeUpdatedMember);
    switchToMemList.send(this);
  }

  /**
   * Gets the root {@link #root} of the {@code View} managed by the {@link ViewController}.
   *
   * @return the root {@link #root} of the {@code View}
   */
  @Override
  public Parent getParent() {
    return root;
  }

  @FXML
  private void fulFillInfo() {
    id.setText(String.valueOf(chosenMember.getId()));
    name.setText(chosenMember.getName());
    phoneNumber.setText(chosenMember.getPhoneNumber());
    address.setText(chosenMember.getAddress());
    email.setText(chosenMember.getEmail());
  }


  /**
   * Notify the {@link ViewController} that its {@code View} is displayed.
   * Sets the ID and name fields based on the chosen member.
   */
  @Override
  public void show() {
    if (chosenMember == null) {
      switchToMemList.send(null);
      return;
    }
    fulFillInfo();
  }
}
