
package com.ashu.ollamaagent.controller;

import com.ashu.ollamaagent.dto.ChatRequest;
import com.ashu.ollamaagent.dto.ChatResponse;
import com.ashu.ollamaagent.service.AgentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AgentService agentService;

    public ChatController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {

        // Use the supplied session ID, or create one if missing.
        String sessionId = request.sessionId();

        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        // Send the message to the LangChain4j AI service.
        String reply = agentService.chat(
                request.message(),
                sessionId
        );

        // Return the session ID and AI response.
        return new ChatResponse(sessionId, reply);
    }
}
