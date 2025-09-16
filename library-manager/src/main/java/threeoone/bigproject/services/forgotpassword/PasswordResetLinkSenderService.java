package threeoone.bigproject.services.forgotpassword;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import threeoone.bigproject.entities.PasswordResetToken;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.repositories.PasswordResetTokenRepo;
import threeoone.bigproject.repositories.UserRepo;
import threeoone.bigproject.services.EmailSenderService;
import threeoone.bigproject.util.RandomStringGenerator;

/**
 * A service to send emails to Users containing the REST API uri that will authenticate requests to reset their password.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
@Validated
@RequiredArgsConstructor
public class PasswordResetLinkSenderService {
  /**
   * Default valid duration of generated {@link PasswordResetToken}.
   */
  public static int VALID_TIME_IN_MINUTES = 10;

  private final EmailSenderService emailSender;
  private final UserRepo userRepo;
  private final PasswordResetTokenRepo tokenRepo;
  private final RandomStringGenerator randomStringGeneratorService;

  @Value("${api.url}")
  private String serverUrl;

  @Value("${api.resetpassword.endpoint}")
  private String passwordResetApiEndpoint;

  @Value("${api.resetpassword.param.tokenvalue}")
  private String passwordResetApiTokenParam;

  private final Logger logger = LoggerFactory.getLogger(PasswordResetLinkSenderService.class);

  /**
   * Sends a password-reset email to the email address of a {@link User} with a specific {@code username}.
   * <p>
   * This method deletes any old {@link PasswordResetToken} associated to the given {@link User} and generate a new one.
   * The new generated {@link PasswordResetToken} will be valid for the duration of {@link #VALID_TIME_IN_MINUTES}.
   * During that time, it can be used to authenticate a single password-reset request of the specified {@link User}.
   *
   * @param username the username of the {@link User} who wants to reset their password
   *
   * @return whether if a {@link User} with the provided {@code username} exists
   *
   * @throws RuntimeException when unexpected errors occur (such as constraints violation)
   */
  @Transactional
  public boolean sendResetLink(String username) {
    User user = userRepo.findByUsername(username);

    if (user == null) {
      return false;
    }

    /*Delete previous tokens to avoid loitering*/
    tokenRepo.deleteByUser(user.getId());

    var token = new PasswordResetToken();

    token.setValue(randomStringGeneratorService.generate(PasswordResetToken.MAX_TOKEN_LENGTH));
    token.setExpireTime(LocalDateTime.now().plusMinutes(VALID_TIME_IN_MINUTES));
    token.setUser(user);

    token = tokenRepo.save(token);

    String url
        = serverUrl
        + passwordResetApiEndpoint
        + "?"
        + passwordResetApiTokenParam
        + "="
        + token.getValue();

    try {
      emailSender.sendEmail(
          user.getEmail(),
          "Reset password for user " + username,
          """
              You made a request to reset your password.
              Please follow this link to do so:
              %s
              If you did not make such a request, kindly ignore this email""".formatted(url));
    } catch (MessagingException e) {
      logger.warn("Failed to send the Email containing the password reset link to User");
      logger.trace(e.getMessage());
    }

    return true;
  }
}
