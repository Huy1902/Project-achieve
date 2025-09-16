package threeoone.bigproject.controller.controllers;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.LoginController;
import threeoone.bigproject.controller.viewcontrollers.ViewController;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.AlreadyLoggedInException;
import threeoone.bigproject.services.authentication.LoginService;

/**
 * Handles the interaction logic for the Login view.
 * <p>
 * Provides methods for validating User information,
 * authenticating User information through interacting with LoginService
 * </p>
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class UserLoginController {
  private final LoginController loginController;
  private final LoginService loginService;
  private final  RequestSender<ViewController> switchToMainMenu;
  private final RequestSender<ViewController> switchToLogin;

  @Getter
  private int failedAttempts = 0;
  private boolean isPauseOnMaxAttempts = false;
  private static final int MAX_ATTEMPTS = 3;
  private static final int LOCKOUT_DURATION = 5; //second



  /**
   * Register request receiver for login request
   *
   * @param loginRequestSender login request receiver
   */
  @Autowired
  private void registerLoginRequestReceiver(RequestSender<User> loginRequestSender) {
    loginRequestSender.registerReceiver(this::authenticateLogin);
  }

  @Autowired
  private void registerLogoutRequestReceiver(RequestSender<User> logoutRequestSender) {
    logoutRequestSender.registerReceiver(this::logout);
  }

  private void logout(User user) {
    loginService.logout();
    switchToLogin.send(null);
  }

  /**
   * Validates the information provided by the user.
   *
   * @return true if the username and password fields are not empty;
   * false otherwise
   */
  public boolean validateInformation(User user) {
    return !(user.getUsername().isEmpty() || user.getPassword().isEmpty());
  }

  /**
   * <p>
   *   Handle all logic for Login page
   * </p>
   * <p>
   * Authenticating user login. If authentication succeeds, it switches scene to Menu page.
   * If authentication fails, it return login page with a descriptive message.
   * </p>
   * <p>
   * Validates the user's credentials,
   * sends the login request if valid, or displays appropriate messages if invalid.
   * Implements a lockout mechanism after a number of failed attempts.
   * </p>
   * @param user User login
   */
  public void authenticateLogin(User user) {
    if (failedAttempts >= MAX_ATTEMPTS) {
      if (isPauseOnMaxAttempts) {
        isPauseOnMaxAttempts = false;
        loginController.setMessage("Too many attempts. Please wait...");

        PauseTransition pause = new PauseTransition(Duration.seconds(LOCKOUT_DURATION));
        pause.setOnFinished(e -> {
          failedAttempts = 0;
          loginController.setMessage("");
        });
        pause.play();
      }
      return;
    }
    if (validateInformation(user)) {
      try {
        if (loginService.login(user)) {
          switchToMainMenu.send(null);
          return;
        }
        else
        {
          ++failedAttempts;
          loginController.setMessage("Username or password doesn't match. Attempt " + failedAttempts);
        }
      } catch (AlreadyLoggedInException e) {
        loginController.setMessage(e.getMessage());
      }
    } else {
      ++failedAttempts;
      loginController.setMessage("Invalid credentials. Attempt " + failedAttempts);
    }

    if (failedAttempts >= MAX_ATTEMPTS) {
      isPauseOnMaxAttempts = true;
    }
  }

}
