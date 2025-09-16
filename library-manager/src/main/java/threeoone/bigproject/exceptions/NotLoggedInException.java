package threeoone.bigproject.exceptions;

import threeoone.bigproject.entities.User;

/**
 * An exception where no Users are logged in when there should be one.
 *
 * @author DUCBRICK
 */
public class NotLoggedInException extends RuntimeException {
  public NotLoggedInException(String message) {
    super(message);
  }
}
