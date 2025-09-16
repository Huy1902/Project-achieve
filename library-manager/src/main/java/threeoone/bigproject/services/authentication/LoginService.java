package threeoone.bigproject.services.authentication;

import java.util.Objects;
import org.springframework.stereotype.Service;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.AlreadyLoggedInException;
import threeoone.bigproject.repositories.UserRepo;

/**
 * A service that handles login/authentication logic.
 * <p>
 * The application can only have at most one logged-in/authenticated {@link User} at any time.
 * <p>
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
public class LoginService {
  private final UserRepo userRepo;

  private Integer loggedInUserId;

  /**
   * Autowired constructor that takes {@link UserRepo} as parameter to gain access to the Database.
   *
   * @param userRepo the repository for {@link User} entities
   */
  public LoginService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  /**
   * Returns the {@code id} of the current logged-in {@link User} if there is one,
   * otherwise, if no {@link User} is currently logged-in, returns {@code null}.
   *
   * @return the {@code id} of the current logged-in {@link User}, or {@code null} if it doesn't exist
   */
  public Integer getLoggedInUserId() {
    return loggedInUserId;
  }

  /**
   * Logs out the current logged-in {@link User}
   */
  public void logout() {
    loggedInUserId = null;
  }

  /**
   * Authenticates the input {@link User}.
   * If another {@link User} is currently logged-in, this method throws {@link AlreadyLoggedInException}.
   * If the {@code loginName} and {@code password} of the input {@link User} doesn't match with any registered User's,
   * returns {@code false}.
   * Otherwise, sets the current logged-in {@link User} to the input {@link User} and returns {@code true}.
   *
   * @param user the {@link User} whose {@code loginName} and {@code password} this method will attempt to authenticate
   *
   * @throws AlreadyLoggedInException if another {@link User} is currently logged-in
   *
   * @return whether this method successfully authenticates the input {@link User}
   */
  //TODO: Implement a password hashing algorithm
  public boolean login(User user) {
    if (loggedInUserId != null) {
      throw new AlreadyLoggedInException("A user is currently logged-in. A user should be able to log in only if no other user is currently logged in");
    }

    String username = user.getUsername();
    String password = user.getPassword();

    User registedUser = userRepo.findByUsername(username);

    if (registedUser == null) {
      return false;
    }

    if (Objects.equals(registedUser.getPassword(), password) == false) {
      return false;
    }

    loggedInUserId = registedUser.getId();
    return true;
  }
}
