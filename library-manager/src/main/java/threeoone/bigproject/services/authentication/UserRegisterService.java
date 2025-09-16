package threeoone.bigproject.services.authentication;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.UserAlreadyExistException;
import threeoone.bigproject.repositories.UserRepo;

/**
 * A service to register a new {@link User} into the Database.
 * This class is a singleton bean in Spring context.
 *
 * @author DUCBRICK
 */
@Service
@RequiredArgsConstructor
@Validated
public class UserRegisterService {
  private final Validator validator;
  private final UserRepo userRepo;

  /**
   * Register a new {@link User} into the Database.
   * Not login when register successfully.
   *
   * @param user a User with necessary information to register.
   *
   * @throws UserAlreadyExistException when another User with the same username exists
   * @throws ConstraintViolationException when the given {@link User} violates existing constraints
   * @throws RuntimeException when unexpected errors occurs when working with the Database
   */
  @Transactional
  public void register(@NonNull @Valid User user) throws UserAlreadyExistException {
    user.setUsername(user.getUsername().stripTrailing());

    Set <ConstraintViolation <User>> violations = validator.validate(user);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }

    if (userRepo.findByUsername(user.getUsername()) != null) {
      throw new UserAlreadyExistException("There is another registered user with that username");
    }

    if (userRepo.existsByEmail(user.getEmail())) {
      throw new UserAlreadyExistException("There is another registered user with that email address");
    }

    user.setId(null);
    userRepo.save(user);
  }
}
