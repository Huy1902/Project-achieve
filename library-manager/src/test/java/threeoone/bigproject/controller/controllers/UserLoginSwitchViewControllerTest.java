package threeoone.bigproject.controller.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import threeoone.bigproject.controller.viewcontrollers.LoginController;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.AlreadyLoggedInException;
import threeoone.bigproject.services.authentication.LoginService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLoginSwitchViewControllerTest extends ApplicationTest {
  @Mock
  private LoginController loginController;

  @InjectMocks
  private UserLoginController userLoginController;

  @Mock
  LoginService loginService;
  private static final int MAX_ATTEMPTS = 3;
  private static final int LOCKOUT_DURATION = 5;


  @Test
  @DisplayName("validateInformation return False")
  void validateInformationReturnFalse() {
    User user = new User("", "");
    assertFalse(userLoginController.validateInformation(user));
    user = new User("adsasd", "");
    assertFalse(userLoginController.validateInformation(user));
    user = new User("", "asdasdad");
    assertFalse(userLoginController.validateInformation(user));
  }

  @Test
  @DisplayName("validateInformation return True")
  void validateInformationReturnTrue() {
    User user = new User("adsad", "jsadfklsd");
    assertTrue(userLoginController.validateInformation(user));
  }

  @Test
  @DisplayName("authenticationLoginWithOverMaxAttempt")
  void authenticationLoginWithOverMaxAttempt() {
    User user = new User("", "");
    userLoginController.authenticateLogin(user);
    userLoginController.authenticateLogin(user);
    userLoginController.authenticateLogin(user);
    Platform.runLater(() -> {
      userLoginController.authenticateLogin(user);
      // Verify PauseTransition behavior
      verify(loginController).setMessage("Too many attempts. Please wait...");
      // Simulate PauseTransition completion
      PauseTransition pause = new PauseTransition(Duration.seconds(LOCKOUT_DURATION));
      pause.setOnFinished(e -> {
        // Verify final state after the pause
        assertEquals(0, userLoginController.getFailedAttempts());
        verify(loginController).setMessage("");
      });
      pause.play();
    });
  }

  @Test
  @DisplayName("authenticationLoginWithInvalidUser")
  void authenticationLoginWithInvalidUser() {
    User user = new User("", "");
    userLoginController.authenticateLogin(user);
    verify(loginController, times(1)).setMessage("Invalid credentials. Attempt 1");
  }

  @Test
  @DisplayName("authenticateLoginWithExceptionFromService")
  void authenticateLoginWithExceptionFromService() {
    User user = new User("valid", "valid");
    //userLoginController.registerService(loginService);
    when(loginService.login(user)).thenThrow(new AlreadyLoggedInException("False"));
    userLoginController.authenticateLogin(user);
    verify(loginController, times(1)).setMessage("False");
  }

  @Test
  @DisplayName("loginAuthenticationFail")
  void loginAuthenticationFail() {
    User user = new User("valid", "valid");
    //userLoginController.registerService(loginService);
    when(loginService.login(user)).thenReturn(false);
    userLoginController.authenticateLogin(user);
    verify(loginController, times(1)).setMessage("Username or password doesn't match. Attempt 1");
  }
}