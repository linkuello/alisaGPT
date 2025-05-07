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
        Map<String, Object> requestMap = (Map<String, Object>) request.get("request");
        String utterance = (String) requestMap.get("original_utterance");

        String reply = openAIService.askGpt(utterance);

        Map<String, Object> response = new HashMap<>();
        response.put("text", reply);
        response.put("end_session", false);

        Map<String, Object> result = new HashMap<>();
        result.put("response", response);
        result.put("version", "1.0");

        return result;
    }
}
