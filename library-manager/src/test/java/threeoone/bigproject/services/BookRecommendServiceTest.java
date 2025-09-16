package threeoone.bigproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookRecommendServiceTest {

  @Mock
  private BookRecommendClient bookRecommendClient;

  @InjectMocks
  private BookRecommendService bookRecommendService;


  @Test
  public void testGetRecommendedBooks() {
    // Given
    String title = "Angels";
    List<String> expectedBooks = Arrays.asList(
            "Faking It",
            "One for the Money (Stephanie Plum Novels (Paperback))",
            "Ishmael: An Adventure of the Mind and Spirit",
            "Girl in Hyacinth Blue",
            "Angels"
    );
    when(bookRecommendClient.getRecommendedBooks(title)).thenReturn(expectedBooks);

    // When
    List<String> recommendedBooks = bookRecommendService.getRecommendedBooks(title);

    // Then
    assertEquals(expectedBooks, recommendedBooks);
  }
}
