package com.research.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class ResearchService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    // Get instance of webClient
    public ResearchService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String processContent(ResearchRequest request) {
        // 1. Build the prompt
        String prompt = buildPrompt(request);

        // 2. Query the AI Model API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                            Map.of("text", prompt)
                        })
                });

        // Make a post request using webClient
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        // 4. Return response
        return extractTextFromResponse(response);
    }

    private String extractTextFromResponse(String response) {
        try {
            // 3. Parse the response
            // Use ObjectMapper to get JSON mapped to object of GeminiResponse
            GeminiResponse geminiResponse = new ObjectMapper().readValue(response, GeminiResponse.class);

            // Return the actual output(text) under the text of first candidate from the API
            if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                GeminiResponse.Candidate firstCandidate = geminiResponse.getCandidates().get(0);
                if (firstCandidate.getContent() != null &&
                        firstCandidate.getContent().getParts() != null &&
                        !firstCandidate.getContent().getParts().isEmpty()) {
                    return firstCandidate.getContent().getParts().get(0).getText();
                }
            }
             return "No text found in the response";
        } catch (Exception e) {
            return "Error parsing: " + e.getMessage();
        }
    }

    // Build the prompt
    private String buildPrompt(ResearchRequest request) {
        StringBuilder prompt = new StringBuilder();
        // Create prompt based on the operation
        switch(request.getOperation()) {
            case "summarize":
                prompt.append("Provide a clear and concise summary of the following text in a few sentences:\n\n");
                break;
            case "suggest":
                prompt.append("Based on the following content: suggest related topics and further reading. Format the response with clear headings and bullet points");
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Opeartion: " + request.getOperation());
        }
        // Appending content to the prompt
        prompt.append(request.getContent());
        return prompt.toString();
    }
}
