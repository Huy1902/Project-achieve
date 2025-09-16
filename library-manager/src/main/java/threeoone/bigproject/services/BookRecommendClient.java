package threeoone.bigproject.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import threeoone.bigproject.config.FeignConfig;

import java.util.List;

/**
 * Feign client interface for interacting with the book recommendation service.
 * This client uses Feign to make HTTP requests to the external service specified in the application properties.
 *
 * @author HUY1902
 */
@FeignClient(name = "${recommender.api.name}", url = "${recommender.api.url}", configuration = FeignConfig.class)
@Component
public interface BookRecommendClient {

  /**
   * Retrieves a list of recommended books based on the provided title.
   * This method maps to the "/get-recommend" endpoint of the book recommendation service.
   *
   * @param title the title of the book to get recommendations for
   * @return a list of recommended book titles
   */
  @GetMapping("/get-recommend")
  List<String> getRecommendedBooks(@RequestParam("title") String title);
}
