package threeoone.bigproject.controller.viewcontrollers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.util.Alerts;

import java.io.File;
import java.util.Set;

/**
 * ViewController for Register Page
 * No logic in this class except compare
 * confirmation and password to ensure
 * User information send to userRegisterController to validate with Service
 *
 * @author HUY1902
 */
@Component
@FxmlView("Register.fxml")
@RequiredArgsConstructor
public class RegisterController implements ViewController {
  private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
  private final Validator validator;

  @FXML
  private Label confirmMessage;

  @FXML
  private PasswordField confirmpass;

  @FXML
  private PasswordField password;

  @FXML
  private Parent root;

  @FXML
  private Button signupButton;

  @FXML
  private TextField username;

  @FXML
  private TextField emailInputField;


  private final RequestSender<ViewController> switchToLogin;
  private final RequestSender<User> registerRequestSender;

  /**
   * Initializes the controller class. This method is automatically called
   * after the fxml file has been loaded.
   */
  public void initialize() {
    username.setOnAction(event -> password.requestFocus());
    password.setOnAction(event -> confirmpass.requestFocus());
    confirmpass.setOnAction(event -> {
      signupButton.fire();
    });
  }

  /**
   * Handles the sign-in button press event.
   *
   * @param event the action event triggered by pressing the sign-in button
   */
  @FXML
  private void pressSignIn(ActionEvent event) {
    switchToLogin.send(null);
  }


  /**
   * Handle the sign-up button press event
   *
   * @param event the action triggered by pressing the sign-up button
   */
  @FXML
  private void pressSignUp(ActionEvent event) {

    if(validateConfirmation()) {
      User user = new User();
      user.setUsername(username.getText());
      user.setPassword(password.getText());
      user.setEmail(emailInputField.getText());
      registerRequestSender.send(user);

    }
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
    clearDataInPage();
  }

  /**
   * Set confirm Message
   *
   * @param confirmMessage alert for password confirmation
   */
  public void setConfirmMessage(String confirmMessage) {
    this.confirmMessage.setText(confirmMessage);
  }

  /**
   * Alert when user signup successfully
   */
  public void showSuccessDialog() {
    Alerts.showAlertInfo("Registration Successful","You have registered successfully!\nPlease log in to your account");
  }

  /**
   * Reset text of all alert message
   */
  public void resetAlert() {
    this.setConfirmMessage("");
  }

  /**
   * Compare confirmation and password
   *
   * @return true if equal; otherwise, false
   */
  private boolean validateConfirmation() {
    if (!confirmpass.getText().equals(password.getText())) {
      confirmMessage.setText("Passwords do not match");
      return false;
    } else {
      confirmMessage.setText("");
      return true;
    }
  }

  private void clearDataInPage() {
    emailInputField.clear();
    username.clear();
    password.clear();
    confirmpass.clear();
    confirmMessage.setText("");
  }

}
