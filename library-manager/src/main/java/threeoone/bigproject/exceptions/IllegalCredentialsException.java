package threeoone.bigproject.exceptions;

/**
 * An exception where a {@link threeoone.bigproject.entities.User} has illegal credentials,
 * such as empty name or password, etc.
 *
 * @author DUCBRICK
 */
public class IllegalCredentialsException extends Exception {
  public IllegalCredentialsException(String message) {
    super(message);
  }
}
