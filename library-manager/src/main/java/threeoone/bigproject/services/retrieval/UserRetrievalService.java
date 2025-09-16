package threeoone.bigproject.services.retrieval;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.entities.User;
import threeoone.bigproject.repositories.UserRepo;

@Service
@RequiredArgsConstructor
public class UserRetrievalService {
  private final UserRepo userRepo;

  /**
   * Retrieves all {@link User} from the Database as a List.
   *
   * @apiNote the returned List will not be {@code NULL}.
   *
   * @return a {@link List} containing every {@link User} in the Database
   */
  public List <User> getAll() {
    return userRepo.findAll();
  }

  /**
   * Retrieves a {@link User} with a specific {@code id}.
   * If no {@link User} with specified {@code id} exists,
   * this method returns {@code NULL}.
   *
   * @param id the {@code id} of the {@link User} to be retrieved
   *
   * @return the desired {@link User} if it exists, or {@code NULL} if otherwise
   */
  public User findById(int id) {
    return userRepo.findById(id).orElse(null);
  }

  /**
   * Retrieves a {@link User} with a specific {@code id} and its {@code uploadedDocuments}.
   * <p>
   * This method retrieves the desired {@link User} and all of its uploaded {@link Document} (if any),
   * as opposed to {@link #findById}, which only retrieves the desired {@link User} but not its {@code uploadedDocuments}.
   * If no {@link User} with the specified {@code id} exists, this method returns {@code NULL}.
   *
   * @apiNote The return User's {@code uploadedDocuments} list will not be {@code NULL}.
   *
   * @param id the id of the desired {@link User}
   *
   * @return the {@link User} with the specified {@code id}, along with its {@code uploadedDocuments},
   *         or {@code NULL} if it doesn't exist
   */
  public User findWithUploadedDocuments(int id) {
    return userRepo.findUserAndUploadedDocuments(id);
  }
}
