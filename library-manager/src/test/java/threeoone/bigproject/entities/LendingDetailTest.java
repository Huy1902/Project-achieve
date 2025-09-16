package threeoone.bigproject.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LendingDetailTest {
  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("Test validations")
  void test_validations() {
    Member member = new Member();
    member.setName("  ");
    member.setPhoneNumber("29384sdkkfsdfskdjf");
    member.setAddress("  ");
    member.setEmail("xyz");

    LendingDetail lendingDetail = new LendingDetail();
    lendingDetail.setMember(member);


    for (ConstraintViolation violation : validator.validate(lendingDetail)) {
      System.out.println(violation.getMessage());
    }
    System.out.println();
  }
}