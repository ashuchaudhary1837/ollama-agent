
package com.ashu.ollamaagent.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public RagService(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore) {

        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    public void ingestText(String text) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "Document content cannot be empty"
            );
        }

        Document document = Document.from(text);

        EmbeddingStoreIngestor ingestor =
                EmbeddingStoreIngestor.builder()
                        .documentSplitter(
                                DocumentSplitters.recursive(500, 50)
                        )
                        .embeddingModel(embeddingModel)
                        .embeddingStore(embeddingStore)
                        .build();

        ingestor.ingest(document);
    }
}
