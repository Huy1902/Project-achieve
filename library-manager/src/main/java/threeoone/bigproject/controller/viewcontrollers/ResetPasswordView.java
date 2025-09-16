package threeoone.bigproject.controller.viewcontrollers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.Objects;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.entities.User;

/**
 * FXML Controller to handles the internal logic of {@code resetpassword.fxml}.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Component
@RequiredArgsConstructor
@FxmlView("resetpassword.fxml")
public class ResetPasswordView implements ViewController {
  private final Validator validator;

  private final RequestSender <ViewController> switchToLogin;
  private final RequestSender <User> resetPasswordRequestSender;

  @Valid
  private User user;

  @FXML
  private Parent root;

  @FXML
  private Label pageLabel;

  @FXML
  private PasswordField enteredPasswordField;

  @FXML
  private PasswordField reEnteredPasswordField;

  @FXML
  private Label errorMessage;

  @FXML
  private void initialize() {
    hideErrorMessage();
  }

  @Override
  public Parent getParent() {
    return root;
  }

  private void showErrorMessage() {
    errorMessage.setVisible(true);
    errorMessage.setManaged(true);
  }

  private void hideErrorMessage() {
    errorMessage.setVisible(false);
    errorMessage.setManaged(false);
  }

  private void setPageLabel() {
    String label = "Reset password for account ";

    if (user != null) {
      label = label + user.getUsername();
    }

    pageLabel.setText(label);
  }

  private void clearOldData() {
    enteredPasswordField.setText("");
    reEnteredPasswordField.setText("");
  }

  /**
   * Configures the page according to the given {@link User}.
   */
  @Override
  public void show() {
    clearOldData();
    setPageLabel();
  }

  /**
   * Sets the {@link User} that wants to have their {@code password} reset.
   * The given {@link User} should be a valid entity from the JPA context.
   *
   * @param user the {@link User} that wants to have their {@code password} reset.
   */
  public void setUser(@Valid @NonNull User user) {
    Set <ConstraintViolation <@NonNull User>> violations = validator.validate(user);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    this.user = user;
  }

  @FXML
  private void switchToLogin() {
    switchToLogin.send(null);
  }

  private String getEnteredPassword() {
    return enteredPasswordField.getText();
  }

  private String getReEnteredPassword() {
    return reEnteredPasswordField.getText();
  }

  private boolean matchPassword() {
    return Objects.equals(getEnteredPassword(), getReEnteredPassword());
  }

  @FXML
  private void submitRequest() {
    if (!matchPassword()) {
      showErrorMessage();
      return;
    }

    hideErrorMessage();

    user.setPassword(getEnteredPassword());
    resetPasswordRequestSender.send(user);
  }
}
