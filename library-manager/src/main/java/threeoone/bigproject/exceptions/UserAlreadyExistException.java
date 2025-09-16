package threeoone.bigproject.exceptions;

/**
 * An exception where a {@link threeoone.bigproject.entities.User} tries to register with the same 
 * {@code loginName} as another User in the Database.
 *
 * @author DUCBRICK
 */
public class UserAlreadyExistException extends Exception {
  public UserAlreadyExistException(String message) {
    super(message);
  }
}
