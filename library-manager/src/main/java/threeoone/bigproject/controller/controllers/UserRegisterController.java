package threeoone.bigproject.controller.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.RegisterController;
import threeoone.bigproject.controller.viewcontrollers.ViewController;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.AlreadyLoggedInException;
import threeoone.bigproject.exceptions.UserAlreadyExistException;
import threeoone.bigproject.services.authentication.UserRegisterService;

/**
 * This class handles all logic for register
 *
 * @author HUY1902
 */
@Component
@RequiredArgsConstructor
public class UserRegisterController {
  private final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

  private final UserRegisterService userRegisterService;
  private final RegisterController registerController;
  private final RequestSender<ViewController> switchToLogin;
  /**
   * Register receiver for registerRequestSender
   *
   * @param registerRequestSender request sender
   */
  @Autowired
  private void registerRegisterRequestReceiver(RequestSender<User> registerRequestSender) {
    registerRequestSender.registerReceiver(this::validateRegister);
  }

  /**
   * Handle exception from service and manipulate view through view controller
   *
   * @param user User's register information
   */
  public void validateRegister(User user) {
    try {
      registerController.resetAlert();

      userRegisterService.register(user);

      registerController.showSuccessDialog();

      switchToLogin.send(null);
    }
    catch (UserAlreadyExistException e) {
      registerController.setConfirmMessage(e.getMessage());
    }
    catch (ConstraintViolationException e) {
      String message = e.getConstraintViolations().iterator().next().getMessage();
      registerController.setConfirmMessage(message);
    }
    catch (RuntimeException e) {
      logger.warn(e.getMessage());
    }
  }
}
