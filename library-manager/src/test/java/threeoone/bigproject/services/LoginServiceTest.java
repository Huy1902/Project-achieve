package threeoone.bigproject.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.exceptions.AlreadyLoggedInException;
import threeoone.bigproject.repositories.UserRepo;
import threeoone.bigproject.services.authentication.LoginService;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
  @Mock
  private UserRepo userRepo;

  @InjectMocks
  private LoginService loginService;

  @Test
  @DisplayName("Get non-existent logged in user id")
  public void getNonExistentLoggedInUserId() {
    assertThat(loginService.getLoggedInUserId()).isNull();
  }

  @Test
  @DisplayName("Log in with non-existent credentials")
  public void loginNonExistentCredentials() {
    User user = new User("name", "password");

    when(userRepo.findByUsername(user.getUsername())).thenReturn(null);

    assertThat(loginService.login(user)).isFalse();
  }

  @Test
  @DisplayName("Log in with incorrect credentials")
  public void loginIncorrectCredentials() {
    User user = new User("name", "password");
    User registered = new User("name", "correctPassword");

    when(userRepo.findByUsername(user.getUsername())).thenReturn(registered);

    assertThat(loginService.login(user)).isFalse();
  }

  @Test
  @DisplayName("Login with correct credentials")
  public void loginCorrectCredentials() {
    User user = new User("name", "password");

    int id = 0;

    User registered = new User("name", "password");
    registered.setId(id);

    when(userRepo.findByUsername(user.getUsername())).thenReturn(registered);

    assertThat(loginService.login(user)).isTrue();
    assertThat(loginService.getLoggedInUserId()).isEqualTo(id);
    assertThatThrownBy(() -> loginService.login(new User("abc", "def")))
        .isInstanceOf(AlreadyLoggedInException.class);

    loginService.logout();

    assertThat(loginService.getLoggedInUserId()).isNull();
    loginService.login(new User("abc", "def"));
  }
}