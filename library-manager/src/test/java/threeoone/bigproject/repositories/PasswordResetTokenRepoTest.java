package threeoone.bigproject.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import threeoone.bigproject.entities.PasswordResetToken;
import threeoone.bigproject.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PasswordResetTokenRepoTest {
  @Autowired
  UserRepo userRepo;

  @Autowired
  PasswordResetTokenRepo passwordResetTokenRepo;

  @Test
  @DisplayName("Delete tokens associated to a user")
  void deleteTokensAssociatedToAUser() {
    User user = new User();
    user.setUsername("dsifajks");
    user.setPassword("sdlkfjsd");
    user.setEmail("sadjfaklfds@rizzler.mog");

    user = userRepo.save(user);

    var token1 = new PasswordResetToken();
    token1.setValue("skldfkja");
    token1.setExpireTime(LocalDateTime.now());
    token1.setUser(user);

    token1 = passwordResetTokenRepo.save(token1);

    var token2 = new PasswordResetToken();
    token2.setValue("sadjfasdlkfj");
    token2.setExpireTime(LocalDateTime.now());
    token2.setUser(user);

    token2 = passwordResetTokenRepo.save(token2);

    passwordResetTokenRepo.deleteByUser(user.getId());

    assertThat(passwordResetTokenRepo.existsById(token1.getId())).isFalse();
    assertThat(passwordResetTokenRepo.existsById(token2.getId())).isFalse();
  }

  @Test
  @DisplayName("Retrieve token by its value")
  void findByValue() {
    User user = new User();
    user.setUsername("sdafjklsdf");
    user.setPassword("ijsdafksa");
    user.setEmail("sdjfklsadf@rizzler.mog");

    user = userRepo.save(user);

    var token1 = new PasswordResetToken();
    token1.setValue("klsjdafsdfk");
    token1.setExpireTime(LocalDateTime.now());
    token1.setUser(user);

    token1 = passwordResetTokenRepo.save(token1);

    var token2 = new PasswordResetToken();
    token2.setValue("ksdafjakfsd");
    token2.setExpireTime(LocalDateTime.now());
    token2.setUser(user);

    token2 = passwordResetTokenRepo.save(token2);

    var token3 = new PasswordResetToken();
    token3.setValue("lksdjkalsdfj");
    token3.setExpireTime(LocalDateTime.now());
    token3.setUser(user);

    token3 = passwordResetTokenRepo.save(token3);

    assertThat(passwordResetTokenRepo.findByValue(null)).isNull();
    assertThat(passwordResetTokenRepo.findByValue("skibidi")).isNull();
    assertThat(passwordResetTokenRepo.findByValue(token1.getValue())).isSameAs(token1);
    assertThat(passwordResetTokenRepo.findByValue(token2.getValue())).isSameAs(token2);
    assertThat(passwordResetTokenRepo.findByValue(token3.getValue())).isSameAs(token3);
  }
}