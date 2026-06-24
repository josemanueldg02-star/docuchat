package com.josemanuel.docuchat.controller;

// IMPORTS
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.josemanuel.docuchat.service.IngestionService;

@RestController
@RequestMapping("/api/ingest")
public class IngestionController {
    
private final IngestionService ingestionService;

public IngestionController(IngestionService ingestionService) {
    this.ingestionService = ingestionService;
}

@PostMapping
public ResponseEntity<Map<String, Object>> ingest(@RequestBody IngestRequest request) {
    int chunks = ingestionService.ingestText(request.text(), request.sourceName());
    return ResponseEntity.ok(Map.of("message", "Document ingested successfully", "chunksCreated", chunks));
}

// Record interno para recibir el JSON del cuerpo de la petición.
public record IngestRequest(String text, String sourceName) {}
}
