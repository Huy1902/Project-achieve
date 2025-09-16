package threeoone.bigproject.controller.viewcontrollers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.SceneName;
import threeoone.bigproject.controller.requestbodies.SwitchScene;
import threeoone.bigproject.entities.User;

/**
 * Controller class for the Login scene.
 * <p>
 *   Provide method for switch between field Text by Keyboard
 * </p>
 * <p>
 *   Switch to Register page if needed
 * </p>
 *
 * @author HUY1902
 */
@Component
@FxmlView("Login.fxml")
@RequiredArgsConstructor
public class LoginController implements ViewController {
  private final RequestSender<User> loginRequestSender;
  private final RequestSender<ViewController> switchToSignup;
  private final RequestSender<ViewController> switchToForgetPassword;
  @FXML
  private Parent root;

  @FXML
  private PasswordField passwordField;

  @FXML
  private TextField usernameField;

  @FXML
  private Button loginButton;

  @FXML
  private Text message;


  /**
   * Initializes the controller class. This method is automatically called
   * after the fxml file has been loaded.
   */
  public void initialize() {
    usernameField.setPromptText("Username");
    passwordField.setPromptText("Password");
    usernameField.setOnAction(event -> passwordField.requestFocus());
    passwordField.setOnAction(event -> loginButton.fire());
  }

  /**
   * Handle press event for HyperLink Forget Password, go to Forget Password page
   *
   * @param event event trigger HyperLink Forget Password
   */
  @FXML
  private void pressForgetPassword(ActionEvent event) {
    switchToForgetPassword.send(null);
  }

  /**
   * Handles the login button press action. All logic for login page now is handled by
   * {@link threeoone.bigproject.controller.controllers.UserLoginController}
   *
   * @param event the event triggered when the login button is pressed
   */
  @FXML
  private void pressLogin(ActionEvent event) {
    loginRequestSender.send(new User(usernameField.getText().trim(), passwordField.getText()));
  }

  /**
   * Sets the message to be displayed on the UI.
   *
   * @param message the message to be displayed
   */
  public void setMessage(String message) {
    this.message.setText(message);
  }

  /**
   * Handle press event for HyperLink Signup, go to Register page
   *
   * @param event event trigger HyperLink Signup
   */
  @FXML
  private void pressSignUp(ActionEvent event) {
    switchToSignup.send(null);
  }

  /**
   * Gets the root {@link Parent} node of this controller's view.
   *
   * @return the root {@link Parent} node
   */
  @Override
  public Parent getParent() {
    return root;
  }

  /**
   * Shows the view managed by this controller.
   */
  @Override
  public void show() {
    passwordField.setText(null);
  }
}
