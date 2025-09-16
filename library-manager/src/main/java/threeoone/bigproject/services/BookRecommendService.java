package threeoone.bigproject.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for handling book recommendations.
 * This service interacts with the BookRecommendClient to fetch recommended book titles.
 *
 * @author HUY1902
 */
@Service
@RequiredArgsConstructor
public class BookRecommendService {

  /**
   * Feign client for interacting with the book recommendation service.
   */
  private final BookRecommendClient bookRecommendClient;

  /**
   * Retrieves a list of recommended books based on the provided title.
   * @apiNote this list returned is not be {@code null}
   *
   * @param title the title of the book to get recommendations for; must not be null
   * @return a list of recommended book titles
   */
  public List<String> getRecommendedBooks(@NonNull String title) {
    return bookRecommendClient.getRecommendedBooks(title);
  }
}
