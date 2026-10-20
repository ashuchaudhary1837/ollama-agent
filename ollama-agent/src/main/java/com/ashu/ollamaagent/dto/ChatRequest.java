
package com.ashu.ollamaagent.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "Message cannot be empty")
        String message,

        @NotBlank(message = "Session ID is required")
        String sessionId
) {}
