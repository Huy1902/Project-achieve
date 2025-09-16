package threeoone.bigproject.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("Test validations")
  void test_validation() {
    Member member = new Member();
    member.setName("  ");
    member.setPhoneNumber("29384sdkkfsdfskdjf");
    member.setAddress("  ");
    member.setEmail("xyz@lolol");

    for (ConstraintViolation violation : validator.validate(member)) {
      System.out.println(violation.getMessage());
    }
    System.out.println();
  }
}