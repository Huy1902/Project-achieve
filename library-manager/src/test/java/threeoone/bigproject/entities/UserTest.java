package threeoone.bigproject.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("Validate username and password")
  void validateUsernamePassword() {
    User user = new User("", "");

    for (ConstraintViolation violation : validator.validate(user)) {
      System.out.println(violation.getMessage());
    }
  }
}