package threeoone.bigproject.controller.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.controller.viewcontrollers.RegisterController;
import threeoone.bigproject.controller.viewcontrollers.ViewController;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.IllegalCredentialsException;
import threeoone.bigproject.exceptions.UserAlreadyExistException;
import threeoone.bigproject.services.authentication.UserRegisterService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterSwitchViewControllerTest {
  @Mock
  private RegisterController registerController;
  @Mock
  private UserRegisterService userRegisterService;
  @Mock
  private RequestSender<ViewController> switchToLogin;
  @InjectMocks
  private UserRegisterController userRegisterController;

  @Test
  @DisplayName("testResetAlert")
  void testResetAlert() {
    User user = new User();
    userRegisterController.validateRegister(user);
    verify(registerController, times(1)).resetAlert();
  }

  @Test
  @DisplayName("testRegister")
  void testRegister() throws UserAlreadyExistException, IllegalCredentialsException {
    User user = new User("", "");
    userRegisterController.validateRegister(user);
    verify(userRegisterService, times(1)).register(user);
  }


  @Test
  void testValidateRegisterSuccess() throws Exception {
    User user = new User(); // No exception is thrown when registering
    doNothing().when(userRegisterService).register(user); // Call the method to test
    userRegisterController.validateRegister(user); // Verify interactions
    verify(registerController).resetAlert();
    verify(userRegisterService).register(user);
  }

  @Test
  void testValidateRegisterUserAlreadyExistException() throws Exception {
    User user = new User(); // Simulate exception being thrown
    doThrow(new UserAlreadyExistException("User already exists")).when(userRegisterService).register(user); // Call the method to test
    userRegisterController.validateRegister(user); // Verify interactions and exception handling
    verify(registerController).resetAlert();
    verify(userRegisterService).register(user);
    verify(registerController).setConfirmMessage("User already exists");
  }
}