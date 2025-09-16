package threeoone.bigproject.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import threeoone.bigproject.entities.Document;
import threeoone.bigproject.util.Alerts;

/**
 * Service class for interacting with the Google Books API.
 *
 * @author HUY1902
 */
@Setter
@Getter
@Service
public class GoogleAPIService {

  @Value("${google.api.url}")
  @Setter
  private String googleBooksURL;

  @Value("${google.api.timeout}")
  private int googleBooksTimeout;

  @Value("${google.api.key}")
  @Setter
  private String googleBooksKey;

  /**
   * Finds a book by its ISBN using the Google Books API.
   *
   * @param isbn the ISBN of the book to search for.
   * @return a {@link Document} representing the book details; otherwise, null if not found
   */
  public Document findBookByISBN(String isbn) {
    JsonObject response = getJson(googleBooksURL + "isbn:" + isbn + "&key=" + googleBooksKey);
    if (response == null) {
      return null;
    }
    if (response.get("totalItems").getAsInt() == 0) {
      Alerts.showAlertWarning("Error!!!", "Not found");
      return null;
    }
    JsonObject info = response.get("items").getAsJsonArray().get(0).getAsJsonObject().get("volumeInfo").getAsJsonObject();
    Document document = new Document(info.get("title").getAsString(), info.get("description").getAsString());
    StringBuilder authors = new StringBuilder();
    if (info.get("authors") != null) {
      for (JsonElement author : info.get("authors").getAsJsonArray()) {
        authors.append(author.getAsString()).append(", ");
      }
      authors.delete(authors.length() - 2, authors.length());
      document.setAuthor(authors.toString());
    }
    document.setIsbn(isbn);
    if (info.get("categories") != null) {
      StringBuilder categories = new StringBuilder();
      for (JsonElement category : info.get("categories").getAsJsonArray()) {
        categories.append(category.getAsString()).append(", ");
      }
      categories.delete(categories.length() - 2, categories.length());
      document.setCategory(categories.toString());
    } else document.setCategory("Unknown");
    if (info.get("imageLinks") != null) {
      document.setCoverImageUrl(info.get("imageLinks").getAsJsonObject().get("thumbnail").getAsString());
    }
    return document;
  }

  /**
   * Creates and configures a {@link RestTemplate} instance with custom timeout settings.
   * <p>
   * The timeout settings are configured using the {@link SimpleClientHttpRequestFactory} based on the
   * {@code googleBooksTimeout} value.
   * </p>
   *
   * @return a configured {@link RestTemplate} instance.
   */
  public RestTemplate getRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    SimpleClientHttpRequestFactory requestFactory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
    requestFactory.setReadTimeout(googleBooksTimeout);
    requestFactory.setConnectTimeout(googleBooksTimeout);

    return restTemplate;
  }

  /**
   * Fetches a JSON response from the given URL.
   * Using customized {@link RestTemplate}
   *
   * @param url the URL to fetch the JSON from.
   * @return a {@link JsonObject} containing the response data.
   */
  public JsonObject getJson(String url) {
    ResponseEntity<String> response = null;
    try {
      response = getRestTemplate().getForEntity(url, String.class);
    } catch (RestClientException e) {
      Platform.runLater(() -> Alerts.showAlertWarning("Error!!!", e.getMessage()));
      return null;
    }

    if (!response.getStatusCode().is2xxSuccessful()) {
      ResponseEntity<String> finalResponse = response;
      Platform.runLater(() -> Alerts.showAlertWarning("Error!!!" + finalResponse.getStatusCode(), finalResponse.getBody()));
      return null;
    }

    return new Gson().fromJson(response.getBody(), JsonObject.class);
  }

}
