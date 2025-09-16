package threeoone.bigproject.controller.controllers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.LoginController;
import threeoone.bigproject.controller.viewcontrollers.ResetPasswordView;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.services.persistence.UserPersistenceService;
import threeoone.bigproject.util.Alerts;
import threeoone.bigproject.view.ViewSwitcher;

/**
 * A controller to handle requests related to resetting users' passwords.
 * This class is singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Controller
@RequiredArgsConstructor
@Validated
public class ResetPasswordController {
  private final Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);

  private final ViewSwitcher viewSwitcher;

  private final ResetPasswordView resetPasswordView;
  private final LoginController loginController;

  private final UserPersistenceService userPersistenceService;

  @Autowired
  private void registerRequestSenders(
    RequestSender <User> redirectToPasswordResetPageRequestSender,
      RequestSender <User> resetPasswordRequestSender) {
    redirectToPasswordResetPageRequestSender.registerReceiver(this::redirectToPage);
    resetPasswordRequestSender.registerReceiver(this::resetPassword);
  }

  /**
   * Receives and processes a {@code redirectToPasswordResetPage} request.
   *
   * @param user the {@link User} to reset password
   */
  private void redirectToPage(@NotNull @Valid User user) {
    /*Should not throw any exceptions*/
    resetPasswordView.setUser(user);
    viewSwitcher.switchToView(resetPasswordView);
  }

  /**
   * Receives and processes a {@code resetPassword} request.
   * This method updates the given {@link User} to the Database,
   * (specifically, only its {@code password}).
   * After that, this method redirects the user to the login page.
   *
   * @param user the {@link User} to update password
   */
  private void resetPassword(@NotNull @Valid User user) {
    try {
      userPersistenceService.update(user);
      Alerts.showAlertInfo("Success", "Password reset! Sign in to continue");
      viewSwitcher.switchToView(loginController);
    }
    catch (ConstraintViolationException e) {
      Alerts.showAlertWarning("Error!", e.getConstraintViolations().iterator().next().getMessage());
    }
    catch (RuntimeException e) {
      logger.warn(e.getMessage());
    }
  }
}
