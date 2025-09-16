package threeoone.bigproject.exceptions;

import threeoone.bigproject.entities.Document;

/**
 * An exception where the user tries to add a {@link Document} that is same as an existing one to the Database.
 *
 * @author DUCBRICK
 */
public class DocumentAlreadyExistException extends Exception {
  public DocumentAlreadyExistException(String message) {
    super(message);
  }
}
