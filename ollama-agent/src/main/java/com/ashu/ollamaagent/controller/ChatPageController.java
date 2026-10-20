
package com.ashu.ollamaagent.controller;

import com.ashu.ollamaagent.service.AgentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatPageController {

    private final AgentService agentService;

    public ChatPageController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/")
    public String home() {
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(
            @RequestParam String message,
            HttpSession session,
            Model model) {

        String sessionId = (String) session.getAttribute("sessionId");

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }

        try {
            String reply = agentService.chat(message, sessionId);

            model.addAttribute("reply", reply);
            model.addAttribute("message", message);

        } catch (Exception e) {
            model.addAttribute(
                    "reply",
                    "Sorry, an error occurred while contacting the AI."
            );
        }

        return "chat";
    }
}
