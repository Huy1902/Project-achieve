package threeoone.bigproject.services;

import com.google.gson.JsonObject;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.util.Alerts;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@ExtendWith(MockitoExtension.class)
class GoogleAPIServiceTest extends ApplicationTest {
  @InjectMocks
  private final GoogleAPIService googleAPIService = new GoogleAPIService();

  @Test
  @DisplayName("testTimeout")
  void testTimeout() {
    googleAPIService.setGoogleBooksTimeout(5000);
    int googleBooksTimeout = googleAPIService.getGoogleBooksTimeout();
    assertEquals(5000, googleBooksTimeout);
  }

  @Test
  @DisplayName("testGetJson")
  void testGetJson1() {
    googleAPIService.setGoogleBooksURL("https://www.googleapis.com/books/v1/volumes?q=isbn:0735619670");
    googleAPIService.setGoogleBooksTimeout(5000);
    JsonObject jsonObject = googleAPIService.getJson(googleAPIService.getGoogleBooksURL());
    assertNotNull(jsonObject);
  }

  @Test
  @DisplayName("testGetJsonWithFailTimeout")
  void testGetJsonWithFailTimeout() {
    googleAPIService.setGoogleBooksTimeout(50);
    Platform.runLater(() -> {
      JsonObject jsonObject = googleAPIService.getJson("https://www.googleapis.com/books/v1/volumes?q=isbn:0735619670");
      assertNull(jsonObject);
    });
  }

  @Test
  @DisplayName("testFindBookByISBN")
  void testFindBookByISBN() {
    googleAPIService.setGoogleBooksTimeout(5000);
    googleAPIService.setGoogleBooksURL("https://www.googleapis.com/books/v1/volumes?q=");
    googleAPIService.setGoogleBooksKey("");
    Document document = googleAPIService.findBookByISBN("0735619670");
    assertEquals("Code Complete", document.getName());
  }

  @Test
  @DisplayName("testWithManyResult")
  void testWithManyResult() {
    googleAPIService.setGoogleBooksTimeout(5000);
    googleAPIService.setGoogleBooksURL("https://www.googleapis.com/books/v1/volumes?q=");
    Platform.runLater(() -> {
      Document document = googleAPIService.findBookByISBN("073");
      assertNull(document);
    });
  }
}