
package com.ashu.ollamaagent.service;

import com.ashu.ollamaagent.tools.AgentTools;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final Assistant assistant;

    public AgentService(
            OllamaChatModel chatModel,
            AgentTools agentTools) {

        ChatMemoryProvider memoryProvider = memoryId ->
                MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .build();

        this.assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryProvider)
                .tools(agentTools)
                .build();
    }

    public String chat(String message, String sessionId) {
        return assistant.chat(sessionId, message);
    }

    interface Assistant {

        @SystemMessage("""
            You are a helpful personal AI assistant.
            Answer questions clearly and accurately.
            Use available tools when needed for arithmetic
            or the current date and time.
            If you do not know an answer, say so.
            """)
        String chat(
                @MemoryId String sessionId,
                @UserMessage String message
        );
    }
}
