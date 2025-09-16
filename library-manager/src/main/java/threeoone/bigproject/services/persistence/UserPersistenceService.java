package threeoone.bigproject.services.persistence;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import threeoone.bigproject.entities.Member;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.repositories.UserRepo;

/**
 * A service to add, update and persist {@link User} entity to the Database.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
@Validated
@RequiredArgsConstructor
public class UserPersistenceService {
  private final UserRepo userRepo;

  /**
   * Updates an existing {@link User} whose {@code id} is the same as the given User's.
   * The given User's {@code id} must match with an existing {@link User} in the Database.
   *
   * @apiNote This method returns the saved {@link User} Entity instance,
   * which may be different from the given instance and may have different data.
   *
   * @param user the {@link User} to update
   *
   * @throws IllegalArgumentException when the given {@link User} has no {@code id}
   * @throws NoSuchElementException when the {@code id} of the given {@link User} doesn't match with any's in the Database
   * @throws ConstraintViolationException when the given {@link User} violates existing constraints
   * @throws RuntimeException when unexpected errors occur when working with Database (such as constraints violations)
   *
   * @return the saved {@link User} Entity instance, which may be different from the given instance
   */
  @Transactional
  public User update(@Valid @NotNull User user) {
    if (user.getId() == null) {
      throw new IllegalArgumentException("Attempting to update a User with no ID");
    }

    if (!userRepo.existsById(user.getId())) {
      throw new NoSuchElementException("No User with that ID exist");
    }

    return userRepo.save(user);
  }
}
