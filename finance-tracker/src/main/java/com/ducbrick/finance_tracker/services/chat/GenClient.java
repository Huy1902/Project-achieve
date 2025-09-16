package com.ducbrick.finance_tracker.services.chat;

import com.ducbrick.finance_tracker.dtos.ChatTokenResponseDto;
import com.ducbrick.finance_tracker.dtos.UserSectionChatDto;

import com.ducbrick.finance_tracker.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;

import java.util.Map;

/**
 * Feign client interface for interacting with the Chat Gateway service.
 * This client uses Feign to make HTTP requests to the external service specified
 * by ${gen.ai.url} (which should point to the Flask Gateway).
 *
 * @author HUY1902 (Updated)
 */
// Ensure name/url properties point to your Flask Gateway deployment
@FeignClient(name = "${gen.ai.name}", url = "${gateway.app.url}", configuration = FeignConfig.class)
@Component
public interface GenClient {

    // --- Keep existing methods if still needed for other purposes ---
    @GetMapping("/get-response")
    String getResponse(@RequestParam("title") String question);

    // --- Method to call the Gateway using DTOs ---

    /**
     * Calls the gateway to start a chat session and get a temporary token using DTOs.
     * Sends a POST request to /gateway/session/start with a JSON body derived from UserIdRequest.
     * Expects a JSON response that maps to ChatTokenResponse.
     *
     * @param userIdRequest The request object containing the user_id.
     * @return The response object containing the chat_token and expires_in.
     */
    @PostMapping(value = "/gateway/session/start",
            consumes = MediaType.APPLICATION_JSON_VALUE, // Expects JSON response
            produces = MediaType.APPLICATION_JSON_VALUE) // Sends JSON body
    ChatTokenResponseDto startChatSessionWithDto(@RequestBody UserSectionChatDto userIdRequest);

    /**
     * Calls the gateway to start a chat session and get a temporary token using Maps.
     * Sends a POST request to /gateway/session/start with a JSON body.
     *
     * @param userIdPayload A Map representing the JSON body, e.g., Map.of("user_id", "someUserId")
     * @return A Map representing the JSON response, e.g., {"chat_token": "...", "expires_in": ...}
     */
    @PostMapping(value = "/gateway/session/start",
            consumes = MediaType.APPLICATION_JSON_VALUE, // Expects JSON response
            produces = MediaType.APPLICATION_JSON_VALUE) // Sends JSON body
    Map<String, Object> startChatSessionWithMap(@RequestBody Map<String, String> userIdPayload);

}
