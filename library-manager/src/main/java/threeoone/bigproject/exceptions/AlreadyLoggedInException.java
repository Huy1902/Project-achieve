package threeoone.bigproject.exceptions;

import threeoone.bigproject.entities.User;

/**
 * An exception where a {@link User} tries to log in while another {@link User} is still currently logged-in.
 *
 * @author DUCBRICK
 */
public class AlreadyLoggedInException extends RuntimeException {
  public AlreadyLoggedInException(String message) {
    super(message);
  }
}
