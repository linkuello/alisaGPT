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

            if (utterance == null || utterance.trim().isEmpty()) {
                utterance = "Привет!";
            }

            String reply = openAIService.askGpt(utterance);
            if (reply == null || reply.trim().isEmpty()) {
                reply = "Извините, я не смог придумать ответ.";
            }

            Map<String, Object> response = new HashMap<>();
            response.put("text", reply);
            response.put("end_session", false);

            Map<String, Object> result = new HashMap<>();
            result.put("response", response);
            result.put("version", "1.0");

            return result;

        } catch (Exception e) {
            // fallback ответ для Алисы при сбое
            Map<String, Object> response = new HashMap<>();
            response.put("text", "Произошла ошибка на сервере.");
            response.put("end_session", true);

            Map<String, Object> result = new HashMap<>();
            result.put("response", response);
            result.put("version", "1.0");

            return result;
        }
    }
}
