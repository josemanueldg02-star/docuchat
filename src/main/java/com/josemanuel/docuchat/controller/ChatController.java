package com.josemanuel.docuchat.controller;

// IMPORTS
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josemanuel.docuchat.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> ask(@RequestBody ChatRequest request) {
        String answer = chatService.ask(request.question());
        return ResponseEntity.ok(Map.of("answer", answer));
    }

    public record ChatRequest(String question) {}

}
