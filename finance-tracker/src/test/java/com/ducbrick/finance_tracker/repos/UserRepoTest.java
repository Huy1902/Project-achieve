package com.ducbrick.finance_tracker.repos;

import com.ducbrick.finance_tracker.entities.User;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepoTest {
  @Autowired
  UserRepo userRepo;

  @DisplayName("Save 2 users with same username")
  @Test
  void usersWithSameUsername() {
    User user1 = User.builder()
            .username("sadhfjsdhafkakjlsfd")
            .email("kjdsf@gmail.com")
            .password("alsdfjljsadf")
            .build();

    User user2 = User.builder()
            .username(user1.getUsername())
            .email("kjdsf@gmail.com")
            .password("alsdfjljsadf")
            .build();

    userRepo.save(user1);

    assertThatThrownBy(() -> userRepo.save(user2))
            .isInstanceOf(RuntimeException.class);
  }
}