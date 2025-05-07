package com.example.demo;

import com.example.demo.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AliceWebhookController {

    private final OpenAIService openAIService;

    public AliceWebhookController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/webhook")
    public Map<String, Object> handleAliceRequest(@RequestBody Map<String, Object> request) {
        Map<String, Object> requestData = (Map<String, Object>) request.get("request");
        String userUtterance = (String) requestData.getOrDefault("original_utterance", "");

        String gptReply = openAIService.askGpt(userUtterance);
        if (gptReply == null || gptReply.trim().isEmpty()) {
            gptReply = "Извините, я не смог придумать ответ.";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("text", gptReply);
        response.put("end_session", false);

        Map<String, Object> fullResponse = new HashMap<>();
        fullResponse.put("response", response);
        fullResponse.put("version", "1.0");

        return fullResponse;
    }


}
