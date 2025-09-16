package com.ducbrick.finance_tracker.config;

import feign.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Feign client options.
 * This class defines the timeout settings for the Feign client.
 *
 * @author HUY1902
 */
@Configuration
public class FeignConfig {

    @Value("${gen.ai.timeout}")
    private int timeout;

    /**
     * Create a request option with specified timeout values
     *
     * @return {@link Request.Options} instance with timeout values set
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(timeout, timeout);
    }
}
