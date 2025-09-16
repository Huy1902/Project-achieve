package com.ducbrick.finance_tracker.repos;

import com.ducbrick.finance_tracker.entities.Category;
import com.ducbrick.finance_tracker.entities.User;
import jakarta.persistence.EntityManager;
import org.apache.catalina.LifecycleState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CategoryRepoTest {

  @Autowired EntityManager entityManager;
  @Autowired CategoryRepo categoryRepo;
  @Autowired UserRepo userRepo;

  @Test
  @DisplayName("Save 1 category")
  public void save1category() {
    User owner = User.builder()
            .username("sdjfafsjkd")
            .password(new BCryptPasswordEncoder().encode("jsdfajkdf"))
            .email("sdjhfk@gmail.com")
            .build();

    owner = userRepo.save(owner);

    Category category = Category.builder()
            .name("djshaf")
            .color("ff0000")
            .owner(owner)
            .build();

    category = categoryRepo.save(category);

    entityManager.refresh(owner);

    owner = userRepo.findById(owner.getId()).orElseThrow();
    List<Category> categories = owner.getCategories();

    assertThat(categories.size()).isEqualTo(1);
    assertThat(categories.getFirst()).isSameAs(category);
    assertThat(category.getOwner()).isSameAs(owner);
  }
}