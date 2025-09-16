package threeoone.bigproject.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import threeoone.bigproject.entities.User;

/**
 * Simple JPA repository for {@link User} entity.
 *
 * @author DUCBRICK
 */
public interface UserRepo extends ListCrudRepository <User, Integer> {
  /**
   * Retrieves a {@link User} with a specific {@code id} and its {@code uploadedDocuments}.
   * This method only executes a single SQL query.
   * This method retrieves the desired {@link User} and all of its uploaded {@link threeoone.bigproject.entities.Document},
   * as opposed to {@link #findById}, which only retrieves the desired {@link User} and not its {@code uploadedDocuments}.
   *
   * @param id the id of the desired {@link User}
   *
   * @return the {@link User} with the input id, along with its {@code uploadedDocuments}.
   *
   * @see User
   * @see threeoone.bigproject.entities.Document
   */
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.uploadedDocuments WHERE u.id = (:id)")
  public User findUserAndUploadedDocuments(@Param("id") int id);

  /**
   * Retrieves an unique {@link User} with a specific {@code loginName}.
   * As each User's {@code loginName} is unique, this method is guaranteed to return at most one {@link User}.
   * If no {@link User} with the specified {@code loginName} exists, returns {@code null}.
   *
   * @param username the loginName of the desired {@link User}
   *
   * @return the unique {@link User} with the specified {@code loginName} if it exists, otherwise null
   */
  @Query("SELECT u FROM User u WHERE u.username = (:username)")
  public User findByUsername(@Param("username") String username);

  /**
   * Checks if a {@link User} with a specific {@code email} exists.
   *
   * @param email the email of the desired {@link User}
   *
   * @return true if a {@link User} with the specified {@code email} exists, otherwise false
   */
  @Query("""
      SELECT
        CASE
          WHEN COUNT(u) > 0 THEN true
          ELSE false
        END
      FROM User u 
      WHERE u.email = (:email)""")
  boolean existsByEmail(@Param("email") String email);
}
