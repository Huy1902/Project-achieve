package threeoone.bigproject.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ORM Entity representing a token used for authenticating a request to reset a {@link User} password.
 * <p>
 * A token has a unique String {@link #value}, expires at {@link #expireTime} and is used to authenticate a specific {@link #user}.
 * <p>
 * {@link PasswordResetToken} has a uni-directional many-to-one relationship with {@link User} but doesn't support cascading.
 * So it expects the {@link User} to already exist.
 * Also, client must manually delete any {@link PasswordResetToken} associated to a specific {@link User} before deleting it.
 *
 * @author DUCBRICK
 */
@Entity
@Table(name = "password_reset_token")
@NoArgsConstructor @Getter @Setter
public class PasswordResetToken {
  public static final int MAX_TOKEN_LENGTH = 64;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true)
  @NotBlank(message = "Password reset token must have a value")
  @Size(max = MAX_TOKEN_LENGTH,
      message = "Password reset token must have at most " + MAX_TOKEN_LENGTH + " characters")
  private String value;

  @Column(name = "expire_time")
  @NotNull(message = "Password reset token must expire at one point")
  private LocalDateTime expireTime;

  //No need for cascading
  @ManyToOne
  @JoinColumn(name = "user_id")
  @NotNull(message = "Password reset token must be linked to a user")
  @Valid
  private User user;
}
