package threeoone.bigproject.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration class for setting up Feign client options.
 * This class defines the timeout settings for the Feign client.
 *
 * @author HUY1902
 */
@Configuration
public class FeignConfig {

  @Value("${recommender.api.timeout}")
  private int timeout;

  /**
   * Creates a Request.Options bean with the specified timeout values.
   * This bean is used by Feign clients to configure the connection and read timeouts.
   *
   * @return a Request.Options instance with the timeout values set
   */
  @Bean
  public Request.Options requestOptions() {
    // Use the same timeout value for both connection and read timeouts
    return new Request.Options(timeout, timeout);
  }
}
