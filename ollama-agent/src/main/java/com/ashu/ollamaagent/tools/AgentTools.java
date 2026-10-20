
package com.ashu.ollamaagent.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AgentTools {

    @Tool("Get the current local date and time")
    public String getCurrentDateTime() {
        return LocalDateTime.now().toString();
    }

    @Tool("Add two numbers together")
    public double add(double a, double b) {
        return a + b;
    }

    @Tool("Multiply two numbers together")
    public double multiply(double a, double b) {
        return a * b;
    }

    @Tool("Subtract the second number from the first")
    public double subtract(double a, double b) {
        return a - b;
    }

    @Tool("Divide the first number by the second number")
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException(
                    "Cannot divide by zero"
            );
        }

        return a / b;
    }
}
