package threeoone.bigproject.services.resetpassword;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import threeoone.bigproject.entities.PasswordResetToken;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.repositories.PasswordResetTokenRepo;

/**
 * A service to authenticate requests to reset a User's password.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
@RequiredArgsConstructor
public class PasswordResetAuthenticationService {
  private final PasswordResetTokenRepo tokenRepo;

  /**
   * Authenticate a request using a given token.
   * <p>
   * This method retrieves a single unique valid {@link PasswordResetToken} with a specific value
   * and returns its associated {@link User}.
   *
   * @param tokenValue the value of the {@link PasswordResetToken}
   *
   * @return the {@link User} associated to the given token, of {@code NULL} if there isn't any
   */
  @Transactional
  public User authenticate(String tokenValue) {
    PasswordResetToken token = tokenRepo.findByValue(tokenValue);

    if (token == null) {
      return null;
    }

    User user = (token.getExpireTime().isAfter(LocalDateTime.now()) ? token.getUser() : null);

    tokenRepo.delete(token);

    return user;
  }
}
