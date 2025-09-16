package com.ducbrick.finance_tracker.services.chat;

// --- Add imports for your DTOs here ---
// Assuming they are in a package like com.ducbrick.finance_tracker.dtos
import com.ducbrick.finance_tracker.dtos.ChatTokenResponseDto;
import com.ducbrick.finance_tracker.dtos.UserSectionChatDto;
// --- End DTO imports ---

import com.ducbrick.finance_tracker.services.LoggedInUserFinderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView; // Example for redirection

/**
 * Service class for handling interactions with the AI/Gateway service.
 * This service interacts with the {@link GenClient} to fetch responses and initiate chat sessions.
 *
 * @author HUY1902 (Updated)
 */
@Service
@RequiredArgsConstructor
public class GenService {

    private final GenClient genClient;
    private final LoggedInUserFinderService loggedInUserFinderService; // Assuming this provides the user ID string

    @Value("${streamlit.app.url}")
    private String streamlitBaseUrl;

    /**
     * Retrieves response based on question and database (Original Method).
     *
     * @param question from chatbot
     * @return response
     */
    public String getResponse(@NonNull String question) {
        // This likely needs updating based on how your chatbot interacts now
        System.out.println("WARNING: getResponse method might need updates for the new flow.");
        return genClient.getResponse(question);
    }


    /**
     * Initiates a chat session for the currently logged-in user by calling the gateway
     * and returns the response containing the chat token.
     *
     * @return ChatTokenResponseDto containing the token and expiry, or null on error.
     */
    public ChatTokenResponseDto initiateChatSessionForCurrentUser() {
        String userId = Integer.toString(loggedInUserFinderService.getId());

        if (userId == null || userId.isBlank()) {
            System.err.println("Error: Could not determine logged-in user ID.");
            return null;
        }

        // 1. Create the DTO for the request body
        UserSectionChatDto requestPayload = new UserSectionChatDto(userId);

        try {
            // 2. Call the Feign client method that uses DTOs
            System.out.println("Calling gateway to start session for user: " + userId);
            ChatTokenResponseDto response = genClient.startChatSessionWithDto(requestPayload);

            // 3. Log and return the response DTO
            if (response != null && response.getChat_token() != null) {
                System.out.println("Gateway responded with token: " + response.getChat_token() +
                        ", expires in: " + response.getExpires_in() + "s");
            } else {
                System.err.println("Error: Gateway did not return a valid response or token for user " + userId);
                return null;
            }
            return response;

        } catch (Exception e) {
            System.err.println("Error initiating chat session via gateway for user " + userId + ": " + e.getMessage());
            return null;
        }
    }

}
