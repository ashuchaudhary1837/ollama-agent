
package com.ashu.ollamaagent.service;

import com.ashu.ollamaagent.tools.AgentTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final ChatClient chatClient;
    private final AgentTools agentTools;

    public AgentService(
            ChatClient.Builder builder,
            AgentTools agentTools) {

        this.agentTools = agentTools;

        ChatMemory memory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();

        this.chatClient = builder
                .defaultSystem("""
                    You are a helpful personal AI assistant.
                    Answer questions clearly and accurately.
                    Use the available tools when needed for
                    arithmetic or the current date and time.
                    If you do not know an answer, say so.
                    """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )
                .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient.prompt()
                .user(message)
                .tools(agentTools)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        sessionId
                ))
                .call()
                .content();
    }
}
