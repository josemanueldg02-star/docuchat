package com.josemanuel.docuchat.controller;

// IMPORTS
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Value("${spring.ai.google.genai.api-key:NO_CARGADA}")
    private String apiKey;

    @GetMapping
    public Map<String, Object> debug() {
        String masked;
        if (apiKey == null || apiKey.equals("NO_CARGADA") || apiKey.isBlank()) {
            masked = "VACÍA o NO CARGADA";
        } else {
            int len = apiKey.length();
            String start = apiKey.substring(0, Math.min(4, len));
            String end = len > 4 ? apiKey.substring(len -4) : "";
            masked = start + "..." + end + " (longitud: " + len + ")";
        }
        return Map.of("apiKeyStatus", masked);
    }
    
}
