package com.ducbrick.finance_tracker.controllers;

// --- Add imports for your DTOs if needed ---
import com.ducbrick.finance_tracker.dtos.ChatTokenResponseDto;
// --- End DTO imports ---

import com.ducbrick.finance_tracker.services.chat.GenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // For reading config
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // To pass data to the view
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// Removed ResponseBody as we are returning a view name

@Controller
@RequestMapping("/ai-chatbot")
@RequiredArgsConstructor
public class AssistantController {

    private final GenService genService;

    // Inject Streamlit base URL from application properties for flexibility
    @Value("${streamlit.app.url:http://localhost:8501}") // Default value if not set
    private String streamlitBaseUrl;

    @GetMapping
    public String aiAssistantPage(Model model) { // Inject Spring's Model
        System.out.println("GET /ai-chatbot received. Initiating chat session...");

        // 1. Initiate session with the gateway to get the token
        ChatTokenResponseDto tokenResponse = genService.initiateChatSessionForCurrentUser();

        String streamlitUrlWithToken = streamlitBaseUrl; // Default URL

        if (tokenResponse != null && tokenResponse.getChat_token() != null) {
            // 2. Construct the full Streamlit URL with the token
            streamlitUrlWithToken = streamlitBaseUrl + "/?token=" + tokenResponse.getChat_token();
            System.out.println("Successfully obtained token. Setting iframe src to: " + streamlitUrlWithToken);
        } else {
            // Handle error: Could not get token from gateway
            System.err.println("Failed to obtain chat token from gateway. Streamlit app might not function correctly.");
        }

        // 3. Add the URL (with or without token) to the Spring Model
        model.addAttribute("streamlitUrl", streamlitUrlWithToken);

        // 4. Return the name of the Thymeleaf template
        return "aiAssistant"; // Renders src/main/resources/templates/aiAssistant.html
    }
}
