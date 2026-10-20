
package com.ashu.ollamaagent.controller;

import com.ashu.ollamaagent.dto.ChatRequest;
import com.ashu.ollamaagent.dto.ChatResponse;
import com.ashu.ollamaagent.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AgentService agentService;

    public ChatController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public ChatResponse chat(
            @Valid @RequestBody ChatRequest request) {

        String reply = agentService.chat(
                request.message(),
                request.sessionId()
        );

        return new ChatResponse(
                request.sessionId(),
                reply
        );
    }
}
