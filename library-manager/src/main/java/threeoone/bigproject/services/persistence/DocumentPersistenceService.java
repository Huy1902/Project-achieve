package threeoone.bigproject.services.persistence;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.DocumentAlreadyExistException;
import threeoone.bigproject.exceptions.IllegalDocumentInfoException;
import threeoone.bigproject.exceptions.NotLoggedInException;
import threeoone.bigproject.repositories.DocumentRepo;
import threeoone.bigproject.repositories.UserRepo;
import threeoone.bigproject.services.authentication.LoginService;

/**
 * A service to modify, which is to add, update, or delete {@link Document} entities from the Database.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
@RequiredArgsConstructor
@Validated
public class DocumentPersistenceService {
  private final Validator validator;

  private final DocumentRepo documentRepo;
  private final UserRepo userRepo;
  private final LoginService loginService;

  /**
   * Saves a new {@link Document}.
   * <p>
   * This method sets the Document's {@code uploader} to the current logged in {@link User}.
   * If no {@link User} is currently logged in, this method throws a {@link NotLoggedInException}.
   * <p>
   * This method sets the new Document's {@code uploadTime} to the {@link LocalDateTime} at the moment.
   *
   * @apiNote This method returns the saved {@link Document} Entity instance,
   * which may be different from the given instance and may have different data.
   *
   * @param document the {@link Document} to add
   *
   * @throws NotLoggedInException when no Users are currently logged in
   * @throws ConstraintViolationException when the given {@link Document} violates existing constraints
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violations)
   *
   * @return the saved {@link Document} Entity instance, which may be different from the given instance
   */
  @Transactional
  public Document add(@NotNull Document document) throws DocumentAlreadyExistException {
    Integer uploaderId = loginService.getLoggedInUserId();

    if (uploaderId == null) {
      throw new NotLoggedInException("Attempting to upload a Document while not logged in");
    }

    if (documentRepo.existsByIsbn(document.getIsbn())) {
      throw new DocumentAlreadyExistException("Another document with that ISBN already exists");
    }

    User uploader = userRepo.findUserAndUploadedDocuments(uploaderId);

    if (uploader == null) {
      throw new NotLoggedInException("No User with that ID exists");
    }

    uploader.addUploadedDocument(document);
    document.setId(null);
    document.setUploadTime(LocalDateTime.now());

    Set <ConstraintViolation <Document>> violations = validator.validate(document);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    return documentRepo.save(document);
  }

  /**
   * Updates an existing {@link Document} whose {@code id} is the same as the given Document's.
   * The given Document's {@code id} must match with an existing {@link Document} in the Database.
   *
   * @apiNote This method returns the saved {@link Document} Entity instance,
   * which may be different from the given instance and may have different data.
   *
   * @param document the {@link Document} to update
   *
   * @throws IllegalDocumentInfoException when the {@code copies} of the {@link Document} is less than the number of currently lent copies
   * @throws IllegalArgumentException when the given Document's {@code id} doesn't match any's in the Database
   * @throws ConstraintViolationException when the given {@link Document} violates existing constraints
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violations)
   *
   * @return the saved {@link Document} Entity instance, which may be different from the given instance
   */
  @Transactional
  public Document update(@Valid @NotNull Document document) {
    if (document.getId() == null) {
      throw new IllegalArgumentException("Attempting to update a Document with no ID");
    }

    Document oldInstance = documentRepo.findWithLendingDetails(document.getId());

    if (oldInstance == null) {
      throw new IllegalArgumentException("No Documents with that ID exist");
    }

    int lentCopies = oldInstance.getLendingDetails().size();

    if (document.getCopies() < lentCopies) {
      throw new IllegalDocumentInfoException("Attempting to set the total number of physical copies to less than the number of currently lent copies");
    }

    return documentRepo.save(document);
  }

  /**
   * Deletes the {@link Document} with the given {@code id}.
   * If the {@link Document} is not found in the persistence store it is silently ignored.
   *
   * @param id the {@code id} of the {@link Document} to delete
   */
  @Transactional
  public void delete(int id) {
    documentRepo.deleteById(id);
  }
}
