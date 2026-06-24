package com.josemanuel.docuchat.service;

// IMPORTS
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;

@Service
public class IngestionService {
    
    private final VectorStore vectorStore;

    public IngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingestText(String text, String sourceName) {
        // 1. Envolver el texto en un Document con metadatos.
        Document document = new Document(text, Map.of("source", sourceName));

        // 2. Trocear en chunks por número de tokens.
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();
        List<Document> chunks = splitter.apply(List.of(document));

        // 3. Guardar en pgvector: generamos los embeddings con Gemini y los persiste.
        vectorStore.add(chunks);
        return chunks.size();
    }

}
