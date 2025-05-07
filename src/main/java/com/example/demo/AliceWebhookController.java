package com.example.demo;

import com.example.demo.OpenAIService;
import org.springframework.web.bind.annotation.*;

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
        try {
            Map<String, Object> requestMap = (Map<String, Object>) request.get("request");
            String utterance = (String) requestMap.get("original_utterance");

            if (utterance == null || utterance.isBlank()) {
                utterance = "Привет";
            }

            String gptResponse = openAIService.askGpt(utterance);
            if (gptResponse == null || gptResponse.isBlank()) {
                gptResponse = "Извините, я не смог придумать ответ.";
            }

            Map<String, Object> response = new HashMap<>();
            response.put("text", gptResponse);
            response.put("end_session", false);

            Map<String, Object> result = new HashMap<>();
            result.put("version", "1.0");
            result.put("response", response);

            return result;

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("text", "Произошла ошибка на сервере.");
            errorResponse.put("end_session", true);

            Map<String, Object> fallback = new HashMap<>();
            fallback.put("version", "1.0");
            fallback.put("response", errorResponse);

            return fallback;
        }
    }
}
