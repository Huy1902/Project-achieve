package threeoone.bigproject.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import threeoone.bigproject.entities.PasswordResetToken;
import threeoone.bigproject.entities.User;

/**
 * Jpa repository for {@link PasswordResetToken} entity.
 *
 * @author DUCBRICK
 */
public interface PasswordResetTokenRepo extends ListCrudRepository <PasswordResetToken, Integer> {
  /**
   * Delete any, every and all {@link PasswordResetToken} associated to a specific {@link User}.
   *
   * @param userId the {@code id} of the {@link User} whose tokens are to be deleted
   */
  @Modifying
  @Query("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
  void deleteByUser(Integer userId);

  /**
   * Retrieves the unique {@link PasswordResetToken} with a specific {@code value}.
   * As each token's {@code value} is unique, this method is guaranteed to return at most one {@link PasswordResetToken}.
   * If no {@link PasswordResetToken} with the specified {@code value} exists, returns {@code NULL}.
   *
   * @param value the value of the desired {@link PasswordResetToken}
   *
   * @return the unique {@link PasswordResetToken} with the specified {@code value} if it exists, otherwise {@code NULL}
   */
  @Query("SELECT t FROM PasswordResetToken t WHERE t.value = (:value)")
  PasswordResetToken findByValue(@Param("value") String value);
}
