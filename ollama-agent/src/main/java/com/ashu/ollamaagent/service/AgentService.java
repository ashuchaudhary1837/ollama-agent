
package com.ashu.ollamaagent.service;

import com.ashu.ollamaagent.tools.AgentTools;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;

import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final Assistant assistant;

    public AgentService(
            OllamaChatModel chatModel,
            AgentTools agentTools,
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore) {

        ChatMemoryProvider memoryProvider = memoryId ->
                MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .build();

        ContentRetriever contentRetriever =
                EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel)
                        .maxResults(3)
                        .build();

        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryProvider)
                .tools(agentTools)
                .contentRetriever(contentRetriever)
                .build();
    }

    public String chat(String message, String sessionId) {
        return assistant.chat(sessionId, message);
    }

    interface Assistant {

        @SystemMessage("""
                You are a helpful personal AI assistant.

                Use the retrieved document context when it is relevant
                to the user's question.

                If the answer is not available in the retrieved context,
                say that the uploaded documents do not contain enough
                information. Do not invent facts about their contents.

                For general questions unrelated to uploaded documents,
                use your general knowledge.

                Keep answers clear and concise.
                """)
        String chat(
                @MemoryId String sessionId,
                @UserMessage String message
        );
    }
}
