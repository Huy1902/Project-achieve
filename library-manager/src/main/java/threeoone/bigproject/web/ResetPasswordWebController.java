package threeoone.bigproject.web;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import threeoone.bigproject.controller.RequestSender;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.services.resetpassword.PasswordResetAuthenticationService;

/**
 * An HTTP Controller for APIs related to resetting users' passwords.
 * This class is a Controller in Spring context.
 *
 * @author DUCBRICK
 */
@Controller
@RequiredArgsConstructor
public class ResetPasswordWebController {
  private final Validator validator;

  private final PasswordResetAuthenticationService authenticationService;
  private final RequestSender <User> redirectToPasswordResetPageRequestSender;

  /**
   * A GET mapping to the path specified by {@code api.resetpassword.endpoint} property.
   * <p>
   * Requests to this endpoint must contain a single parameter,
   * which is the value of the token that is used to authenticate each request.
   * The name of this parameter is specified by {@code api.resetpassword.param.tokenvalue} property.
   * <p>
   * This method authenticates each request using the provided token value.
   * If the token is valid, it sends the user to the password reset page.
   * Otherwise, it returns a page indicating the request was denied.
   *
   * @param tokenValue the request parameter containing the value of the authentication token
   *
   * @return the view to be displayed
   */
  @GetMapping("${api.resetpassword.endpoint}")
  public String resetPassword(
      @RequestParam("${api.resetpassword.param.tokenvalue}") String tokenValue) {
    User user = authenticationService.authenticate(tokenValue);

    if (user == null || !validator.validate(user).isEmpty()) {
      return "requestdenied.html";
    }

    redirectToPasswordResetPageRequestSender.send(user);

    return "requestauthenticated.html";
  }
}
